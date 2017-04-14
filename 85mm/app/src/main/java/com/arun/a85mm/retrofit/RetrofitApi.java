package com.arun.a85mm.retrofit;


import com.arun.a85mm.bean.ArticleDetailResponse;
import com.arun.a85mm.bean.ArticleListResponse;


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface RetrofitApi {

    @GET(RetrofitUrl.ARTCLE_LIST)
    Observable<ArticleListResponse> getArticleList(@Query("pageNum") int pageNum);

    @GET(RetrofitUrl.ARTCLE_DETAIL)
    Observable<ArticleDetailResponse> getArticleDetail(@Query("id") String id);
}
