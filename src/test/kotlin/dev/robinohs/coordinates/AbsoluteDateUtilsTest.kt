package dev.robinohs.coordinates

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.orekit.time.AbsoluteDate
import org.orekit.time.TimeScalesFactory

/**
 * @author : Robin Ohs
 * @created : 16.09.2022
 * @since : 1.0.0
 */
internal class AbsoluteDateUtilsTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            CoordinatesApplication.loadOrekitData()
        }
    }

    @Test
    fun ofEpochSeconds() {
        val expectedDate = AbsoluteDate("2021-08-11T12:26:20Z", TimeScalesFactory.getUTC())

        val actualDate = AbsoluteDateUtils.ofEpochSeconds(1628684780)

        assertEquals(expectedDate, actualDate, "The expected and actual timestamps are not the same.")
    }

    @Test
    fun ofEpochMillis() {
        val expectedDate = AbsoluteDate("2021-08-11T12:26:20.862Z", TimeScalesFactory.getUTC())

        val actualDate = AbsoluteDateUtils.ofEpochMillis(1628684780862L)

        assertEquals(expectedDate, actualDate, "The expected and actual timestamps are not the same.")
    }

    @Test
    fun toEpochMillis() {
        val date = AbsoluteDate("2021-09-07T13:15:35.308Z", TimeScalesFactory.getUTC())
        val expectedMillis = 1631020535308L

        val actualMillis = AbsoluteDateUtils.toEpochMillis(date)

        assertEquals(expectedMillis, actualMillis, "The expected and actual millis are not the same.")
    }
}