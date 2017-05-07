package com.arun.a85mm.retrofit;


import com.arun.a85mm.bean.ArticleDetailResponse;
import com.arun.a85mm.bean.ArticleListResponse;
import com.arun.a85mm.bean.CommonResponse;
import com.arun.a85mm.bean.CommunityResponse;
import com.arun.a85mm.bean.LeftWorksResponse;
import com.arun.a85mm.bean.ProductListResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;


public interface RetrofitApi {

    @GET(RetrofitUrl.ARTICLE_LIST)
    Observable<ArticleListResponse> getArticleList(@Query("pageNum") int pageNum);

    @GET(RetrofitUrl.ARTICLE_DETAIL)
    Observable<ArticleDetailResponse> getArticleDetail(@Query("id") String id);

    @GET(RetrofitUrl.WORKS_LIST)
    Observable<ProductListResponse> getWorksList(@Query("uid") String uid, @Query("deviceId") String deviceId, @Query("lastWorkId") String lastWorkId);

    @GET(RetrofitUrl.WORKS_GOODS)
    Observable<CommunityResponse> getWorksGoods(@Query("uid") String uid, @Query("deviceId") String deviceId, @Query("lastDate") String lastDate);

    @GET(RetrofitUrl.WORKS_ONE_DAY_LEFT)
    Observable<LeftWorksResponse> getWorksOneDayLeft(@Query("uid") String uid, @Query("deviceId") String deviceId, @Query("date") String date, @Query("start") int start);

    @GET
    Call<ResponseBody> downLoadImage(@Url String url);

    @FormUrlEncoded
    @POST(RetrofitUrl.USER_ACTION)
    Observable<CommonResponse> recordUserAction(@Query("uid") String uid, @Query("deviceId") String deviceId, @Query("appVersion") String appVersion, @Query("osVersion") String osVersion, @Query("deviceModel") String deviceModel, @Query("actionList") String actionListJson);
}
