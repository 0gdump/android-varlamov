package com.varlamov.android.util

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

object DateTimeUtils {

    fun parseString(string: String, pattern: String, timeZone: DateTimeZone): DateTime =
        DateTimeFormat
            .forPattern(pattern)
            .withZone(timeZone)
            .parseDateTime(string)
}