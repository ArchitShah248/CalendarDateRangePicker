package com.archit.calendardaterangepicker.customviews

import android.graphics.Typeface
import android.view.View
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

interface DateView {

    interface OnDateClickListener {
        fun onDateClicked(view: View, selectedDate: Calendar)
    }

    enum class DateState {
        /**
         * State for hiding date in month.
         */
        HIDDEN,

        /**
         * State when date is visible but not selectable.
         */
        DISABLE,

        /**
         * State when date is not selected but can be selected.
         */
        SELECTABLE,

        /**
         * Selected start date.
         */
        START,

        /**
         * Selected end date.
         */
        END,

        /**
         * State when date falls in between start and end date
         */
        MIDDLE,

        /**
         * When start and end dates are same
         */
        START_END_SAME
    }

    var dateTextSize: Float
    var stripColor: Int
    var selectedDateCircleColor: Int
    var selectedDateColor: Int
    var defaultDateColor: Int
    var disableDateColor: Int
    var rangeDateColor: Int

    fun setDateTag(date: Calendar)

    fun setDateText(date: String)

    fun setDateStyleAttributes(attr: CalendarStyleAttributes)

    fun setTypeface(typeface: Typeface)

    fun updateDateBackground(dateState: DateState)

    fun refreshLayout()

    fun setDateClickListener(listener: OnDateClickListener)

    companion object {

        fun getContainerKey(cal: Calendar): Long {
            val simpleDateFormat = SimpleDateFormat(CalendarDateRangeManager.DATE_FORMAT, Locale.getDefault())
            val str = simpleDateFormat.format(cal.time)
            return str.toLong()
        }
    }
}