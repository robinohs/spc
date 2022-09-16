package dev.robinohs.coordinates.dtos

import org.orekit.time.AbsoluteDate

/**
 * @author : Robin Ohs
 * @created : 16.09.2022
 * @since : 1.0.0
 */
data class SatPosition(
    val epochMillis: Long,
    val llaPos: LLAPos,
    val xyzPos: XYZPos
)
