package com.test.awesomecalendar

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.archit.calendardaterangepicker.models.CalendarStyleAttrImpl
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes.DateSelectionMode
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes.DateSelectionMode.FIXED_RANGE
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes.DateSelectionMode.SINGLE
import com.archit.calendardaterangepicker.models.InvalidCalendarAttributeException
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CalendarAttributesTest {

    private var mockContext: Context = ApplicationProvider.getApplicationContext()
    val mTested = CalendarStyleAttrImpl(mockContext)

    @Test
    fun `test default attributes`() {
        Assert.assertEquals(false, mTested.isShouldEnabledTime)
        Assert.assertEquals(0, mTested.weekOffset)
        Assert.assertEquals(true, mTested.isEditable)
        Assert.assertEquals(DateSelectionMode.FREE_RANGE, mTested.dateSelectionMode)
        Assert.assertEquals(CalendarStyleAttributes.DEFAULT_FIXED_DAYS_SELECTION, mTested.fixedDaysSelectionNumber)
    }

    @Test
    fun `test fixed date selection value`() {
        Assert.assertEquals(7, mTested.fixedDaysSelectionNumber)
        mTested.dateSelectionMode = FIXED_RANGE
        mTested.fixedDaysSelectionNumber = 15
        Assert.assertEquals(15, mTested.fixedDaysSelectionNumber)
    }

    @Test
    fun `test invalid negative fixed date selection value`() {
        try {
            mTested.dateSelectionMode = FIXED_RANGE
            mTested.fixedDaysSelectionNumber = -1
            Assert.fail("Fixed day selection validation is failing.")
        } catch (e: InvalidCalendarAttributeException) {
            Assert.assertEquals("Fixed days can be between 0 to 365.", e.message)
        }
    }

    @Test
    fun `test invalid out of range fixed date selection value`() {
        try {
            mTested.dateSelectionMode = FIXED_RANGE
            mTested.fixedDaysSelectionNumber = 366
            Assert.fail("Fixed date selection validation is failing.")
        } catch (e: InvalidCalendarAttributeException) {
            Assert.assertEquals("Fixed days can be between 0 to 365.", e.message)
        }
    }

    @Test
    fun `test invalid date selection mode for fixed date selection value`() {
        try {
            mTested.dateSelectionMode = SINGLE
            mTested.fixedDaysSelectionNumber = 366
            Assert.fail("Fixed date selection validation is failing.")
        } catch (e: InvalidCalendarAttributeException) {
            Assert.assertEquals("Selected date selection mode is not `fixed_range` for `date_selection_mode` " +
                    "attribute in layout.", e.message)
        }
    }

    @Test
    fun `test week offset`() {
        Assert.assertEquals(0, mTested.weekOffset)
        mTested.weekOffset = 5
        Assert.assertEquals(5, mTested.weekOffset)
    }

    @Test
    fun `test invalid negative week offset`() {
        try {
            mTested.weekOffset = -1
            Assert.fail("Week offset validation is failing.")
        } catch (e: InvalidCalendarAttributeException) {
            Assert.assertEquals("Week offset can only be between 0 to 6. 0->Sun, 1->Mon, 2->Tue, 3->Wed, 4->Thu, 5->Fri, 6->Sat", e.message)
        }
    }

    @Test
    fun `test invalid out of range week offset`() {
        try {
            mTested.weekOffset = 7
            Assert.fail("Week offset validation is failing.")
        } catch (e: InvalidCalendarAttributeException) {
            Assert.assertEquals("Week offset can only be between 0 to 6. 0->Sun, 1->Mon, 2->Tue, 3->Wed, 4->Thu, 5->Fri, 6->Sat", e.message)
        }
    }
}