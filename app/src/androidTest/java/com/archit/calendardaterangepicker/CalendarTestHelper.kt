package com.archit.calendardaterangepicker

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.archit.calendardaterangepicker.R.id
import com.archit.calendardaterangepicker.customviews.CustomDateView
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import java.util.Calendar

fun clickOnRightNavigationArrow(times: Int = 1) {
    for (number in 1..times){
        Espresso.onView(ViewMatchers.withId(id.imgVNavRight)).perform(ViewActions.click())
    }
}

fun clickOnLeftNavigationArrow(times: Int = 1) {
    for (number in 1..times){
        Espresso.onView(ViewMatchers.withId(id.imgVNavLeft)).perform(ViewActions.click())
    }
}

class withCustomDateTag(private val dateTag : Long) : BaseMatcher<View>() {

    override fun describeTo(description: Description?) {
        description?.appendText("Matching view with dateTag($dateTag) and CustomDateView class.")
    }

    override fun matches(item: Any?): Boolean = item is CustomDateView && item.tag == dateTag && item.visibility == View.VISIBLE

    override fun describeMismatch(item: Any?, mismatchDescription: Description?) {
        mismatchDescription?.appendText("CustomDateView with tag($dateTag) not matched.")
    }
}

fun getCalendar(date: Int, month: Int, year: Int): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, date, 0, 0, 0)
    return calendar
}