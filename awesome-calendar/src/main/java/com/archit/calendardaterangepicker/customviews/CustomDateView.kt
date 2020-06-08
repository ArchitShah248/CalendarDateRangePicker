package com.archit.calendardaterangepicker.customviews

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff.Mode.SRC_IN
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.archit.calendardaterangepicker.R
import com.archit.calendardaterangepicker.R.drawable
import com.archit.calendardaterangepicker.customviews.DateView.DateState
import com.archit.calendardaterangepicker.customviews.DateView.DateState.DISABLE
import com.archit.calendardaterangepicker.customviews.DateView.DateState.END
import com.archit.calendardaterangepicker.customviews.DateView.DateState.HIDDEN
import com.archit.calendardaterangepicker.customviews.DateView.DateState.MIDDLE
import com.archit.calendardaterangepicker.customviews.DateView.DateState.SELECTABLE
import com.archit.calendardaterangepicker.customviews.DateView.DateState.START
import com.archit.calendardaterangepicker.customviews.DateView.DateState.START_END_SAME
import com.archit.calendardaterangepicker.customviews.DateView.OnDateClickListener
import com.archit.calendardaterangepicker.models.CalendarStyleAttrImpl
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CustomDateView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), DateView {

    private val tvDate: CustomTextView
    private val strip: View
    private val simpleDateFormat = SimpleDateFormat(CalendarDateRangeManager.DATE_FORMAT, Locale.getDefault())
    private val filterMode = SRC_IN

    private var onDateClickListener: OnDateClickListener? = null
    private var mDateState: DateState
    private val isRightToLeft = resources.getBoolean(R.bool.cdr_is_right_to_left)

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_calendar_day, this, true)
        tvDate = findViewById(R.id.dayOfMonthText)
        strip = findViewById(R.id.viewStrip)
        mDateState = SELECTABLE
        if (!isInEditMode) {
            setDateStyleAttributes(CalendarStyleAttrImpl.getDefAttributes(context))
            updateDateBackground(mDateState)
        }
    }

    private val defCalendarStyleAttr: CalendarStyleAttrImpl = CalendarStyleAttrImpl.getDefAttributes(context)
    override var dateTextSize: Float = defCalendarStyleAttr.textSizeDate
    override var defaultDateColor: Int = defCalendarStyleAttr.defaultDateColor
    override var disableDateColor: Int = defCalendarStyleAttr.disableDateColor
    override var selectedDateCircleColor: Int = defCalendarStyleAttr.selectedDateCircleColor
    override var selectedDateColor: Int = defCalendarStyleAttr.selectedDateColor
    override var rangeDateColor: Int = defCalendarStyleAttr.rangeDateColor
    override var stripColor: Int = defCalendarStyleAttr.rangeStripColor

    private val mViewClickListener = OnClickListener {
        val key = it.tag as Long
        if (onDateClickListener != null) {
            val selectedCal = Calendar.getInstance()
            var date = Date()
            try {
                date = simpleDateFormat.parse(key.toString())
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            selectedCal.time = date
            onDateClickListener?.onDateClicked(it, selectedCal)
        }
    }

    override fun setDateText(date: String) {
        tvDate.text = date
    }

    override fun setDateStyleAttributes(attr: CalendarStyleAttributes) {
        disableDateColor = attr.disableDateColor
        defaultDateColor = attr.defaultDateColor
        selectedDateCircleColor = attr.selectedDateCircleColor
        selectedDateColor = attr.selectedDateColor
        stripColor = attr.rangeStripColor
        rangeDateColor = attr.rangeDateColor
        tvDate.textSize = attr.textSizeDate
        refreshLayout()
    }

    override fun setTypeface(typeface: Typeface) {
        tvDate.typeface = typeface
    }

    override fun setDateTag(date: Calendar) {
        tag = DateView.getContainerKey(date)
    }

    override fun updateDateBackground(dateState: DateState) {
        mDateState = dateState
        when (dateState) {
            START, END, START_END_SAME -> makeAsSelectedDate(dateState)
            HIDDEN -> hideDayContainer()
            SELECTABLE -> enabledDayContainer()
            DISABLE -> disableDayContainer()
            MIDDLE -> makeAsRangeDate()
            else -> throw IllegalArgumentException("$dateState is an invalid state.")
        }
    }

    override fun refreshLayout() {
        tvDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, dateTextSize)
    }

    override fun setDateClickListener(listener: OnDateClickListener) {
        onDateClickListener = listener
    }

    /**
     * To hide date if date is from previous month.
     */
    private fun hideDayContainer() {
        tvDate.text = ""
        tvDate.setBackgroundColor(Color.TRANSPARENT)
        strip.setBackgroundColor(Color.TRANSPARENT)
        setBackgroundColor(Color.TRANSPARENT)
        visibility = View.INVISIBLE
        setOnClickListener(null)
    }

    /**
     * To disable past date. Click listener will be removed.
     */
    private fun disableDayContainer() {
        tvDate.setBackgroundColor(Color.TRANSPARENT)
        strip.setBackgroundColor(Color.TRANSPARENT)
        setBackgroundColor(Color.TRANSPARENT)
        tvDate.setTextColor(disableDateColor)
        visibility = View.VISIBLE
        setOnClickListener(null)
    }

    /**
     * To enable date by enabling click listeners.
     */
    private fun enabledDayContainer() {
        tvDate.setBackgroundColor(Color.TRANSPARENT)
        strip.setBackgroundColor(Color.TRANSPARENT)
        setBackgroundColor(Color.TRANSPARENT)
        tvDate.setTextColor(defaultDateColor)
        visibility = View.VISIBLE
        setOnClickListener(mViewClickListener)
    }

    /**
     * To draw date container as selected as end selection or middle selection.
     *
     * @param state - DateState
     */
    private fun makeAsSelectedDate(state: DateState) {
        when (state) {
            START_END_SAME -> {
                val layoutParams = strip.layoutParams as LayoutParams
                strip.setBackgroundColor(Color.TRANSPARENT)
                layoutParams.setMargins(0, 0, 0, 0)
                strip.layoutParams = layoutParams
            }
            START -> {
                if (isRightToLeft) {
                    setRightFacedSelectedDate()
                } else {
                    setLeftFacedSelectedDate()
                }
            }
            END -> {
                if (isRightToLeft) {
                    setLeftFacedSelectedDate()
                } else {
                    setRightFacedSelectedDate()
                }
            }
            else -> {
                throw IllegalArgumentException("$state is an invalid state.")
            }
        }
        val mDrawable = ContextCompat.getDrawable(context, drawable.green_circle)
        mDrawable!!.colorFilter = PorterDuffColorFilter(selectedDateCircleColor, filterMode)
        tvDate.background = mDrawable
        setBackgroundColor(Color.TRANSPARENT)
        tvDate.setTextColor(selectedDateColor)
        visibility = View.VISIBLE
        setOnClickListener(mViewClickListener)
    }

    private fun setLeftFacedSelectedDate() {
        val layoutParams = strip.layoutParams as LayoutParams
        val drawable = ContextCompat.getDrawable(context, drawable.range_bg_left)
        drawable!!.colorFilter = PorterDuffColorFilter(stripColor, filterMode)
        strip.background = drawable
        layoutParams.setMargins(20, 0, 0, 0)
        strip.layoutParams = layoutParams
    }

    private fun setRightFacedSelectedDate() {
        val layoutParams = strip.layoutParams as LayoutParams
        val drawable = ContextCompat.getDrawable(context, drawable.range_bg_right)
        drawable!!.colorFilter = PorterDuffColorFilter(stripColor, filterMode)
        strip.background = drawable
        layoutParams.setMargins(0, 0, 20, 0)
        strip.layoutParams = layoutParams
    }

    /**
     * To draw date as middle date
     */
    private fun makeAsRangeDate() {
        tvDate.setBackgroundColor(Color.TRANSPARENT)
        val mDrawable = ContextCompat.getDrawable(context, drawable.range_bg)
        mDrawable!!.colorFilter = PorterDuffColorFilter(stripColor, filterMode)
        strip.background = mDrawable
        setBackgroundColor(Color.TRANSPARENT)
        tvDate.setTextColor(rangeDateColor)
        visibility = View.VISIBLE
        val layoutParams = strip.layoutParams as LayoutParams
        layoutParams.setMargins(0, 0, 0, 0)
        strip.layoutParams = layoutParams
        setOnClickListener(mViewClickListener)
    }
}