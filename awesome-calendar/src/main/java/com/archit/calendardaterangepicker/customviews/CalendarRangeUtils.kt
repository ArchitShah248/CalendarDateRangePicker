package com.archit.calendardaterangepicker.customviews

import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager.CalendarRangeType
import java.util.Calendar

class CalendarRangeUtils {

    companion object {

        /**
         * Resets date time to HH:mm:ss SSS = 00:00:00 000 or 23:59:59 999
         *
         * @param date [Calendar]
         */
        @JvmStatic
        fun resetTime(date: Calendar, rangeType: CalendarRangeType) {
            when {
                rangeType === CalendarRangeType.START_DATE -> {
                    date[Calendar.HOUR_OF_DAY] = 0
                    date[Calendar.MINUTE] = 0
                    date[Calendar.SECOND] = 0
                    date[Calendar.MILLISECOND] = 0
                }
                rangeType === CalendarRangeType.LAST_DATE -> {
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
        @JvmStatic
        fun printDate(calendar: Calendar?): String {
            return if (calendar != null) {
                "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)}/${calendar.get(Calendar.YEAR)}"
            } else {
                "null"
            }
        }

        @JvmStatic
        fun isMonthSame(one: Calendar, second: Calendar): Boolean {
            return one[Calendar.YEAR] == second[Calendar.YEAR]
                    && one[Calendar.MONTH] == second[Calendar.MONTH]
        }

        @JvmStatic
        fun isDateSame(one: Calendar, second: Calendar): Boolean {
            return isMonthSame(one, second)
                    && one[Calendar.DATE] == second[Calendar.DATE]
        }
    }
}