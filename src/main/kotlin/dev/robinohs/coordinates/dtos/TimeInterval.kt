package dev.robinohs.coordinates.dtos

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import dev.robinohs.coordinates.serializer.AbsoluteDateDeserializer
import dev.robinohs.coordinates.serializer.AbsoluteDateSerializer
import org.orekit.time.AbsoluteDate

/**
 * @author : Robin Ohs
 * @created : 15.09.2022
 * @since : 1.0.0
 */
data class TimeInterval(
    @JsonSerialize(using = AbsoluteDateSerializer::class)
    @JsonDeserialize(using = AbsoluteDateDeserializer::class)
    val start: AbsoluteDate,
    @JsonSerialize(using = AbsoluteDateSerializer::class)
    @JsonDeserialize(using = AbsoluteDateDeserializer::class)
    val end: AbsoluteDate
)
