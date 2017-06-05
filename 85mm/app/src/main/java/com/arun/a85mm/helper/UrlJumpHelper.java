package com.arun.a85mm.helper;

import android.content.Context;
import android.net.Uri;

import com.arun.a85mm.activity.FragmentCommonActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wy on 2017/6/5.
 */

public class UrlJumpHelper {
    public static final String WORK_ID = "workId";

    public static void urlJumpTo(Context context, String url, String title) {
        Uri uri = Uri.parse(url);
        String path = uri.getPath();

        if (path.contains("/works/detail")) {
            String workId = uri.getQueryParameter(WORK_ID);
            Map<String, String> map = new HashMap<>();
            map.put(WORK_ID, workId);
            FragmentCommonActivity.jumpToFragmentCommonActivity(context, FragmentCommonActivity.FRAGMENT_ONE_WORK, title, map);
        }
    }
}
