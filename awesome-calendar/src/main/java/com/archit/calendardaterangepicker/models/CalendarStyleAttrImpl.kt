package com.archit.calendardaterangepicker.models

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.archit.calendardaterangepicker.R
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes.Companion.DEFAULT_FIXED_DAYS_SELECTION
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes.DateSelectionMode
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes.DateSelectionMode.FIXED_RANGE
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes.DateSelectionMode.FREE_RANGE

class CalendarStyleAttrImpl(context: Context, attributeSet: AttributeSet? = null) : CalendarStyleAttributes {
    override var fonts: Typeface? = null
    override var titleColor = ContextCompat.getColor(context, R.color.title_color)
        private set
    override var headerBg: Drawable? = null
    override var weekColor = ContextCompat.getColor(context, R.color.week_color)
        private set
    override var rangeStripColor = ContextCompat.getColor(context, R.color.range_bg_color)
        private set
    override var selectedDateCircleColor = ContextCompat.getColor(context, R.color.selected_date_circle_color)
        private set
    override var selectedDateColor = ContextCompat.getColor(context, R.color.selected_date_color)
        private set
    override var defaultDateColor = ContextCompat.getColor(context, R.color.default_date_color)
        private set
    override var disableDateColor = ContextCompat.getColor(context, R.color.disable_date_color)
        private set
    override var rangeDateColor = ContextCompat.getColor(context, R.color.range_date_color)
        private set
    override var textSizeTitle = context.resources.getDimension(R.dimen.text_size_title)
        private set
    override var textSizeWeek = context.resources.getDimension(R.dimen.text_size_week)
        private set
    override var textSizeDate = context.resources.getDimension(R.dimen.text_size_date)
        private set
    override var isShouldEnabledTime = false

    /**
     * To set week offset
     *
     * @param weekOffset
     */
    override var weekOffset = 0
        set(weekOffset) {
            if (weekOffset < 0 || weekOffset > 6) {
                throw InvalidCalendarAttributeException("Week offset can only be between 0 to 6. 0->Sun, 1->Mon, 2->Tue, 3->Wed, 4->Thu, 5->Fri, 6->Sat")
            }
            field = weekOffset
        }
    override var isEditable = true
    override var dateSelectionMode = FREE_RANGE
    override var fixedDaysSelectionNumber: Int = DEFAULT_FIXED_DAYS_SELECTION
        set(value) {
            if (dateSelectionMode !== FIXED_RANGE) {
                throw InvalidCalendarAttributeException("Selected date selection mode is not `fixed_range` for `date_selection_mode` " +
                        "attribute in layout.")
            }
            if (value < 0 || value > 365) {
                throw InvalidCalendarAttributeException("Fixed days can be between 0 to 365.")
            }
            field = value
        }

    init {
        attributeSet?.apply { setAttributes(context, this) }
    }

    private fun setAttributes(context: Context, attributeSet: AttributeSet?) {
        if (attributeSet != null) {
            val ta = context.obtainStyledAttributes(attributeSet, R.styleable.DateRangeMonthView, 0, 0)
            try {
                titleColor = ta.getColor(R.styleable.DateRangeMonthView_title_color, titleColor)
                headerBg = ta.getDrawable(R.styleable.DateRangeMonthView_header_bg)
                weekColor = ta.getColor(R.styleable.DateRangeMonthView_week_color, weekColor)
                rangeStripColor = ta.getColor(R.styleable.DateRangeMonthView_range_color, rangeStripColor)
                selectedDateCircleColor = ta.getColor(R.styleable.DateRangeMonthView_selected_date_circle_color, selectedDateCircleColor)
                isShouldEnabledTime = ta.getBoolean(R.styleable.DateRangeMonthView_enable_time_selection, false)
                isEditable = ta.getBoolean(R.styleable.DateRangeMonthView_editable, true)
                textSizeTitle = ta.getDimension(R.styleable.DateRangeMonthView_text_size_title, textSizeTitle)
                textSizeWeek = ta.getDimension(R.styleable.DateRangeMonthView_text_size_week, textSizeWeek)
                textSizeDate = ta.getDimension(R.styleable.DateRangeMonthView_text_size_date, textSizeDate)
                selectedDateColor = ta.getColor(R.styleable.DateRangeMonthView_selected_date_color, selectedDateColor)
                defaultDateColor = ta.getColor(R.styleable.DateRangeMonthView_default_date_color, defaultDateColor)
                rangeDateColor = ta.getColor(R.styleable.DateRangeMonthView_range_date_color, rangeDateColor)
                disableDateColor = ta.getColor(R.styleable.DateRangeMonthView_disable_date_color, disableDateColor)
                weekOffset = ta.getColor(R.styleable.DateRangeMonthView_week_offset, 0)
                dateSelectionMode = DateSelectionMode.values()[ta.getInt(R.styleable.DateRangeMonthView_date_selection_mode, 0)]
            } finally {
                ta.recycle()
            }
        }
    }

    companion object {
        /**
         * To parse attributes from xml layout to configure calendar views.
         */
        fun getDefAttributes(context: Context): CalendarStyleAttrImpl {
            val calendarStyleAttr = CalendarStyleAttrImpl(context)
            calendarStyleAttr.textSizeTitle = context.resources.getDimension(R.dimen.text_size_title)
            calendarStyleAttr.textSizeWeek = context.resources.getDimension(R.dimen.text_size_week)
            calendarStyleAttr.textSizeDate = context.resources.getDimension(R.dimen.text_size_date)
            calendarStyleAttr.weekColor = ContextCompat.getColor(context, R.color.week_color)
            calendarStyleAttr.rangeStripColor = ContextCompat.getColor(context, R.color.range_bg_color)
            calendarStyleAttr.selectedDateCircleColor = ContextCompat.getColor(context, R.color.selected_date_circle_color)
            calendarStyleAttr.selectedDateColor = ContextCompat.getColor(context, R.color.selected_date_color)
            calendarStyleAttr.defaultDateColor = ContextCompat.getColor(context, R.color.default_date_color)
            calendarStyleAttr.rangeDateColor = ContextCompat.getColor(context, R.color.range_date_color)
            calendarStyleAttr.disableDateColor = ContextCompat.getColor(context, R.color.disable_date_color)
            return calendarStyleAttr
        }
    }
}