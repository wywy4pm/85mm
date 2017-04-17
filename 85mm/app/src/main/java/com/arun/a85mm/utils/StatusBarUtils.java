package com.arun.a85mm.utils;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

/**
 * Created by wy on 2017/4/17.
 */

public class StatusBarUtils {

    /**
     * 动态显示和隐藏状态栏
     *
     * @param activity
     * @param enable
     */
    public static void setStatusBar(Activity activity, boolean enable) {//true隐藏状态栏
        if (enable) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(lp);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = activity.getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setAttributes(attr);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
}
