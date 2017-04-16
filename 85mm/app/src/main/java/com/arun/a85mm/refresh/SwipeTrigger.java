package com.arun.a85mm.refresh;

/**
 * Created by WY on 2017/4/15.
 */
public interface SwipeTrigger {
    void onPrepare();

    void onMove(int var1, boolean var2, boolean var3);

    void onRelease();

    void onComplete();

    void onReset();
}
