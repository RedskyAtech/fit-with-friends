package com.fit_with_friends.common.helpers;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.style.ForegroundColorSpan;
import com.fit_with_friends.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.ArrayList;
import java.util.HashSet;

public class LeaveDatesDecorator implements DayViewDecorator {

    private HashSet<CalendarDay> dates;
    Activity mActivity;

    public LeaveDatesDecorator(Activity context, ArrayList<CalendarDay> dates) {
        mActivity = context;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(mActivity.getResources().getDrawable(R.drawable.my_selector));
        //view.addSpan(new StyleSpan(R.style.TextAppearance_MyCustomDay));
        view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.white)));
    }

    public void add(ArrayList<CalendarDay> date) {
        dates.addAll(date);
    }

    public void remove(CalendarDay date) {
        dates.remove(date);
    }

    public void removeAll() {
        dates.clear();
    }

    public void addAll(ArrayList<CalendarDay> dateList) {
        dates.clear();
        dates.addAll(dateList);
    }
}
