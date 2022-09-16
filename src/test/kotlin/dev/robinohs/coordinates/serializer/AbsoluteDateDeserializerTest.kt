package dev.robinohs.coordinates.serializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import dev.robinohs.coordinates.CoordinatesApplication
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.orekit.time.AbsoluteDate
import org.orekit.time.TimeScalesFactory
import java.io.IOException

/**
 * @author : Robin Ohs
 * @created : 16.09.2022
 * @since : 1.0.0
 */
internal class AbsoluteDateDeserializerTest {
    private lateinit var cut: AbsoluteDateDeserializer
    private lateinit var parser: JsonParser
    private lateinit var context: DeserializationContext

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            CoordinatesApplication.loadOrekitData()
        }
    }

    @BeforeEach
    fun setUp() {
        cut = AbsoluteDateDeserializer()
        parser = Mockito.mock(JsonParser::class.java)
        context = Mockito.mock(DeserializationContext::class.java)
    }

    @Test
    @Throws(IOException::class)
    fun deserialize_Long() {
        Mockito.`when`(parser.longValue).thenReturn(1627994483642L)
        Mockito.`when`(parser.numberType).thenReturn(JsonParser.NumberType.LONG)
        val expectedDate = AbsoluteDate("2021-08-03T12:41:23.642Z", TimeScalesFactory.getUTC())

        val actualDate = cut.deserialize(parser, context)

        assertEquals(expectedDate, actualDate, "The expected and actual dates are not the same.")
    }

    @Test
    @Throws(IOException::class)
    fun deserialize_ShortLong() {
        Mockito.`when`(parser.longValue).thenReturn(162799449L)
        Mockito.`when`(parser.numberType).thenReturn(JsonParser.NumberType.LONG)
        val expectedDate = AbsoluteDate("1970-01-02T21:13:19.449Z", TimeScalesFactory.getUTC())

        val actualDate = cut.deserialize(parser, context)

        assertEquals(expectedDate, actualDate, "The expected and actual dates are not the same.")
    }

    @Test
    @Throws(IOException::class)
    fun deserialize_Int() {
        Mockito.`when`(parser.intValue).thenReturn(1627994483)
        Mockito.`when`(parser.numberType).thenReturn(JsonParser.NumberType.INT)
        val expectedDate = AbsoluteDate("1970-01-19T20:13:14.483Z", TimeScalesFactory.getUTC())

        val actualDate = cut.deserialize(parser, context)

        assertEquals(expectedDate, actualDate, "The expected and actual dates are not the same.")
    }
}