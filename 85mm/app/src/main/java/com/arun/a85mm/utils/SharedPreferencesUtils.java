package com.arun.a85mm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by WY on 2017/5/7.
 */
public class SharedPreferencesUtils {
    private static final String PATH_USER = "user";
    private static final String PATH_CONFIG = "config";
    private static final String KEY_USER_UID = "uid";
    public static final String KEY_DEVICE_TOKEN = "device_token";
    public static final String KEY_MORE_IMAGE = "more_image";
    public static final String KEY_HIDE_READ_ENABLED = "hide_read_enabled";
    public static final String KEY_HIDE_READ_OPENED = "hide_read_opened";
    public static final String KEY_HIDE_READ_TIPS = "hide_read_tips";
    public static final String KEY_NEW_MESSAGE = "new_message";
    public static final String KEY_AUDIT_SELECT_TAG = "select_tag";
    public static final String KEY_AUDIT_SELECT_SORT = "select_sort";
    public static final String KEY_ASSOCIATION_TAG = "association_tag";

    public static void saveUid(Context context, String uid) {
        if (!TextUtils.isEmpty(uid) && TextUtils.isEmpty(SharedPreferencesUtils.getUid(context))) {
            SharedPreferencesUtils.setUid(context, uid);
        }
    }

    public static void setUid(Context context, String uid) {
        SharedPreferences preferences = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            if (editor != null) {
                editor.putString(KEY_USER_UID, uid);
                editor.apply();
            }
        }
    }

    public static String getUid(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
        String uid = "";
        if (preferences != null) {
            uid = preferences.getString(KEY_USER_UID, "");
        }
        return uid;
    }

    /*public static void setMoreImage(Context context, String imageUrl) {
        SharedPreferences preferences = context.getSharedPreferences(PATH_CONFIG, Context.MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            if (editor != null) {
                editor.putString(KEY_MORE_IMAGE, imageUrl);
                editor.apply();
            }
        }
    }

    public static String getMoreImage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PATH_CONFIG, Context.MODE_PRIVATE);
        String uid = "";
        if (preferences != null) {
            uid = preferences.getString(KEY_MORE_IMAGE, "");
        }
        return uid;
    }*/

    public static void setConfigString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(PATH_CONFIG, Context.MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            if (editor != null) {
                editor.putString(key, value);
                editor.apply();
            }
        }
    }

    public static String getConfigString(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PATH_CONFIG, Context.MODE_PRIVATE);
        String value = "";
        if (preferences != null) {
            value = preferences.getString(key, "");
        }
        return value;
    }

    public static void setConfigLong(Context context, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences(PATH_CONFIG, Context.MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            if (editor != null) {
                editor.putLong(key, value);
                editor.apply();
            }
        }
    }

    public static long getConfigLong(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PATH_CONFIG, Context.MODE_PRIVATE);
        long value = 0;
        if (preferences != null) {
            value = preferences.getLong(key, 0);
        }
        return value;
    }

    public static void setConfigInt(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(PATH_CONFIG, Context.MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            if (editor != null) {
                editor.putInt(key, value);
                editor.apply();
            }
        }
    }

    public static int getConfigInt(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PATH_CONFIG, Context.MODE_PRIVATE);
        int value = 0;
        if (preferences != null) {
            value = preferences.getInt(key, 0);
        }
        return value;
    }

}
