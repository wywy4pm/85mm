package com.arun.a85mm.retrofit;


import com.arun.a85mm.bean.ActionBean;
import com.arun.a85mm.bean.ActionRequest;
import com.arun.a85mm.bean.ArticleDetailResponse;
import com.arun.a85mm.bean.ArticleListResponse;
import com.arun.a85mm.bean.CommonResponse;
import com.arun.a85mm.bean.CommunityResponse;
import com.arun.a85mm.bean.ConfigResponse;
import com.arun.a85mm.bean.LeftWorksResponse;
import com.arun.a85mm.bean.ProductListResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;


public interface RetrofitApi {

    @GET(RetrofitUrl.ARTICLE_LIST)
    Observable<ArticleListResponse> getArticleList(@Query("pageNum") int pageNum, @Query("uid") String uid, @Query("deviceId") String deviceId);

    @GET(RetrofitUrl.ARTICLE_DETAIL)
    Observable<ArticleDetailResponse> getArticleDetail(@Query("id") String id, @Query("uid") String uid, @Query("deviceId") String deviceId);

    @GET(RetrofitUrl.WORKS_LIST)
    Observable<ProductListResponse> getWorksList(@Query("uid") String uid, @Query("deviceId") String deviceId, @Query("lastWorkId") String lastWorkId);

    @GET(RetrofitUrl.WORKS_GOODS)
    Observable<CommunityResponse> getWorksGoods(@Query("uid") String uid, @Query("deviceId") String deviceId, @Query("lastDate") String lastDate);

    @GET(RetrofitUrl.WORKS_ONE_DAY_LEFT)
    Observable<LeftWorksResponse> getWorksOneDayLeft(@Query("uid") String uid, @Query("deviceId") String deviceId, @Query("date") String date, @Query("start") int start);

    @GET
    Call<ResponseBody> downLoadImage(@Url String url);

    @Headers({"Content-Type:application/json;charset=UTF-8"})
    @POST(RetrofitUrl.USER_ACTION)
    Observable<CommonResponse> recordUserAction(@Body ActionRequest actionRequest);

    @GET(RetrofitUrl.CONFIG_QUERY)
    Observable<ConfigResponse> queryConfig(@Query("deviceId") String deviceId);
}
