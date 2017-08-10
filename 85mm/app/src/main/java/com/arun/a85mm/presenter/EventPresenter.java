package com.arun.a85mm.presenter;

import android.content.Context;
import android.util.Log;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.EventBackBean;
import com.arun.a85mm.bean.request.ActionRequest;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.retrofit.RetrofitInit;
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

    public void recordUserAction(final int type, final ActionRequest actionRequest) {
        Subscriber<CommonApiResponse> subscriber = new Subscriber<CommonApiResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CommonApiResponse commonApiResponse) {
                if (commonApiResponse != null) {
                    Log.d("TAG", "code = " + commonApiResponse.code);
                    if (commonApiResponse.code == ErrorCode.SUCCESS) {
                        Log.d("TAG", "recordUserAction Success");
                        if (getMvpView() != null) {
                            if (type == EventConstant.WORK_REPORT
                                    || type == EventConstant.WORK_SCALE_OVER
                                    || type == EventConstant.WORK_BAD_COMMNET
                                    || type == EventConstant.WORK_REPEAT
                                    || type == EventConstant.WORK_SHOW_SEQ
                                    || type == EventConstant.WORK_AUDIT_RECOMMEND
                                    || type == EventConstant.WORK_ASSOCIATION_RECOMMEND
                                    || type == EventConstant.WORK_MOVE_TO_ASSOCIATION
                                    || type == EventConstant.WORK_MOVE_TO_AUDIT
                                    || type == EventConstant.WORK_ASSOCIATION_DELETE) {
                                getMvpView().eventDone(type);
                            } else if (type == EventConstant.ASSOCIATION_COMMENT_DELETE) {
                                getMvpView().eventDoneExtra(type,actionRequest.actionList);
                            }
                            if (commonApiResponse.body != null && commonApiResponse.body instanceof EventBackBean) {
                                getMvpView().eventSuccess(((EventBackBean) commonApiResponse.body).hasNewMsg);
                            }
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
