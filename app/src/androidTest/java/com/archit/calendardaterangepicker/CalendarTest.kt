package com.archit.calendardaterangepicker

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.archit.calendardaterangepicker.R.id
import com.archit.calendardaterangepickerdemo.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.DateFormatSymbols
import java.time.Month
import java.util.Calendar
import java.util.Locale


@RunWith(AndroidJUnit4::class)
class CalendarTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        // Setting up before every test
    }

    @Test
    fun checkCalendar() {
        Espresso.onView(ViewMatchers.withId(id.imgVNavRight)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(id.imgVNavRight)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(id.imgVNavLeft)).perform(ViewActions.click())
        val currentCalendarMonth = Calendar.getInstance()
        currentCalendarMonth.add(Calendar.MONTH, 1)
        var dateText = DateFormatSymbols(Locale.getDefault()).months.get(currentCalendarMonth.get(Calendar.MONTH))
        dateText = dateText.substring(0, 1).toUpperCase() + dateText.subSequence(1, dateText.length)
        val yearTitle = dateText.toString() + " " + currentCalendarMonth.get(Calendar.YEAR)
        Espresso.onView(ViewMatchers.withId(id.tvYearTitle)).check(ViewAssertions.matches(ViewMatchers.withText(yearTitle)))
    }
}