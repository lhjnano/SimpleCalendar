package com.whiteduck.simplecalendar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.whiteduck.simplecalendar.data.DayEvent;
import com.whiteduck.simplecalendar.data.StylePackage;
import com.whiteduck.simplecalendar.databinding.CalendarSimpleBinding;
import com.whiteduck.simplecalendar.util.WeekFormat;

import java.util.Calendar;
import java.util.List;

public class SimpleCalendar extends FrameLayout{

    private CalendarSimpleBinding binding = null;
    private SimpleCalendarAdapter adapter = null;
    public static final int MAX_PAGES = Integer.MAX_VALUE;

    public SimpleCalendar(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public SimpleCalendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SimpleCalendar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @ColorInt
    private static int getColor(Resources res, @ColorRes int colorID)
    {
        return ResourcesCompat.getColor(res, colorID, null );
    }


    private void init(Context context, AttributeSet attrs)
    {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SimpleCalendar);

        StylePackage stylePackage = new StylePackage();

        Resources res = getResources();
        int backgroundEvent	 = getColor(res, R.color.colorEventBackground);
        int text 			 = getColor(res, R.color.colorText);
        int textEvent        = getColor(res, R.color.colorTextEvent);
        int textHoliday      = getColor(res, R.color.colorHolidayText);
        int divider          = getColor(res, R.color.colorDivider );

        stylePackage.colors.backgroundEvent = typedArray.getColor(R.styleable.SimpleCalendar_backgroundEvent,   backgroundEvent);
        stylePackage.colors.text            = typedArray.getColor(R.styleable.SimpleCalendar_textColor,         text);
        stylePackage.colors.textEvent       = typedArray.getColor(R.styleable.SimpleCalendar_textEventColor,    textEvent);
        stylePackage.colors.textHoliday     = typedArray.getColor(R.styleable.SimpleCalendar_textHolidayColor,  textHoliday);
        stylePackage.colors.divider         = typedArray.getColor(R.styleable.SimpleCalendar_dividerColor,      divider);
        stylePackage.colors.overflow        = 0x44FFFFFF;

        stylePackage.startDayOfWeek = typedArray.getInteger(R.styleable.SimpleCalendar_startDayOfWeek, 0);
        stylePackage.font           = typedArray.getString(R.styleable.SimpleCalendar_android_fontFamily);

        typedArray.recycle();

        MONTH_OF_FIRST_PAGE = System.currentTimeMillis();
        binding = CalendarSimpleBinding.inflate(LayoutInflater.from(context), this, false);
        adapter = new SimpleCalendarAdapter()
                .setStylePackage(stylePackage)
                .setTime(MONTH_OF_FIRST_PAGE);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.setCurrentItem(MAX_PAGES / 2, false);
        addView(binding.getRoot());
    }

    /**
     * Show the first calender on @param date
     */
    public void setDate(long date)
    {
        MONTH_OF_FIRST_PAGE = date;
        if( adapter != null )
            adapter.setTime(MONTH_OF_FIRST_PAGE);
        if( binding != null )
            binding.viewPager.setCurrentItem(MAX_PAGES / 2, false);
        if( onSwipeMonthListener == null ) return;
        onSwipeMonthListener.onChanged(date);
    }

    long MONTH_OF_FIRST_PAGE = 0;
    private ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if( onSwipeMonthListener == null ) return;
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(MONTH_OF_FIRST_PAGE);
            onSwipeMonthListener.onChanged(
                    WeekFormat.addMonth(c, (position - (MAX_PAGES/2) ))
            );
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if( binding == null ) return;
        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if( binding == null ) return;
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback);
    }

    public interface OnEventListener {
        /**
         * This function wanna event for showing calendar.
         * Must to ready 3 months the List of DayEvent for Implemented Calendar to ViewPager2
         */
        List<DayEvent> onEvent(long startTimeStamp, long endTimeStamp);
    }
    public interface OnHolidayListener {
        /**
         * If you wanna check on holiday, use this function.
         * return @return list is the days on @param month.
         */
        List<Long> onHoliday(int year, int month);
    }
    public interface OnDayClickListener {
        /**
         * User can click the day, Cuz to do something for this day.
         * So @param time is the clicked day, you can use this function.
         */
        void onClick(long time);
    }
    public interface OnSwipeMonthListener {
        /**
         * Swipe ViewPager2, You wanna know what calendar show.
         */
        void onChanged(long time);
    }
    private OnSwipeMonthListener onSwipeMonthListener = null;
    public void setOnSwipeMonthListener(OnSwipeMonthListener onSwipeMonthListener) {
        this.onSwipeMonthListener = onSwipeMonthListener;
    }

    public void setOnEventListener(OnEventListener onEventListener) {
        if( adapter == null ) return;
        adapter.setOnEventListener(onEventListener);
        adapter.notifyDataSetChanged();
    }

    public void setOnHolidayListener(OnHolidayListener onHolidayListener) {
        if( adapter == null ) return;
        adapter.setOnHolidayListener(onHolidayListener);
        adapter.notifyDataSetChanged();
    }

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        if( adapter == null ) return;
        adapter.setOnDayClickListener(onDayClickListener);
        adapter.notifyDataSetChanged();
    }

    public void notifyDataSetChanged()
    {
        adapter.notifyDataSetChanged();
    }


}
