package com.arun.a85mm.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.arun.a85mm.activity.SplashActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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

    public static Bitmap createBitmapByUrl(String imageUrl) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(imageUrl).openStream();
            byte[] streamByte = IOUtils.getBytes(inputStream);
            bitmap = BitmapFactory.decodeByteArray(streamByte, 0, streamByte.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
