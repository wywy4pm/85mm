package com.arun.a85mm.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;

import com.arun.a85mm.R;
import com.arun.a85mm.matisse.Matisse;
import com.arun.a85mm.matisse.MimeType;
import com.arun.a85mm.matisse.engine.impl.GlideEngine;

import java.util.Map;

/**
 * Created by wy on 2017/6/27.
 */

public class MatisseHelper {
    public static void startPicturePicker(Activity activity, String mode) {
        Matisse.from(activity)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(9)
                //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(400)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .addBackMode(mode)
                .forResult(0);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
