package com.archit.calendardaterangepicker.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.archit.calendardaterangepicker.R;
import com.archit.calendardaterangepicker.models.DayContainer;
import com.archit.calendardaterangepicker.timepicker.AwesomeTimePickerDialog;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by archit.shah on 08/09/2017.
 */

public class DateRangeCalendarView extends LinearLayout {

    private static final String LOG_TAG = DateRangeCalendarView.class.getSimpleName();
    private Context mContext;
    private LinearLayout llDaysContainer;
    private LinearLayout llTitleWeekContainer;
    private CustomTextView tvYearTitle;
    private ImageView imgVNavLeft, imgVNavRight;
    private Locale locale;

    private Calendar currentCalendarMonth;

    private Calendar minSelectedDate, maxSelectedDate;

    private ArrayList<Integer> selectedDatesRange = new ArrayList<>();

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    private static final int STRIP_TYPE_NONE = 0;
    private static final int STRIP_TYPE_LEFT = 1;
    private static final int STRIP_TYPE_RIGHT = 2;

    private Typeface fonts;

    private int weekColor;
    private int titleColor;
    private int rangeStripColor;
    private int selectedDateCircleColor;
    private int selectedDateColor, defaultDateColor, disableDateColor, rangeDateColor;
    private boolean shouldEnabledTime = false;
    private Drawable headerBg;
    private static final PorterDuff.Mode drawableMode = PorterDuff.Mode.SRC_OVER;

    private float textSizeTitle, textSizeWeek, textSizeDate;

    public interface CalendarListener {
        void onFirstDateSelected(Calendar startDate);

        void onDateRangeSelected(Calendar startDate, Calendar endDate);
    }


    private CalendarListener calendarListener;

    public CalendarListener getCalendarListener() {
        return calendarListener;
    }

    public void setCalendarListener(CalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    public DateRangeCalendarView(Context context) {
        super(context);
        initView(context, null);
    }

    public DateRangeCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public DateRangeCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DateRangeCalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        locale = context.getResources().getConfiguration().locale;
        setAttributes(attributeSet);

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        LinearLayout mainView = (LinearLayout) layoutInflater.inflate(R.layout.layout_calendar_month, this, true);
        llDaysContainer = (LinearLayout) mainView.findViewById(R.id.llDaysContainer);
        llTitleWeekContainer = (LinearLayout) mainView.findViewById(R.id.llTitleWeekContainer);
        tvYearTitle = (CustomTextView) mainView.findViewById(R.id.tvYearTitle);
        imgVNavLeft = (ImageView) mainView.findViewById(R.id.imgVNavLeft);
        imgVNavRight = (ImageView) mainView.findViewById(R.id.imgVNavRight);
        RelativeLayout rlHeaderCalendar = (RelativeLayout) mainView.findViewById(R.id.rlHeaderCalendar);
        rlHeaderCalendar.setBackground(headerBg);

        setListeners();

        if (isInEditMode()) {
            return;
        }

        drawCalendarForMonth(getCurrentMonth(Calendar.getInstance()));

        setWeekTitleColor(weekColor);
    }

