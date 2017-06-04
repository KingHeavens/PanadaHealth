package com.jws.pandahealth.component.services.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by zhaijinjing on 2016/12/21.
 * 可控制ViewPager是否可以滑动
 */

public class MovedControllerViewPager extends ViewPager {

    private boolean mNoFocus; ////if true, keep View don't move

    public MovedControllerViewPager(Context context) {
        super(context);
    }

    public MovedControllerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mNoFocus) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    public void setNoFocus(boolean b){
        mNoFocus = b;
    }
}
