package com.arun.a85mm.listener;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.view.MvpView;

/**
 * Created by wy on 2017/5/22.
 */

public abstract class RequestListenerImpl implements CommonRequestListener<CommonApiResponse> {
    private MvpView mvpView;

    public RequestListenerImpl(MvpView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onComplete() {
        if (mvpView != null) {
            mvpView.onRefreshComplete();
        }
    }

    @Override
    public void onError(int errorType, int errorMsg) {
        if (mvpView != null) {
            mvpView.onError(errorType, errorMsg);
        }
    }
}
