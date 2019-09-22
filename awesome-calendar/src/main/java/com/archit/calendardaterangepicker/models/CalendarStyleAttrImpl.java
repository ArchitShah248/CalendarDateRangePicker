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

public class CalendarStyleAttrImpl implements CalendarStyleAttributes {

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
    private boolean isEditable = true;

    private CalendarStyleAttrImpl(final Context context) {
        setDefAttributes(context);
    }

    public CalendarStyleAttrImpl(final Context context, final AttributeSet attributeSet) {
        setDefAttributes(context);
        setAttributes(context, attributeSet);
    }

    /**
     * To parse attributes from xml layout to configure calendar views.
     */
    public static CalendarStyleAttrImpl getDefAttributes(@NonNull final Context context) {

        final CalendarStyleAttrImpl calendarStyleAttr = new CalendarStyleAttrImpl(context);
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
    private void setDefAttributes(@NonNull final Context context) {

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

    private void setAttributes(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
        if (attributeSet != null) {
            final TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.DateRangeMonthView, 0, 0);
            try {
                titleColor = ta.getColor(R.styleable.DateRangeMonthView_title_color, titleColor);
                headerBg = ta.getDrawable(R.styleable.DateRangeMonthView_header_bg);
                weekColor = ta.getColor(R.styleable.DateRangeMonthView_week_color, weekColor);
                rangeStripColor = ta.getColor(R.styleable.DateRangeMonthView_range_color, rangeStripColor);
                selectedDateCircleColor = ta.getColor(R.styleable.DateRangeMonthView_selected_date_circle_color, selectedDateCircleColor);
                shouldEnabledTime = ta.getBoolean(R.styleable.DateRangeMonthView_enable_time_selection, false);
                isEditable = ta.getBoolean(R.styleable.DateRangeMonthView_editable, true);

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

    @Override
    public Typeface getFonts() {
        return fonts;
    }

    @Override
    public void setFonts(@NonNull final Typeface fonts) {
        this.fonts = fonts;
    }

    @Override
    public int getTitleColor() {
        return titleColor;
    }

    private void setTitleColor(final int titleColor) {
        this.titleColor = titleColor;
    }

    @NonNull
    @Override
    public Drawable getHeaderBg() {
        return headerBg;
    }

    public void setHeaderBg(@NonNull final Drawable headerBg) {
        this.headerBg = headerBg;
    }

    @Override
    public int getWeekColor() {
        return weekColor;
    }

    private void setWeekColor(final int weekColor) {
        this.weekColor = weekColor;
    }

    @Override
    public int getRangeStripColor() {
        return rangeStripColor;
    }

    private void setRangeStripColor(final int rangeStripColor) {
        this.rangeStripColor = rangeStripColor;
    }

    @Override
    public int getSelectedDateCircleColor() {
        return selectedDateCircleColor;
    }

    private void setSelectedDateCircleColor(final int selectedDateCircleColor) {
        this.selectedDateCircleColor = selectedDateCircleColor;
    }

    @Override
    public int getSelectedDateColor() {
        return selectedDateColor;
    }

    private void setSelectedDateColor(final int selectedDateColor) {
        this.selectedDateColor = selectedDateColor;
    }

    @Override
    public int getDefaultDateColor() {
        return defaultDateColor;
    }

    private void setDefaultDateColor(final int defaultDateColor) {
        this.defaultDateColor = defaultDateColor;
    }

    @Override
    public int getDisableDateColor() {
        return disableDateColor;
    }

    private void setDisableDateColor(final int disableDateColor) {
        this.disableDateColor = disableDateColor;
    }

    @Override
    public int getRangeDateColor() {
        return rangeDateColor;
    }

    private void setRangeDateColor(final int rangeDateColor) {
        this.rangeDateColor = rangeDateColor;
    }

    @Override
    public float getTextSizeTitle() {
        return textSizeTitle;
    }

    private void setTextSizeTitle(final float textSizeTitle) {
        this.textSizeTitle = textSizeTitle;
    }

    @Override
    public float getTextSizeWeek() {
        return textSizeWeek;
    }

    private void setTextSizeWeek(final float textSizeWeek) {
        this.textSizeWeek = textSizeWeek;
    }

    @Override
    public float getTextSizeDate() {
        return textSizeDate;
    }

    private void setTextSizeDate(final float textSizeDate) {
        this.textSizeDate = textSizeDate;
    }

    @Override
    public boolean isShouldEnabledTime() {
        return shouldEnabledTime;
    }

    public void setShouldEnabledTime(final boolean shouldEnabledTime) {
        this.shouldEnabledTime = shouldEnabledTime;
    }

    @Override
    public int getWeekOffset() {
        return weekOffset;
    }

    /**
     * To set week offset
     *
     * @param weekOffset
     */
    @Override
    public void setWeekOffset(final int weekOffset) {
        if (weekOffset < 0 || weekOffset > 6) {
            throw new IllegalArgumentException("Week offset can only be between 0 to 6. " +
                    "0->Sun, 1->Mon, 2->Tue, 3->Wed, 4->Thu, 5->Fri, 6->Sat");
        }
        this.weekOffset = weekOffset;
    }

    @Override
    public boolean isEditable() {
        return isEditable;
    }

    @Override
    public void setEditable(final boolean editable) {
        isEditable = editable;
    }
}
