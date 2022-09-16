package dev.robinohs.coordinates

import org.orekit.time.AbsoluteDate
import org.orekit.time.TimeScalesFactory
import java.time.Instant

/**
 * @author : Robin Ohs
 * @created : 15.09.2022
 * @since : 1.0.0
 */
object AbsoluteDateUtils {
    private val UTC = TimeScalesFactory.getUTC()

    /**
     * Takes the seconds as [Long] since epoch and computes the [AbsoluteDate] of it.
     *
     * @param seconds the seconds since epoch (UTC).
     * @return the corresponding [AbsoluteDate] for the seconds since epoch.
     */
    fun ofEpochSeconds(seconds: Long): AbsoluteDate {
        val instant = Instant.ofEpochSecond(seconds)
        return AbsoluteDate(instant.toString(), UTC)
    }

    /**
     * Takes the millis as [Long] since epoch and computes the [AbsoluteDate] of it.
     *
     * @param millis the millis since epoch (UTC).
     * @return the corresponding [AbsoluteDate] for the millis since epoch.
     */
    fun ofEpochMillis(millis: Long): AbsoluteDate {
        val instant = Instant.ofEpochMilli(millis)
        return AbsoluteDate(instant.toString(), UTC)
    }

    /**
     * Takes an [AbsoluteDate] and computes the millis since epoch.
     *
     * @param absoluteDate the [AbsoluteDate] to compute the millis since epoch.
     * @return the millis since epoch as [Long].
     */
    fun toEpochMillis(absoluteDate: AbsoluteDate): Long {
        return absoluteDate.toDate(UTC).time
    }
}