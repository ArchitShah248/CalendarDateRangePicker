package com.archit.calendardaterangepicker.models;

import android.view.View;
import android.widget.RelativeLayout;

import com.archit.calendardaterangepicker.customviews.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Archit Shah on 8/13/2017.
 */

public class DayContainer {

    public RelativeLayout rootView;
    public CustomTextView tvDate;
    public View strip;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    public DayContainer(RelativeLayout rootView) {
        this.rootView = rootView;
        strip = rootView.getChildAt(0);
        tvDate = (CustomTextView) rootView.getChildAt(1);
    }

    public static int GetContainerKey(Calendar cal) {
//            Calendar calendar = (Calendar) cal.clone();
//            calendar.set(Calendar.HOUR, 0);
//            calendar.set(Calendar.MINUTE, 0);
//            calendar.set(Calendar.SECOND, 0);
//            String key = String.valueOf(cal.getTime().getTime());

        String str = simpleDateFormat.format(cal.getTime());
        int key = Integer.valueOf(str);
        return key;
    }


}
