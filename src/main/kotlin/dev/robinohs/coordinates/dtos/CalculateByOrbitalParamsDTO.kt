package dev.robinohs.coordinates.dtos

/**
 * @author : Robin Ohs
 * @created : 16.09.2022
 * @since : 1.0.0
 */
data class CalculateByOrbitalParamsDTO(
    val interval: TimeInterval,
    val satOrbitParams: SatOrbitParams
)
