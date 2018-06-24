package com.archit.calendardaterangepicker.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.archit.calendardaterangepicker.R;
import com.archit.calendardaterangepicker.manager.DateRangeCalendarManager;
import com.archit.calendardaterangepicker.models.CalendarStyleAttr;
import com.archit.calendardaterangepicker.models.DayContainer;
import com.archit.calendardaterangepicker.timepicker.AwesomeTimePickerDialog;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by archit.shah on 08/09/2017.
 */

public class DateRangeMonthView extends LinearLayout {

    private static final String LOG_TAG = DateRangeMonthView.class.getSimpleName();
    private Context mContext;
    private LinearLayout llDaysContainer;
    private LinearLayout llTitleWeekContainer;

    private Calendar currentCalendarMonth;

    private CalendarStyleAttr calendarStyleAttr;

    private static final PorterDuff.Mode drawableMode = PorterDuff.Mode.SRC_OVER;


    private DateRangeCalendarView.CalendarListener calendarListener;

    private DateRangeCalendarManager dateRangeCalendarManager;

    public DateRangeCalendarView.CalendarListener getCalendarListener() {
        return calendarListener;
    }

    public void setCalendarListener(DateRangeCalendarView.CalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    public DateRangeMonthView(Context context) {
        super(context);
        initView(context, null);
    }

    public DateRangeMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public DateRangeMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DateRangeMonthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    /**
     * To initialize child views
     *
     * @param context      - App context
     * @param attributeSet - Attr set
     */
    private void initView(Context context, AttributeSet attributeSet) {
        mContext = context;

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        LinearLayout mainView = (LinearLayout) layoutInflater.inflate(R.layout.layout_calendar_month, this, true);
        llDaysContainer = (LinearLayout) mainView.findViewById(R.id.llDaysContainer);
        llTitleWeekContainer = (LinearLayout) mainView.findViewById(R.id.llTitleWeekContainer);

        setListeners();

        if (isInEditMode()) {
            return;
        }

    }



    /**
     * To set listeners.
     */
    private void setListeners() {


    }


    private OnClickListener dayClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            int key = (int) view.getTag();
            final Calendar selectedCal = Calendar.getInstance();
            Date date = new Date();
            try {
                date = DateRangeCalendarManager.SIMPLE_DATE_FORMAT.parse(String.valueOf(key));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            selectedCal.setTime(date);

            Calendar minSelectedDate = dateRangeCalendarManager.getMinSelectedDate();
            Calendar maxSelectedDate = dateRangeCalendarManager.getMaxSelectedDate();

            if (minSelectedDate != null && maxSelectedDate == null) {
                maxSelectedDate = selectedCal;

                int startDateKey = DayContainer.GetContainerKey(minSelectedDate);
                int lastDateKey = DayContainer.GetContainerKey(maxSelectedDate);

                if (startDateKey == lastDateKey) {
                    minSelectedDate = maxSelectedDate;
                } else if (startDateKey > lastDateKey) {
                    Calendar temp = (Calendar) minSelectedDate.clone();
                    minSelectedDate = maxSelectedDate;
                    maxSelectedDate = temp;
                }
            } else if (maxSelectedDate == null) {
                //This will call one time only
                minSelectedDate = selectedCal;
            } else {
                minSelectedDate = selectedCal;
                maxSelectedDate = null;
            }

            dateRangeCalendarManager.setMinSelectedDate(minSelectedDate);
            dateRangeCalendarManager.setMaxSelectedDate(maxSelectedDate);
            drawCalendarForMonth(currentCalendarMonth);

            if (calendarStyleAttr.isShouldEnabledTime()) {
                final Calendar finalMinSelectedDate = minSelectedDate;
                final Calendar finalMaxSelectedDate = maxSelectedDate;
                AwesomeTimePickerDialog awesomeTimePickerDialog = new AwesomeTimePickerDialog(mContext, mContext.getString(R.string.select_time), new AwesomeTimePickerDialog.TimePickerCallback() {
                    @Override
                    public void onTimeSelected(int hours, int mins) {
                        selectedCal.set(Calendar.HOUR, hours);
                        selectedCal.set(Calendar.MINUTE, mins);

                        Log.i(LOG_TAG, "Time: " + selectedCal.getTime().toString());
                        if (calendarListener != null) {

                            if (finalMaxSelectedDate != null) {
                                calendarListener.onDateRangeSelected(finalMinSelectedDate, finalMaxSelectedDate);
                            } else {
                                calendarListener.onFirstDateSelected(finalMinSelectedDate);
                            }
                        }
                    }

                    @Override
                    public void onCancel() {
                        DateRangeMonthView.this.resetAllSelectedViews();
                    }
                });
                awesomeTimePickerDialog.showDialog();
            } else {
                Log.i(LOG_TAG, "Time: " + selectedCal.getTime().toString());
                if (maxSelectedDate != null) {
                    calendarListener.onDateRangeSelected(minSelectedDate, maxSelectedDate);
                } else {
                    calendarListener.onFirstDateSelected(minSelectedDate);
                }
            }
        }
    };

