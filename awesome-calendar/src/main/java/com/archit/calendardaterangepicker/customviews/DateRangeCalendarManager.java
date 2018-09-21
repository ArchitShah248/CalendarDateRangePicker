package com.archit.calendardaterangepicker.customviews;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.archit.calendardaterangepicker.customviews.DateRangeCalendarManager.RANGE_TYPE.LAST_DATE;
import static com.archit.calendardaterangepicker.customviews.DateRangeCalendarManager.RANGE_TYPE.MIDDLE_DATE;
import static com.archit.calendardaterangepicker.customviews.DateRangeCalendarManager.RANGE_TYPE.NOT_IN_RANGE;
import static com.archit.calendardaterangepicker.customviews.DateRangeCalendarManager.RANGE_TYPE.START_DATE;

class DateRangeCalendarManager {

    private Calendar minSelectedDate, maxSelectedDate;
    private List<Calendar> calendarMonths = new ArrayList<>();
    private final static String DATE_FORMAT = "yyyyMMdd";
    public static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NOT_IN_RANGE, START_DATE, MIDDLE_DATE, LAST_DATE})
    public @interface RANGE_TYPE {
        int NOT_IN_RANGE = 0;
        int START_DATE = 1;
        int MIDDLE_DATE = 2;
        int LAST_DATE = 3;
    }

    public DateRangeCalendarManager() {
    }

    public void setMinSelectedDate(Calendar minSelectedDate) {
        this.minSelectedDate = minSelectedDate;
    }

    public void setMaxSelectedDate(Calendar maxSelectedDate) {
        this.maxSelectedDate = maxSelectedDate;
    }

    public Calendar getMaxSelectedDate() {
        return maxSelectedDate;
    }

    public Calendar getMinSelectedDate() {
        return minSelectedDate;
    }

    /**
     * To check whether date belongs to range or not
     *
     * @return Date type
     */
    @RANGE_TYPE
    public int checkDateRange(Calendar selectedDate) {

        String dateStr = SIMPLE_DATE_FORMAT.format(selectedDate.getTime());

        if (minSelectedDate != null && maxSelectedDate == null) {
            String minDateStr = SIMPLE_DATE_FORMAT.format(minSelectedDate.getTime());
            if (dateStr.equalsIgnoreCase(minDateStr)) {
                return START_DATE;
            } else {
                return NOT_IN_RANGE;
            }
        } else if (minSelectedDate != null) {
            //Min date and Max date are selected
            long selectedDateVal = Long.valueOf(dateStr);

            String minDateStr = SIMPLE_DATE_FORMAT.format(minSelectedDate.getTime());
            String maxDateStr = SIMPLE_DATE_FORMAT.format(maxSelectedDate.getTime());

            long minDateVal = Long.valueOf(minDateStr);
            long maxDateVal = Long.valueOf(maxDateStr);

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
}
