package com.arun.a85mm.retrofit;

import com.arun.a85mm.bean.AllUserBodyBean;
import com.arun.a85mm.bean.ArticleDetailBody;
import com.arun.a85mm.bean.ArticleListBody;
import com.arun.a85mm.bean.AuditBean;
import com.arun.a85mm.bean.CommonWorkListBean;
import com.arun.a85mm.bean.ConfigBean;
import com.arun.a85mm.bean.EventBackBean;
import com.arun.a85mm.bean.HottestBean;
import com.arun.a85mm.bean.LoginBodyBean;
import com.arun.a85mm.bean.MessageListBean;
import com.arun.a85mm.bean.UserInfoBean;
import com.arun.a85mm.bean.UserMainPageBean;
import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.bean.WorkDetailBean;
import com.arun.a85mm.bean.WorkMixBean;
import com.arun.a85mm.bean.request.ActionRequest;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.request.AddCommentRequest;
import com.arun.a85mm.bean.request.AddCommunityRequest;
import com.arun.a85mm.bean.request.AddMessageRequest;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface RetrofitApi {

    /*@GET
    Call<ResponseBody> downLoadImage(@Url String url);*/

    @GET(RetrofitUrl.ARTICLE_LIST)
    Observable<CommonApiResponse<ArticleListBody>> getArticleList(@Query("lastId") String lastId);

    @GET(RetrofitUrl.ARTICLE_DETAIL + "/{id}")
    Observable<CommonApiResponse<ArticleDetailBody>> getArticleDetail(@Path("id") String id);

    /*@GET(RetrofitUrl.WORKS_LIST)
    Observable<CommonApiResponse<List<WorkListBean>>> getWorksList(@Query("lastWorkId") String lastWorkId);*/

    @GET(RetrofitUrl.WORKS_GOODS)
    Observable<CommonApiResponse<HottestBean>> getWorksGoods(@Query("lastDate") String lastDate);

    /*@GET(RetrofitUrl.WORKS_ONE_DAY_LEFT)
    Observable<CommonApiResponse<List<WorkListBean>>> getWorksOneDayLeft(@Query("date") String date, @Query("start") int start);*/

    @Headers({"Content-Type:application/json;charset=UTF-8"})
    @POST(RetrofitUrl.USER_ACTION)
    Observable<CommonApiResponse<EventBackBean>> recordUserAction(@Body ActionRequest actionRequest);

    @GET(RetrofitUrl.CONFIG_QUERY)
    Observable<CommonApiResponse<ConfigBean>> queryConfig();

    @GET(RetrofitUrl.USER_DEVICE_TOKEN)
    Observable<CommonApiResponse> postDeviceToken(@Query("deviceToken") String deviceToken);

    @GET(RetrofitUrl.USER_HIDE_SWITCH)
    Observable<CommonApiResponse> setHideReadStatus(@Query("type") String type);

    @GET(RetrofitUrl.WORKS_SINGLE_DETAIL + "/{workId}")
    Observable<CommonApiResponse<WorkDetailBean>> getSingleWork(@Path("workId") String workId);

    @GET(RetrofitUrl.USER_MESSAGE_LIST)
    Observable<CommonApiResponse<MessageListBean>> getMessageList(@Query("msgType") int msgType, @Query("lastId") int lastId);

    @Headers({"Content-Type:application/json;charset=UTF-8"})
    @POST(RetrofitUrl.USER_ADD_MESSAGE)
    Observable<CommonApiResponse> addMessage(@Body AddMessageRequest addMessageRequest);

    @GET(RetrofitUrl.WORKS_AUDIT)
    Observable<CommonApiResponse<AuditBean>> getAuditWorkList(@Query("searchName") String searchName, @Query("orderType") String orderType, @Query("lastId") String lastId);

    /*@GET(RetrofitUrl.WORKS_COMMUNITY_LIST)
    Observable<CommonApiResponse<List<AssociationBean>>> getCommunityList(@Query("start") int start, @Query("dataType") int dataType);*/

    @GET(RetrofitUrl.USER_THIRD_LOGIN)
    Observable<CommonApiResponse<LoginBodyBean>> postLoginInfo(@Query("openId") String openId, @Query("headUrl") String headUrl, @Query("nickName") String nickName);

    @Headers({"Content-Type:application/json;charset=UTF-8"})
    @POST(RetrofitUrl.USER_ADD_COMMENT)
    Observable<CommonApiResponse> addComment(@Body AddCommentRequest addCommentRequest);

    @Headers({"Content-Type:application/json;charset=UTF-8"})
    @POST(RetrofitUrl.USER_ADD_WORK)
    Observable<CommonApiResponse> addCommunity(@Body AddCommunityRequest addCommunityRequest);

    @GET(RetrofitUrl.WORKS_MIX)
    Observable<CommonApiResponse<WorkMixBean>> getWorkMix();

    @Headers({"Content-Type:application/json;charset=UTF-8"})
    @POST(RetrofitUrl.USER_UPDATE_INFO)
    Observable<CommonApiResponse> updateUserInfo(@Body UserInfoBean userInfoBean);

    @GET(RetrofitUrl.USER_LOG_OUT)
    Observable<CommonApiResponse> userLogout();

    @GET(RetrofitUrl.USER_MAIN_PAGE + "/{authorId}")
    Observable<CommonApiResponse<UserMainPageBean>> getUserMainPage(@Path("authorId") String authorId);

    @GET(RetrofitUrl.USER_MAIN_PAGE + "/{authorId}/more")
    Observable<CommonApiResponse<UserMainPageBean>> getUserMainPageMore(@Path("authorId") String authorId, @Query("lastWorkId") String lastWorkId, @Query("type") int type);

    @Headers({"Content-Type:application/json;charset=UTF-8"})
    @POST(RetrofitUrl.TAG_UPDATE_LIST)
    Observable<CommonApiResponse> updateUserTag(@Body Map<String, List<UserTagBean>> tagList);


    /**
     * @param dataType 0：最新作品，
     *                 1：某日作品，
     *                 2：社区精选，
     *                 3：社区最新，
     *                 4：社区最热，
     *                 5：用户标签关联的作品列表，
     *                 6：标签关联的所有作品列表，
     *                 7：个人主页已发布作品列表，
     *                 8：个人主页已下载作品列表
     * @return
     */
    @GET(RetrofitUrl.WORK_COMMON_LIST)
    Observable<CommonApiResponse<CommonWorkListBean>> getWorkList(@Query("lastId") String lastId, @Query("date") String date, @Query("tagName") String tagName, @Query("dataType") int dataType);

    @GET(RetrofitUrl.TAG_TAG_WORK)
    Observable<CommonApiResponse> tagWork(@Query("tagName") String tagName, @Query("workId") String workId, @Query("type") int type);

    @GET(RetrofitUrl.USER_INFO)
    Observable<CommonApiResponse<AllUserBodyBean>> getUserInfo();
}