    /**
     * To draw calendar for the given month. Here calendar object should start from date of 1st.
     *
     * @param month
     */
    public void drawCalendarForMonth(CalendarStyleAttr calendarStyleAttr, Calendar month, DateRangeCalendarManager dateRangeCalendarManager) {
        this.calendarStyleAttr = calendarStyleAttr;
        this.currentCalendarMonth = (Calendar) month.clone();
        this.dateRangeCalendarManager = dateRangeCalendarManager;
        setFonts();
        setWeekTitleColor(calendarStyleAttr.getWeekColor());
        drawCalendarForMonth(currentCalendarMonth);
    }

    /**
     * To draw calendar for the given month. Here calendar object should start from date of 1st.
     *
     * @param month
     */
    private void drawCalendarForMonth(Calendar month) {

        Log.v(LOG_TAG, "Current cal: " + month.getTime().toString());
        currentCalendarMonth = (Calendar) month.clone();
        int startDay = month.get(Calendar.DAY_OF_WEEK);


        month.add(Calendar.DATE, -startDay + 1);

        for (int i = 0; i < llDaysContainer.getChildCount(); i++) {
            LinearLayout weekRow = (LinearLayout) llDaysContainer.getChildAt(i);

            for (int j = 0; j < 7; j++) {
                RelativeLayout rlDayContainer = (RelativeLayout) weekRow.getChildAt(j);

                DayContainer container = new DayContainer(rlDayContainer);

                container.tvDate.setText(String.valueOf(month.get(Calendar.DATE)));
                if (calendarStyleAttr.getFonts() != null) {
                    container.tvDate.setTypeface(calendarStyleAttr.getFonts());
                }
                Log.v(LOG_TAG, "Date: " + month.getTime().toString());
                drawDayContainer(container, month);
                month.add(Calendar.DATE, 1);
            }
        }
    }

    /**
     * To draw specific date container according to past date, today, selected or from range.
     *
     * @param container - Date container
     * @param calendar  - Calendar obj of specific date of the month.
     */
    private void drawDayContainer(DayContainer container, Calendar calendar) {

        Calendar today = Calendar.getInstance();

        int date = calendar.get(Calendar.DATE);

        if (currentCalendarMonth.get(Calendar.MONTH) != calendar.get(Calendar.MONTH)) {
            hideDayContainer(container);
        } else if (today.after(calendar) && (today.get(Calendar.DAY_OF_YEAR) != calendar.get(Calendar.DAY_OF_YEAR))) {
            disableDayContainer(container);
            container.tvDate.setText(String.valueOf(date));
        } else {

            @DateRangeCalendarManager.RANGE_TYPE
            int type = dateRangeCalendarManager.checkDateRange(calendar);
            if (type == DateRangeCalendarManager.RANGE_TYPE.START_DATE || type == DateRangeCalendarManager.RANGE_TYPE.LAST_DATE) {
                makeAsSelectedDate(container, type);
            } else if (type == DateRangeCalendarManager.RANGE_TYPE.MIDDLE_DATE) {
                makeAsRangeDate(container);
            } else {
                enabledDayContainer(container);
            }

            container.tvDate.setText(String.valueOf(date));
        }

        container.rootView.setTag(DayContainer.GetContainerKey(calendar));
    }

    /**
     * To hide date if date is from previous month.
     *
     * @param container - Container
     */
    private void hideDayContainer(DayContainer container) {
        container.tvDate.setText("");
        container.tvDate.setBackgroundColor(Color.TRANSPARENT);
        container.strip.setBackgroundColor(Color.TRANSPARENT);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.rootView.setVisibility(INVISIBLE);
        container.rootView.setOnClickListener(null);
    }

