package com.archit.calendardaterangepicker.customviews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarManager.CalendarRangeType;

import java.util.Calendar;

final class CalendarRangeUtils {
    /**
     * Resets date time to HH:mm:ss SSS = 00:00:00 000
     *
     * @param date {@link Calendar object}
     */
    static void resetTime(@NonNull final Calendar date, @CalendarRangeType final int rangeType) {
        if (rangeType == CalendarRangeType.START_DATE) {
            date.set(Calendar.HOUR, 0);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);
        } else if (rangeType == CalendarRangeType.LAST_DATE) {
            date.set(Calendar.HOUR, 23);
            date.set(Calendar.MINUTE, 59);
            date.set(Calendar.SECOND, 59);
            date.set(Calendar.MILLISECOND, 999);
        } else {
            date.set(Calendar.HOUR, 0);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);
        }
    }

    /**
     * To print calendar date.
     *
     * @param calendar date
     * @return Date string
     */
    static String printDate(@Nullable final Calendar calendar) {
        return calendar != null ? calendar.getTime().toString() : "null";
    }
}