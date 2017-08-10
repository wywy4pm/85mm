package com.arun.a85mm.utils;

import android.text.TextUtils;

/**
 * Created by wy on 2017/8/10.
 */

public class PatternUtils {
    public static boolean judgeChangeUser(String text) {
        if (!TextUtils.isEmpty(text)) {
            return text.matches("^c\\s[0-9]*$");
        }
        return false;
    }
}
