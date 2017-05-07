package com.arun.a85mm.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by WY on 2017/5/7.
 */
public class SharedPerferenceUtils {
    private static final String PATH_USER = "user";
    private static final String KEY_USER_UID = "uid";

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
}
