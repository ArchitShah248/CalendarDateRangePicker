package com.archit.calendardaterangepicker.customviews;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.util.Calendar;

public interface DateRangeCalendarViewApi {

    interface CalendarListener {
        void onFirstDateSelected(@NonNull final Calendar startDate);

        void onDateRangeSelected(@NonNull final Calendar startDate, @NonNull final Calendar endDate);
    }

    /**
     * To set calendar listener
     *
     * @param calendarListener Listener
     */
    public void setCalendarListener(@NonNull CalendarListener calendarListener);

    /**
     * To apply custom fonts to all the text views
     *
     * @param fonts - Typeface that you want to apply
     */
    public void setFonts(@NonNull Typeface fonts);

    /**
     * To remove all selection and redraw current calendar
     */
    public void resetAllSelectedViews();

    /**
     * To set week offset. To start week from any of the day. Default is 0 (Sunday).
     *
     * @param offset 0-Sun, 1-Mon, 2-Tue, 3-Wed, 4-Thu, 5-Fri, 6-Sat
     */
    public void setWeekOffset(int offset);

    /**
     * To set left navigation ImageView drawable
     */
    public void setNavLeftImage(@NonNull Drawable leftDrawable);

    /**
     * To set right navigation ImageView drawable
     */
    public void setNavRightImage(@NonNull Drawable rightDrawable);

    /**
     * Sets start and end date.<br>
     * <B>Note:</B><br>
     * You can not set null start date with valid end date.<br>
     * You can not set end date before start date.<br>
     * If you are setting custom month range than do not call this before calling (@method setVisibleMonthRange).<br>
     *
     * @param startDate Start date
     * @param endDate   End date
     */
    public void setSelectedDateRange(@NonNull Calendar startDate, @NonNull Calendar endDate);

    /**
     * To get start date.
     */
    @NonNull
    public Calendar getStartDate();

    /**
     * To get end date.
     */
    @NonNull
    public Calendar getEndDate();

    /**
     * To set editable mode. Default value will be true.
     *
     * @param isEditable true if you want user to select date range else false
     */
    public void setEditable(boolean isEditable);

    /**
     * To get editable mode.
     */
    public boolean isEditable();

    /**
     * To provide month range to be shown to user. If start month is greater than end month than it will give {@link IllegalArgumentException}.<br>
     * <b>Note:</b> Do not call this method after calling date selection method (@method setSelectedDateRange) as it will reset date selection.
     *
     * @param startMonth Start month of the calendar
     * @param endMonth   End month of the calendar
     */
    public void setVisibleMonthRange(@NonNull Calendar startMonth, @NonNull Calendar endMonth);

    /**
     * To set current visible month.
     *
     * @param calendar Month to be set as current
     */
    public void setCurrentMonth(@NonNull Calendar calendar);
}
