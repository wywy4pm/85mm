package com.arun.a85mm.retrofit;


import com.arun.a85mm.bean.ArticleDetailResponse;
import com.arun.a85mm.bean.ArticleListResponse;
import com.arun.a85mm.bean.ProductListResponse;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;


public interface RetrofitApi {

    @GET(RetrofitUrl.ARTICLE_LIST)
    Observable<ArticleListResponse> getArticleList(@Query("pageNum") int pageNum);

    @GET(RetrofitUrl.ARTICLE_DETAIL)
    Observable<ArticleDetailResponse> getArticleDetail(@Query("id") String id);

    @GET(RetrofitUrl.WORKS_LIST)
    Observable<ProductListResponse> getWorksList(@Query("uid") String uid, @Query("lastWorkId") String lastWorkId);

    @GET
    Call<ResponseBody> downLoadImage(@Url String url);
}
