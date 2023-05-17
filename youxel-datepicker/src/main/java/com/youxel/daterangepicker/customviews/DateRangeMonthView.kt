package com.youxel.daterangepicker.customviews

import android.annotation.TargetApi
import android.content.Context
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.youxel.datepicker.R
import com.youxel.datepicker.R.*
import com.youxel.daterangepicker.customviews.DateView.Companion.getContainerKey
import com.youxel.daterangepicker.models.CalendarStyleAttributes
import com.youxel.daterangepicker.models.CalendarStyleAttributes.DateSelectionMode.*
import com.youxel.daterangepicker.models.DateTiming
import com.youxel.daterangepicker.timepicker.AwesomeTimePickerDialog
import com.youxel.daterangepicker.timepicker.AwesomeTimePickerDialog.TimePickerCallback
import java.util.*

/**
 * Created by archit.shah on 08/09/2017.
 */
internal class DateRangeMonthView : LinearLayout {
    private lateinit var llDaysContainer: LinearLayout
    private lateinit var llTitleWeekContainer: LinearLayout
    private lateinit var currentCalendarMonth: Calendar
    private lateinit var calendarStyleAttr: CalendarStyleAttributes
    private var calendarListener: CalendarListener? = null
    private lateinit var dateRangeCalendarManager: CalendarDateRangeManager

    private val mOnDateClickListener: DateView.OnDateClickListener =
        object : DateView.OnDateClickListener {
            override fun onDateClicked(view: View, selectedDate: Calendar) {
                if (calendarStyleAttr.isEditable) {
                    if (calendarStyleAttr.isShouldEnabledTime) {
                        val awesomeTimePickerDialog = AwesomeTimePickerDialog(context,
                            context.getString(string.select_time), object : TimePickerCallback {
                                override fun onTimeSelected(hours: Int, mins: Int) {
                                    selectedDate[Calendar.HOUR] = hours
                                    selectedDate[Calendar.MINUTE] = mins
                                    setSelectedDate(selectedDate)
                                }

                                override fun onCancel() {
                                    resetAllSelectedViews()
                                }
                            })
                        awesomeTimePickerDialog.showDialog()
                    } else {
                        setSelectedDate(selectedDate)
                    }
                }
            }
        }

