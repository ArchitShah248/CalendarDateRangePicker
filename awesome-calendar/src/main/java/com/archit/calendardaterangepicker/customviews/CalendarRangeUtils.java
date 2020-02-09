package com.archit.calendardaterangepicker.customviews;

import androidx.annotation.NonNull;

import java.util.Calendar;

final class CalendarRangeUtils {
    /**
     * Resets date time to HH:mm:ss SSS = 00:00:00 000
     *
     * @param date {@link Calendar object}
     */
    static void resetTime(@NonNull final Calendar date) {
        date.set(Calendar.HOUR, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
    }
}