    /**
     * To parse attributes from xml layout to configure calendar views.
     *
     * @param attributeSet - Attribute set
     */
    private void setAttributes(AttributeSet attributeSet) {

        textSizeTitle = getResources().getDimension(R.dimen.text_size_title);
        textSizeWeek = getResources().getDimension(R.dimen.text_size_week);
        textSizeDate = getResources().getDimension(R.dimen.text_size_date);

        weekColor = ContextCompat.getColor(mContext, R.color.week_color);
        titleColor = ContextCompat.getColor(mContext, R.color.title_color);
        rangeStripColor = ContextCompat.getColor(mContext, R.color.range_bg_color);
        selectedDateCircleColor = ContextCompat.getColor(mContext, R.color.selected_date_circle_color);
        selectedDateColor = ContextCompat.getColor(mContext, R.color.selected_date_color);
        defaultDateColor = ContextCompat.getColor(mContext, R.color.default_date_color);
        rangeDateColor = ContextCompat.getColor(mContext, R.color.range_date_color);
        disableDateColor = ContextCompat.getColor(mContext, R.color.disable_date_color);

        if (attributeSet != null) {
            TypedArray ta = mContext.obtainStyledAttributes(attributeSet, R.styleable.DateRangeCalendarView, 0, 0);
            try {
                weekColor = ta.getColor(R.styleable.DateRangeCalendarView_week_color, weekColor);
                titleColor = ta.getColor(R.styleable.DateRangeCalendarView_title_color, titleColor);
                rangeStripColor = ta.getColor(R.styleable.DateRangeCalendarView_range_color, rangeStripColor);
                selectedDateCircleColor = ta.getColor(R.styleable.DateRangeCalendarView_selected_date_circle_color, selectedDateCircleColor);
                shouldEnabledTime = ta.getBoolean(R.styleable.DateRangeCalendarView_enable_time_selection, false);

                textSizeTitle = ta.getDimension(R.styleable.DateRangeCalendarView_text_size_title, textSizeTitle);
                textSizeWeek = ta.getDimension(R.styleable.DateRangeCalendarView_text_size_week, textSizeWeek);
                textSizeDate = ta.getDimension(R.styleable.DateRangeCalendarView_text_size_date, textSizeDate);

                selectedDateColor = ta.getColor(R.styleable.DateRangeCalendarView_selected_date_color, selectedDateColor);
                defaultDateColor = ta.getColor(R.styleable.DateRangeCalendarView_default_date_color, defaultDateColor);
                rangeDateColor = ta.getColor(R.styleable.DateRangeCalendarView_range_date_color, rangeDateColor);
                disableDateColor = ta.getColor(R.styleable.DateRangeCalendarView_disable_date_color, disableDateColor);

                headerBg = ta.getDrawable(R.styleable.DateRangeCalendarView_header_bg);

            } finally {
                ta.recycle();
            }
        }
    }


    /**
     * To set listeners.
     */
    private void setListeners() {

        imgVNavLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCalendarMonth.add(Calendar.MONTH, -1);
                Log.v("Hello", "Nav after: " + currentCalendarMonth.getTime().toString());
                drawCalendarForMonth(currentCalendarMonth);
            }
        });
        imgVNavRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCalendarMonth.add(Calendar.MONTH, 1);
                Log.v("Hello", "Nav after: " + currentCalendarMonth.getTime().toString());
                drawCalendarForMonth(currentCalendarMonth);
            }
        });
    }


    private OnClickListener dayClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
