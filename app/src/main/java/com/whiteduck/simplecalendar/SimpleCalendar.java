package com.whiteduck.simplecalendar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.content.res.TypedArrayUtils;

import com.whiteduck.simplecalendar.data.DayEvent;
import com.whiteduck.simplecalendar.data.StylePackage;
import com.whiteduck.simplecalendar.databinding.CalendarSimpleBinding;
import com.whiteduck.simplecalendar.util.CalendarColors;

import java.util.List;

public class SimpleCalendar extends FrameLayout{

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


        CalendarSimpleBinding binding = CalendarSimpleBinding.inflate(LayoutInflater.from(context), null, false);
        adapter = new SimpleCalendarAdapter()
                .setStylePackage(stylePackage)
                .setTime(System.currentTimeMillis());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.setCurrentItem(MAX_PAGES / 2, false);
        addView(binding.getRoot());

    }

    public interface OnEventListener {
        List<DayEvent> onEvent(long startTimeStamp, long endTimeStamp);
    }
    public interface OnHolidayListener {
        List<Long> onHoliday(int year, int month);
    }
    public interface OnDayClickListener {
        void onClick(long time);
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
