package com.fit_with_friends.common.helpers;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.style.ForegroundColorSpan;
import com.fit_with_friends.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;

public class OneDayDecorator implements DayViewDecorator {

    private CalendarDay dates;
    private Activity mActivity;

    public OneDayDecorator(Activity context) {
        mActivity = context;
        this.dates = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(dates);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(mActivity.getResources().getDrawable(R.drawable.custom_circle_blue_unfilled));
        //view.addSpan(new StyleSpan(R.style.TextAppearance_MyCustomDay));
        view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.theme_color)));
    }

    public void setDate(Date date) {
        this.dates = CalendarDay.from(date);
    }
}