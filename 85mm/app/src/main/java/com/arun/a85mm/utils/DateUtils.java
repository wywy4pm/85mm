package com.arun.a85mm.utils;

/**
 * Created by wy on 2017/5/17.
 */

public class DateUtils {
    public static final int HOUR_24 = 24 * 60 * 1000;

    public static boolean isToday(long currentDayStartTime) {
        boolean isToady = false;
        if (System.currentTimeMillis() >= currentDayStartTime
                && System.currentTimeMillis() < currentDayStartTime + HOUR_24) {
            isToady = true;
        }
        return isToady;
    }
}
