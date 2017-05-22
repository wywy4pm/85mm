package com.arun.a85mm.helper;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.CommunityResponse;
import com.arun.a85mm.bean.ProductListResponse;

/**
 * Created by wy on 2017/5/17.
 */

public class CommunityListCacheManager {

    private static CommonApiResponse commonApiResponse;

    public static CommonApiResponse getCommonApiResponse() {
        return commonApiResponse;
    }

    public static void setCommonApiResponse(CommonApiResponse response) {
        CommunityListCacheManager.commonApiResponse = response;
    }
}
