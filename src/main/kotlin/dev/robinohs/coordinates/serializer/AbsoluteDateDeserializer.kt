package dev.robinohs.coordinates.serializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import dev.robinohs.coordinates.AbsoluteDateUtils
import org.orekit.time.AbsoluteDate
import java.io.IOException

/**
 * @author : Robin Ohs
 * @created : 16.09.2022
 * @since : 1.0.0
 */
class AbsoluteDateDeserializer : JsonDeserializer<AbsoluteDate>() {
    @Throws(IOException::class)
    override fun deserialize(jsonParser: JsonParser, ctx: DeserializationContext): AbsoluteDate {
        val millis: Long = when (jsonParser.numberType) {
            JsonParser.NumberType.INT -> jsonParser.intValue.toLong()
            JsonParser.NumberType.LONG -> jsonParser.longValue
            else -> throw IOException()
        }
        return AbsoluteDateUtils.ofEpochMillis(millis)
    }
}