package com.fit_with_friends.common.helpers;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import com.fit_with_friends.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class MySelectorDecorator implements DayViewDecorator {

    private final Drawable drawable;
    //Activity activity;

    public MySelectorDecorator(Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.my_selector);
        //activity = context;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
        //view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(activity, R.color.white)));
    }
}