// To use this code, add the following Maven dependency to your project:
//
//     com.fasterxml.jackson.core     : jackson-databind          : 2.9.0
//     com.fasterxml.jackson.datatype : jackson-datatype-jsr310   : 2.9.0
//
// Import this package:
//
//     import kr.ac.tukorea.ge.spgp2025.a2dg.framework.map.Converter
//
// Then you can deserialize a JSON string with
//
//     val data = Converter.fromJsonString(jsonString)

package kr.ac.tukorea.ge.spgp2025.a2dg.framework.map

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import java.io.IOException
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

object Converter {
    // Date-time helpers
    private val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendOptional(DateTimeFormatter.ISO_DATE_TIME)
        .appendOptional(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        .appendOptional(DateTimeFormatter.ISO_INSTANT)
        .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SX"))
        .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX"))
        .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        .toFormatter()
        .withZone(ZoneOffset.UTC)

    fun parseDateTimeString(str: String): OffsetDateTime {
        return ZonedDateTime.from(DATE_TIME_FORMATTER.parse(str)).toOffsetDateTime()
    }

    private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendOptional(DateTimeFormatter.ISO_TIME)
        .appendOptional(DateTimeFormatter.ISO_OFFSET_TIME)
        .parseDefaulting(ChronoField.YEAR, 2020)
        .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .toFormatter()
        .withZone(ZoneOffset.UTC)

    fun parseTimeString(str: String): OffsetTime {
        return ZonedDateTime.from(TIME_FORMATTER.parse(str)).toOffsetDateTime().toOffsetTime()
    }

    // Serialize/deserialize helpers
    fun fromJsonString(json: String): TiledMap {
        return reader.readValue(json)
    }

    fun toJsonString(obj: TiledMap): String {
        return writer.writeValueAsString(obj)
    }

    private val reader: ObjectReader by lazy { mapper.readerFor(TiledMap::class.java) }
    private val writer: ObjectWriter by lazy { mapper.writerFor(TiledMap::class.java) }

    private val mapper: ObjectMapper by lazy {
        ObjectMapper().apply {
            findAndRegisterModules()
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            val module = SimpleModule()
            module.addDeserializer(OffsetDateTime::class.java,
                object : JsonDeserializer<OffsetDateTime>() {
                    override fun deserialize(
                        jsonParser: JsonParser,
                        deserializationContext: DeserializationContext
                    ): OffsetDateTime {
                        val value = jsonParser.text
                        return parseDateTimeString(value)
                    }
                })
            registerModule(module)
        }
    }
}