    /**
     * To disable past date. Click listener will be removed.
     *
     * @param container - Container
     */
    private void disableDayContainer(DayContainer container) {
        container.tvDate.setBackgroundColor(Color.TRANSPARENT);
        container.strip.setBackgroundColor(Color.TRANSPARENT);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.tvDate.setTextColor(calendarStyleAttr.getDisableDateColor());
        container.rootView.setVisibility(VISIBLE);
        container.rootView.setOnClickListener(null);
    }

    /**
     * To enable date by enabling click listeners.
     *
     * @param container - Container
     */
    private void enabledDayContainer(DayContainer container) {
        container.tvDate.setBackgroundColor(Color.TRANSPARENT);
        container.strip.setBackgroundColor(Color.TRANSPARENT);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.tvDate.setTextColor(calendarStyleAttr.getDefaultDateColor());
        container.rootView.setVisibility(VISIBLE);
        container.rootView.setOnClickListener(dayClickListener);
    }

    /**
     * To draw date container as selected as end selection or middle selection.
     *
     * @param container - Container
     * @param stripType - Right end date, Left end date or middle
     */
    private void makeAsSelectedDate(DayContainer container,@DateRangeCalendarManager.RANGE_TYPE int stripType) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) container.strip.getLayoutParams();

        Calendar maxDate = dateRangeCalendarManager.getMaxSelectedDate();

        if (stripType == DateRangeCalendarManager.RANGE_TYPE.START_DATE && maxDate != null) {
            GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.range_bg_left);
            mDrawable.setColor(calendarStyleAttr.getRangeStripColor());
            container.strip.setBackground(mDrawable);
            layoutParams.setMargins(20, 0, 0, 0);
        } else if (stripType == DateRangeCalendarManager.RANGE_TYPE.LAST_DATE) {
            GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.range_bg_right);
            mDrawable.setColor(calendarStyleAttr.getRangeStripColor());
            container.strip.setBackground(mDrawable);
            layoutParams.setMargins(0, 0, 20, 0);
        } else {
            container.strip.setBackgroundColor(Color.TRANSPARENT);
            layoutParams.setMargins(0, 0, 0, 0);
        }
        container.strip.setLayoutParams(layoutParams);
        GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.green_circle);
        mDrawable.setColor(calendarStyleAttr.getSelectedDateCircleColor());
        container.tvDate.setBackground(mDrawable);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.tvDate.setTextColor(calendarStyleAttr.getSelectedDateColor());
        container.rootView.setVisibility(VISIBLE);
        container.rootView.setOnClickListener(dayClickListener);
    }

    /**
     * To draw date as middle date
     *
     * @param container - Container
     */
    private void makeAsRangeDate(DayContainer container) {
        container.tvDate.setBackgroundColor(Color.TRANSPARENT);
        GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.range_bg);
        mDrawable.setColor(calendarStyleAttr.getRangeStripColor());
        container.strip.setBackground(mDrawable);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.tvDate.setTextColor(calendarStyleAttr.getRangeDateColor());
        container.rootView.setVisibility(VISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) container.strip.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        container.strip.setLayoutParams(layoutParams);
        container.rootView.setOnClickListener(dayClickListener);
    }


    /**
     * To remove all selection and redraw current calendar
     */
    public void resetAllSelectedViews() {

        dateRangeCalendarManager.setMinSelectedDate(null);
        dateRangeCalendarManager.setMaxSelectedDate(null);

        drawCalendarForMonth(currentCalendarMonth);

    }


    /**
     * To set week title color
     *
     * @param color - resource color value
     */
    public void setWeekTitleColor(@ColorInt int color) {
        for (int i = 0; i < llTitleWeekContainer.getChildCount(); i++) {
            CustomTextView textView = (CustomTextView) llTitleWeekContainer.getChildAt(i);
            textView.setTextColor(color);
        }
    }

    /**
     * To apply custom fonts to all the text views
     */
    private void setFonts() {

        drawCalendarForMonth(currentCalendarMonth);

        for (int i = 0; i < llTitleWeekContainer.getChildCount(); i++) {

            CustomTextView textView = (CustomTextView) llTitleWeekContainer.getChildAt(i);
            textView.setTypeface(calendarStyleAttr.getFonts());

        }
    }
}