    fun setCalendarListener(calendarListener: CalendarListener?) {
        this.calendarListener = calendarListener
    }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context)
    }

    /**
     * To initialize child views
     *
     * @param context      - App context
     * @param attributeSet - Attr set
     */
    private fun initView(context: Context) {
        val layoutInflater = LayoutInflater.from(context)
        val mainView =
            layoutInflater.inflate(layout.layout_calendar_month, this, true) as LinearLayout
        llDaysContainer = mainView.findViewById(R.id.llDaysContainer)
        llTitleWeekContainer = mainView.findViewById(R.id.llTitleWeekContainer)
    }

    private fun setSelectedDate(selectedDate: Calendar) {
        val selectionMode = calendarStyleAttr.dateSelectionMode
        var minSelectedDate = dateRangeCalendarManager.getMinSelectedDate()
        var maxSelectedDate = dateRangeCalendarManager.getMaxSelectedDate()

        when (selectionMode) {
            FREE_RANGE -> {
                if (minSelectedDate != null && maxSelectedDate == null) {
                    maxSelectedDate = selectedDate
                    val startDateKey = getContainerKey(minSelectedDate)
                    val lastDateKey = getContainerKey(maxSelectedDate)
                    if (startDateKey == lastDateKey) {
                        minSelectedDate = maxSelectedDate
                    } else if (startDateKey > lastDateKey) {
                        val temp = minSelectedDate.clone() as Calendar
                        minSelectedDate = maxSelectedDate
                        maxSelectedDate = temp
                    }
                } else if (maxSelectedDate == null) {
                    //This will call one time only
                    minSelectedDate = selectedDate
                } else {
                    minSelectedDate = selectedDate
                    maxSelectedDate = null
                }
            }
            SINGLE -> {
                minSelectedDate = selectedDate
                maxSelectedDate = selectedDate
            }
            FIXED_RANGE -> {
                minSelectedDate = selectedDate
                maxSelectedDate = selectedDate.clone() as Calendar
                maxSelectedDate.add(Calendar.DATE, calendarStyleAttr.fixedDaysSelectionNumber)
            }
        }

        dateRangeCalendarManager.setSelectedDateRange(minSelectedDate, maxSelectedDate)
        drawCalendarForMonth(currentCalendarMonth)
        Log.i(LOG_TAG, "Time: " + selectedDate.time.toString())
        if (maxSelectedDate != null) {
            calendarListener!!.onDateRangeSelected(minSelectedDate, maxSelectedDate)
        } else {
            calendarListener!!.onFirstDateSelected(minSelectedDate)
        }
    }

    /**
     * To draw calendar for the given month. Here calendar object should start from date of 1st.
     *
     * @param calendarStyleAttr        Calendar style attributes
     * @param month                    Month to be drawn
     * @param dateRangeCalendarManager Calendar data manager
     */
    fun drawCalendarForMonth(
        calendarStyleAttr: CalendarStyleAttributes,
        month: Calendar,
        dateRangeCalendarManager: CalendarDateRangeManager,
    ) {
        this.calendarStyleAttr = calendarStyleAttr
        currentCalendarMonth = month.clone() as Calendar
        this.dateRangeCalendarManager = dateRangeCalendarManager
        drawCalendarForMonth(currentCalendarMonth)
    }

    /**
     * To draw calendar for the given month. Here calendar object should start from date of 1st.
     *
     * @param month Calendar month
     */
    private fun drawCalendarForMonth(month: Calendar) {
        setWeekTextAttributes()
        currentCalendarMonth = month.clone() as Calendar
        currentCalendarMonth[Calendar.DATE] = 1
        resetTime(currentCalendarMonth, DateTiming.NONE)
        val weekTitle = context.resources.getStringArray(array.week_sun_sat)

        //To set week day title as per offset
        for (i in 0 until TOTAL_DAYS_IN_A_WEEK) {
            val textView = llTitleWeekContainer.getChildAt(i) as CustomTextView
            val weekStr = weekTitle[(i + calendarStyleAttr.weekOffset) % TOTAL_DAYS_IN_A_WEEK]
            textView.text = weekStr
        }
        var startDay = month[Calendar.DAY_OF_WEEK] - calendarStyleAttr.weekOffset

        //To rotate week day according to offset
        if (startDay < 1) {
            startDay = startDay + TOTAL_DAYS_IN_A_WEEK
        }
        month.add(Calendar.DATE, -startDay + 1)
        for (i in 0 until llDaysContainer.childCount) {
            val weekRow = llDaysContainer.getChildAt(i) as LinearLayout
            for (j in 0 until TOTAL_DAYS_IN_A_WEEK) {
                val customDateView = weekRow.getChildAt(j) as CustomDateView
                drawDayContainer(customDateView, month)
                month.add(Calendar.DATE, 1)
            }
        }
    }

    /**
     * To draw specific date container according to past date, today, selected or from range.
     *
     * @param customDateView - Date container
     * @param date       - Calendar obj of specific date of the month.
     */
    private fun drawDayContainer(customDateView: CustomDateView, date: Calendar) {
        customDateView.setDateText(date[Calendar.DATE].toString())
        customDateView.setDateStyleAttributes(calendarStyleAttr)
        customDateView.setDateClickListener(mOnDateClickListener)
        calendarStyleAttr.fonts?.let { customDateView.setTypeface(it) }
        val dateState: DateView.DateState =
            if (currentCalendarMonth[Calendar.MONTH] != date[Calendar.MONTH]) {
                DateView.DateState.HIDDEN
            } else {
                val type = dateRangeCalendarManager.checkDateRange(date)
                if (type === CalendarDateRangeManager.DateSelectionState.START_DATE) {
                    DateView.DateState.START
                } else if (type === CalendarDateRangeManager.DateSelectionState.LAST_DATE) {
                    DateView.DateState.END
                } else if (type === CalendarDateRangeManager.DateSelectionState.START_END_SAME) {
                    DateView.DateState.START_END_SAME
                } else if (type === CalendarDateRangeManager.DateSelectionState.IN_SELECTED_RANGE) {
                    DateView.DateState.MIDDLE
                } else {
                    if (dateRangeCalendarManager.isSelectableDate(date)) {
                        DateView.DateState.SELECTABLE
                    } else {
                        DateView.DateState.DISABLE
                    }
                }
            }
        customDateView.updateDateBackground(dateState)
        customDateView.tag = getContainerKey(date)
    }

    /**
     * To remove all selection and redraw current calendar
     */
    fun resetAllSelectedViews() {
        dateRangeCalendarManager.resetSelectedDateRange()
        drawCalendarForMonth(currentCalendarMonth)
    }

    /**
     * To apply configs to all the text views
     */
    private fun setWeekTextAttributes() {
        for (i in 0 until llTitleWeekContainer.childCount) {
            val textView = llTitleWeekContainer.getChildAt(i) as CustomTextView
            textView.typeface = calendarStyleAttr.fonts
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, calendarStyleAttr.textSizeWeek)
            textView.setTextColor(calendarStyleAttr.weekColor)
        }
    }

    companion object {
        private val LOG_TAG = DateRangeMonthView::class.java.simpleName
        private const val TOTAL_DAYS_IN_A_WEEK = 7
    }
}
