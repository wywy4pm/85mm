package com.arun.a85mm.retrofit;


import com.arun.a85mm.bean.ActionRequest;
import com.arun.a85mm.bean.ArticleDetailResponse;
import com.arun.a85mm.bean.ArticleListResponse;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.CommonResponse;
import com.arun.a85mm.bean.CommunityResponse;
import com.arun.a85mm.bean.ConfigResponse;
import com.arun.a85mm.bean.MessageItemBean;
import com.arun.a85mm.bean.WorkListBean;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;


public interface RetrofitApi {

    @GET
    Call<ResponseBody> downLoadImage(@Url String url);

    @GET(RetrofitUrl.ARTICLE_LIST)
    Observable<CommonApiResponse<List<ArticleListResponse.ArticleListBean>>> getArticleList(@Query("pageNum") int pageNum);

    @GET(RetrofitUrl.ARTICLE_DETAIL)
    Observable<CommonApiResponse<ArticleDetailResponse.ArticleBean>> getArticleDetail(@Query("id") String id);

    @GET(RetrofitUrl.WORKS_LIST)
    Observable<CommonApiResponse<List<WorkListBean>>> getWorksList(@Query("lastWorkId") String lastWorkId);

    @GET(RetrofitUrl.WORKS_GOODS)
    Observable<CommonApiResponse<List<CommunityResponse.GoodsListBean>>> getWorksGoods(@Query("lastDate") String lastDate);

    @GET(RetrofitUrl.WORKS_ONE_DAY_LEFT)
    Observable<CommonApiResponse<List<WorkListBean>>> getWorksOneDayLeft(@Query("date") String date, @Query("start") int start);

    @Headers({"Content-Type:application/json;charset=UTF-8"})
    @POST(RetrofitUrl.USER_ACTION)
    Observable<CommonResponse> recordUserAction(@Body ActionRequest actionRequest);

    @GET(RetrofitUrl.CONFIG_QUERY)
    Observable<CommonApiResponse<List<ConfigResponse.GuidePageBean>>> queryConfig();

    @GET(RetrofitUrl.USER_DEVICE_TOKEN)
    Observable<CommonApiResponse> postDeviceToken(@Query("deviceToken") String deviceToken);

    @GET(RetrofitUrl.USER_HIDE_SWITCH)
    Observable<CommonApiResponse> setHideReadStatus(@Query("type") String type);

    @GET(RetrofitUrl.WORKS_SINGLE_DETAIL + "/{workId}")
    Observable<CommonApiResponse<WorkListBean>> getSingleWork(@Path("workId") String workId);

    @GET(RetrofitUrl.USER_MESSAGE_LIST)
    Observable<CommonApiResponse<List<MessageItemBean>>> getMessageList(@Query("uid") String uid, @Query("msgType") int msgType, @Query("lastMsgId") int lastMsgId);

   /* @FormUrlEncoded
    @POST(RetrofitUrl.USER_ADD_MESSAGE)
    Observable<CommonApiResponse> addMessage(@Field("sender") String sender, @Field("receiver") String receiver, @Field("content") String content);*/
}
