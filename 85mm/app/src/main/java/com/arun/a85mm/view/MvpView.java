package com.arun.a85mm.view;

import android.support.annotation.StringRes;

public interface MvpView {

    void onError(int errorType, @StringRes int errorMsg);

    void onError(int errorType, String errorMsg);

    void onRefreshComplete();
}
