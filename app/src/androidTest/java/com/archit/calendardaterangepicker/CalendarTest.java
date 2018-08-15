package com.archit.calendardaterangepicker;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.archit.calendardaterangepickerdemo.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CalendarTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkCalendar(){

        Espresso.onView(ViewMatchers.withId(R.id.imgVNavRight)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.imgVNavRight)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.imgVNavLeft)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.tvYearTitle)).check(ViewAssertions.matches(ViewMatchers.withText("September 2018")));


    }

}
