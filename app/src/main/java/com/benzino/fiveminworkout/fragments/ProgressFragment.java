package com.benzino.fiveminworkout.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.benzino.fiveminworkout.Model;
import com.benzino.fiveminworkout.MyAdapter;
import com.benzino.fiveminworkout.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Anas on 23/1/16.
 */
public class ProgressFragment extends android.support.v4.app.Fragment{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        MaterialCalendarView calendarView = (MaterialCalendarView) view.findViewById(R.id.calendar);
        TextView text = (TextView) view.findViewById(R.id.textView);

        //calendarView.setPagingEnabled(false);

        List<CalendarDay> list = new ArrayList<CalendarDay>();
        Calendar calendar = Calendar.getInstance();

        CalendarDay calendarDay = CalendarDay.from(calendar);
        list.add(calendarDay);

        calendarView.addDecorators(new EventDecorator(Color.RED, list));

        return view;
    }

    private class EventDecorator implements DayViewDecorator {

        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }

}
