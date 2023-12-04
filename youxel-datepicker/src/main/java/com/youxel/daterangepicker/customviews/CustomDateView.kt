package com.youxel.daterangepicker.customviews

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
import com.youxel.datepicker.R
import com.youxel.daterangepicker.customviews.DateView.DateState
import com.youxel.daterangepicker.models.CalendarStyleAttrImpl
import com.youxel.daterangepicker.models.CalendarStyleAttributes
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("TooManyFunctions")
class CustomDateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), DateView {

    private val tvDate: CustomTextView
    private val strip: View
    private val simpleDateFormat =
        SimpleDateFormat(CalendarDateRangeManager.DATE_FORMAT, Locale.getDefault())
    private val filterMode = SRC_IN

    private var onDateClickListener: DateView.OnDateClickListener? = null
    private var mDateState: DateState
    private val isRightToLeft = resources.getBoolean(R.bool.cdr_is_right_to_left)

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_calendar_day, this, true)
        tvDate = findViewById(R.id.dayOfMonthText)
        strip = findViewById(R.id.viewStrip)
        mDateState = DateState.SELECTABLE
        if (!isInEditMode) {
            setDateStyleAttributes(CalendarStyleAttrImpl.getDefAttributes(context))
            updateDateBackground(mDateState)
        }
    }

    private val defCalendarStyleAttr: CalendarStyleAttrImpl =
        CalendarStyleAttrImpl.getDefAttributes(context)
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
            DateState.START, DateState.END, DateState.START_END_SAME -> makeAsSelectedDate(
                dateState
            )

            DateState.HIDDEN -> hideDayContainer()
            DateState.SELECTABLE -> enabledDayContainer()
            DateState.DISABLE -> disableDayContainer()
            DateState.MIDDLE -> makeAsRangeDate()
        }
    }

    override fun refreshLayout() {
        tvDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, dateTextSize)
    }

    override fun setDateClickListener(listener: DateView.OnDateClickListener) {
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
            DateState.START_END_SAME -> {
                val layoutParams = strip.layoutParams as LayoutParams
                strip.setBackgroundColor(Color.TRANSPARENT)
                layoutParams.setMargins(0, 0, 0, 0)
                strip.layoutParams = layoutParams
            }

            DateState.START -> {
                if (isRightToLeft) {
                    setRightFacedSelectedDate()
                } else {
                    setLeftFacedSelectedDate()
                }
            }

            DateState.END -> {
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
        val mDrawable = ContextCompat.getDrawable(context, R.drawable.green_circle)
        mDrawable!!.colorFilter = PorterDuffColorFilter(selectedDateCircleColor, filterMode)
        tvDate.background = mDrawable
        setBackgroundColor(Color.TRANSPARENT)
        tvDate.setTextColor(selectedDateColor)
        visibility = View.VISIBLE
        setOnClickListener(mViewClickListener)
    }

    private fun setLeftFacedSelectedDate() {
        val layoutParams = strip.layoutParams as LayoutParams
        val drawable = ContextCompat.getDrawable(context, R.drawable.range_bg_left)
        drawable!!.colorFilter = PorterDuffColorFilter(stripColor, filterMode)
        strip.background = drawable
        layoutParams.setMargins(MARGIN_LEFT, 0, 0, 0)
        strip.layoutParams = layoutParams
    }

    private fun setRightFacedSelectedDate() {
        val layoutParams = strip.layoutParams as LayoutParams
        val drawable = ContextCompat.getDrawable(context, R.drawable.range_bg_right)
        drawable!!.colorFilter = PorterDuffColorFilter(stripColor, filterMode)
        strip.background = drawable
        layoutParams.setMargins(0, 0, MARGIN_RIGHT, 0)
        strip.layoutParams = layoutParams
    }

    /**
     * To draw date as middle date
     */
    private fun makeAsRangeDate() {
        tvDate.setBackgroundColor(Color.TRANSPARENT)
        val mDrawable = ContextCompat.getDrawable(context, R.drawable.range_bg)
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

    companion object {
        private const val MARGIN_RIGHT = 20
        private const val MARGIN_LEFT = 20
    }
}
