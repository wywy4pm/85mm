package com.arun.a85mm.presenter;

import android.content.Context;

import com.arun.a85mm.bean.ArticleListResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.ArticleFragment;
import com.arun.a85mm.retrofit.RetrofitInit;
import com.arun.a85mm.view.CommonView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by WY on 2017/4/13.
 */
public class ArticleFragmentPresenter extends BasePresenter<CommonView> {
    public ArticleFragmentPresenter(Context context) {
        super(context);
    }

    public void getArticleListData(final int pageNum, String uid, String deviceId) {
        Subscriber<ArticleListResponse> subscriber = new Subscriber<ArticleListResponse>() {
            @Override
            public void onCompleted() {
                if (getMvpView() != null) {
                    getMvpView().onRefreshComplete();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (getMvpView() != null) {
                    getMvpView().onError(e.toString(), null);
                }

            }

            @SuppressWarnings("unchecked")
            @Override
            public void onNext(ArticleListResponse articleListResponse) {
                if (getMvpView() != null) {
                    if (articleListResponse != null && articleListResponse.code == ErrorCode.SUCCESS) {
                        if (pageNum == 1) {
                            getMvpView().refresh(articleListResponse);
                        } else if (pageNum > 1) {
                            getMvpView().refreshMore(articleListResponse);
                        }
                    } else {
                        ((ArticleFragment) getMvpView()).setHaveMore(false);
                    }
                }

            }
        };

        addSubscriber(subscriber);
        RetrofitInit.getApi().getArticleList(pageNum, uid, deviceId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
