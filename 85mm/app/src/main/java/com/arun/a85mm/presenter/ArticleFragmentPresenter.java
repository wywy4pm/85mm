package com.arun.a85mm.presenter;

import android.content.Context;

import com.arun.a85mm.bean.ArticleListResponse;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.ArticleFragment;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.ArticleModel;
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
        addSubscriber(ArticleModel.getInstance()
                .getArticleListData(pageNum, uid, deviceId, new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null && data.code == ErrorCode.SUCCESS) {
                                if (pageNum == 1) {
                                    getMvpView().refresh(data.articleList);
                                } else if (pageNum > 1) {
                                    getMvpView().refreshMore(data.articleList);
                                }
                            } else {
                                ((ArticleFragment) getMvpView()).setHaveMore(false);
                            }
                        }
                    }
                }));

        /*Subscriber<ArticleListResponse> subscriber = new Subscriber<ArticleListResponse>() {
            @Override
            public void onCompleted() {
                if (getMvpView() != null) {
                    getMvpView().onRefreshComplete();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (getMvpView() != null) {
                    getMvpView().onError(ErrorCode.NETWORK_ERROR, null);
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
        RetrofitInit.getApi().getArticleList(pageNum, uid, deviceId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);*/
    }
}
