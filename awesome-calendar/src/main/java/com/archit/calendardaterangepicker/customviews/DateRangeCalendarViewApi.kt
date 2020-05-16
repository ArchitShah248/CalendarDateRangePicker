package com.archit.calendardaterangepicker.customviews

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import java.util.Calendar

interface DateRangeCalendarViewApi {
    /**
     * To set calendar listener
     *
     * @param calendarListener Listener
     */
    fun setCalendarListener(calendarListener: CalendarListener)

    /**
     * To apply custom fonts to all the text views
     *
     * @param fonts - Typeface that you want to apply
     */
    fun setFonts(fonts: Typeface)

    /**
     * To remove all selection and redraw current calendar
     */
    fun resetAllSelectedViews()

    /**
     * To set week offset. To start week from any of the day. Default is 0 (Sunday).
     *
     * @param offset 0-Sun, 1-Mon, 2-Tue, 3-Wed, 4-Thu, 5-Fri, 6-Sat
     */
    fun setWeekOffset(offset: Int)

    /**
     * To set left navigation ImageView drawable
     */
    fun setNavLeftImage(leftDrawable: Drawable)

    /**
     * To set right navigation ImageView drawable
     */
    fun setNavRightImage(rightDrawable: Drawable)

    /**
     * To get editable mode.
     */
    /**
     * To set editable mode. Default value will be true.
     *
     * @param isEditable true if you want user to select date range else false
     */
    var isEditable: Boolean

    /**
     * To provide month range to be shown to user. If start month is greater than end month than it will give [IllegalArgumentException].<br></br>
     * **Note:** Do not call this method after calling date selection method (@method setSelectedDateRange) as it will reset date selection.
     *
     * @param startMonth Start month of the calendar
     * @param endMonth   End month of the calendar
     */
    fun setVisibleMonthRange(startMonth: Calendar, endMonth: Calendar)

    /**
     * To set current visible month.
     *
     * @param calendar Month to be set as current
     */
    fun setCurrentMonth(calendar: Calendar)

    /**
     * Sets selectable dates from start date to end date. By default all the visible dates will
     * selectable.
     *
     * @param startDate Start date
     * @param endDate   End date
     */
    fun setSelectableDateRange(startDate: Calendar, endDate: Calendar)

    /**
     * Sets start and end date.<br></br>
     * <B>Note:</B><br></br>
     * You can not set end date before start date.<br></br>
     * If you are setting custom month range than do not call this before calling (@method setVisibleMonthRange).<br></br>
     * If you have selected date selection mode as `single` or `fixed_range` then end date will be ignored.
     * @param startDate Start date
     * @param endDate   End date
     */
    fun setSelectedDateRange(startDate: Calendar, endDate: Calendar)

    /**
     * Sets number of days only when date selection mode is <B>free_range</B>. If date selection mode is not set to `free_range`
     * then exception will be thrown. Default number of days selection is 7 days from the selected date.
     * @param numberOfDaysSelection - Number of days that needs to be selected from the selected date.
     */
    fun setFixedDaysSelection(numberOfDaysSelection: Int)

    /**
     * To get start date.
     */
    val startDate: Calendar?

    /**
     * To get end date.
     */
    val endDate: Calendar?
}