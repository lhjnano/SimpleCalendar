package com.whiteduck.simplecalendar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.whiteduck.simplecalendar.data.DayEvent;
import com.whiteduck.simplecalendar.databinding.ActivityMainBinding;
import com.whiteduck.simplecalendar.util.WeekFormat;
import com.whiteduck.simplecalendar.view.CalendarNote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding = null;
    private List<DayEvent> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Example : Set the event list
        long today = System.currentTimeMillis();

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(today);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);

        list.addAll(Arrays.asList(
                new DayEvent("sample11111", today, today),
                new DayEvent("sample3", today, today + CalendarNote.DAY, Color.GRAY),
                new DayEvent("sample4", today - CalendarNote.DAY, today + CalendarNote.DAY * 2),
                new DayEvent("sample5", today + CalendarNote.DAY, today + CalendarNote.DAY),
                new DayEvent("sample6", c.getTimeInMillis(), c.getTimeInMillis())
        ));
        binding.simpleCalendar.setOnEventListener((startTimeStamp, endTimeStamp) -> list);

        // Example : Set the holiday
        binding.simpleCalendar.setOnHolidayListener((year, month) -> {
            // New Year Holiday (1/1) of Korea.
            if( month == 1 ) return Collections.singletonList(1L);
            // TODO write your holiday format (day list)
            return null;
        });

        // Example : If The box is clicked, Add Event to calendar
        binding.simpleCalendar.setOnDayClickListener(time -> {
            Log.i(MainActivity.class.getSimpleName(), "click on : " + WeekFormat.getDateStringBestFmt(new Date(time), "yyyy MM dd"));

            list.add(new DayEvent("sample", time, time));
            binding.simpleCalendar.notifyDataSetChanged();

            // TODO write what do you do
        });

        setContentView(binding.getRoot());
    }
}