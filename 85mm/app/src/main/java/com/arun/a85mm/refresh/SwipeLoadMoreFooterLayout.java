package com.arun.a85mm.refresh;

/**
 * Created by WY on 2017/4/15.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class SwipeLoadMoreFooterLayout extends FrameLayout implements SwipeLoadMoreTrigger, SwipeTrigger {
    public SwipeLoadMoreFooterLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public SwipeLoadMoreFooterLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLoadMoreFooterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onLoadMore() {
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

