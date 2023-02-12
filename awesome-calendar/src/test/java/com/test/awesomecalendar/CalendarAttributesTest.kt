package com.test.awesomecalendar

import android.content.Context
import android.content.res.Resources
import com.archit.calendardaterangepicker.models.CalendarStyleAttrImpl
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes.DateSelectionMode
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes.DateSelectionMode.FIXED_RANGE
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes.DateSelectionMode.SINGLE
import com.archit.calendardaterangepicker.models.InvalidCalendarAttributeException
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class CalendarAttributesTest {

    private val mockResources = mockk<Resources>(relaxUnitFun = true) {
        every { getColor(any()) } returns COLOR
        every { getDimension(any()) } returns DIMENSION
    }
    private val mockContext: Context = mockk(relaxUnitFun = true) {
        every { resources } returns mockResources
    }
    private val mTested = CalendarStyleAttrImpl(mockContext)

    @Test
    fun `test default attributes`() {
        assertEquals(false, mTested.isShouldEnabledTime)
        assertEquals(0, mTested.weekOffset)
        assertEquals(true, mTested.isEditable)
        assertEquals(DateSelectionMode.FREE_RANGE, mTested.dateSelectionMode)
        assertEquals(CalendarStyleAttributes.DEFAULT_FIXED_DAYS_SELECTION, mTested.fixedDaysSelectionNumber)
    }

    @Test
    fun `test fixed date selection value`() {
        assertEquals(7, mTested.fixedDaysSelectionNumber)
        mTested.dateSelectionMode = FIXED_RANGE
        mTested.fixedDaysSelectionNumber = 15
        assertEquals(15, mTested.fixedDaysSelectionNumber)
    }

    @Test
    fun `test invalid negative fixed date selection value`() {
        try {
            mTested.dateSelectionMode = FIXED_RANGE
            mTested.fixedDaysSelectionNumber = -1
            fail("Fixed day selection validation is failing.")
        } catch (e: InvalidCalendarAttributeException) {
            assertEquals("Fixed days can be between 0 to 365.", e.message)
        }
    }

    @Test
    fun `test invalid out of range fixed date selection value`() {
        try {
            mTested.dateSelectionMode = FIXED_RANGE
            mTested.fixedDaysSelectionNumber = 366
            fail("Fixed date selection validation is failing.")
        } catch (e: InvalidCalendarAttributeException) {
            assertEquals("Fixed days can be between 0 to 365.", e.message)
        }
    }

    @Test
    fun `test invalid date selection mode for fixed date selection value`() {
        try {
            mTested.dateSelectionMode = SINGLE
            mTested.fixedDaysSelectionNumber = 366
            fail("Fixed date selection validation is failing.")
        } catch (e: InvalidCalendarAttributeException) {
            assertEquals(
                "Selected date selection mode is not `fixed_range` for `date_selection_mode` " +
                        "attribute in layout.", e.message
            )
        }
    }

    @Test
    fun `test week offset`() {
        assertEquals(0, mTested.weekOffset)
        mTested.weekOffset = 5
        assertEquals(5, mTested.weekOffset)
    }

    @Test
    fun `test invalid negative week offset`() {
        try {
            mTested.weekOffset = -1
            fail("Week offset validation is failing.")
        } catch (e: InvalidCalendarAttributeException) {
            assertEquals(
                "Week offset can only be between 0 to 6. 0->Sun, 1->Mon, 2->Tue, 3->Wed, 4->Thu, 5->Fri, 6->Sat",
                e.message
            )
        }
    }

    @Test
    fun `test invalid out of range week offset`() {
        try {
            mTested.weekOffset = 7
            fail("Week offset validation is failing.")
        } catch (e: InvalidCalendarAttributeException) {
            assertEquals(
                "Week offset can only be between 0 to 6. 0->Sun, 1->Mon, 2->Tue, 3->Wed, 4->Thu, 5->Fri, 6->Sat",
                e.message
            )
        }
    }

    private companion object {
        const val COLOR = 12345
        const val DIMENSION = 12345f
    }
}
