package com.archit.calendardaterangepicker.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.archit.calendardaterangepicker.R;
import com.archit.calendardaterangepicker.models.CalendarStyleAttr;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DateRangeCalendarView extends RelativeLayout {

    public interface CalendarListener {
        void onFirstDateSelected(Calendar startDate);

        void onDateRangeSelected(Calendar startDate, Calendar endDate);
    }

    private CustomTextView tvYearTitle;
    private CustomTextView tvSubtitle;
    private AppCompatImageView imgVNavLeft, imgVNavRight;
    private List<Calendar> dataList = new ArrayList<>();

    private AdapterEventCalendarMonths adapterEventCalendarMonths;
    private Locale locale;

    private ViewPager vpCalendar;
    private CalendarStyleAttr calendarStyleAttr;
    private CalendarListener mCalendarListener;
    private TextView tvClear;
    private Button btConfirm;

    private final static int TOTAL_ALLOWED_MONTHS = 30;

    public DateRangeCalendarView(Context context) {
        super(context);
        initViews(context, null);
    }

    public DateRangeCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public DateRangeCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {

        locale = context.getResources().getConfiguration().locale;
        calendarStyleAttr = new CalendarStyleAttr(context, attrs);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.layout_calendar_container, this, true);

        RelativeLayout rlHeaderCalendar = findViewById(R.id.rlHeaderCalendar);
        rlHeaderCalendar.setBackground(calendarStyleAttr.getHeaderBg());

        tvYearTitle = findViewById(R.id.tvYearTitle);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        imgVNavLeft = findViewById(R.id.imgVNavLeft);
        imgVNavRight = findViewById(R.id.imgVNavRight);

        vpCalendar = findViewById(R.id.vpCalendar);

        tvClear = findViewById(R.id.tvClear);

        btConfirm = findViewById(R.id.btConfirm);

        dataList.clear();
        Calendar today = (Calendar) Calendar.getInstance().clone();
        today.add(Calendar.MONTH, -TOTAL_ALLOWED_MONTHS);

        for (int i = 0; i < TOTAL_ALLOWED_MONTHS * 2; i++) {
            dataList.add((Calendar) today.clone());
            today.add(Calendar.MONTH, 1);
        }

        adapterEventCalendarMonths = new AdapterEventCalendarMonths(context, dataList, calendarStyleAttr);
        vpCalendar.setAdapter(adapterEventCalendarMonths);
        vpCalendar.setOffscreenPageLimit(0);
        vpCalendar.setCurrentItem(TOTAL_ALLOWED_MONTHS);
        initClearAttr();
        setCalendarYearTitle(TOTAL_ALLOWED_MONTHS);
        initClearEvent();
        initSubtitleAttr();
        setListeners();
    }

    private void initClearAttr() {
        tvClear.setTextSize(calendarStyleAttr.getClearSize());
        tvClear.setTextColor(calendarStyleAttr.getClearTextColor());
        tvClear.setCompoundDrawablePadding(20);
        tvClear.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(calendarStyleAttr.getClearIcon()), null, null, null);
    }

    private void initClearEvent() {
        tvClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAllSelectedViews();
            }
        });
    }

    private void initSubtitleAttr() {
        tvSubtitle.setText(calendarStyleAttr.getSubtitle());
        tvSubtitle.setTextColor(calendarStyleAttr.getSubtitleColor());
        tvSubtitle.setVisibility(calendarStyleAttr.isSubtitleVisible() ? View.VISIBLE : View.GONE);
        tvSubtitle.setTextSize(calendarStyleAttr.getSubtitleSize());
        LayoutParams margins = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        margins.setMargins(0, calendarStyleAttr.getSubtitleMarginTop(), 0, calendarStyleAttr.getSubtitleMarginBottom());
        tvSubtitle.setLayoutParams(margins);
    }

    private void setListeners() {
        vpCalendar.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCalendarYearTitle(position);
                setNavigationHeader(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imgVNavLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int newPosition = vpCalendar.getCurrentItem() - 1;
                if (newPosition > -1) {
                    vpCalendar.setCurrentItem(newPosition);
                }
            }
        });
        imgVNavRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int newPosition = vpCalendar.getCurrentItem() + 1;
                if (newPosition < dataList.size()) {
                    vpCalendar.setCurrentItem(newPosition);
                }
            }
        });
    }


    /**
     * To set navigation header ( Left-Right button )
     *
     * @param position New page position
     */
    private void setNavigationHeader(int position) {
        imgVNavRight.setVisibility(VISIBLE);
        imgVNavLeft.setVisibility(VISIBLE);
        if (dataList.size() == 1) {
            imgVNavLeft.setVisibility(INVISIBLE);
            imgVNavRight.setVisibility(INVISIBLE);
        } else if (position == 0) {
            imgVNavLeft.setVisibility(INVISIBLE);
        } else if (position == dataList.size() - 1) {
            imgVNavRight.setVisibility(INVISIBLE);
        }
    }

    /**
     * To set calendar year title
     *
     * @param position data list position for getting date
     */
    private void setCalendarYearTitle(int position) {

        Calendar currentCalendarMonth = dataList.get(position);
        String dateText = new DateFormatSymbols(locale).getMonths()[currentCalendarMonth.get(Calendar.MONTH)];
        dateText = dateText.substring(0, 1).toUpperCase() + dateText.subSequence(1, dateText.length());

        String yearTitle = dateText + ", " + currentCalendarMonth.get(Calendar.YEAR);

        tvYearTitle.setText(yearTitle);
        tvYearTitle.setTextColor(calendarStyleAttr.getTitleColor());
    }

    /**
     * To set calendar listener
     *
     * @param calendarListener Listener
     */
    public void setCalendarListener(final CalendarListener calendarListener) {
        mCalendarListener = calendarListener;
        adapterEventCalendarMonths.setCalendarListener(mCalendarListener);
    }

    /**
     * To apply custom fonts to all the text views
     *
     * @param fonts - Typeface that you want to apply
     */
    public void setFonts(Typeface fonts) {
        tvYearTitle.setTypeface(fonts);
        tvSubtitle.setTypeface(fonts);
        calendarStyleAttr.setFonts(fonts);
        tvClear.setTypeface(fonts);
        btConfirm.setTypeface(fonts);
        adapterEventCalendarMonths.invalidateCalendar();
    }

    public void setYearTitleFont(Typeface font) {
        tvYearTitle.setTypeface(font);
    }

    public void setSubtitleFont(Typeface font) {
        tvSubtitle.setTypeface(font);
    }

    public void setCalendarFont(Typeface font) {
        calendarStyleAttr.setFonts(font);
        adapterEventCalendarMonths.invalidateCalendar();
    }

    /**
     * To remove all selection and redraw current calendar
     */
    public void resetAllSelectedViews() {
        adapterEventCalendarMonths.resetAllSelectedViews();
    }

    /**
     * To set week offset. To start week from any of the day. Default is 0 (Sunday).
     *
     * @param offset 0-Sun, 1-Mon, 2-Tue, 3-Wed, 4-Thu, 5-Fri, 6-Sat
     */
    public void setWeekOffset(int offset) {
        calendarStyleAttr.setWeekOffset(offset);
        adapterEventCalendarMonths.invalidateCalendar();
    }

    /**
     * To set left navigation ImageView drawable
     */
    public void setNavLeftImage(@NonNull Drawable leftDrawable) {
        imgVNavLeft.setImageDrawable(leftDrawable);
    }

    /**
     * To set right navigation ImageView drawable
     */
    public void setNavRightImage(@NonNull Drawable rightDrawable) {
        imgVNavRight.setImageDrawable(rightDrawable);
    }

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
    public void setSelectedDateRange(@Nullable Calendar startDate, @Nullable Calendar endDate) {
        if (startDate == null && endDate != null) {
            throw new RuntimeException("Start date can not be null if you are setting end date.");
        } else if (endDate != null && endDate.before(startDate)) {
            throw new RuntimeException("Start date can not be after end date.");
        }
        adapterEventCalendarMonths.setSelectedDate(startDate, endDate);
    }

    /**
     * To get start date.
     */
    public Calendar getStartDate() {
        return adapterEventCalendarMonths.getMinSelectedDate();
    }


    /**
     * To get end date.
     */
    public Calendar getEndDate() {
        return adapterEventCalendarMonths.getMaxSelectedDate();
    }


    /**
     * To set editable mode. Default value will be true.
     *
     * @param isEditable true if you want user to select date range else false
     */
    public void setEditable(boolean isEditable) {
        adapterEventCalendarMonths.setEditable(isEditable);
    }

    /**
     * To get editable mode.
     */
    public boolean isEditable() {
        return adapterEventCalendarMonths.isEditable();
    }


    /**
     * To provide month range to be shown to user. If start month is greater than end month than it will give {@link IllegalArgumentException}.<br>
     * <b>Note:</b> Do not call this method after calling date selection method (@method setSelectedDateRange) as it will reset date selection.
     *
     * @param startMonth Start month of the calendar
     * @param endMonth   End month of the calendar
     */
    public void setVisibleMonthRange(Calendar startMonth, Calendar endMonth) {

        if (startMonth == null) {
            throw new IllegalArgumentException("Start month can not be null.");
        }
        startMonth.set(Calendar.DATE, 1);
        startMonth.set(Calendar.HOUR, 0);
        startMonth.set(Calendar.MINUTE, 0);
        startMonth.set(Calendar.SECOND, 0);
        startMonth.set(Calendar.MILLISECOND, 0);


        if (endMonth == null) {
            throw new IllegalArgumentException("End month can not be null.");
        }
        endMonth.set(Calendar.DATE, 1);
        endMonth.set(Calendar.HOUR, 0);
        endMonth.set(Calendar.MINUTE, 0);
        endMonth.set(Calendar.SECOND, 0);
        endMonth.set(Calendar.MILLISECOND, 0);

        if (startMonth.after(endMonth)) {
            throw new IllegalArgumentException("Start month can not be greater than end month.");
        }
        dataList.clear();

        do {
            dataList.add((Calendar) startMonth.clone());
            startMonth.add(Calendar.MONTH, 1);
        }
        while (startMonth.compareTo(endMonth) != 0);

        adapterEventCalendarMonths = new AdapterEventCalendarMonths(getContext(), dataList, calendarStyleAttr);
        vpCalendar.setAdapter(adapterEventCalendarMonths);
        vpCalendar.setOffscreenPageLimit(0);
        vpCalendar.setCurrentItem(0);
        setCalendarYearTitle(0);
        setNavigationHeader(0);
        adapterEventCalendarMonths.setCalendarListener(mCalendarListener);
    }

    public void setBtConfirmListener(OnClickListener listener) {
        btConfirm.setOnClickListener(listener);
    }
}
