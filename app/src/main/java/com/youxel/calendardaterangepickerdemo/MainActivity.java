package com.youxel.calendardaterangepickerdemo;

import static com.youxel.daterangepicker.customviews.CalendarRangeUtilsKt.printDate;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.archit.calendardaterangepickerdemo.R;
import com.youxel.daterangepicker.customviews.CalendarListener;
import com.youxel.daterangepicker.customviews.DateRangeCalendarView;

import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DateRangeCalendarView calendar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = findViewById(R.id.cdrvCalendar);

        final Typeface typeface = Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf");
        calendar.setFonts(typeface);

        calendar.setCalendarListener(calendarListener);

        calendar.setLocale(new Locale("ar", "SA"));
        findViewById(R.id.btnReset).setOnClickListener(v -> calendar.resetAllSelectedViews());

//        calendar.setNavLeftImage(ContextCompat.getDrawable(this,R.drawable.ic_left));
//        calendar.setNavRightImage(ContextCompat.getDrawable(this,R.drawable.ic_right));

        final Calendar startMonth = Calendar.getInstance();
        startMonth.set(2019, Calendar.DECEMBER, 20);
        final Calendar endMonth = (Calendar) startMonth.clone();
        endMonth.add(Calendar.MONTH, 5);
        Log.d(TAG, "Start month: " + startMonth.getTime().toString() + " :: End month: " + endMonth.getTime().toString());
        calendar.setVisibleMonthRange(startMonth, endMonth);

        final Calendar startDateSelectable = (Calendar) startMonth.clone();
        startDateSelectable.add(Calendar.DATE, 20);
        final Calendar endDateSelectable = (Calendar) endMonth.clone();
        endDateSelectable.add(Calendar.DATE, -20);
        Log.d(TAG, "startDateSelectable: " + startDateSelectable.getTime().toString() + " :: endDateSelectable: " + endDateSelectable.getTime().toString());
        calendar.setSelectableDateRange(startDateSelectable, endDateSelectable);

        final Calendar startSelectedDate = (Calendar) startDateSelectable.clone();
        startSelectedDate.add(Calendar.DATE, 10);
        final Calendar endSelectedDate = (Calendar) endDateSelectable.clone();
        endSelectedDate.add(Calendar.DATE, -10);
        Log.d(TAG, "startSelectedDate: " + startSelectedDate.getTime().toString() + " :: endSelectedDate: " + endSelectedDate.getTime().toString());
        calendar.setSelectedDateRange(startSelectedDate, endSelectedDate);

        final Calendar current = (Calendar) startMonth.clone();
        current.add(Calendar.MONTH, 1);
        calendar.setCurrentMonth(current);
//        calendar.setFixedDaysSelection(2);
    }

    private final CalendarListener calendarListener = new CalendarListener() {
        @Override
        public void onFirstDateSelected(@NonNull final Calendar startDate) {
            Toast.makeText(MainActivity.this, "Start Date: " + startDate.getTime().toString(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Selected dates: Start: " + printDate(calendar.getStartDate()) +
                    " End:" + printDate(calendar.getEndDate()));
        }

        @Override
        public void onDateRangeSelected(@NonNull final Calendar startDate, @NonNull final Calendar endDate) {
            Toast.makeText(MainActivity.this, "Start Date: " + startDate.getTime().toString() + " End date: " + endDate.getTime().toString(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Selected dates: Start: " + printDate(calendar.getStartDate()) +
                    " End:" + printDate(calendar.getEndDate()));
        }
    };
}
