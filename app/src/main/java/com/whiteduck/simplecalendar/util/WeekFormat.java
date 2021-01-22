package com.whiteduck.simplecalendar.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeekFormat {
    @SuppressLint("SimpleDateFormat")
    public static String getDateStringBestFmt(Date date, String fmt)
    {
        String pattern = android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), fmt);
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date addDate(Calendar c, int amount)
    {
        c.add(Calendar.DATE, amount);
        return c.getTime();
    }

}
