package com.archit.calendardaterangepicker.models;

import android.view.View;
import android.widget.RelativeLayout;

import com.archit.calendardaterangepicker.customviews.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager.DATE_FORMAT;

/**
 * Created by Archit Shah on 8/13/2017.
 */

public class DayContainer {

    public RelativeLayout rootView;
    public CustomTextView tvDate;
    public View strip;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public DayContainer(final RelativeLayout rootView) {
        this.rootView = rootView;
        strip = rootView.getChildAt(0);
        tvDate = (CustomTextView) rootView.getChildAt(1);
    }

    public static long getContainerKey(final Calendar cal) {
        final String str = simpleDateFormat.format(cal.getTime());
        return Long.parseLong(str);
    }
}
