package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.bean.CommunityResponse;
import com.arun.a85mm.bean.ConfigResponse;
import com.arun.a85mm.bean.ProductListResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.retrofit.RetrofitInit;
import com.arun.a85mm.view.CommonView2;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wy on 2017/5/12.
 */

public class SettingPresenter extends BasePresenter<CommonView2> {
    public SettingPresenter(Context context) {
        super(context);
    }

    public void queryConfig(String deviceId) {
        Subscriber<ConfigResponse> subscriber = new Subscriber<ConfigResponse>() {
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
            public void onNext(ConfigResponse configResponse) {
                if (configResponse != null) {
                    if (configResponse.code == ErrorCode.SUCCESS) {
                        if (getMvpView() != null) {
                            getMvpView().refresh(configResponse);
                        }
                    }
                }
            }
        };
        addSubscriber(subscriber);
        RetrofitInit.getApi().queryConfig(deviceId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    public void getWorksGoods(String userId, String deviceId, final String lastDate) {
        Subscriber<CommunityResponse> subscriber = new Subscriber<CommunityResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onNext(CommunityResponse communityResponse) {
                if (getMvpView() != null) {
                    if (communityResponse != null && communityResponse.code == ErrorCode.SUCCESS) {
                        getMvpView().refresh(communityResponse);
                    }
                }
            }
        };

        addSubscriber(subscriber);
        RetrofitInit.getApi().getWorksGoods(userId, deviceId, lastDate)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
