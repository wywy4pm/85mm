package com.arun.a85mm.bean;

import android.support.annotation.ColorRes;

/**
 * Created by WY on 2017/5/6.
 */
public class ShowTopBean {
    public boolean isShowingTop;
    public String showData;
    public int backgroundResId;

    public ShowTopBean(boolean isShowingTop, String showData) {
        this.isShowingTop = isShowingTop;
        this.showData = showData;
    }

    public ShowTopBean(boolean isShowingTop, String showData, @ColorRes int backgroundResId) {
        this.isShowingTop = isShowingTop;
        this.showData = showData;
        this.backgroundResId = backgroundResId;
    }
}
