package com.arun.a85mm.retrofit;

import android.database.Observable;

import com.arun.a85mm.bean.ArticleListResponse;


import retrofit2.http.GET;


public interface RetrofitApi {

    @GET(RetrofitUrl.ARTCLE_LIST)
    Observable<ArticleListResponse> getArticleList();

}
