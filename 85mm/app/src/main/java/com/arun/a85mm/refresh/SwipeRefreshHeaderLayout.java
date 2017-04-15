package com.arun.a85mm.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;


/**
 * Created by WY on 2017/4/15.
 */
public class SwipeRefreshHeaderLayout extends FrameLayout implements SwipeRefreshTrigger, SwipeTrigger {
    public SwipeRefreshHeaderLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public SwipeRefreshHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRefreshHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onRefresh() {
    }

    public void onPrepare() {
    }

    public void onMove(int y, boolean isComplete, boolean automatic) {
    }

    public void onRelease() {
    }

    public void onComplete() {
    }

    public void onReset() {
    }
}

