package com.archit.calendardaterangepicker.customviews;

import androidx.annotation.NonNull;

import java.util.Calendar;

public interface CalendarListener {
    /**
     * Called on first date selection.
     * @param startDate First selected date.
     */
    void onFirstDateSelected(@NonNull final Calendar startDate);

    /**
     * Called on first and last date selection.
     * @param startDate First date.
     * @param endDate Last date.
     */
    void onDateRangeSelected(@NonNull final Calendar startDate, @NonNull final Calendar endDate);
}
