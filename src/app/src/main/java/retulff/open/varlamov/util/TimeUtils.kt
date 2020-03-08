package retulff.open.varlamov.util

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import retulff.open.varlamov.App
import retulff.open.varlamov.R
import java.util.*

class TimeUtils {
    companion object {

        fun parseString(string: String, pattern: String, timeZone: DateTimeZone): DateTime {

            val formatter = DateTimeFormat
                .forPattern(pattern)
                .withZone(timeZone)

            return formatter.parseDateTime(string)
        }

        fun isToday(time: DateTime): Boolean {
            return LocalDate.now() == LocalDate(time)
        }

        fun isYesterday(time: DateTime): Boolean {
            return LocalDate.now().minusDays(1) == LocalDate(time)
        }

        fun isTomorrow(time: DateTime): Boolean {
            return LocalDate.now().plusDays(1) == LocalDate(time)
        }

        fun isCurrentYear(time: DateTime): Boolean {
            return LocalDate.now().year == LocalDate(time).year
        }

        private fun dateTimeStringTemplate(dateTime: DateTime): String {

            return when {

                isToday(dateTime) -> "HH:mm"
                isYesterday(dateTime) -> "HH:mm"
                isCurrentYear(dateTime) -> "d MMMM, HH:mm"

                else -> "d MMMM yyyy, HH:mm"
            }
        }

        fun dateTimeToString(dateTime: DateTime): String {
            return when {

                isToday(dateTime) -> "${App.res.getString(R.string.time_today)}, ${dateTime.toString("HH:mm")}"
                isYesterday(dateTime) -> "${App.res.getString(R.string.time_yesterday)}, ${dateTime.toString(
                    "HH:mm"
                )}"
                isCurrentYear(dateTime) -> dateTime.toString("d MMMM, HH:mm")

                else -> dateTime.toString("d MMMM yyyy, HH:mm")
            }
        }

        fun dateTimeToString(dateTime: DateTime, timeZone: TimeZone): String {

            val localDateTime = dateTime.withZone(DateTimeZone.forTimeZone(timeZone))

            return dateTimeToString(localDateTime)
        }

        fun dateTimeToString(dateTime: DateTime, timeZone: DateTimeZone): String {

            val localDateTime = dateTime.withZone(timeZone)

            return dateTimeToString(localDateTime)
        }

        fun dateTimeToString(dateTime: DateTime, timeZone: TimeZone, template: String): String {
            return dateTime.withZone(DateTimeZone.forTimeZone(timeZone)).toString(template)
        }

        fun dateTimeToString(dateTime: DateTime, timeZone: DateTimeZone, template: String): String {
            return dateTime.withZone(timeZone).toString(template)
        }
    }
}