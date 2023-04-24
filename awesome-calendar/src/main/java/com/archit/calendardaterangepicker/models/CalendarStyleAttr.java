package com.archit.calendardaterangepicker.models;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.TotalCaptureResult;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.archit.calendardaterangepicker.R;

public class CalendarStyleAttr {

    private Typeface fonts;
    private Drawable headerBg;

    private int titleColor;

    private int weekColor;
    private int rangeStripColor;
    private int selectedDateCircleColor;
    private int selectedDateColor, defaultDateColor, disableDateColor, rangeDateColor;
    private float textSizeTitle, textSizeWeek, textSizeDate;
    private boolean shouldEnabledTime = false;
    private int weekOffset = 0;
    private boolean enabledPastDates = false;
    private boolean isEditable = true;
    private boolean singleDateSelection = false;
    public CalendarStyleAttr(Context context) {
        setDefAttributes(context);
    }

    public CalendarStyleAttr(Context context, AttributeSet attributeSet) {
        setDefAttributes(context);
        setAttributes(context, attributeSet);
    }

    /**
     * To parse attributes from xml layout to configure calendar views.
     */
    public static CalendarStyleAttr getDefAttributes(Context context) {

        CalendarStyleAttr calendarStyleAttr = new CalendarStyleAttr(context);
        calendarStyleAttr.setTextSizeTitle(context.getResources().getDimension(R.dimen.text_size_title));
        calendarStyleAttr.setTextSizeWeek(context.getResources().getDimension(R.dimen.text_size_week));
        calendarStyleAttr.setTextSizeDate(context.getResources().getDimension(R.dimen.text_size_date));

        calendarStyleAttr.setWeekColor(ContextCompat.getColor(context, R.color.week_color));
        calendarStyleAttr.setRangeStripColor(ContextCompat.getColor(context, R.color.range_bg_color));
        calendarStyleAttr.setSelectedDateCircleColor(ContextCompat.getColor(context, R.color.selected_date_circle_color));
        calendarStyleAttr.setSelectedDateColor(ContextCompat.getColor(context, R.color.selected_date_color));
        calendarStyleAttr.setDefaultDateColor(ContextCompat.getColor(context, R.color.default_date_color));
        calendarStyleAttr.setRangeDateColor(ContextCompat.getColor(context, R.color.range_date_color));
        calendarStyleAttr.setDisableDateColor(ContextCompat.getColor(context, R.color.disable_date_color));

        return calendarStyleAttr;
    }

    /**
     * To parse attributes from xml layout to configure calendar views.
     */
    public void setDefAttributes(Context context) {

        setTextSizeTitle(context.getResources().getDimension(R.dimen.text_size_title));
        setTextSizeWeek(context.getResources().getDimension(R.dimen.text_size_week));
        setTextSizeDate(context.getResources().getDimension(R.dimen.text_size_date));

        setTitleColor(ContextCompat.getColor(context, R.color.title_color));
        setWeekColor(ContextCompat.getColor(context, R.color.week_color));
        setRangeStripColor(ContextCompat.getColor(context, R.color.range_bg_color));
        setSelectedDateCircleColor(ContextCompat.getColor(context, R.color.selected_date_circle_color));
        setSelectedDateColor(ContextCompat.getColor(context, R.color.selected_date_color));
        setDefaultDateColor(ContextCompat.getColor(context, R.color.default_date_color));
        setRangeDateColor(ContextCompat.getColor(context, R.color.range_date_color));
        setDisableDateColor(ContextCompat.getColor(context, R.color.disable_date_color));

    }

