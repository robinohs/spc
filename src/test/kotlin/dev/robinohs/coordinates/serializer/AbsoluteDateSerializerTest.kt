package dev.robinohs.coordinates.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
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
internal class AbsoluteDateSerializerTest {
    private lateinit var cut: AbsoluteDateSerializer

    private lateinit var jsonGenerator: JsonGenerator
    private lateinit var provider: SerializerProvider

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            CoordinatesApplication.loadOrekitData()
        }
    }

    @BeforeEach
    fun setUp() {
        cut = AbsoluteDateSerializer()
        jsonGenerator = Mockito.mock(JsonGenerator::class.java)
        provider = Mockito.mock(SerializerProvider::class.java)
    }

    @Test
    @Throws(IOException::class)
    fun serialize1() {
        val date = AbsoluteDate("2021-08-11T12:23:03Z", TimeScalesFactory.getUTC())
        val expectedMillis = 1628684583000L

        cut.serialize(date, jsonGenerator, provider)

        Mockito.verify(jsonGenerator).writeNumber(expectedMillis)
    }

    @Test
    @Throws(IOException::class)
    fun serialize2() {
        val date = AbsoluteDate("1970-01-19T20:24:44Z", TimeScalesFactory.getUTC())
        val expectedMillis = 1628684000L

        cut.serialize(date, jsonGenerator, provider)

        Mockito.verify(jsonGenerator).writeNumber(expectedMillis)
    }
}