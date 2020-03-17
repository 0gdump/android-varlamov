package open.v0gdump.varlamov.extension

import open.v0gdump.varlamov.App
import open.v0gdump.varlamov.R
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import java.util.*

internal fun defaultStringFormatter(dateTime: DateTime): String = when {
    dateTime.isToday() ->
        "${App.res.getString(R.string.time_today)}, ${dateTime.toString("HH:mm")}"

    dateTime.isYesterday() ->
        "${App.res.getString(R.string.time_yesterday)}, ${dateTime.toString("HH:mm")}"

    dateTime.isCurrentYear() ->
        dateTime.toString("d MMMM, HH:mm")

    else ->
        dateTime.toString("d MMMM yyyy, HH:mm")
}

fun DateTime.toString(timeZone: TimeZone): String =
    defaultStringFormatter(withZone(DateTimeZone.forTimeZone(timeZone)))

fun DateTime.toString(timeZone: DateTimeZone): String =
    defaultStringFormatter(withZone(timeZone))

fun DateTime.toString(timeZone: TimeZone, template: String): String =
    withZone(DateTimeZone.forTimeZone(timeZone)).toString(template)

fun DateTime.toString(timeZone: DateTimeZone, template: String): String =
    withZone(timeZone).toString(template)

fun DateTime.isToday(): Boolean =
    LocalDate(this) == LocalDate.now()

fun DateTime.isYesterday(): Boolean =
    LocalDate(this) == LocalDate.now().minusDays(1)

fun DateTime.isTomorrow(): Boolean =
    LocalDate(this) == LocalDate.now().plusDays(1)

fun DateTime.isCurrentYear(): Boolean =
    year == LocalDate.now().year