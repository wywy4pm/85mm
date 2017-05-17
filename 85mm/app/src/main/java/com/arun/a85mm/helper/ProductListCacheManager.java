package com.arun.a85mm.helper;

import com.arun.a85mm.bean.ProductListResponse;

/**
 * Created by wy on 2017/5/17.
 */

public class ProductListCacheManager {
    private static ProductListResponse productListResponse;

    public static ProductListResponse getProductListResponse() {
        return productListResponse;
    }

    public static void setProductListResponse(ProductListResponse response) {
        ProductListCacheManager.productListResponse = response;
    }
}
