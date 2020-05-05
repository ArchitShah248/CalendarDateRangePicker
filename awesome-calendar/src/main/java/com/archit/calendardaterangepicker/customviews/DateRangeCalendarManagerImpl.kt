package com.archit.calendardaterangepicker.customviews

import android.util.Log
import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager.CalendarRangeType
import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager.Companion.DATE_FORMAT
import com.archit.calendardaterangepicker.customviews.CalendarRangeUtils.Companion.isDateSame
import com.archit.calendardaterangepicker.customviews.CalendarRangeUtils.Companion.printDate
import com.archit.calendardaterangepicker.models.DayContainer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

internal class DateRangeCalendarManagerImpl(startMonthDate: Calendar, endMonthDate: Calendar) : CalendarDateRangeManager {
    private lateinit var mStartVisibleMonth: Calendar
    private lateinit var mEndVisibleMonth: Calendar
    private lateinit var mStartSelectableDate: Calendar
    private lateinit var mEndSelectableDate: Calendar
    private var mMinSelectedDate: Calendar? = null
    private var mMaxSelectedDate: Calendar? = null
    private val mVisibleMonths = mutableListOf<Calendar>()

    companion object {
        private val TAG = DateRangeCalendarManagerImpl::class.java.simpleName
        val SIMPLE_DATE_FORMAT = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    }

    init {
        setVisibleMonths(startMonthDate, endMonthDate)
    }

    override fun getMaxSelectedDate(): Calendar? {
        return mMaxSelectedDate
    }

    override fun getMinSelectedDate(): Calendar? {
        return mMinSelectedDate
    }

    override fun getVisibleMonthDataList(): List<Calendar> {
        return mVisibleMonths
    }

    override fun getMonthIndex(month: Calendar): Int {
        for (i in mVisibleMonths.indices) {
            val item: Calendar = mVisibleMonths[i]
            if (month[Calendar.YEAR] == item.get(Calendar.YEAR)) {
                if (month[Calendar.MONTH] == item.get(Calendar.MONTH)) {
                    return i
                }
            }
        }
        throw RuntimeException("Month(" + month.time.toString() + ") is not available in the given month range.")
    }

    override fun setVisibleMonths(startMonth: Calendar, endMonth: Calendar) {
        validateDatesOrder(startMonth, endMonth)
        val startMonthDate = startMonth.clone() as Calendar
        val endMonthDate = endMonth.clone() as Calendar

        startMonthDate[Calendar.DAY_OF_MONTH] = 1
        CalendarRangeUtils.resetTime(startMonthDate, CalendarRangeType.START_DATE)

        endMonthDate[Calendar.DAY_OF_MONTH] = endMonthDate.getActualMaximum(Calendar.DAY_OF_MONTH)
        CalendarRangeUtils.resetTime(endMonthDate, CalendarRangeType.LAST_DATE)

        mStartVisibleMonth = startMonthDate.clone() as Calendar
        CalendarRangeUtils.resetTime(mStartVisibleMonth, CalendarRangeType.START_DATE)
        mEndVisibleMonth = endMonthDate.clone() as Calendar
        CalendarRangeUtils.resetTime(mEndVisibleMonth, CalendarRangeType.LAST_DATE)

        // Creating visible months data list
        mVisibleMonths.clear()
        val temp = mStartVisibleMonth.clone() as Calendar
        while (!CalendarRangeUtils.isMonthSame(temp, mEndVisibleMonth)) {
            mVisibleMonths.add(temp.clone() as Calendar)
            temp.add(Calendar.MONTH, 1)
        }
        mVisibleMonths.add(temp.clone() as Calendar)
        setSelectableDateRange(mStartVisibleMonth, mEndVisibleMonth)
    }

    override fun getStartVisibleMonth() = mStartVisibleMonth

    override fun getEndVisibleMonth() = mEndVisibleMonth

