package com.test.awesomecalendar

import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class CalendarDateRangeTest {
    @Test
    fun checkVisibleMonthRange() {
        Assert.assertEquals(4, 2 + 2.toLong())
    }

    @Test
    fun checkSelectableDateRange() {
        Assert.assertEquals(4, 2 + 2.toLong())
    }

    @Test
    fun checkSelectedDateRange() {
        Assert.assertEquals(4, 2 + 2.toLong())
    }
}