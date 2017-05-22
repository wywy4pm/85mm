package com.arun.a85mm.model;


import com.arun.a85mm.listener.CommonRequestListener;
import com.arun.a85mm.retrofit.RetrofitInit;

import rx.Subscriber;

/**
 * Created by wy on 2017/5/22.
 */

public class ArticleModel extends BaseModel {
    private volatile static ArticleModel instance;

    public static ArticleModel getInstance() {
        if (instance == null)
            synchronized (ArticleModel.class) {
                if (instance == null) {
                    instance = new ArticleModel();
                }
            }
        return instance;
    }

    public Subscriber getArticleListData(int pageNum, String uid, String deviceId, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getArticleList(pageNum, uid, deviceId), listener);
    }

    public Subscriber getArticleDetailData(String articleId, String uid, String deviceId, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getArticleDetail(articleId, uid, deviceId), listener);
    }
}
