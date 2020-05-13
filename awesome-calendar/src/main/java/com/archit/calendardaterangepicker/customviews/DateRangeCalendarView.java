package com.archit.calendardaterangepicker.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.ViewPager;

import com.archit.calendardaterangepicker.R;
import com.archit.calendardaterangepicker.models.CalendarStyleAttrImpl;
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class DateRangeCalendarView extends LinearLayout implements DateRangeCalendarViewApi {

    private CustomTextView tvYearTitle;
    private AppCompatImageView imgVNavLeft, imgVNavRight;
    private AdapterEventCalendarMonths adapterEventCalendarMonths;
    private Locale locale;
    private ViewPager vpCalendar;
    private CalendarStyleAttributes calendarStyleAttr;
    private DateRangeCalendarManagerImpl mDateRangeCalendarManager;

    private final static int TOTAL_ALLOWED_MONTHS = 30;

    public DateRangeCalendarView(final Context context) {
        super(context);
        initViews(context, null);
    }

    public DateRangeCalendarView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public DateRangeCalendarView(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    private void initViews(final Context context, final AttributeSet attrs) {
        locale = context.getResources().getConfiguration().locale;
        calendarStyleAttr = new CalendarStyleAttrImpl(context, attrs);
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.layout_calendar_container, this, true);
        final RelativeLayout rlHeaderCalendar = findViewById(R.id.rlHeaderCalendar);
        rlHeaderCalendar.setBackground(calendarStyleAttr.getHeaderBg());
        tvYearTitle = findViewById(R.id.tvYearTitle);
        tvYearTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, calendarStyleAttr.getTextSizeTitle());
        imgVNavLeft = findViewById(R.id.imgVNavLeft);
        imgVNavRight = findViewById(R.id.imgVNavRight);
        vpCalendar = findViewById(R.id.vpCalendar);

        final Calendar defStartMonth = (Calendar) Calendar.getInstance().clone();
        defStartMonth.add(Calendar.MONTH, -TOTAL_ALLOWED_MONTHS);

        final Calendar defEndMonth = (Calendar) Calendar.getInstance().clone();
        defEndMonth.add(Calendar.MONTH, TOTAL_ALLOWED_MONTHS);

        mDateRangeCalendarManager = new DateRangeCalendarManagerImpl(defStartMonth, defEndMonth);

        adapterEventCalendarMonths = new AdapterEventCalendarMonths(context, mDateRangeCalendarManager, calendarStyleAttr);
        vpCalendar.setAdapter(adapterEventCalendarMonths);
        vpCalendar.setOffscreenPageLimit(0);
        vpCalendar.setCurrentItem(TOTAL_ALLOWED_MONTHS);
        setCalendarYearTitle(TOTAL_ALLOWED_MONTHS);
        setListeners();
    }

    private void setListeners() {

        vpCalendar.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                setCalendarYearTitle(position);
                setNavigationHeader(position);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
        imgVNavLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                final int newPosition = vpCalendar.getCurrentItem() - 1;
                if (newPosition > -1) {
                    vpCalendar.setCurrentItem(newPosition);
                }
            }
        });
        imgVNavRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                final int newPosition = vpCalendar.getCurrentItem() + 1;
                if (newPosition < mDateRangeCalendarManager.getVisibleMonthDataList().size()) {
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
    private void setNavigationHeader(final int position) {
        imgVNavRight.setVisibility(VISIBLE);
        imgVNavLeft.setVisibility(VISIBLE);
        if (mDateRangeCalendarManager.getVisibleMonthDataList().size() == 1) {
            imgVNavLeft.setVisibility(INVISIBLE);
            imgVNavRight.setVisibility(INVISIBLE);
        } else if (position == 0) {
            imgVNavLeft.setVisibility(INVISIBLE);
        } else if (position == mDateRangeCalendarManager.getVisibleMonthDataList().size() - 1) {
            imgVNavRight.setVisibility(INVISIBLE);
        }
    }

    /**
     * To set calendar year title
     *
     * @param position data list position for getting date
     */
    private void setCalendarYearTitle(final int position) {
        final Calendar currentCalendarMonth = mDateRangeCalendarManager.getVisibleMonthDataList().get(position);
        String dateText = new DateFormatSymbols(locale).getMonths()[currentCalendarMonth.get(Calendar.MONTH)];
        dateText = dateText.substring(0, 1).toUpperCase() + dateText.subSequence(1, dateText.length());

        final String yearTitle = dateText + " " + currentCalendarMonth.get(Calendar.YEAR);

        tvYearTitle.setText(yearTitle);
        tvYearTitle.setTextColor(calendarStyleAttr.getTitleColor());
    }

    /**
     * To set calendar listener
     *
     * @param calendarListener Listener
     */
    @Override
    public void setCalendarListener(@NonNull final CalendarListener calendarListener) {
        adapterEventCalendarMonths.setCalendarListener(calendarListener);
    }

    /**
     * To apply custom fonts to all the text views
     *
     * @param fonts - Typeface that you want to apply
     */
    @Override
    public void setFonts(@NonNull final Typeface fonts) {
        tvYearTitle.setTypeface(fonts);
        calendarStyleAttr.setFonts(fonts);
        adapterEventCalendarMonths.invalidateCalendar();
    }

    /**
     * To remove all selection and redraw current calendar
     */
    @Override
    public void resetAllSelectedViews() {
        mDateRangeCalendarManager.resetSelectedDateRange();
        adapterEventCalendarMonths.resetAllSelectedViews();
    }

    /**
     * To set week offset. To start week from any of the day. Default is 0 (Sunday).
     *
     * @param offset 0-Sun, 1-Mon, 2-Tue, 3-Wed, 4-Thu, 5-Fri, 6-Sat
     */
    @Override
    public void setWeekOffset(final int offset) {
        calendarStyleAttr.setWeekOffset(offset);
        adapterEventCalendarMonths.invalidateCalendar();
    }

    /**
     * To set left navigation ImageView drawable
     */
    @Override
    public void setNavLeftImage(@NonNull final Drawable leftDrawable) {
        imgVNavLeft.setImageDrawable(leftDrawable);
    }

    /**
     * To set right navigation ImageView drawable
     */
    @Override
    public void setNavRightImage(@NonNull final Drawable rightDrawable) {
        imgVNavRight.setImageDrawable(rightDrawable);
    }

    /**
     * Sets start and end date.<br>
     * <B>Note:</B><br>
     * You can not set end date before start date.<br>
     * If you are setting custom month range than do not call this before calling (@method setVisibleMonthRange).<br>
     *
     * @param startDate Start date
     * @param endDate   End date
     */
    @Override
    public void setSelectedDateRange(@NonNull final Calendar startDate, @NonNull final Calendar endDate) {
        mDateRangeCalendarManager.setSelectedDateRange(startDate, endDate);
        adapterEventCalendarMonths.notifyDataSetChanged();
    }

    /**
     * To get start date.
     */
    @Override
    public Calendar getStartDate() {
        return mDateRangeCalendarManager.getMinSelectedDate();
    }

    /**
     * To get end date.
     */
    @Override
    public Calendar getEndDate() {
        return mDateRangeCalendarManager.getMaxSelectedDate();
    }

    /**
     * To set editable mode. Default value will be true.
     *
     * @param isEditable true if you want user to select date range else false
     */
    @Override
    public void setEditable(final boolean isEditable) {
        adapterEventCalendarMonths.setEditable(isEditable);
    }

    /**
     * To get editable mode.
     */
    @Override
    public boolean isEditable() {
        return adapterEventCalendarMonths.isEditable();
    }

    /**
     * To provide month range to be shown to user. If start month is greater than end month than it will give {@link IllegalArgumentException}.<br>
     * By default it will also make selectable date range as per visible month's dates. If you want to customize the selectable date range then
     * use {@link #setSelectableDateRange(Calendar, Calendar)}.<br><br>
     * <b>Note:</b> Do not call this method after calling date selection method {@link #setSelectableDateRange(Calendar, Calendar)}
     * / {@link #setSelectedDateRange(Calendar, Calendar)} as it will reset date selection.
     *
     * @param startMonth Start month of the calendar
     * @param endMonth   End month of the calendar
     */
    @Override
    public void setVisibleMonthRange(@NonNull final Calendar startMonth, @NonNull final Calendar endMonth) {
        mDateRangeCalendarManager.setVisibleMonths(startMonth, endMonth);
        adapterEventCalendarMonths.notifyDataSetChanged();
        vpCalendar.setCurrentItem(0);
        setCalendarYearTitle(0);
        setNavigationHeader(0);
    }

    /**
     * To set current visible month.
     *
     * @param calendar Month to be set as current
     */
    @Override
    public void setCurrentMonth(@NonNull final Calendar calendar) {
        vpCalendar.setCurrentItem(mDateRangeCalendarManager.getMonthIndex(calendar));
    }

    @Override
    public void setSelectableDateRange(@NonNull final Calendar startDate, @NonNull final Calendar endDate) {
        mDateRangeCalendarManager.setSelectableDateRange(startDate, endDate);
        adapterEventCalendarMonths.notifyDataSetChanged();
    }

    @Override
    public void setFixedDaysSelection(final int numberOfDaysSelection) {
        calendarStyleAttr.setFixedDaysSelectionNumber(numberOfDaysSelection);
        adapterEventCalendarMonths.invalidateCalendar();
    }
}
