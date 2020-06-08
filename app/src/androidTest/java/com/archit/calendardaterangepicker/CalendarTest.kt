package com.archit.calendardaterangepicker

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.Visibility.INVISIBLE
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.archit.calendardaterangepicker.customviews.DateView
import com.archit.calendardaterangepicker.customviews.isDateSame
import com.archit.calendardaterangepickerdemo.MainActivity
import com.archit.calendardaterangepickerdemo.R
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
@LargeTest
class CalendarTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun checkCalendarVisibleMonthRange() {

        // Calendar visible month range set to 2019-Dec to 2020-May and current visible month set to January 2020
        Espresso.onView(ViewMatchers.withId(R.id.tvYearTitle)).check(ViewAssertions.matches(ViewMatchers.withText("January 2020")))
        clickOnRightNavigationArrow()
        Espresso.onView(ViewMatchers.withId(R.id.tvYearTitle)).check(ViewAssertions.matches(ViewMatchers.withText("February 2020")))
        clickOnRightNavigationArrow()
        Espresso.onView(ViewMatchers.withId(R.id.tvYearTitle)).check(ViewAssertions.matches(ViewMatchers.withText("March 2020")))
        clickOnRightNavigationArrow(2)
        // Reached to May 2020
        Espresso.onView(ViewMatchers.withId(R.id.imgVNavRight)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(INVISIBLE)))
        clickOnLeftNavigationArrow(5)
        Espresso.onView(ViewMatchers.withId(R.id.tvYearTitle)).check(ViewAssertions.matches(ViewMatchers.withText("December 2019")))
        // Reached to December 2019
        Espresso.onView(ViewMatchers.withId(R.id.imgVNavLeft)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(INVISIBLE)))
    }

    @Test
    fun checkSelectableDateRange() {
        // Calendar visible month range set to 2019-Dec to 2020-May and current visible month set to January 2020
        val calendar = activityTestRule.activity.findViewById(R.id.cdrvCalendar) as DateRangeCalendarView
        // Start date is set to 19/Jan/2020 and end date 20/April/2020.
        checkNotNull(calendar.startDate)
        checkNotNull(calendar.endDate)
        Assert.assertTrue(isDateSame(getCalendar(19, Calendar.JANUARY, 2020), calendar.startDate!!))
        Assert.assertTrue(isDateSame(getCalendar(20, Calendar.APRIL, 2020), calendar.endDate!!))
        val expectedSelectableStartDate = getCalendar(8, Calendar.JANUARY, 2020)
        Espresso.onView(withCustomDateTag(DateView.getContainerKey(expectedSelectableStartDate))).perform(ViewActions.click())
        clickOnRightNavigationArrow(4)
        val expectedSelectableEndDate = getCalendar(1, Calendar.MAY, 2020)
        Espresso.onView(withCustomDateTag(DateView.getContainerKey(expectedSelectableEndDate))).perform(ViewActions.click())
        Assert.assertTrue(isDateSame(getCalendar(19, Calendar.JANUARY, 2020), calendar.startDate!!))
        Assert.assertTrue(isDateSame(getCalendar(20, Calendar.APRIL, 2020), calendar.endDate!!))
    }

    @Test
    fun checkSelectedDateRange() {
        // Calendar visible month range set to 2019-Dec to 2020-May and current visible month set to January 2020
        val calendar = activityTestRule.activity.findViewById(R.id.cdrvCalendar) as DateRangeCalendarView
        // Start date is set to 19/Jan/2020 and end date 20/April/2020.
        checkNotNull(calendar.startDate)
        checkNotNull(calendar.endDate)
        Assert.assertTrue(isDateSame(getCalendar(19, Calendar.JANUARY, 2020), calendar.startDate!!))
        Assert.assertTrue(isDateSame(getCalendar(20, Calendar.APRIL, 2020), calendar.endDate!!))
        // Clicking on date 10/Feb/2020.
        clickOnRightNavigationArrow()
        Espresso.onView(ViewMatchers.withId(R.id.tvYearTitle)).check(ViewAssertions.matches(ViewMatchers.withText("February 2020")))
        val expectedSelectedStartDate = getCalendar(10, Calendar.FEBRUARY, 2020)
        Espresso.onView(withCustomDateTag(DateView.getContainerKey(expectedSelectedStartDate)))
                .perform(ViewActions.click())
        Assert.assertNotNull(calendar.startDate)
        Assert.assertNull(calendar.endDate)
        Assert.assertTrue(isDateSame(expectedSelectedStartDate, calendar.startDate!!))
        // Clicking on date 18/March/2020.
        clickOnRightNavigationArrow()
        Espresso.onView(ViewMatchers.withId(R.id.tvYearTitle)).check(ViewAssertions.matches(ViewMatchers.withText("March 2020")))
        val expectedSelectedEndDate = getCalendar(18, Calendar.MARCH, 2020)
        Espresso.onView(withCustomDateTag(DateView.getContainerKey(expectedSelectedEndDate)))
                .perform(ViewActions.click())
        Assert.assertNotNull(calendar.endDate)
        Assert.assertTrue(isDateSame(expectedSelectedEndDate, calendar.endDate!!))
    }
}