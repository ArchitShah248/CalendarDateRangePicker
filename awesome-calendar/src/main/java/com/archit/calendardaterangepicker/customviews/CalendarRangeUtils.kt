package com.archit.calendardaterangepicker.customviews

import com.archit.calendardaterangepicker.models.DateTiming
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Resets date time to HH:mm:ss SSS = 00:00:00 000 or 23:59:59 999
 *
 * @param date [Calendar]
 */
internal fun resetTime(date: Calendar, dateTiming: DateTiming) {
    when (dateTiming) {
        DateTiming.START -> {
            date[Calendar.HOUR_OF_DAY] = 0
            date[Calendar.MINUTE] = 0
            date[Calendar.SECOND] = 0
            date[Calendar.MILLISECOND] = 0
        }
        DateTiming.END -> {
            date[Calendar.HOUR_OF_DAY] = LAST_HOUR_OF_THE_DAY
            date[Calendar.MINUTE] = LAST_MINUTE
            date[Calendar.SECOND] = LAST_SECOND
            date[Calendar.MILLISECOND] = LAST_MILLI_SECOND
        }
        else -> {
            date[Calendar.HOUR_OF_DAY] = 0
            date[Calendar.MINUTE] = 0
            date[Calendar.SECOND] = 0
            date[Calendar.MILLISECOND] = 0
        }
    }
}

private const val LAST_HOUR_OF_THE_DAY = 23
private const val LAST_MINUTE = 59
private const val LAST_SECOND = 59
private const val LAST_MILLI_SECOND = 999

/**
 * To print calendar date.
 *
 * @param calendar date
 * @return Date string in dd/MM/yyyy
 */
fun printDate(calendar: Calendar?): String {
    return if (calendar != null) {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return simpleDateFormat.format(calendar.time)
    } else {
        "null"
    }
}

fun isMonthSame(one: Calendar, second: Calendar): Boolean {
    return one[Calendar.YEAR] == second[Calendar.YEAR]
            && one[Calendar.MONTH] == second[Calendar.MONTH]
}

fun isDateSame(one: Calendar, second: Calendar): Boolean {
    return isMonthSame(one, second)
            && one[Calendar.DATE] == second[Calendar.DATE]
}

