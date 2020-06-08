package com.archit.calendardaterangepicker.customviews

import android.util.Log
import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager.DateSelectionState
import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager.DateSelectionState.IN_SELECTED_RANGE
import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager.DateSelectionState.LAST_DATE
import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager.DateSelectionState.START_DATE
import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager.DateSelectionState.START_END_SAME
import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager.DateSelectionState.UNKNOWN
import com.archit.calendardaterangepicker.customviews.DateTiming.END
import com.archit.calendardaterangepicker.customviews.DateTiming.START
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes.DateSelectionMode
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes.DateSelectionMode.FIXED_RANGE
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes.DateSelectionMode.FREE_RANGE
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes.DateSelectionMode.SINGLE
import java.util.Calendar

internal class CalendarDateRangeManagerImpl(startMonthDate: Calendar,
                                            endMonthDate: Calendar,
                                            private val calendarStyleAttributes: CalendarStyleAttributes) : CalendarDateRangeManager {
    private lateinit var mStartVisibleMonth: Calendar
    private lateinit var mEndVisibleMonth: Calendar
    private lateinit var mStartSelectableDate: Calendar
    private lateinit var mEndSelectableDate: Calendar
    private var mMinSelectedDate: Calendar? = null
    private var mMaxSelectedDate: Calendar? = null
    private val mVisibleMonths = mutableListOf<Calendar>()

    companion object {
        private const val TAG = "CDRManagerImpl"
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
        resetTime(startMonthDate, START)

        endMonthDate[Calendar.DAY_OF_MONTH] = endMonthDate.getActualMaximum(Calendar.DAY_OF_MONTH)
        resetTime(endMonthDate, END)

        mStartVisibleMonth = startMonthDate.clone() as Calendar
        resetTime(mStartVisibleMonth, START)
        mEndVisibleMonth = endMonthDate.clone() as Calendar
        resetTime(mEndVisibleMonth, END)

        // Creating visible months data list
        mVisibleMonths.clear()
        val temp = mStartVisibleMonth.clone() as Calendar
        while (!isMonthSame(temp, mEndVisibleMonth)) {
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
        resetTime(mStartSelectableDate, START)
        mEndSelectableDate = endDate.clone() as Calendar
        resetTime(mEndSelectableDate, END)
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
        if (endDate?.after(mEndSelectableDate) == true) {
            throw InvalidDateException("End date(${printDate(endDate)}) is out of selectable date range.")
        }
        val selectionMode: DateSelectionMode = calendarStyleAttributes.dateSelectionMode
        val finalEndDate: Calendar?
        when (selectionMode) {
            SINGLE -> {
                finalEndDate = startDate.clone() as Calendar
                Log.w(TAG, "End date is ignored due date selection mode: $selectionMode")
            }
            FIXED_RANGE -> {
                Log.w(TAG, "End date is ignored due date selection mode: $selectionMode")
                finalEndDate = startDate.clone() as Calendar
                finalEndDate.add(Calendar.DATE, calendarStyleAttributes.fixedDaysSelectionNumber)
            }
            FREE_RANGE -> finalEndDate = endDate
            else -> throw IllegalArgumentException("Unsupported selectionMode: $selectionMode")
        }
        Log.i(TAG, "Selected dates: Start(${printDate(startDate)})-End(${printDate(finalEndDate)}) for mode:$selectionMode")
        this.mMinSelectedDate = startDate.clone() as Calendar
        this.mMaxSelectedDate = finalEndDate?.clone() as Calendar?
    }

    /**
     * To check whether date belongs to selected range.
     *
     * @return DateSelectionState state
     */
    override fun checkDateRange(selectedDate: Calendar): DateSelectionState {

        if (mMinSelectedDate != null && mMaxSelectedDate != null) {

            val selectedDateVal = DateView.getContainerKey(selectedDate)
            val minDateVal = DateView.getContainerKey(mMinSelectedDate!!)
            val maxDateVal = DateView.getContainerKey(mMaxSelectedDate!!)

            if (isDateSame(selectedDate, mMinSelectedDate!!) && isDateSame(selectedDate, mMaxSelectedDate!!)) {
                return START_END_SAME
            } else if (isDateSame(selectedDate, mMinSelectedDate!!)) {
                return START_DATE
            } else if (isDateSame(selectedDate, mMaxSelectedDate!!)) {
                return LAST_DATE
            } else if (selectedDateVal in minDateVal until maxDateVal) {
                return IN_SELECTED_RANGE
            }
        } else if (mMinSelectedDate != null) {
            // When only single date is selected
            if (isDateSame(selectedDate, mMinSelectedDate!!)) {
                return START_END_SAME
            }
        }
        return UNKNOWN
    }

    override fun isSelectableDate(date: Calendar): Boolean {
        // It would work even if date is exactly equal to one of the end cases
        val isSelectable = !(date.before(mStartSelectableDate) || date.after(mEndSelectableDate))
        if (!(!isSelectable && checkDateRange(date) !== UNKNOWN)) {
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