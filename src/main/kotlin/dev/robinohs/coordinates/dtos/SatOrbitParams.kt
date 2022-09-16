package dev.robinohs.coordinates.dtos

/**
 * Data Transfer Object to pass orbit params for a satellite from the frontend to the backend.
 *
 * @author : Robin Ohs
 * @created : 14.09.2022
 * @since : 1.0.0
 */
data class SatOrbitParams(
    val epoch: Long,
    val meanAnomaly: Double,
    val altitude: Double,
    val perigeeArgument: Double,
    val inclination: Double,
    val raan: Double,
)
