package dev.robinohs.coordinates

import dev.robinohs.coordinates.dtos.CalculateByOrbitalParamsDTO
import dev.robinohs.coordinates.dtos.SatPosition
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author : Robin Ohs
 * @created : 16.09.2022
 * @since : 1.0.0
 */
@RestController
@RequestMapping("/api/position")
class PositionCalculationController(private val positionCalculationService: PositionCalculationService) {
    @PostMapping("/by_orbital_params")
    fun computePositionByOrbitalParams(@RequestBody calculateByOrbitalParamsDTO: CalculateByOrbitalParamsDTO): List<SatPosition> {
        val orbit = positionCalculationService.calculateOrbit(calculateByOrbitalParamsDTO.satOrbitParams)
        return positionCalculationService.propagateInterval(
            initialOrbit = orbit,
            timeInterval = calculateByOrbitalParamsDTO.interval
        )
    }
}