package com.test.awesomecalendar

import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager
import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager.DateSelectionState
import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManagerImpl
import com.archit.calendardaterangepicker.customviews.InvalidDateException
import com.archit.calendardaterangepicker.customviews.isDateSame
import com.archit.calendardaterangepicker.customviews.isMonthSame
import com.archit.calendardaterangepicker.customviews.printDate
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes
import io.mockk.every
import io.mockk.mockk
import java.util.Calendar
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DateRangeCalendarManagerTest {

    private lateinit var mCalendarManagerImpl: CalendarDateRangeManager
    private val mDefStartMonth = getCalendar(12, Calendar.OCTOBER, 2019)
    private val mDefEndMonth = getCalendar(20, Calendar.JUNE, 2020)
    private val mockCalendarStyleAttributes = mockk<CalendarStyleAttributes>(relaxUnitFun = true)

    @Before
    fun setUp() {
        // GIVEN
        every {
            mockCalendarStyleAttributes.dateSelectionMode
        } returns CalendarStyleAttributes.DateSelectionMode.FREE_RANGE
        mCalendarManagerImpl =
            CalendarDateRangeManagerImpl(mDefStartMonth, mDefEndMonth, mockCalendarStyleAttributes)
    }

    @Test
    fun `test visible month range`() {
        // GIVEN
        val expectedStartDate = getCalendar(1, Calendar.OCTOBER, 2019)
        val expectedEndDate = getCalendar(30, Calendar.JUNE, 2020)
        val selectableDatePrev = getCalendar(30, Calendar.SEPTEMBER, 2019)
        val selectableDateIn = getCalendar(1, Calendar.JANUARY, 2020)
        val selectableDatePost = getCalendar(1, Calendar.JULY, 2020)
        // THEN
        val data = mCalendarManagerImpl.getVisibleMonthDataList()
        assertEquals(9, data.size)
        assertTrue(isMonthSame(expectedStartDate, data.first()))
        assertTrue(isMonthSame(expectedEndDate, data.last()))
        assertFalse(mCalendarManagerImpl.isSelectableDate(selectableDatePrev))
        assertTrue(mCalendarManagerImpl.isSelectableDate(selectableDateIn))
        assertFalse(mCalendarManagerImpl.isSelectableDate(selectableDatePost))
        assertNull(mCalendarManagerImpl.getMinSelectedDate())
        assertNull(mCalendarManagerImpl.getMaxSelectedDate())
    }

    @Test
    fun `test visible month range date order validations`() {
        try {
            // WHEN
            mCalendarManagerImpl.setVisibleMonths(mDefEndMonth, mDefStartMonth)
        } catch (e: InvalidDateException) {
            // THEN
            checkDateOrderValidation(e.localizedMessage, mDefEndMonth, mDefStartMonth)
        }
    }

    @Test
    fun `test selectable dates order validations`() {
        try {
            // WHEN
            mCalendarManagerImpl.setSelectableDateRange(mDefEndMonth, mDefStartMonth)
        } catch (e: InvalidDateException) {
            // THEN
            checkDateOrderValidation(e.localizedMessage, mDefEndMonth, mDefStartMonth)
        }
    }

    @Test
    fun `test selected dates order validations`() {
        try {
            // WHEN
            mCalendarManagerImpl.setSelectedDateRange(mDefEndMonth, mDefStartMonth)
        } catch (e: InvalidDateException) {
            // THEN
            checkDateOrderValidation(e.localizedMessage, mDefEndMonth, mDefStartMonth)
        }
    }

    @Test
    fun `check selectable date out of visible range`() {
        // GIVEN
        val selectableStartDate = getCalendar(30, Calendar.SEPTEMBER, 2019)
        val selectableEndDate = getCalendar(2, Calendar.JUNE, 2020)
        try {
            // WHEN
            mCalendarManagerImpl.setSelectableDateRange(selectableStartDate, selectableEndDate)
        } catch (e: InvalidDateException) {
            // THEN
            assertEquals(
                "Selectable start date ${(printDate(selectableStartDate))} is out of visible months" +
                        "(${printDate(mCalendarManagerImpl.getStartVisibleMonth())} " +
                        "- ${printDate(mCalendarManagerImpl.getEndVisibleMonth())}).",
                e.localizedMessage
            )
        }

        // GIVEN
        val selectableStartDate1 = getCalendar(20, Calendar.OCTOBER, 2019)
        val selectableEndDate1 = getCalendar(1, Calendar.JULY, 2020)
        try {
            // WHEN
            mCalendarManagerImpl.setSelectableDateRange(selectableStartDate1, selectableEndDate1)
        } catch (e: InvalidDateException) {
            // THEN
            assertEquals(
                "Selectable end date ${(printDate(selectableEndDate1))} is out of visible months" +
                        "(${printDate(mCalendarManagerImpl.getStartVisibleMonth())} - ${
                            printDate(
                                mCalendarManagerImpl.getEndVisibleMonth()
                            )
                        }).", e.localizedMessage
            )
        }
    }

    @Test
    fun `check selectable date range`() {
        // GIVEN
        val selectableStartDate = getCalendar(20, Calendar.OCTOBER, 2019)
        val selectableEndDate = getCalendar(2, Calendar.JUNE, 2020)
        // WHEN
        mCalendarManagerImpl.setSelectableDateRange(selectableStartDate, selectableEndDate)

        val selectableDatePrev = getCalendar(1, Calendar.AUGUST, 2019)
        val selectableDateIn = getCalendar(1, Calendar.JANUARY, 2020)
        val selectableDatePost = getCalendar(1, Calendar.AUGUST, 2020)
        // THEN
        assertTrue(mCalendarManagerImpl.isSelectableDate(selectableStartDate))
        assertTrue(mCalendarManagerImpl.isSelectableDate(selectableEndDate))
        assertFalse(mCalendarManagerImpl.isSelectableDate(selectableDatePrev))
        assertTrue(mCalendarManagerImpl.isSelectableDate(selectableDateIn))
        assertFalse(mCalendarManagerImpl.isSelectableDate(selectableDatePost))
        assertNull(mCalendarManagerImpl.getMinSelectedDate())
        assertNull(mCalendarManagerImpl.getMaxSelectedDate())
    }

    @Test
    fun `test selected date range date validation`() {
        // GIVEN
        val selectedStartDate = getCalendar(2, Calendar.MARCH, 2020)
        val selectedEndDate = getCalendar(10, Calendar.MARCH, 2020)
        try {
            // WHEN
            mCalendarManagerImpl.setSelectedDateRange(selectedEndDate, selectedStartDate)
        } catch (e: InvalidDateException) {
            // THEN
            checkDateOrderValidation(e.localizedMessage, selectedEndDate, selectedStartDate)
        }
    }

    @Test
    fun `test selected date range out of selectable range validation`() {
        // GIVEN
        val selectableStartDate = getCalendar(1, Calendar.MARCH, 2020)
        val selectableEndDate = getCalendar(11, Calendar.MARCH, 2020)
        mCalendarManagerImpl.setSelectableDateRange(selectableStartDate, selectableEndDate)

        val selectedStartDate1 = getCalendar(29, Calendar.FEBRUARY, 2020)
        val selectedEndDate1 = getCalendar(10, Calendar.MARCH, 2020)
        try {
            // WHEN
            mCalendarManagerImpl.setSelectedDateRange(selectedStartDate1, selectedEndDate1)
        } catch (e: InvalidDateException) {
            // THEN
            assertEquals(
                "Start date(${printDate(selectedStartDate1)}) is out of selectable date range.",
                e.localizedMessage
            )
        }

        // GIVEN
        val selectedStartDate2 = getCalendar(2, Calendar.MARCH, 2020)
        val selectedEndDate2 = getCalendar(12, Calendar.MARCH, 2020)

        try {
            // WHEN
            mCalendarManagerImpl.setSelectedDateRange(selectedStartDate2, selectedEndDate2)
        } catch (e: InvalidDateException) {
            // THEN
            assertEquals(
                "End date(${printDate(selectedEndDate2)}) is out of selectable date range.",
                e.localizedMessage
            )
        }
    }

    @Test
    fun `test selected date range with date selection mode free range`() {
        // GIVEN
        val selectableStartDate = getCalendar(1, Calendar.MARCH, 2020)
        val selectableEndDate = getCalendar(11, Calendar.MARCH, 2020)
        val selectedStartDate = getCalendar(2, Calendar.MARCH, 2020)
        val selectedEndDate = getCalendar(10, Calendar.MARCH, 2020)

        val middleDate1 = getCalendar(3, Calendar.MARCH, 2020)
        val middleDate2 = getCalendar(5, Calendar.MARCH, 2020)
        val middleDate3 = getCalendar(9, Calendar.MARCH, 2020)
        val dateOutOfRange1 = getCalendar(29, Calendar.JANUARY, 2020)
        val dateOutOfRange2 = getCalendar(29, Calendar.APRIL, 2020)
        // WHEN
        mCalendarManagerImpl.setSelectableDateRange(selectableStartDate, selectableEndDate)
        mCalendarManagerImpl.setSelectedDateRange(selectedStartDate, selectedEndDate)
        // THEN
        assertTrue(isDateSame(selectedStartDate, mCalendarManagerImpl.getMinSelectedDate()!!))
        assertTrue(isDateSame(selectedEndDate, mCalendarManagerImpl.getMaxSelectedDate()!!))
        assertEquals(selectedStartDate, mCalendarManagerImpl.getMinSelectedDate()!!)
        assertEquals(selectedEndDate, mCalendarManagerImpl.getMaxSelectedDate()!!)
        assertEquals(DateSelectionState.START_DATE, mCalendarManagerImpl.checkDateRange(selectedStartDate))
        assertEquals(DateSelectionState.LAST_DATE, mCalendarManagerImpl.checkDateRange(selectedEndDate))
        assertEquals(DateSelectionState.IN_SELECTED_RANGE, mCalendarManagerImpl.checkDateRange(middleDate1))
        assertEquals(DateSelectionState.IN_SELECTED_RANGE, mCalendarManagerImpl.checkDateRange(middleDate2))
        assertEquals(DateSelectionState.IN_SELECTED_RANGE, mCalendarManagerImpl.checkDateRange(middleDate3))
        assertEquals(DateSelectionState.UNKNOWN, mCalendarManagerImpl.checkDateRange(selectableStartDate))
        assertEquals(DateSelectionState.UNKNOWN, mCalendarManagerImpl.checkDateRange(selectableEndDate))
        assertEquals(DateSelectionState.UNKNOWN, mCalendarManagerImpl.checkDateRange(dateOutOfRange1))
        assertEquals(DateSelectionState.UNKNOWN, mCalendarManagerImpl.checkDateRange(dateOutOfRange2))
    }

    @Test
    fun `test selected date range with date selection mode single`() {
        // GIVEN
        every { mockCalendarStyleAttributes.dateSelectionMode } returns CalendarStyleAttributes.DateSelectionMode.SINGLE
        val selectedStartDate = getCalendar(5, Calendar.MARCH, 2020)
        val selectedEndDate = getCalendar(10, Calendar.MARCH, 2020)

        val dateOutOfRange1 = getCalendar(4, Calendar.MARCH, 2020)
        val dateOutOfRange2 = getCalendar(6, Calendar.MARCH, 2020)
        // WHEN
        mCalendarManagerImpl.setSelectedDateRange(selectedStartDate, selectedEndDate)
        // THEN
        assertTrue(isDateSame(selectedStartDate, mCalendarManagerImpl.getMinSelectedDate()!!))
        assertTrue(isDateSame(selectedStartDate, mCalendarManagerImpl.getMaxSelectedDate()!!))
        assertEquals(selectedStartDate, mCalendarManagerImpl.getMinSelectedDate()!!)
        assertEquals(selectedStartDate, mCalendarManagerImpl.getMaxSelectedDate()!!)
        assertEquals(DateSelectionState.START_END_SAME, mCalendarManagerImpl.checkDateRange(selectedStartDate))
        assertEquals(DateSelectionState.UNKNOWN, mCalendarManagerImpl.checkDateRange(selectedEndDate))
        assertEquals(DateSelectionState.UNKNOWN, mCalendarManagerImpl.checkDateRange(dateOutOfRange1))
        assertEquals(DateSelectionState.UNKNOWN, mCalendarManagerImpl.checkDateRange(dateOutOfRange2))
    }

    @Test
    fun `test selected date range with date selection mode fixed range`() {
        // GIVEN
        with(mockCalendarStyleAttributes) {
            every { dateSelectionMode } returns CalendarStyleAttributes.DateSelectionMode.FIXED_RANGE
            every { fixedDaysSelectionNumber } returns 2
        }
        val selectedStartDate = getCalendar(5, Calendar.MARCH, 2020)
        val selectedEndDate = getCalendar(10, Calendar.MARCH, 2020)

        val dateInRange1 = getCalendar(6, Calendar.MARCH, 2020)
        val dateOutOfRange1 = getCalendar(8, Calendar.MARCH, 2020)
        // WHEN
        mCalendarManagerImpl.setSelectedDateRange(selectedStartDate, selectedEndDate)
        // THEN
        val expectedSelectedEndDate = getCalendar(7, Calendar.MARCH, 2020)
        assertTrue(isDateSame(selectedStartDate, mCalendarManagerImpl.getMinSelectedDate()!!))
        assertTrue(isDateSame(expectedSelectedEndDate, mCalendarManagerImpl.getMaxSelectedDate()!!))
        assertEquals(selectedStartDate, mCalendarManagerImpl.getMinSelectedDate()!!)
        assertEquals(DateSelectionState.START_DATE, mCalendarManagerImpl.checkDateRange(selectedStartDate))
        assertEquals(DateSelectionState.LAST_DATE, mCalendarManagerImpl.checkDateRange(expectedSelectedEndDate))
        assertEquals(DateSelectionState.UNKNOWN, mCalendarManagerImpl.checkDateRange(selectedEndDate))
        assertEquals(DateSelectionState.IN_SELECTED_RANGE, mCalendarManagerImpl.checkDateRange(dateInRange1))
        assertEquals(DateSelectionState.UNKNOWN, mCalendarManagerImpl.checkDateRange(dateOutOfRange1))
    }

    @Test
    fun `test selected date range with same date`() {
        // GIVEN
        val selectableStartDate = getCalendar(1, Calendar.MARCH, 2020)
        val selectableEndDate = getCalendar(11, Calendar.MARCH, 2020)
        val selectedStartDate = getCalendar(2, Calendar.MARCH, 2020)
        selectedStartDate.set(Calendar.HOUR_OF_DAY, 1)
        selectedStartDate.set(Calendar.MINUTE, 10)
        val selectedEndDate = getCalendar(2, Calendar.MARCH, 2020)
        selectedEndDate.set(Calendar.HOUR_OF_DAY, 16)
        selectedEndDate.set(Calendar.MINUTE, 10)

        // WHEN
        mCalendarManagerImpl.setSelectableDateRange(selectableStartDate, selectableEndDate)
        mCalendarManagerImpl.setSelectedDateRange(selectedStartDate, selectedEndDate)
        // THEN
        assertTrue(isDateSame(selectedStartDate, mCalendarManagerImpl.getMinSelectedDate()!!))
        assertTrue(isDateSame(selectedEndDate, mCalendarManagerImpl.getMaxSelectedDate()!!))
        assertEquals(selectedStartDate, mCalendarManagerImpl.getMinSelectedDate()!!)
        assertEquals(selectedEndDate, mCalendarManagerImpl.getMaxSelectedDate()!!)
        assertEquals(DateSelectionState.START_END_SAME, mCalendarManagerImpl.checkDateRange(selectedStartDate))
        assertEquals(DateSelectionState.START_END_SAME, mCalendarManagerImpl.checkDateRange(selectedEndDate))
    }

    private fun checkDateOrderValidation(errorMsg: String?, start: Calendar, end: Calendar) {
        assertEquals("Start date(${printDate(start)}) can not be after end date(${printDate(end)}).", errorMsg)
    }
}

fun getCalendar(date: Int, month: Int, year: Int): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, date)
    return calendar
}
