package com.arun.a85mm.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.arun.a85mm.activity.FragmentCommonActivity;
import com.arun.a85mm.utils.AppUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wy on 2017/6/5.
 */

public class UrlJumpHelper {
    public static final String WORK_ID = "workId";
    public static final String TITLE = "title";
    public static final String KEY_JUMP_APP = "jump_app";
    public static final String KEY_JUMP_MAP = "jump_map";
    public static final String JUMP_APP_WORK_DETAIL = "jump_detail";


    public static void urlJumpTo(Context context, String url, String title) {
        Uri uri = Uri.parse(url);
        String path = uri.getPath();

        if (path.contains("/works/detail")) {
            String workId = uri.getQueryParameter(WORK_ID);
            Map<String, String> map = new HashMap<>();
            map.put(WORK_ID, workId);
            map.put(TITLE, title);
            if (AppUtils.isAppRunning(context, "com.arun.a85mm")) {
                FragmentCommonActivity.jumpToFragmentCommonActivity(context, FragmentCommonActivity.FRAGMENT_ONE_WORK, map);
            } else {
                startAppToJump(context, "com.arun.a85mm", JUMP_APP_WORK_DETAIL, map);
            }
        }
    }

    public static void startAppToJump(Context context, String packageName, String type, Map<String, String> map) {
        Intent launchIntent = context.getPackageManager().
                getLaunchIntentForPackage(packageName);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        launchIntent.putExtra(KEY_JUMP_APP, type);
        launchIntent.putExtra(KEY_JUMP_MAP, (Serializable) map);
        context.startActivity(launchIntent);
    }
}
