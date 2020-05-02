package com.test.awesomecalendar

import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager
import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager.CalendarRangeType
import com.archit.calendardaterangepicker.customviews.CalendarRangeUtils
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarManagerImpl
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class CalendarDateRangeTest {

    lateinit var mCalendarManagerImpl: CalendarDateRangeManager

    @Before
    fun setUp() {
        val defStartMonth = getCalendar(12, Calendar.OCTOBER, 2019)
        val defEndMonth = getCalendar(20, Calendar.JUNE, 2020)
        mCalendarManagerImpl = DateRangeCalendarManagerImpl(defStartMonth, defEndMonth)
    }

    @Test
    fun checkVisibleMonthRange() {
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
    }

    @Test
    fun checkSelectablePrevDateException() {
        // GIVEN
        val selectableStartDate = getCalendar(30, Calendar.SEPTEMBER, 2019)
        val selectableEndDate = getCalendar(2, Calendar.JUNE, 2020)
        try {
            // WHEN
            mCalendarManagerImpl.setSelectableDateRange(selectableStartDate, selectableEndDate)
            // THEN
            Assert.fail("Exception is expected.")
        } catch (e: Exception) {
            println("Error: ${e.localizedMessage}")
        }
    }

    @Test
    fun checkSelectablePostDateException() {
        // GIVEN
        val selectableStartDate = getCalendar(20, Calendar.OCTOBER, 2019)
        val selectableEndDate = getCalendar(1, Calendar.JULY, 2020)
        try {
            // WHEN
            mCalendarManagerImpl.setSelectableDateRange(selectableStartDate, selectableEndDate)
            // THEN
            Assert.fail("Exception is expected.")
        } catch (e: Exception) {
            println("Error: ${e.localizedMessage}")
        }
    }

    @Test
    fun checkSelectedDateRange() {
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
        Assert.assertEquals(CalendarRangeType.START_DATE, mCalendarManagerImpl.checkDateRange(selectedStartDate))
        Assert.assertEquals(CalendarRangeType.LAST_DATE, mCalendarManagerImpl.checkDateRange(selectedEndDate))
        Assert.assertEquals(CalendarRangeType.MIDDLE_DATE, mCalendarManagerImpl.checkDateRange(middleDate1))
        Assert.assertEquals(CalendarRangeType.MIDDLE_DATE, mCalendarManagerImpl.checkDateRange(middleDate2))
        Assert.assertEquals(CalendarRangeType.MIDDLE_DATE, mCalendarManagerImpl.checkDateRange(middleDate3))
        Assert.assertEquals(CalendarRangeType.NOT_IN_RANGE, mCalendarManagerImpl.checkDateRange(selectableStartDate))
        Assert.assertEquals(CalendarRangeType.NOT_IN_RANGE, mCalendarManagerImpl.checkDateRange(selectableEndDate))
        Assert.assertEquals(CalendarRangeType.NOT_IN_RANGE, mCalendarManagerImpl.checkDateRange(dateOutOfRange1))
        Assert.assertEquals(CalendarRangeType.NOT_IN_RANGE, mCalendarManagerImpl.checkDateRange(dateOutOfRange2))
    }

    private fun getCalendar(date: Int, month: Int, year: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, date)
        return calendar
    }
}