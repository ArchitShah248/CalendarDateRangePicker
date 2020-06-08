package com.archit.calendardaterangepicker.customviews

import com.archit.calendardaterangepicker.customviews.DateTiming.END
import com.archit.calendardaterangepicker.customviews.DateTiming.START
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class DateTiming {
    NONE,
    START,
    END
}

/**
 * Resets date time to HH:mm:ss SSS = 00:00:00 000 or 23:59:59 999
 *
 * @param date [Calendar]
 */
fun resetTime(date: Calendar, dateTiming: DateTiming) {
    when (dateTiming) {
        START -> {
            date[Calendar.HOUR_OF_DAY] = 0
            date[Calendar.MINUTE] = 0
            date[Calendar.SECOND] = 0
            date[Calendar.MILLISECOND] = 0
        }
        END -> {
            date[Calendar.HOUR_OF_DAY] = 23
            date[Calendar.MINUTE] = 59
            date[Calendar.SECOND] = 59
            date[Calendar.MILLISECOND] = 999
        }
        else -> {
            date[Calendar.HOUR_OF_DAY] = 0
            date[Calendar.MINUTE] = 0
            date[Calendar.SECOND] = 0
            date[Calendar.MILLISECOND] = 0
        }
    }
}

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

