package com.archit.calendardaterangepicker.customviews;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.archit.calendardaterangepicker.R;
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes;

import java.util.Calendar;

class AdapterEventCalendarMonths extends PagerAdapter {

    private final Context mContext;
    private final CalendarStyleAttributes mCalendarStyleAttr;
    private CalendarListener mCalendarListener;
    private final CalendarDateRangeManager mDateRangeCalendarManager;
    private final Handler mHandler;

    AdapterEventCalendarMonths(@NonNull final Context mContext,
                               @NonNull final DateRangeCalendarManagerImpl dateRangeCalendarManager,
                               @NonNull final CalendarStyleAttributes calendarStyleAttr) {
        this.mContext = mContext;
        this.mDateRangeCalendarManager = dateRangeCalendarManager;
        this.mCalendarStyleAttr = calendarStyleAttr;
        mHandler = new Handler();
    }

    @Override
    public int getCount() {
        return mDateRangeCalendarManager.getVisibleMonthDataList().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull final View view, @NonNull final Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {

        final Calendar modelObject = mDateRangeCalendarManager.getVisibleMonthDataList().get(position);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.layout_pager_month, container, false);

        final DateRangeMonthView dateRangeMonthView = layout.findViewById(R.id.cvEventCalendarView);
        dateRangeMonthView.drawCalendarForMonth(mCalendarStyleAttr, getCurrentMonth(modelObject), mDateRangeCalendarManager);
        dateRangeMonthView.setCalendarListener(calendarAdapterListener);

        container.addView(layout);
        return layout;
    }

    /**
     * To clone calendar obj and get current month calendar starting from 1st date.
     *
     * @param calendar - Calendar
     * @return - Modified calendar obj of month of 1st date.
     */
    private Calendar getCurrentMonth(final @NonNull Calendar calendar) {
        final Calendar current = (Calendar) calendar.clone();
        current.set(Calendar.DAY_OF_MONTH, 1);
        return current;
    }

    @Override
    public void destroyItem(@NonNull final ViewGroup collection, final int position, @NonNull final Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getItemPosition(@NonNull final Object object) {
        return POSITION_NONE;
    }

    private final CalendarListener calendarAdapterListener = new CalendarListener() {
        @Override
        public void onFirstDateSelected(@NonNull final Calendar startDate) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            }, 50);

            if (mCalendarListener != null) {
                mCalendarListener.onFirstDateSelected(startDate);
            }
        }

        @Override
        public void onDateRangeSelected(@NonNull final Calendar startDate, @NonNull final Calendar endDate) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            }, 50);
            if (mCalendarListener != null) {
                mCalendarListener.onDateRangeSelected(startDate, endDate);
            }
        }
    };

    void setCalendarListener(final CalendarListener calendarListener) {
        this.mCalendarListener = calendarListener;
    }

    /**
     * To redraw calendar.
     */
    void invalidateCalendar() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 50);
    }

    /**
     * To remove all selection and redraw current calendar
     */
    void resetAllSelectedViews() {
        notifyDataSetChanged();
    }

    /**
     * To set editable mode. Set true if you want user to select date range else false. Default value will be true.
     */
    void setEditable(final boolean isEditable) {
        mCalendarStyleAttr.setEditable(isEditable);
        notifyDataSetChanged();
    }

    /**
     * To get editable mode.
     */
    boolean isEditable() {
        return mCalendarStyleAttr.isEditable();
    }
}
