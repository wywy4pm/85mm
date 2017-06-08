package com.arun.a85mm.presenter;

import android.content.Context;

import com.arun.a85mm.bean.ArticleDetailResponse;
import com.arun.a85mm.bean.ArticleListResponse;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.ArticleModel;
import com.arun.a85mm.retrofit.RetrofitInit;
import com.arun.a85mm.view.CommonView;
import com.arun.a85mm.view.CommonView2;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by WY on 2017/4/14.
 */
public class ArticleActivityPresenter extends BasePresenter<CommonView2> {

    public ArticleActivityPresenter(Context context) {
        super(context);
    }

    public void getArticleDetailsData(String deviceId) {
        addSubscriber(ArticleModel.getInstance()
                .getArticleDetailData(deviceId, new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null && data.code == ErrorCode.SUCCESS) {
                                getMvpView().refresh(data.body);
                            }
                        }
                    }
                }));
    }
}
