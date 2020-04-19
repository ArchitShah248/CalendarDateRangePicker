package com.archit.calendardaterangepicker.customviews;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.archit.calendardaterangepicker.customviews.DateRangeCalendarManager.CalendarRangeType.LAST_DATE;
import static com.archit.calendardaterangepicker.customviews.DateRangeCalendarManager.CalendarRangeType.MIDDLE_DATE;
import static com.archit.calendardaterangepicker.customviews.DateRangeCalendarManager.CalendarRangeType.NOT_IN_RANGE;
import static com.archit.calendardaterangepicker.customviews.DateRangeCalendarManager.CalendarRangeType.START_DATE;

class DateRangeCalendarManager {

    private static final String TAG = DateRangeCalendarManager.class.getSimpleName();
    private Calendar minSelectedDate, maxSelectedDate;
    private Calendar mStartSelectableDate, mEndSelectableDate;
    private final static String DATE_FORMAT = "yyyyMMdd";
    static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NOT_IN_RANGE, START_DATE, MIDDLE_DATE, LAST_DATE})
    public @interface CalendarRangeType {
        int NOT_IN_RANGE = 0;
        int START_DATE = 1;
        int MIDDLE_DATE = 2;
        int LAST_DATE = 3;
    }

    DateRangeCalendarManager(@NonNull final Calendar startSelectableDate, @NonNull final Calendar endSelectableDate) {
        setSelectableDateRange(startSelectableDate, endSelectableDate);
    }

    void setMinSelectedDate(@Nullable final Calendar minSelectedDate) {
        this.minSelectedDate = (Calendar) (minSelectedDate != null ? minSelectedDate.clone() : null);
    }

    void setMaxSelectedDate(@Nullable final Calendar maxSelectedDate) {
        this.maxSelectedDate = (Calendar) (maxSelectedDate != null ? maxSelectedDate.clone() : null);
    }

    Calendar getMaxSelectedDate() {
        return maxSelectedDate;
    }

    Calendar getMinSelectedDate() {
        return minSelectedDate;
    }

    void setSelectableDateRange(@NonNull final Calendar startDate, @NonNull final Calendar endDate) {
        mStartSelectableDate = (Calendar) startDate.clone();
        CalendarRangeUtils.resetTime(mStartSelectableDate, START_DATE);
        mEndSelectableDate = (Calendar) endDate.clone();
        CalendarRangeUtils.resetTime(mEndSelectableDate, LAST_DATE);
    }

    /**
     * To check whether date belongs to range or not
     *
     * @return Date type
     */
    @CalendarRangeType
    int checkDateRange(@NonNull final Calendar selectedDate) {

        final String dateStr = SIMPLE_DATE_FORMAT.format(selectedDate.getTime());

        if (minSelectedDate != null && maxSelectedDate == null) {
            final String minDateStr = SIMPLE_DATE_FORMAT.format(minSelectedDate.getTime());
            if (dateStr.equalsIgnoreCase(minDateStr)) {
                return START_DATE;
            } else {
                return NOT_IN_RANGE;
            }
        } else if (minSelectedDate != null) {
            //Min date and Max date are selected
            final long selectedDateVal = Long.parseLong(dateStr);

            final String minDateStr = SIMPLE_DATE_FORMAT.format(minSelectedDate.getTime());
            final String maxDateStr = SIMPLE_DATE_FORMAT.format(maxSelectedDate.getTime());

            final long minDateVal = Long.parseLong(minDateStr);
            final long maxDateVal = Long.parseLong(maxDateStr);

            if (selectedDateVal == minDateVal) {
                return START_DATE;
            } else if (selectedDateVal == maxDateVal) {
                return LAST_DATE;
            } else if (selectedDateVal > minDateVal && selectedDateVal < maxDateVal) {
                return MIDDLE_DATE;
            } else {
                return NOT_IN_RANGE;
            }
        } else {
            return NOT_IN_RANGE;
        }
    }

    boolean isSelectableDate(@NonNull final Calendar date) {
        // It would work even if date is exactly equal to one of the end cases
        final boolean isSelectable = !(date.before(mStartSelectableDate) || date.after(mEndSelectableDate));
        if (!isSelectable && checkDateRange(date) != NOT_IN_RANGE) {
            throw new IllegalArgumentException("Selected date can not be out of Selectable Date range." +
                    " Date: " + CalendarRangeUtils.printDate(date) +
                    " Min: " + CalendarRangeUtils.printDate(minSelectedDate) +
                    " Max: " + CalendarRangeUtils.printDate(maxSelectedDate));
        }
        return isSelectable;
    }
}
