package com.arun.a85mm.helper;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.arun.a85mm.R;

import java.util.Random;

/**
 * Created by wy on 2017/5/13.
 */

public class RandomColorHelper {

    private static int[] colors = new int[]{R.color.work_bg_color_1, R.color.work_bg_color_2, R.color.work_bg_color_3,
            R.color.work_bg_color_4, R.color.work_bg_color_5, R.color.work_bg_color_6, R.color.work_bg_color_7};

    /**
     * 生成随机颜色
     *
     * @param context
     * @return color int 类型颜色
     */
    public static int getRandomColor(Context context) {
        int randomIndex = randomIndex(7);
        return ContextCompat.getColor(context, colors[randomIndex]);
    }

    /**
     * 生成随机颜色
     *
     * @return color 资源id
     */
    public static int getRandomColor() {
        int randomIndex = randomIndex(7);
        return colors[randomIndex];
    }

    public static int randomIndex(int end) {
        return new Random().nextInt(end);
    }
}