    override fun setSelectableDateRange(startDate: Calendar, endDate: Calendar) {
        validateDatesOrder(startDate, endDate)
        mStartSelectableDate = startDate.clone() as Calendar
        CalendarRangeUtils.resetTime(mStartSelectableDate, CalendarRangeType.START_DATE)
        mEndSelectableDate = endDate.clone() as Calendar
        CalendarRangeUtils.resetTime(mEndSelectableDate, CalendarRangeType.LAST_DATE)
        if (mStartSelectableDate.before(mStartVisibleMonth)) {
            throw InvalidDateException("Selectable start date ${printDate(startDate)} is out of visible months" +
                    "(${printDate(mStartVisibleMonth)} " +
                    "- ${printDate(mEndVisibleMonth)}).")
        }
        if (mEndSelectableDate.after(mEndVisibleMonth)) {
            throw InvalidDateException("Selectable end date ${printDate(endDate)} is out of visible months" +
                    "(${printDate(mStartVisibleMonth)} " +
                    "- ${printDate(mEndVisibleMonth)}).")
        }
        resetSelectedDateRange()
    }

    override fun resetSelectedDateRange() {
        this.mMinSelectedDate = null
        this.mMaxSelectedDate = null
    }

    override fun setSelectedDateRange(startDate: Calendar, endDate: Calendar?) {
        validateDatesOrder(startDate, endDate)
        if (startDate.before(mStartSelectableDate)) {
            throw InvalidDateException("Start date(${printDate(startDate)}) is out of selectable date range.")
        }
        if (endDate != null && endDate.after(mEndSelectableDate)) {
            throw InvalidDateException("End date(${printDate(endDate)}) is out of selectable date range.")
        }
        Log.i(TAG, "Selected dates: Start(${printDate(startDate)})-End(${printDate(endDate)})")
        this.mMinSelectedDate = startDate.clone() as Calendar
        this.mMaxSelectedDate = endDate?.clone() as Calendar?
    }

    /**
     * To check whether date belongs to range or not
     *
     * @return Date type
     */
    override fun checkDateRange(selectedDate: Calendar): CalendarRangeType {
        return if (mMinSelectedDate != null && mMaxSelectedDate == null) {
            if (isDateSame(selectedDate, mMinSelectedDate!!)) {
                CalendarRangeType.START_DATE
            } else {
                CalendarRangeType.NOT_IN_RANGE
            }
        } else if (mMinSelectedDate != null) {
            //Min date and Max date are selected
            val selectedDateVal = DayContainer.getContainerKey(selectedDate)
            val minDateVal = DayContainer.getContainerKey(mMinSelectedDate)
            val maxDateVal = DayContainer.getContainerKey(mMaxSelectedDate)
            if (isDateSame(selectedDate, mMinSelectedDate!!)) {
                CalendarRangeType.START_DATE
            } else if (isDateSame(selectedDate, mMaxSelectedDate!!)) {
                CalendarRangeType.LAST_DATE
            } else if (selectedDateVal in minDateVal until maxDateVal) {
                CalendarRangeType.MIDDLE_DATE
            } else {
                CalendarRangeType.NOT_IN_RANGE
            }
        } else {
            CalendarRangeType.NOT_IN_RANGE
        }
    }

    override fun isSelectableDate(date: Calendar): Boolean {
        // It would work even if date is exactly equal to one of the end cases
        val isSelectable = !(date.before(mStartSelectableDate) || date.after(mEndSelectableDate))
        if (!(!isSelectable && checkDateRange(date) !== CalendarRangeType.NOT_IN_RANGE)) {
            "Selected date can not be out of Selectable Date range." +
                    " Date: ${printDate(date)}" +
                    " Min: ${printDate(mMinSelectedDate)}" +
                    " Max: ${printDate(mMaxSelectedDate)}"
        }
        return isSelectable
    }

    private fun validateDatesOrder(start: Calendar, end: Calendar?) {
        if (start.after(end)) {
            throw InvalidDateException("Start date(${printDate(start)}) can not be after end date(${printDate(end)}).")
        }
    }
}