package com.whiteduck.simplecalendar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.whiteduck.simplecalendar.data.StylePackage;
import com.whiteduck.simplecalendar.view.CalendarNote;

import java.util.Calendar;

public class SimpleCalendarAdapter extends RecyclerView.Adapter<SimpleCalendarAdapter.CViewHolder> {

    private StylePackage sp;

    private long showingMonth = 0;
    private SimpleCalendar.OnEventListener    onEventListener    = null;
    private SimpleCalendar.OnHolidayListener  onHolidayListener  = null;
    private SimpleCalendar.OnDayClickListener onDayClickListener = null;

    public SimpleCalendarAdapter setStylePackage(StylePackage sp) {
        this.sp = sp;
        return this;
    }

    public void setOnEventListener(SimpleCalendar.OnEventListener onEventListener) {
        this.onEventListener = onEventListener;
    }

    public void setOnHolidayListener(SimpleCalendar.OnHolidayListener onHolidayListener) {
        this.onHolidayListener = onHolidayListener;
    }

    public void setOnDayClickListener(SimpleCalendar.OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
    }

    public static class CViewHolder extends RecyclerView.ViewHolder {

        public CalendarNote calendarNote;

        public CViewHolder(@NonNull View itemView) {
            super(itemView);
            calendarNote = (CalendarNote)itemView;
        }
    }


    public SimpleCalendarAdapter setTime(long timeStamp) {
        showingMonth = timeStamp;
        return this;
    }


    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CalendarNote calendarNote = new CalendarNote(parent.getContext()).setStylePackage(sp);
        calendarNote.setLayoutParams(
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        );

        return new CViewHolder(calendarNote.getRootView());
    }

    @Override
    public void onBindViewHolder(@NonNull CViewHolder holder, int position) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(showingMonth);
        c.add(Calendar.MONTH, position - SimpleCalendar.MAX_PAGES / 2);
        holder.calendarNote.setCalendar(c.getTimeInMillis());
        if( onEventListener != null ) holder.calendarNote.setOnEventListener(onEventListener);
        if( onHolidayListener != null ) holder.calendarNote.setOnHolidayListener(onHolidayListener);
        if( onDayClickListener != null ) holder.calendarNote.setOnDayClickListener(onDayClickListener);
    }



    @Override
    public int getItemCount() {
        return SimpleCalendar.MAX_PAGES;
    }
}
