package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.bean.CommunityResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.retrofit.RetrofitInit;
import com.arun.a85mm.view.CommonView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by WY on 2017/5/3.
 */
public class CommunityPresenter extends BasePresenter<CommonView>{
    public CommunityPresenter(Context context) {
        super(context);
    }

    public void getWorksGoods(String userId, String deviceId, final String lastDate){
        Subscriber<CommunityResponse> subscriber = new Subscriber<CommunityResponse>() {
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
            public void onNext(CommunityResponse communityResponse) {
                if (getMvpView() != null) {
                    if (communityResponse != null && communityResponse.code == ErrorCode.SUCCESS) {
                        if (TextUtils.isEmpty(lastDate)) {
                            getMvpView().refresh(communityResponse);
                        } else {
                            getMvpView().refreshMore(communityResponse);
                        }
                    } else {
                        ((ProductionFragment) getMvpView()).setHaveMore(false);
                    }
                }
            }
        };

        addSubscriber(subscriber);
        RetrofitInit.getApi().getWorksGoods(userId, deviceId, lastDate).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
