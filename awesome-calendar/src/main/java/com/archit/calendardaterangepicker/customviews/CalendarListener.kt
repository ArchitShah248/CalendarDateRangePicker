package com.archit.calendardaterangepicker.customviews

import java.util.Calendar

interface CalendarListener {
    /**
     * Called on first date selection.
     * @param startDate First selected date.
     */
    fun onFirstDateSelected(startDate: Calendar)

    /**
     * Called on first and last date selection.
     * @param startDate First date.
     * @param endDate Last date.
     */
    fun onDateRangeSelected(startDate: Calendar, endDate: Calendar)
}