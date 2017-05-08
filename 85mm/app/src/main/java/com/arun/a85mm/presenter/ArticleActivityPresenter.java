package com.arun.a85mm.presenter;

import android.content.Context;

import com.arun.a85mm.bean.ArticleDetailResponse;
import com.arun.a85mm.bean.ArticleListResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.retrofit.RetrofitInit;
import com.arun.a85mm.view.CommonView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by WY on 2017/4/14.
 */
public class ArticleActivityPresenter extends BasePresenter<CommonView> {

    public ArticleActivityPresenter(Context context) {
        super(context);
    }

    public void getArticleDetailsData(String articleId, String uid, String deviceId) {
        Subscriber<ArticleDetailResponse> subscriber = new Subscriber<ArticleDetailResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getMvpView() != null) {
                    getMvpView().onError(e.toString(), null);
                }

            }

            @SuppressWarnings("unchecked")
            @Override
            public void onNext(ArticleDetailResponse articleDetailResponse) {
                if (getMvpView() != null) {
                    if (articleDetailResponse != null && articleDetailResponse.code == ErrorCode.SUCCESS) {
                        getMvpView().refresh(articleDetailResponse);
                    }
                }

            }
        };

        addSubscriber(subscriber);
        RetrofitInit.getApi().getArticleDetail(articleId, uid, deviceId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
