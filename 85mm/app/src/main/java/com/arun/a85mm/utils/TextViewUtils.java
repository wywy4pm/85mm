package com.arun.a85mm.utils;

import android.text.TextPaint;
import android.widget.TextView;

/**
 * Created by WY on 2017/5/20.
 */
public class TextViewUtils {

    public static void setTextBold(TextView textView, boolean isBold) {
        if (textView != null) {
            TextPaint textPaint = textView.getPaint();
            if (textPaint != null) {
                textPaint.setFakeBoldText(isBold);
            }
        }
    }

}
