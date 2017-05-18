package com.arun.a85mm.helper;

import com.arun.a85mm.bean.CommunityResponse;
import com.arun.a85mm.bean.ProductListResponse;

/**
 * Created by wy on 2017/5/17.
 */

public class CommunityListCacheManager {

    private static CommunityResponse communityResponse;

    public static CommunityResponse getCommunityResponse() {
        return communityResponse;
    }

    public static void setProductListResponse(CommunityResponse response) {
        CommunityListCacheManager.communityResponse = response;
    }
}
