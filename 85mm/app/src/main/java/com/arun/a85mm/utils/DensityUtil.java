package com.arun.a85mm.utils;

import android.content.Context;

/**
 * Created by WY on 2017/4/11.
 */
public class DensityUtil {

    public static float dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale;
    }
}
