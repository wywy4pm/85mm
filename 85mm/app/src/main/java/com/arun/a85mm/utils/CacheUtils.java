package com.arun.a85mm.utils;

import android.content.Context;
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
                aCache.put(key, data);
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
}
