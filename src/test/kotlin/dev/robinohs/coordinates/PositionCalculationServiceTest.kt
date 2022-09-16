package dev.robinohs.coordinates

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import dev.robinohs.coordinates.dtos.SatOrbitParams
import dev.robinohs.coordinates.dtos.TimeInterval
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.hipparchus.util.FastMath
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.orekit.utils.Constants
import org.orekit.utils.Constants.IERS2010_EARTH_MU
import org.springframework.test.util.AssertionErrors.assertEquals
import java.io.File
import java.text.SimpleDateFormat


/**
 * @author : Robin Ohs
 * @created : 15.09.2022
 * @since : 1.0.0
 */
internal class PositionCalculationServiceTest {

    internal data class PosCSV(
        val time: Long,
        val x: Double,
        val y: Double,
        val z: Double
    )

    private lateinit var cut: PositionCalculationService

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            CoordinatesApplication.loadOrekitData()
        }
    }

    @BeforeEach
    fun setUp() {
        cut = PositionCalculationService()
    }

    @Test
    fun calculateOrbit() {
        val altitude = 600.0
        val inclination = 98.0
        val meanAnomaly = 220.0
        val perigeeArgument = 0.0
        val raan = 310.0
        val satOrbitParams = SatOrbitParams(
            epoch = 1577907600000,
            meanAnomaly = meanAnomaly,
            altitude = altitude,
            perigeeArgument = perigeeArgument,
            inclination = inclination,
            raan = raan
        )
        val orbit = cut.calculateOrbit(satOrbitParams)
        Assertions.assertEquals(altitude * 1_000 + Constants.WGS84_EARTH_EQUATORIAL_RADIUS, orbit.a)
        Assertions.assertEquals(inclination, FastMath.toDegrees(orbit.i))
        Assertions.assertEquals(meanAnomaly, FastMath.toDegrees(orbit.meanAnomaly))
        Assertions.assertEquals(perigeeArgument, FastMath.toDegrees(orbit.perigeeArgument))
        Assertions.assertEquals(raan, FastMath.toDegrees(orbit.rightAscensionOfAscendingNode))
        Assertions.assertEquals(IERS2010_EARTH_MU, orbit.mu)
    }

    @ParameterizedTest
    @CsvSource(
        "1577907600000,220.0,600.0,0.0,98.0,310.0,LEO_0_0-LLA-POS.csv,LEO_0_0-XYZ-POS.csv",
        "1577907600000,240.0,600.0,0.0,98.0,310.0,LEO_0_1-LLA-POS.csv,LEO_0_1-XYZ-POS.csv",
        "1577907600000,240.0,600.0,0.0,98.0,330.0,LEO_1_1-LLA-POS.csv,LEO_1_1-XYZ-POS.csv",
        "1577907600000,220.0,600.0,0.0,98.0,10.0,LEO_3_0-LLA-POS.csv,LEO_3_0-XYZ-POS.csv",
        "1577907600000,280.0,600.0,0.0,98.0,9.99999999999999,LEO_3_3-LLA-POS.csv,LEO_3_3-XYZ-POS.csv",
    )
    fun propagateInterval(
        epoch: Long,
        meanAnomaly: Double,
        altitude: Double,
        perigeeArgument: Double,
        inclination: Double,
        raan: Double,
        llaFileName: String,
        xyzFileName: String,
    ) {
        val satOrbitParams = SatOrbitParams(
            epoch = epoch,
            meanAnomaly = meanAnomaly,
            altitude = altitude,
            perigeeArgument = perigeeArgument,
            inclination = inclination,
            raan = raan
        )
        val orbit = cut.calculateOrbit(satOrbitParams)
        val start = AbsoluteDateUtils.ofEpochMillis(1577907600000)
        val end = AbsoluteDateUtils.ofEpochMillis(1577910600000)
        val timeInterval = TimeInterval(start, end)

        val satPositions = cut.propagateInterval(orbit, timeInterval)

        val llaPosEntries = loadLLAPosCSV(llaFileName)
        val xyzPosEntries = loadXYZPosCSV(xyzFileName)
        assertAll("List sizes are equal",
            { assertEquals("LLA", llaPosEntries.size, satPositions.size) },
            { assertEquals("XYZ", xyzPosEntries.size, satPositions.size) }
        )
        satPositions.forEachIndexed { index, satPosition ->
            val llaPos = llaPosEntries[index]
            val xyzPos = xyzPosEntries[index]
            val allowedDeviation = Offset.offset(0.09)
            assertAll(
                "Values at index $index are equal for LLA.",
                { assertThat(satPosition.llaPos.lat).isCloseTo(llaPos.x, allowedDeviation) },
                { assertThat(satPosition.llaPos.lon).isCloseTo(llaPos.y, allowedDeviation) },
                { assertThat(satPosition.llaPos.alt).isCloseTo(llaPos.z, allowedDeviation) },
            )
            assertAll(
                "Values at index $index are equal for XYZ.",
                { assertThat(satPosition.xyzPos.x).isCloseTo(xyzPos.x, allowedDeviation) },
                { assertThat(satPosition.xyzPos.y).isCloseTo(xyzPos.y, allowedDeviation) },
                { assertThat(satPosition.xyzPos.z).isCloseTo(xyzPos.z, allowedDeviation) },
            )
        }
    }

    private fun parseDate(date: String): Long {
        val pattern = "d MMM yyyy HH:mm:ss"
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.parse(date).time
    }

    private fun loadLLAPosCSV(fileName: String): List<PosCSV> {
        val xyzPosEntries = csvReader().open(File("src/test/resources/$fileName")) {
            readAllWithHeaderAsSequence().map {
                PosCSV(
                    time = parseDate(it["TIME[UTC]"] ?: throw IllegalArgumentException()),
                    x = it["LAT[deg]"]?.toDouble() ?: throw IllegalArgumentException(),
                    y = it["LON[deg]"]?.toDouble() ?: throw IllegalArgumentException(),
                    z = it["ALT[km]"]?.toDouble() ?: throw IllegalArgumentException(),
                )
            }.toList()
        }
        return xyzPosEntries
    }

    private fun loadXYZPosCSV(fileName: String): List<PosCSV> {
        val xyzPosEntries = csvReader().open(File("src/test/resources/$fileName")) {
            readAllWithHeaderAsSequence().map {
                PosCSV(
                    time = parseDate(it["TIME[UTC]"] ?: throw IllegalArgumentException()),
                    x = it["X[km]"]?.toDouble() ?: throw IllegalArgumentException(),
                    y = it["Y[km]"]?.toDouble() ?: throw IllegalArgumentException(),
                    z = it["Z[km]"]?.toDouble() ?: throw IllegalArgumentException(),
                )
            }.toList()
        }
        return xyzPosEntries
    }
}