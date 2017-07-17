package com.arun.a85mm.utils;

import com.arun.a85mm.common.Constant;

/**
 * Created by wy on 2017/7/17.
 */

public class ShareParaUtils {

    public static String getWorkDetailShareDescription(String authorName) {
        return authorName + "@85mm人像摄影社区";
    }

    public static String getWorkDetailShareUrl(String workId) {
        return Constant.WEB_BASE_URL + Constant.PATH_WORK_DETAIL + "/" + workId;
    }
}
