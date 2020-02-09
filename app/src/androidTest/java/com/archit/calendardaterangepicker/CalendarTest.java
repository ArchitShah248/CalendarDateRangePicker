package com.archit.calendardaterangepicker;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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
