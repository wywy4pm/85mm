package com.arun.a85mm.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/5/12.
 */

public class CacheUtils {
    private static ACache aCache;
    public static final String KEY_STRING_CONFIG = "string_config";
    public static final String KEY_OBJECT_CONFIG = "object_config";
    public static final String KEY_OBJECT_PRODUCT_RESPONSE = "object_product_response";
    public static final String KEY_BITMAP_CONFIG = "bitmap_config";

    private static void newInstance(Context context) {
        synchronized (CacheUtils.class) {
            if (aCache == null) {
                aCache = ACache.get(context);
            }
        }
    }

    public static void saveString(Context context, String key, String data) {
        newInstance(context);
        if (aCache != null) {
            if (!TextUtils.isEmpty(data)) {
                aCache.put(key, data);
            }
        }
    }

    public static String getString(Context context, String key) {
        newInstance(context);
        String data = "";
        if (aCache != null) {
            if (!TextUtils.isEmpty(key)) {
                data = aCache.getAsString(key);
            }
        }
        return data;
    }

    public static void saveObject(Context context, String key, Serializable data) {
        newInstance(context);
        if (aCache != null) {
            if (data != null) {
                if (!TextUtils.isEmpty(key)) {
                    aCache.put(key, data);
                }
            }
        }
    }

    public static Object getObject(Context context, String key) {
        newInstance(context);
        Object data = null;
        if (aCache != null) {
            if (!TextUtils.isEmpty(key)) {
                data = aCache.getAsObject(key);
            }
        }
        return data;
    }

    public static void saveBitmap(Context context, String key, Bitmap bitmap) {
        newInstance(context);
        if (aCache != null) {
            if (bitmap != null) {
                if (!TextUtils.isEmpty(key)) {
                    aCache.put(key, bitmap);
                }
            }
        }
    }

    public static Bitmap getBitmap(Context context, String key) {
        newInstance(context);
        Bitmap bitmap = null;
        if (aCache != null) {
            if (!TextUtils.isEmpty(key)) {
                bitmap = aCache.getAsBitmap(key);
            }
        }
        return bitmap;
    }
}
