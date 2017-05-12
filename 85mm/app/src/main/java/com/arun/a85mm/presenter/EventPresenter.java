package com.arun.a85mm.presenter;

import android.content.Context;
import android.util.Log;

import com.arun.a85mm.bean.ActionRequest;
import com.arun.a85mm.bean.CommonResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.retrofit.RetrofitInit;
import com.arun.a85mm.view.CommonView;
import com.arun.a85mm.view.CommonView2;
import com.arun.a85mm.view.EventView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by WY on 2017/5/7.
 */
public class EventPresenter extends BasePresenter<EventView> {
    public EventPresenter(Context context) {
        super(context);
    }

    public void recordUserAction(final int type, ActionRequest actionRequest) {
        Subscriber<CommonResponse> subscriber = new Subscriber<CommonResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CommonResponse commonResponse) {
                if (commonResponse != null) {
                    Log.d("TAG", "code = " + commonResponse.code);
                    if (commonResponse.code == ErrorCode.SUCCESS) {
                        Log.d("TAG", "recordUserAction Success");
                        if(getMvpView() != null){
                            getMvpView().eventSuccess(type);
                        }
                    }
                }
            }
        };
        addSubscriber(subscriber);
        RetrofitInit.getApi().recordUserAction(actionRequest)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
