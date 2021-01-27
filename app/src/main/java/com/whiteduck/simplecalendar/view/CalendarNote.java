package com.whiteduck.simplecalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.whiteduck.simplecalendar.SimpleCalendar;
import com.whiteduck.simplecalendar.data.DayEvent;
import com.whiteduck.simplecalendar.data.StylePackage;
import com.whiteduck.simplecalendar.util.CalendarColors;
import com.whiteduck.simplecalendar.util.WeekFormat;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarNote extends View implements View.OnTouchListener{

    private static final int MAX_BLOCK_X = 7;
    private static final int MAX_BLOCK_Y = 6;


    private float blockWidth = 0f;
    private float blockHeight = 0f;
    private float dividerSize = 4;
    private float weekStringHeight = 64;
    private float eventBoxHeight = 0f;

    public static final long DAY =  24 * 60 * 60 * 1000;
    public static final long WEEK = 7 * DAY;

    private static class TextSizes {
        int topWeeklyText = 0;
        int textNormal = 0;
        int textEvent = 0;
        int textPadding = 16;
    }
    private TextSizes textSizes = new TextSizes();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private String[] DAY_OF_THE_WEEK;

    private StylePackage sp = new StylePackage();

    public CalendarNote(@NonNull Context context) {
        super(context);
        init();
    }

    public CalendarNote(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarNote(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init()
    {
        setOnTouchListener(this);
        setStartDayOfWeek();
    }

    public CalendarNote setStylePackage(StylePackage sp) {
        this.sp = sp;
        setStartDayOfWeek();

        if( sp.font == null ) return this;
        if( sp.font.length() <= 0 ) return this;
        Typeface typeface = Typeface.create(sp.font, Typeface.NORMAL);

        if( typeface == null ) return this;
        paint.setTypeface(typeface);
        return this;
    }

    public void setStartDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, 9, 6 - sp.startDayOfWeek, 0, 0, 0); // Sunday
        try {
            // Fit the Default Language
            DAY_OF_THE_WEEK = new String[MAX_BLOCK_X];
            for (int i = 0; i < MAX_BLOCK_X; i++)
                DAY_OF_THE_WEEK[i] = WeekFormat.getDateStringBestFmt(WeekFormat.addDate(calendar, 1), "EEE");

        }catch (Exception e) { // For Xml Preview Exception
            switch (sp.startDayOfWeek)
            {
                case 1:
                    DAY_OF_THE_WEEK = new String[] {"Sun", "Mon", "Tue", "Web", "Thu", "Fri", "Sat"}; break;
                case 2:
                    DAY_OF_THE_WEEK = new String[] {"Sat", "Sun", "Mon", "Tue", "Web", "Thu", "Fri"}; break;
                default:
                    DAY_OF_THE_WEEK = new String[] {"Mon", "Tue", "Web", "Thu", "Fri", "Sat", "Sun"}; break;

            }

        }
        setCalendar(new Date().getTime()); // default
    }




    /**
     *  Select for showing month in this note
     */
    private long calendarStartTime = 0;
    private int  showingYear = 0;
    private int  showingMonth = 0;
    public void setCalendar(long timeStamp)
    {
        if( timeStamp <= 0 )
            timeStamp = new Date().getTime();

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp);
        showingYear = c.get(Calendar.YEAR);
        showingMonth = c.get(Calendar.MONTH);
        c.set(Calendar.DAY_OF_MONTH, 1); // day 1
        int firstDayOfWeek 	= c.get(Calendar.DAY_OF_WEEK) + sp.startDayOfWeek - 2;
        c.add(Calendar.DATE, -firstDayOfWeek); // start calendar
        calendarStartTime = c.getTimeInMillis();

        invalidate();
        requestLayout();
    }



    /**
     * Guess the dp size to px
     */
    private static int dpToPx(Context context, int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    /**
     * Guess the size of view components
     */
    private void getDimension()
    {
        blockWidth     = (float)getWidth() / MAX_BLOCK_X;
        blockHeight    = (getHeight() - weekStringHeight) / MAX_BLOCK_Y;
        eventBoxHeight = ( blockHeight - dividerSize ) / 5;

        textSizes.topWeeklyText = dpToPx(getContext(), 13);
        textSizes.textNormal    = dpToPx(getContext(), 13);
        textSizes.textEvent     = dpToPx(getContext(), 12);
    }

    /**
     * Draw top of table for day of the week and boxes.
     */
    private void drawBoxList(Canvas canvas)
    {
        // Day of the week background
        paint.setColor(sp.colors.backgroundEvent);
        canvas.drawRect(0, 0, getWidth(), weekStringHeight, paint);

        // Day of the week texts
        paint.setColor(sp.colors.textEvent);
        paint.setTextSize(textSizes.textNormal);
        paint.setTextAlign(Paint.Align.LEFT);

        for( int date =0; date < DAY_OF_THE_WEEK.length; date++ )
            canvas.drawText(DAY_OF_THE_WEEK[date],
                    date * blockWidth + textSizes.textPadding,
                    weekStringHeight / 2 + textSizes.textPadding ,
                    paint
            );

        // divider
        paint.setColor(sp.colors.divider);
        for( int x = 0; x < MAX_BLOCK_X + 1; x++ ) {
            canvas.drawLine(x * blockWidth,
                    0,
                    x * blockWidth,
                    (float)getHeight(),
                    paint
            );
        }
        for( int y = 0; y < MAX_BLOCK_Y + 1; y++ ) {
            canvas.drawLine(0,
                    y * blockHeight + weekStringHeight, getWidth(),
                    y * blockHeight + weekStringHeight,
                    paint
            );
        }
    }

    /**
     * Get Position of Calendar with @param timeStamp and starting time of calendar
     */
    private PointF getEventPosition(long timeStamp)
    {
        int intervalDay = (int) (( timeStamp - calendarStartTime ) / DAY);
        if( intervalDay < 0 ) return  null;
        if( intervalDay > MAX_BLOCK_X * MAX_BLOCK_Y ) return null;

        int x = intervalDay % MAX_BLOCK_X;
        int y = intervalDay / MAX_BLOCK_X;
        return new PointF(x, y);
    }

    /**
     * Draw Text about day of month.
     */
    private void drawDayText(Canvas canvas)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(calendarStartTime);
        CalendarColors colors = sp.colors;

        List<Long> holidayList = null;
        if( onHolidayListener != null )
            holidayList = onHolidayListener.onHoliday(showingYear,  showingMonth + 1);

        for( int x = 0; x < MAX_BLOCK_X ; x++ )
        {
            for(  int y = 0 ; y < MAX_BLOCK_Y ; y++ )
            {
                long showingDay = calendarStartTime + ( x * DAY ) + (y * WEEK);
                c.setTimeInMillis(showingDay);
                boolean isShowingMonth = (long)c.get(Calendar.MONTH) == showingMonth;

                paint.setColor(isShowingMonth ? colors.text : colors.text & colors.overflow);

                //if( x == WEEKEND ) // Saturday
                //    paint.setColor(isShowingMonth ? colors.textHoliday2 : colors.textHoliday2 & colors.overflow);

                if( holidayList != null && holidayList.contains((long)c.get(Calendar.DAY_OF_MONTH)))
                    paint.setColor(isShowingMonth ? colors.textHoliday : colors.textHoliday & colors.overflow);

                if( sp.startDayOfWeek == 1 )
                {
                    if( x == 0 ) // Sunday
                        paint.setColor(isShowingMonth ? colors.textHoliday : colors.textHoliday & colors.overflow);
                }
                else
                {
                    if( x == MAX_BLOCK_X - 1 ) // Sunday
                        paint.setColor(isShowingMonth ? colors.textHoliday : colors.textHoliday & colors.overflow);
                }

                canvas.drawText("" + c.get(Calendar.DAY_OF_MONTH),
                        x * blockWidth + textSizes.textPadding,
                        y * blockHeight + weekStringHeight + textSizes.textNormal + textSizes.textPadding,
                        paint
                );
            }
        }
    }

    /**
     * Sync And Draw Events
     */
    private void drawEventAll(Canvas canvas)
    {
        if( onEventListener == null ) return;

        List<DayEvent> eventList = onEventListener.onEvent(calendarStartTime, calendarStartTime + 5 * WEEK);
        if( eventList == null ) return;

        Collections.sort(eventList, (e1, e2) -> {
            /*
             * Priority is.. day and period
             */
            int day1 = (int) ((e1.getStartTime() - calendarStartTime ) / DAY);
            int day2 = (int) ((e2.getStartTime() - calendarStartTime ) / DAY);

            int period1 = (int)((e1.getEndTime() - e1.getStartTime()) / DAY);
            int period2 = (int)((e2.getEndTime() - e2.getStartTime()) / DAY);
            if( day1 == day2 ) return period2 - period1;
            else return day1 - day2;
        });

        paint.setTextSize(textSizes.textEvent);
        Map<Integer, Integer> eventCount = new HashMap<>();
        for (DayEvent dayEvent : eventList) {
            PointF eventStart = getEventPosition(dayEvent.getStartTime());
            PointF eventEnd   = getEventPosition(dayEvent.getEndTime());

            if( eventStart == null && eventEnd == null ) continue;

            if( eventStart == null ) // previous month
                eventStart = new PointF(0, 0);
            else if (eventEnd == null ) // next month
                eventEnd = new PointF(MAX_BLOCK_X, MAX_BLOCK_Y);

            drawEvent(canvas, dayEvent, eventStart, eventEnd, eventCount);
        }
        eventCount.clear();
    }

    /**
     * Draw event on Canvas
     */
    private void drawEvent(Canvas canvas, DayEvent event, PointF start, PointF end, Map<Integer, Integer> eventCntMap)
    {
        long startTime = event.getStartTime();

        while( true )
        {
            // Get Event count for the time of point
            int eventCnt = 1;
            int mapDay = (int) ((calendarStartTime - startTime) / DAY);
            if( eventCntMap.get(mapDay) == null )
                eventCntMap.put(mapDay, eventCnt);
            else
            {
                eventCnt = eventCntMap.get(mapDay);
                eventCnt++;
                eventCntMap.put(mapDay, eventCnt);
            }
            // Draw
            drawEventDbD(canvas, startTime, event, start, eventCnt);

            // Check the point is end.
            if( start.x == end.x && start.y == end.y ) break;

            // Move Point
            start.x ++;
            start.y += start.x == MAX_BLOCK_X ? 1 : 0;
            start.x += start.x == MAX_BLOCK_X ? -MAX_BLOCK_X : 0;
            startTime+= DAY;
        }
    }

    /**
     * Draw Event Day by Day
     */
    private void drawEventDbD(Canvas canvas, long paintingDay, DayEvent event, PointF ep, int evtCnt)
    {
        final float boxY              = ep.y * blockHeight + weekStringHeight;
        final float stackEventHeight  = (evtCnt - 1) * eventBoxHeight;
        final float stackEventDivider = (evtCnt - 1) * dividerSize;
        final float dayTextBottomY    = boxY + textSizes.textNormal + textSizes.textPadding * 2;
        final float eventY            = dayTextBottomY + stackEventHeight + stackEventDivider;
        final float boxX              = ep.x * blockWidth; // from 0 to left of box

        final float eventBottom       = dayTextBottomY + stackEventHeight + stackEventDivider + eventBoxHeight;
        final float boxBottom         = boxY + blockHeight;

        final float OVERFLOW_CIRCLE_RADIUS = 6;

        // event is over the box
        if( boxBottom < eventBottom )
        {
            paint.setColor(Color.RED);
            canvas.drawCircle(
                    boxX + blockWidth - textSizes.textPadding * 2 ,
                    dayTextBottomY - (float)(textSizes.textNormal / 2) - OVERFLOW_CIRCLE_RADIUS * 2 /* Diameter */,
                    OVERFLOW_CIRCLE_RADIUS,
                    paint
            );
            return;
        }

        // Draw Event Box
        int backgroundColor = event.getColorBackground() == 0 ? sp.colors.backgroundEvent : event.getColorBackground();

        paint.setColor(backgroundColor);
        canvas.drawRect(
                boxX + ( event.isBackLink(paintingDay) ? 0 : dividerSize),
                eventY,
                boxX + blockWidth - (event.isNextLink(paintingDay) ? 0 : dividerSize),
                eventY + eventBoxHeight,
                paint
        );

        // title set once.
        if( !(ep.x == 0 && ep.y == 0) && event.isBackLink(paintingDay))  return;

        // title color
        int titleColor = event.getColorTitle() == 0 ? sp.colors.textEvent : event.getColorTitle();
        paint.setColor( titleColor );

        // title text length for overflow the eventBox. So fit the size of box.
        String eventTitle = event.getTitle();
        Rect bounds = new Rect();
        paint.getTextBounds(eventTitle, 0, eventTitle.length(), bounds);
        int eventBoxWidth = (int) (blockWidth - dividerSize * 2 - textSizes.textPadding) - 10;

        if( bounds.width() > eventBoxWidth )
        {
            // The bound of title is bigger than box.
            for (int i = 1; i < eventTitle.length(); i++) {
                paint.getTextBounds(eventTitle, 0, i, bounds);
                if( bounds.width() <= eventBoxWidth ) continue;
                eventTitle = eventTitle.substring(0, i);
                break;
            }
        }

        canvas.drawText(eventTitle,
                boxX + textSizes.textPadding,
                eventY + textSizes.textEvent,
                paint
        );
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        getDimension();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoxList(canvas);
        drawDayText(canvas);
        drawEventAll(canvas);
    }


    private static final long THRESHOLD_CLICK_TIME = 200;
    private long clickTime  = 0;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if( onDayClickListener == null ) return true;

        float x = event.getX();
        float y = event.getY();

        if( y <= weekStringHeight)
            return true;

        int blockX = (int) (x / blockWidth);
        int blockY = (int) ((y - weekStringHeight) / blockHeight);

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN :
                clickTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                // over the time
                if( System.currentTimeMillis() - clickTime > THRESHOLD_CLICK_TIME )
                    return true;

                onDayClickListener.onClick((blockX + blockY * MAX_BLOCK_X ) * DAY + calendarStartTime);
                break;
        }
        return true;
    }



    private SimpleCalendar.OnEventListener onEventListener = null;
    public void setOnEventListener(SimpleCalendar.OnEventListener onEventListener) {
        this.onEventListener = onEventListener;
    }

    private SimpleCalendar.OnHolidayListener onHolidayListener = null;
    public void setOnHolidayListener(SimpleCalendar.OnHolidayListener onHolidayListener) {
        this.onHolidayListener = onHolidayListener;
    }

    private SimpleCalendar.OnDayClickListener onDayClickListener = null;

    public void setOnDayClickListener(SimpleCalendar.OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
    }
}
