package com.arun.a85mm.utils;

import android.graphics.BitmapFactory;

/**
 * Created by WY on 2017/5/6.
 */
public class BitmapUtils {
    /**
     * 设置Bitmap的Options
     */
    public static BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }
}
