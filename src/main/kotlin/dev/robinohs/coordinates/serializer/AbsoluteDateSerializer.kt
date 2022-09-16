package dev.robinohs.coordinates.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.robinohs.coordinates.AbsoluteDateUtils
import org.orekit.time.AbsoluteDate
import java.io.IOException

/**
 * @author : Robin Ohs
 * @created : 16.09.2022
 * @since : 1.0.0
 */
class AbsoluteDateSerializer : JsonSerializer<AbsoluteDate>() {
    @Throws(IOException::class)
    override fun serialize(
        absoluteDate: AbsoluteDate,
        jsonGenerator: JsonGenerator,
        serializerProvider: SerializerProvider
    ) {
        jsonGenerator.writeNumber(AbsoluteDateUtils.toEpochMillis(absoluteDate))
    }
}