    public void setAttributes(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.DateRangeMonthView, 0, 0);
            try {
                titleColor = ta.getColor(R.styleable.DateRangeMonthView_title_color, titleColor);
                headerBg = ta.getDrawable(R.styleable.DateRangeMonthView_header_bg);
                weekColor = ta.getColor(R.styleable.DateRangeMonthView_week_color, weekColor);
                rangeStripColor = ta.getColor(R.styleable.DateRangeMonthView_range_color, rangeStripColor);
                selectedDateCircleColor = ta.getColor(R.styleable.DateRangeMonthView_selected_date_circle_color, selectedDateCircleColor);
                shouldEnabledTime = ta.getBoolean(R.styleable.DateRangeMonthView_enable_time_selection, false);
                enabledPastDates = ta.getBoolean(R.styleable.DateRangeMonthView_enable_past_date,false);
                isEditable = ta.getBoolean(R.styleable.DateRangeMonthView_editable,true);
                singleDateSelection = ta.getBoolean(R.styleable.DateRangeMonthView_single_date,false);
                
                textSizeTitle = ta.getDimension(R.styleable.DateRangeMonthView_text_size_title, textSizeTitle);
                textSizeWeek = ta.getDimension(R.styleable.DateRangeMonthView_text_size_week, textSizeWeek);
                textSizeDate = ta.getDimension(R.styleable.DateRangeMonthView_text_size_date, textSizeDate);

                selectedDateColor = ta.getColor(R.styleable.DateRangeMonthView_selected_date_color, selectedDateColor);
                defaultDateColor = ta.getColor(R.styleable.DateRangeMonthView_default_date_color, defaultDateColor);
                rangeDateColor = ta.getColor(R.styleable.DateRangeMonthView_range_date_color, rangeDateColor);
                disableDateColor = ta.getColor(R.styleable.DateRangeMonthView_disable_date_color, disableDateColor);
                setWeekOffset(ta.getColor(R.styleable.DateRangeMonthView_week_offset, 0));


            } finally {
                ta.recycle();
            }
        }
    }

    public Typeface getFonts() {
        return fonts;
    }

    public void setFonts(Typeface fonts) {
        this.fonts = fonts;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public Drawable getHeaderBg() {
        return headerBg;
    }

    public void setHeaderBg(Drawable headerBg) {
        this.headerBg = headerBg;
    }

    public int getWeekColor() {
        return weekColor;
    }

    public void setWeekColor(int weekColor) {
        this.weekColor = weekColor;
    }

    public int getRangeStripColor() {
        return rangeStripColor;
    }

    public void setRangeStripColor(int rangeStripColor) {
        this.rangeStripColor = rangeStripColor;
    }

    public int getSelectedDateCircleColor() {
        return selectedDateCircleColor;
    }

    public void setSelectedDateCircleColor(int selectedDateCircleColor) {
        this.selectedDateCircleColor = selectedDateCircleColor;
    }

    public int getSelectedDateColor() {
        return selectedDateColor;
    }

    public void setSelectedDateColor(int selectedDateColor) {
        this.selectedDateColor = selectedDateColor;
    }

    public int getDefaultDateColor() {
        return defaultDateColor;
    }

    public void setDefaultDateColor(int defaultDateColor) {
        this.defaultDateColor = defaultDateColor;
    }

    public int getDisableDateColor() {
        return disableDateColor;
    }

    public void setDisableDateColor(int disableDateColor) {
        this.disableDateColor = disableDateColor;
    }

    public int getRangeDateColor() {
        return rangeDateColor;
    }

    public void setRangeDateColor(int rangeDateColor) {
        this.rangeDateColor = rangeDateColor;
    }

    public float getTextSizeTitle() {
        return textSizeTitle;
    }

    public void setTextSizeTitle(float textSizeTitle) {
        this.textSizeTitle = textSizeTitle;
    }

    public float getTextSizeWeek() {
        return textSizeWeek;
    }

    public void setTextSizeWeek(float textSizeWeek) {
        this.textSizeWeek = textSizeWeek;
    }

    public float getTextSizeDate() {
        return textSizeDate;
    }

    public void setTextSizeDate(float textSizeDate) {
        this.textSizeDate = textSizeDate;
    }

    public boolean isShouldEnabledTime() {
        return shouldEnabledTime;
    }

    public void setShouldEnabledTime(boolean shouldEnabledTime) {
        this.shouldEnabledTime = shouldEnabledTime;
    }

    public int getWeekOffset() {
        return weekOffset;
    }

    public boolean isEnabledPastDates() {
        return enabledPastDates;
    }

    public void setEnabledPastDates(boolean enabledPastDates) {
        this.enabledPastDates = enabledPastDates;
    }

    /**
     * To set week offset
     *
     * @param weekOffset
     */
    public void setWeekOffset(int weekOffset) {
        if (weekOffset < 0 || weekOffset > 6) {
            throw new RuntimeException("Week offset can only be between 0 to 6. " +
                    "0->Sun, 1->Mon, 2->Tue, 3->Wed, 4->Thu, 5->Fri, 6->Sat");
        }
        this.weekOffset = weekOffset;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }
    
    public boolean isSingleDateSelection() {
        return singleDateSelection;
    }

    public void setSingleDateSelection(boolean editable) {
        singleDateSelection = editable;
    }
}
