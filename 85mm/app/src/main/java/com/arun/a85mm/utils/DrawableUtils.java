package com.arun.a85mm.utils;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

import com.arun.a85mm.helper.RandomColorHelper;

/**
 * Created by wy on 2017/7/11.
 */

public class DrawableUtils {
    public static GradientDrawable getHeadBgDrawable(View view) {
        GradientDrawable drawable = null;
        if (view != null) {
            drawable = (GradientDrawable) view.getBackground();
            if (drawable != null) {
                drawable.setColor(view.getResources().getColor(RandomColorHelper.getRandomColor()));
            }
        }
        return drawable;
    }
}