//            drawCalendarForMonth(currentCalendarMonth);

            DayContainer container = new DayContainer((RelativeLayout) view);
            int key = (int) view.getTag();
            final Calendar selectedCal = Calendar.getInstance();
            Date date = new Date();
            try {
                date = simpleDateFormat.parse(String.valueOf(key));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            selectedCal.setTime(date);

            if (minSelectedDate != null && maxSelectedDate == null) {
                maxSelectedDate = selectedCal;
                drawSelectedDateRange(minSelectedDate, maxSelectedDate);
            } else if (maxSelectedDate == null) {
                //This will call one time only
                minSelectedDate = selectedCal;
                makeAsSelectedDate(container, 0);

            } else {
                resetAllSelectedViews();
                minSelectedDate = selectedCal;
                maxSelectedDate = null;
                makeAsSelectedDate(container, 0);
            }

            if (shouldEnabledTime) {
                AwesomeTimePickerDialog awesomeTimePickerDialog = new AwesomeTimePickerDialog(mContext, mContext.getString(R.string.select_time), new AwesomeTimePickerDialog.TimePickerCallback() {
                    @Override
                    public void onTimeSelected(int hours, int mins) {
                        selectedCal.set(Calendar.HOUR, hours);
                        selectedCal.set(Calendar.MINUTE, mins);

                        Log.i(LOG_TAG, "Time: " + selectedCal.getTime().toString());
                        if (calendarListener != null ) {

                            if(minSelectedDate != null && maxSelectedDate != null) {
                                calendarListener.onDateRangeSelected(minSelectedDate, maxSelectedDate);
                            }else if(minSelectedDate != null && maxSelectedDate == null){
                                calendarListener.onFirstDateSelected(minSelectedDate);
                            }
                        }
                    }

                    @Override
                    public void onCancel() {
                        DateRangeCalendarView.this.resetAllSelectedViews();
                    }
                });
                awesomeTimePickerDialog.showDialog();
            } else {
                Log.i(LOG_TAG, "Time: " + selectedCal.getTime().toString());
                if(minSelectedDate != null && maxSelectedDate != null) {
                    calendarListener.onDateRangeSelected(minSelectedDate, maxSelectedDate);
                }else if(minSelectedDate != null && maxSelectedDate == null){
                    calendarListener.onFirstDateSelected(minSelectedDate);
                }
            }
        }
    };

    /**
     * To clone calendar obj and get current month calendar starting from 1st date.
     *
     * @param calendar - Calendar
     * @return - Modified calendar obj of month of 1st date.
     */
    private Calendar getCurrentMonth(Calendar calendar) {
        Calendar current = (Calendar) calendar.clone();
        current.set(Calendar.DAY_OF_MONTH, 1);
        return current;
    }

    /**
     * To draw calendar for the given month. Here calendar object should start from date of 1st.
     *
     * @param month
     */
    private void drawCalendarForMonth(Calendar month) {

        Log.v("Hello", "Current cal: " + month.getTime().toString());
        currentCalendarMonth = (Calendar) month.clone();
        int startDay = month.get(Calendar.DAY_OF_WEEK);

        String dateText = new DateFormatSymbols(locale).getMonths()[currentCalendarMonth.get(Calendar.MONTH)];
        dateText = dateText.substring(0, 1).toUpperCase() + dateText.subSequence(1, dateText.length());
        tvYearTitle.setText(dateText + " " + currentCalendarMonth.get(Calendar.YEAR));
        tvYearTitle.setTextColor(titleColor);

        month.add(Calendar.DATE, -startDay + 1);

        for (int i = 0; i < llDaysContainer.getChildCount(); i++) {
            LinearLayout weekRow = (LinearLayout) llDaysContainer.getChildAt(i);

            for (int j = 0; j < 7; j++) {
                RelativeLayout rlDayContainer = (RelativeLayout) weekRow.getChildAt(j);

                DayContainer container = new DayContainer(rlDayContainer);

                container.tvDate.setText(String.valueOf(month.get(Calendar.DATE)));
                if (fonts != null) {
                    container.tvDate.setTypeface(fonts);
                }
                Log.v("Hello", "Date: " + month.getTime().toString());
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
            int key = DayContainer.GetContainerKey(calendar);
            if (selectedDatesRange.indexOf(key) == 0) {
                makeAsSelectedDate(container, STRIP_TYPE_LEFT);
            } else if (selectedDatesRange.size() != 0 && selectedDatesRange.indexOf(key) == selectedDatesRange.size() - 1) {
                makeAsSelectedDate(container, STRIP_TYPE_RIGHT);
            } else if (selectedDatesRange.contains(key)) {
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
        container.tvDate.setTextColor(disableDateColor);
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
        container.tvDate.setTextColor(defaultDateColor);
        container.rootView.setVisibility(VISIBLE);
        container.rootView.setOnClickListener(dayClickListener);
    }

    /**
     * To draw date container as selected as end selection or middle selection.
     *
     * @param container - Container
     * @param stripType - Right end date, Left end date or middle
     */
    private void makeAsSelectedDate(DayContainer container, int stripType) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) container.strip.getLayoutParams();
        if (stripType == STRIP_TYPE_LEFT) {
            GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.range_bg_left);
            mDrawable.setColor(rangeStripColor);
            container.strip.setBackground(mDrawable);
            layoutParams.setMargins(20, 0, 0, 0);
        } else if (stripType == STRIP_TYPE_RIGHT) {
            GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.range_bg_right);
            mDrawable.setColor(rangeStripColor);
            container.strip.setBackground(mDrawable);
            layoutParams.setMargins(0, 0, 20, 0);
        } else {
            container.strip.setBackgroundColor(Color.TRANSPARENT);
            layoutParams.setMargins(0, 0, 0, 0);
        }
        container.strip.setLayoutParams(layoutParams);
        GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.green_circle);
        mDrawable.setColor(selectedDateCircleColor);
        container.tvDate.setBackground(mDrawable);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.tvDate.setTextColor(selectedDateColor);
        container.rootView.setVisibility(VISIBLE);
    }

    /**
     * To draw date as middle date
     *
     * @param container - Container
     */
    private void makeAsRangeDate(DayContainer container) {
        container.tvDate.setBackgroundColor(Color.TRANSPARENT);
        GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.range_bg);
        mDrawable.setColor(rangeStripColor);
        container.strip.setBackground(mDrawable);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.tvDate.setTextColor(rangeDateColor);
        container.rootView.setVisibility(VISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) container.strip.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        container.strip.setLayoutParams(layoutParams);
    }

    /**
     * To draw selected date range.
     *
     * @param startDate - Start date
     * @param lastDate  - End date
     */
    private void drawSelectedDateRange(Calendar startDate, Calendar lastDate) {

        selectedDatesRange.clear();
        int startDateKey = DayContainer.GetContainerKey(startDate);
        int lastDateKey = DayContainer.GetContainerKey(lastDate);

        Calendar temp = (Calendar) startDate.clone();

        if (startDateKey == lastDateKey) {
//            minSelectedDate = maxSelectedDate = startDate;
        } else if (startDateKey > lastDateKey) {
            maxSelectedDate = startDate;
            minSelectedDate = lastDate;

            int tempXyz = startDateKey;
            startDateKey = lastDateKey;
            lastDateKey = tempXyz;

            temp = (Calendar) lastDate.clone();
        }


        RelativeLayout rlDayContainer = (RelativeLayout) llDaysContainer.findViewWithTag(startDateKey);
        if (rlDayContainer != null && minSelectedDate.get(Calendar.MONTH) == currentCalendarMonth.get(Calendar.MONTH)) {
            DayContainer container = new DayContainer(rlDayContainer);
            int stripType = STRIP_TYPE_LEFT;
            if (startDateKey == lastDateKey) {
                stripType = STRIP_TYPE_NONE;
            }
            makeAsSelectedDate(container, stripType);
        }

        rlDayContainer = (RelativeLayout) llDaysContainer.findViewWithTag(lastDateKey);
        if (rlDayContainer != null && maxSelectedDate.get(Calendar.MONTH) == currentCalendarMonth.get(Calendar.MONTH)) {
            DayContainer container = new DayContainer(rlDayContainer);
            int stripType = STRIP_TYPE_RIGHT;
            if (startDateKey == lastDateKey) {
                stripType = STRIP_TYPE_NONE;
            }
            makeAsSelectedDate(container, stripType);
        }

        selectedDatesRange.add(startDateKey);
        temp.add(Calendar.DATE, 1);
        int nextDateKey = DayContainer.GetContainerKey(temp);
        while (lastDateKey > nextDateKey) {

            if (temp.get(Calendar.MONTH) == currentCalendarMonth.get(Calendar.MONTH)) {
                rlDayContainer = (RelativeLayout) llDaysContainer.findViewWithTag(nextDateKey);
                if (rlDayContainer != null) {
                    DayContainer container = new DayContainer(rlDayContainer);

                    makeAsRangeDate(container);
                }
            }
            selectedDatesRange.add(nextDateKey);
            temp.add(Calendar.DATE, 1);
            nextDateKey = DayContainer.GetContainerKey(temp);
        }
        selectedDatesRange.add(lastDateKey);

    }

    /**
     * To remove all selection and redraw current calendar
     */
    public void resetAllSelectedViews() {

        selectedDatesRange.clear();

        drawCalendarForMonth(currentCalendarMonth);

        minSelectedDate = null;
        maxSelectedDate = null;

    }


    /**
     * To set week title color
     *
     * @param color - resource color value
     */
    public void setWeekTitleColor(@ColorInt int color) {
        weekColor = color;
        for (int i = 0; i < llTitleWeekContainer.getChildCount(); i++) {
            CustomTextView textView = (CustomTextView) llTitleWeekContainer.getChildAt(i);
            textView.setTextColor(color);
        }
    }

    /**
     * To apply custom fonts to all the text views
     *
     * @param fonts - Typeface that you want to apply
     */
    public void setFonts(Typeface fonts) {
        this.fonts = fonts;
        if (fonts != null) {
            drawCalendarForMonth(currentCalendarMonth);
            tvYearTitle.setTypeface(fonts);

            for (int i = 0; i < llTitleWeekContainer.getChildCount(); i++) {

                CustomTextView textView = (CustomTextView) llTitleWeekContainer.getChildAt(i);
                textView.setTypeface(fonts);

            }
        }
    }
}
