package com.whiteduck.simplecalendar.data;

import androidx.annotation.ColorInt;

public class DayEvent {

    private String title = "";
    private long startTime = 0;
    private long endTime = 0;

    private static final int DAY = 24 * 60 * 60 * 1000;

    @ColorInt
    private int colorBackground = 0;

    @ColorInt
    private int colorTitle = 0;

    public DayEvent() {
    }

    public DayEvent(String title, long startTime, long endTime) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DayEvent(String title, long startTime, long endTime, int colorBackground) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.colorBackground = colorBackground;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setColorTitle(int colorTitle) {
        this.colorTitle = colorTitle;
    }

    public int getColorTitle() {
        return colorTitle;
    }

    public static int getDAY() {
        return DAY;
    }

    public int getColorBackground() {
        return colorBackground;
    }

    public void setColorBackground(int colorBackground) {
        this.colorBackground = colorBackground;
    }


    public boolean isBackLink(long thisDay)
    {
        return (thisDay - startTime) / DAY > 0;
    }

    public boolean isNextLink(long thisDay)
    {
        return (endTime - thisDay) / DAY > 0;
    }

}
