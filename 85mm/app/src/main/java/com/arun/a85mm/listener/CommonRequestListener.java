package com.arun.a85mm.listener;

import android.support.annotation.StringRes;

/**
 * Created by wy on 2017/5/22.
 */

public interface CommonRequestListener<T> {
    void onStart();

    void onComplete();

    void onSuccess(T data);

    void onError(int errorType, @StringRes int errorMsg);

}
