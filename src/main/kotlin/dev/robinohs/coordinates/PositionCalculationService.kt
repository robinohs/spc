package dev.robinohs.coordinates

import dev.robinohs.coordinates.dtos.*
import org.hipparchus.util.FastMath
import org.orekit.bodies.GeodeticPoint
import org.orekit.bodies.OneAxisEllipsoid
import org.orekit.frames.Frame
import org.orekit.frames.FramesFactory
import org.orekit.orbits.KeplerianOrbit
import org.orekit.orbits.PositionAngle
import org.orekit.propagation.SpacecraftState
import org.orekit.propagation.analytical.KeplerianPropagator
import org.orekit.time.AbsoluteDate
import org.orekit.utils.Constants
import org.orekit.utils.IERSConventions
import org.springframework.stereotype.Service


/**
 * @author : Robin Ohs
 * @created : 14.09.2022
 * @since : 1.0.0
 */
@Service
class PositionCalculationService {

    private val itrf: Frame = FramesFactory.getITRF(IERSConventions.IERS_2010, false)
    private val j2000: Frame = FramesFactory.getEME2000()
    private val earth =
        OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS, Constants.WGS84_EARTH_FLATTENING, itrf)

    /**
     * Creates a keplerian orbit with given [SatOrbitParams].
     */
    fun calculateOrbit(satOrbitParams: SatOrbitParams): KeplerianOrbit {
        val semiMajorAxis = satOrbitParams.altitude * 1_000 + Constants.WGS84_EARTH_EQUATORIAL_RADIUS
        val inclination = FastMath.toRadians(satOrbitParams.inclination)
        val omega = FastMath.toRadians(satOrbitParams.perigeeArgument)
        val raan = FastMath.toRadians(satOrbitParams.raan)
        val meanAnomaly = FastMath.toRadians(satOrbitParams.meanAnomaly)
        val frame = FramesFactory.getEME2000()
        val date = AbsoluteDateUtils.ofEpochMillis(satOrbitParams.epoch)
        return KeplerianOrbit(
            semiMajorAxis,
            0.0,
            inclination,
            omega,
            raan,
            meanAnomaly,
            PositionAngle.MEAN,
            frame,
            date,
            Constants.IERS2010_EARTH_MU
        )
    }

    /**
     * Takes an orbit and propagates the satellite through a given [TimeInterval].
     * Optional: Set the step size for the propagation.
     */
    fun propagateInterval(
        initialOrbit: KeplerianOrbit,
        timeInterval: TimeInterval,
        stepSizeSeconds: Double = 1.0
    ): List<SatPosition> {
        val satPositions = mutableListOf<SatPosition>()
        val kepler = KeplerianPropagator(initialOrbit)
        var extrapDate: AbsoluteDate = timeInterval.start
        while (extrapDate <= timeInterval.end) {
            val currentState = kepler.propagate(extrapDate)
            val llaPos: LLAPos = generateLLAPos(currentState, extrapDate)
            val xyzPos: XYZPos = generateXYZPos(currentState, extrapDate)
            val satPosition = SatPosition(
                epochMillis = AbsoluteDateUtils.toEpochMillis(extrapDate),
                llaPos = llaPos,
                xyzPos = xyzPos
            )
            satPositions.add(satPosition)
            extrapDate = extrapDate.shiftedBy(stepSizeSeconds)
        }
        return satPositions
    }

    // calculate lat, lon and altitude (J2000/EME2000)
    private fun generateLLAPos(currentState: SpacecraftState, extrapDate: AbsoluteDate): LLAPos {
        val satLatLonAlt: GeodeticPoint =
            earth.transform(currentState.pvCoordinates.position, j2000, extrapDate)
        return LLAPos(
            lat = FastMath.toDegrees(satLatLonAlt.latitude),
            lon = FastMath.toDegrees(satLatLonAlt.longitude),
            alt = satLatLonAlt.altitude / 1_000
        )
    }

    // calculate X, Y, Z (ITRF/ECEF)
    private fun generateXYZPos(currentState: SpacecraftState, extrapDate: AbsoluteDate): XYZPos {
        // compute in M
        val posInM = j2000.getTransformTo(itrf, extrapDate).transformPosition(currentState.pvCoordinates.position)
        // convert to KM
        val posInKM = posInM.scalarMultiply(1 / 1000.0)
        return XYZPos(
            x = posInKM.x,
            y = posInKM.y,
            z = posInKM.z
        )
    }
}