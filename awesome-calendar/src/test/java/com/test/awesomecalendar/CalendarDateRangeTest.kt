package com.test.awesomecalendar

import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager
import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager.DateSelectionState
import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager.DateSelectionState.LAST_DATE
import com.archit.calendardaterangepicker.customviews.CalendarRangeUtils
import com.archit.calendardaterangepicker.customviews.CalendarRangeUtils.Companion.printDate
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarManagerImpl
import com.archit.calendardaterangepicker.customviews.InvalidDateException
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class CalendarDateRangeTest {

    private lateinit var mCalendarManagerImpl: CalendarDateRangeManager
    private val mDefStartMonth = getCalendar(12, Calendar.OCTOBER, 2019)
    private val mDefEndMonth = getCalendar(20, Calendar.JUNE, 2020)

    @Before
    fun setUp() {
        // GIVEN
        mCalendarManagerImpl = DateRangeCalendarManagerImpl(mDefStartMonth, mDefEndMonth)
    }

    @Test
    fun testVisibleMonthRange() {
        // GIVEN
        val expectedStartDate = getCalendar(1, Calendar.OCTOBER, 2019)
        val expectedEndDate = getCalendar(30, Calendar.JUNE, 2020)
        val selectableDatePrev = getCalendar(30, Calendar.SEPTEMBER, 2019)
        val selectableDateIn = getCalendar(1, Calendar.JANUARY, 2020)
        val selectableDatePost = getCalendar(1, Calendar.JULY, 2020)
        // THEN
        val data = mCalendarManagerImpl.getVisibleMonthDataList()
        Assert.assertEquals(9, data.size)
        Assert.assertTrue(CalendarRangeUtils.isMonthSame(expectedStartDate, data.first()))
        Assert.assertTrue(CalendarRangeUtils.isMonthSame(expectedEndDate, data.last()))
        Assert.assertFalse(mCalendarManagerImpl.isSelectableDate(selectableDatePrev))
        Assert.assertTrue(mCalendarManagerImpl.isSelectableDate(selectableDateIn))
        Assert.assertFalse(mCalendarManagerImpl.isSelectableDate(selectableDatePost))
        Assert.assertNull(mCalendarManagerImpl.getMinSelectedDate())
        Assert.assertNull(mCalendarManagerImpl.getMaxSelectedDate())
    }

    @Test
    fun testVisibleMonthRangeDateOrderValidations() {
        try {
            // WHEN
            mCalendarManagerImpl.setVisibleMonths(mDefEndMonth, mDefStartMonth)
        } catch (e: InvalidDateException) {
            // THEN
            checkDateOrderValidation(e.localizedMessage, mDefEndMonth, mDefStartMonth)
        }
    }

    @Test
    fun testSelectableDatesOrderValidations() {
        try {
            // WHEN
            mCalendarManagerImpl.setSelectableDateRange(mDefEndMonth, mDefStartMonth)
        } catch (e: InvalidDateException) {
            // THEN
            checkDateOrderValidation(e.localizedMessage, mDefEndMonth, mDefStartMonth)
        }
    }

    @Test
    fun testSelectedDatesOrderValidations() {
        try {
            // WHEN
            mCalendarManagerImpl.setSelectedDateRange(mDefEndMonth, mDefStartMonth)
        } catch (e: InvalidDateException) {
            // THEN
            checkDateOrderValidation(e.localizedMessage, mDefEndMonth, mDefStartMonth)
        }
    }

    @Test
    fun checkSelectableDateOutOfVisibleRange() {
        // GIVEN
        val selectableStartDate = getCalendar(30, Calendar.SEPTEMBER, 2019)
        val selectableEndDate = getCalendar(2, Calendar.JUNE, 2020)
        try {
            // WHEN
            mCalendarManagerImpl.setSelectableDateRange(selectableStartDate, selectableEndDate)
        } catch (e: InvalidDateException) {
            // THEN
            Assert.assertEquals("Selectable start date ${(printDate(selectableStartDate))} is out of visible months" +
                    "(${printDate(mCalendarManagerImpl.getStartVisibleMonth())} " +
                    "- ${printDate(mCalendarManagerImpl.getEndVisibleMonth())}).",
                    e.localizedMessage)
        }

        // GIVEN
        val selectableStartDate1 = getCalendar(20, Calendar.OCTOBER, 2019)
        val selectableEndDate1 = getCalendar(1, Calendar.JULY, 2020)
        try {
            // WHEN
            mCalendarManagerImpl.setSelectableDateRange(selectableStartDate1, selectableEndDate1)
        } catch (e: InvalidDateException) {
            // THEN
            Assert.assertEquals("Selectable end date ${(printDate(selectableEndDate1))} is out of visible months" +
                    "(${printDate(mCalendarManagerImpl.getStartVisibleMonth())} - ${printDate(mCalendarManagerImpl.getEndVisibleMonth())}).", e.localizedMessage)
        }
    }

    @Test
    fun checkSelectableDateRange() {
        // GIVEN
        val selectableStartDate = getCalendar(20, Calendar.OCTOBER, 2019)
        val selectableEndDate = getCalendar(2, Calendar.JUNE, 2020)
        // WHEN
        mCalendarManagerImpl.setSelectableDateRange(selectableStartDate, selectableEndDate)

        val selectableDatePrev = getCalendar(1, Calendar.AUGUST, 2019)
        val selectableDateIn = getCalendar(1, Calendar.JANUARY, 2020)
        val selectableDatePost = getCalendar(1, Calendar.AUGUST, 2020)
        // THEN
        Assert.assertTrue(mCalendarManagerImpl.isSelectableDate(selectableStartDate))
        Assert.assertTrue(mCalendarManagerImpl.isSelectableDate(selectableEndDate))
        Assert.assertFalse(mCalendarManagerImpl.isSelectableDate(selectableDatePrev))
        Assert.assertTrue(mCalendarManagerImpl.isSelectableDate(selectableDateIn))
        Assert.assertFalse(mCalendarManagerImpl.isSelectableDate(selectableDatePost))
        Assert.assertNull(mCalendarManagerImpl.getMinSelectedDate())
        Assert.assertNull(mCalendarManagerImpl.getMaxSelectedDate())
    }

    @Test
    fun testSelectedDateRangeDateValidation() {
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
    fun testSelectedDateRangeOutOfSelectableRangeValidation() {
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
            Assert.assertEquals("Start date(${printDate(selectedStartDate1)}) is out of selectable date range.", e.localizedMessage)
        }

        // GIVEN
        val selectedStartDate2 = getCalendar(2, Calendar.MARCH, 2020)
        val selectedEndDate2 = getCalendar(12, Calendar.MARCH, 2020)

        try {
            // WHEN
            mCalendarManagerImpl.setSelectedDateRange(selectedStartDate2, selectedEndDate2)
        } catch (e: InvalidDateException) {
            // THEN
            Assert.assertEquals("End date(${printDate(selectedEndDate2)}) is out of selectable date range.", e.localizedMessage)
        }
    }

    @Test
    fun testSelectedDateRange() {
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
        Assert.assertTrue(CalendarRangeUtils.isDateSame(selectedStartDate, mCalendarManagerImpl.getMinSelectedDate()!!))
        Assert.assertTrue(CalendarRangeUtils.isDateSame(selectedEndDate, mCalendarManagerImpl.getMaxSelectedDate()!!))
        Assert.assertEquals(selectedStartDate, mCalendarManagerImpl.getMinSelectedDate()!!)
        Assert.assertEquals(selectedEndDate, mCalendarManagerImpl.getMaxSelectedDate()!!)
        Assert.assertEquals(DateSelectionState.START_DATE, mCalendarManagerImpl.checkDateRange(selectedStartDate))
        Assert.assertEquals(DateSelectionState.LAST_DATE, mCalendarManagerImpl.checkDateRange(selectedEndDate))
        Assert.assertEquals(DateSelectionState.IN_SELECTED_RANGE, mCalendarManagerImpl.checkDateRange(middleDate1))
        Assert.assertEquals(DateSelectionState.IN_SELECTED_RANGE, mCalendarManagerImpl.checkDateRange(middleDate2))
        Assert.assertEquals(DateSelectionState.IN_SELECTED_RANGE, mCalendarManagerImpl.checkDateRange(middleDate3))
        Assert.assertEquals(DateSelectionState.UNKNOWN, mCalendarManagerImpl.checkDateRange(selectableStartDate))
        Assert.assertEquals(DateSelectionState.UNKNOWN, mCalendarManagerImpl.checkDateRange(selectableEndDate))
        Assert.assertEquals(DateSelectionState.UNKNOWN, mCalendarManagerImpl.checkDateRange(dateOutOfRange1))
        Assert.assertEquals(DateSelectionState.UNKNOWN, mCalendarManagerImpl.checkDateRange(dateOutOfRange2))
    }

    @Test
    fun testSelectedDateRangeWithSameDate() {
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
        Assert.assertTrue(CalendarRangeUtils.isDateSame(selectedStartDate, mCalendarManagerImpl.getMinSelectedDate()!!))
        Assert.assertTrue(CalendarRangeUtils.isDateSame(selectedEndDate, mCalendarManagerImpl.getMaxSelectedDate()!!))
        Assert.assertEquals(selectedStartDate, mCalendarManagerImpl.getMinSelectedDate()!!)
        Assert.assertEquals(selectedEndDate, mCalendarManagerImpl.getMaxSelectedDate()!!)
        Assert.assertEquals(DateSelectionState.START_END_SAME, mCalendarManagerImpl.checkDateRange(selectedStartDate))
        Assert.assertEquals(DateSelectionState.START_END_SAME, mCalendarManagerImpl.checkDateRange(selectedEndDate))
    }

    private fun checkDateOrderValidation(errorMsg: String, start: Calendar, end: Calendar) {
        Assert.assertEquals("Start date(${printDate(start)}) can not be after end date(${printDate(end)}).", errorMsg)
    }

    private fun getCalendar(date: Int, month: Int, year: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, date)
        return calendar
    }
}