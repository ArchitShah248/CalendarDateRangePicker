package com.archit.calendardaterangepicker.models

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

interface CalendarStyleAttributes {

    companion object {
        val DEFAULT_FIXED_DAYS_SELECTION = 7
    }

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
    val weekColor: Int

    @get:ColorInt
    val rangeStripColor: Int

    @get:ColorInt
    val selectedDateCircleColor: Int

    @get:ColorInt
    val selectedDateColor: Int

    @get:ColorInt
    val defaultDateColor: Int

    @get:ColorInt
    val disableDateColor: Int

    @get:ColorInt
    val rangeDateColor: Int
    val textSizeTitle: Float
    val textSizeWeek: Float
    val textSizeDate: Float
    val isShouldEnabledTime: Boolean
    var weekOffset: Int
    var isEditable: Boolean
    var dateSelectionMode: DateSelectionMode
    var fixedDaysSelectionNumber: Int
}

class InvalidCalendarAttributeException(override val message: String?) : IllegalArgumentException(message)