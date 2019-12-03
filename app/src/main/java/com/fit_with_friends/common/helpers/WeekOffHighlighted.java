package com.fit_with_friends.common.helpers;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeekOffHighlighted implements DayViewDecorator {

    private final Calendar calendar = Calendar.getInstance();
    private final Drawable highlightDrawable;
    private List<CalendarDay> weekOffDays = new ArrayList<>();
    private static final int color = Color.parseColor("#228BC34A");

    public WeekOffHighlighted() {
        highlightDrawable = new ColorDrawable(color);
    }

    public WeekOffHighlighted(Activity activity, List<CalendarDay> weekOffDates) {
        highlightDrawable = new ColorDrawable(color);
        this.weekOffDays = weekOffDates;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        //day.copyTo(calendar);
        //int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        //return weekDay == Calendar.SATURDAY || weekDay == Calendar.SUNDAY;
        return weekOffDays.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(true);
    }
}
