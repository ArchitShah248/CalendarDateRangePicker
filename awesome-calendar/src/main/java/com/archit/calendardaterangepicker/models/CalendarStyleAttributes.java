package com.archit.calendardaterangepicker.models;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.archit.calendardaterangepicker.R;

public interface CalendarStyleAttributes {

    Typeface getFonts();

    void setFonts(@NonNull Typeface fonts);

    int getTitleColor();

    @NonNull
    Drawable getHeaderBg();

    int getWeekColor();

    int getRangeStripColor();

    int getSelectedDateCircleColor();

    int getSelectedDateColor();

    int getDefaultDateColor();

    int getDisableDateColor();

    int getRangeDateColor();

    float getTextSizeTitle();

    float getTextSizeWeek();

    float getTextSizeDate();

    boolean isShouldEnabledTime();

    void setWeekOffset(int weekOffset);

    int getWeekOffset();

    boolean isEnabledPastDates();

    boolean isEditable();

    void setEditable(boolean editable);
}
