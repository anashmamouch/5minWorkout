package com.benzino.fiveminworkout;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

/**
 * Created by Anas on 28/1/16.
 */
public class CustomViewPager extends ViewPager {

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if(v instanceof MaterialCalendarView){
            return false;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }

}