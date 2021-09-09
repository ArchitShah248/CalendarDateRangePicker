package com.archit.calendardaterangepicker.customviews

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.archit.calendardaterangepicker.R.id
import com.archit.calendardaterangepicker.R.layout
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes
import java.util.Calendar

internal class AdapterEventCalendarMonths(private val mContext: Context,
                                          calendarDateRangeManager: CalendarDateRangeManagerImpl,
                                          calendarStyleAttr: CalendarStyleAttributes) : PagerAdapter() {
    private val mCalendarStyleAttr: CalendarStyleAttributes
    private var mCalendarListener: CalendarListener? = null
    private val mDateRangeCalendarManager: CalendarDateRangeManager
    private val mHandler = Handler()
    override fun getCount(): Int {
        return mDateRangeCalendarManager.getVisibleMonthDataList().size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val modelObject = mDateRangeCalendarManager.getVisibleMonthDataList()[position]
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(layout.layout_pager_month, container, false) as ViewGroup
        val dateRangeMonthView: DateRangeMonthView = layout.findViewById(id.cvEventCalendarView)
        dateRangeMonthView.drawCalendarForMonth(mCalendarStyleAttr, getCurrentMonth(modelObject), mDateRangeCalendarManager)
        dateRangeMonthView.setCalendarListener(calendarAdapterListener)
        container.addView(layout)
        return layout
    }

    /**
     * To clone calendar obj and get current month calendar starting from 1st date.
     *
     * @param calendar - Calendar
     * @return - Modified calendar obj of month of 1st date.
     */
    private fun getCurrentMonth(calendar: Calendar): Calendar {
        val current = calendar.clone() as Calendar
        current[Calendar.DAY_OF_MONTH] = 1
        return current
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    private val calendarAdapterListener: CalendarListener = object : CalendarListener {
        override fun onFirstDateSelected(startDate: Calendar) {
            mHandler.postDelayed({ notifyDataSetChanged() }, 50)
            if (mCalendarListener != null) {
                mCalendarListener!!.onFirstDateSelected(startDate)
            }
        }

        override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
            mHandler.postDelayed({ notifyDataSetChanged() }, 50)
            if (mCalendarListener != null) {
                mCalendarListener!!.onDateRangeSelected(startDate, endDate)
            }
        }
    }

    fun setCalendarListener(calendarListener: CalendarListener?) {
        mCalendarListener = calendarListener
    }

    /**
     * To redraw calendar.
     */
    fun invalidateCalendar() {
        mHandler.postDelayed({ notifyDataSetChanged() }, 50)
    }

    /**
     * To remove all selection and redraw current calendar
     */
    fun resetAllSelectedViews() {
        notifyDataSetChanged()
    }

    /**
     * To get editable mode.
     */
    /**
     * To set editable mode. Set true if you want user to select date range else false. Default value will be true.
     */
    var isEditable: Boolean
        get() = mCalendarStyleAttr.isEditable
        set(isEditable) {
            mCalendarStyleAttr.isEditable = isEditable
            notifyDataSetChanged()
        }

    init {
        mDateRangeCalendarManager = calendarDateRangeManager
        mCalendarStyleAttr = calendarStyleAttr
    }
}