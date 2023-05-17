package com.youxel.daterangepicker.models

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

interface CalendarStyleAttributes {

    enum class DateSelectionMode {
        FREE_RANGE,
        SINGLE,
        FIXED_RANGE
    }

    var fonts: Typeface?

    @get:ColorInt
    val titleColor: Int
    var headerBg: Drawable?

    @get:ColorInt
    var weekColor: Int

    @get:ColorInt
    var rangeStripColor: Int

    @get:ColorInt
    var selectedDateCircleColor: Int

    @get:ColorInt
    var selectedDateColor: Int

    @get:ColorInt
    var defaultDateColor: Int

    @get:ColorInt
    val disableDateColor: Int

    @get:ColorInt
    var rangeDateColor: Int
    val textSizeTitle: Float
    val textSizeWeek: Float
    val textSizeDate: Float
    val isShouldEnabledTime: Boolean
    var weekOffset: Int
    var isEditable: Boolean
    var dateSelectionMode: DateSelectionMode
    var fixedDaysSelectionNumber: Int

    companion object {
        const val DEFAULT_FIXED_DAYS_SELECTION = 7
    }
}

class InvalidCalendarAttributeException(override val message: String?) : IllegalArgumentException(message)
