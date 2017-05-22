package com.arun.a85mm.model;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.listener.CommonRequestListener;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by wy on 2017/5/22.
 */

public class BaseModel {

    @SuppressWarnings("unchecked")
    public Subscriber request(Observable observable, final CommonRequestListener listener) {
        if (observable != null) {
            Subscriber<CommonApiResponse> subscriber = new Subscriber<CommonApiResponse>() {
                @Override
                public void onCompleted() {
                    if (listener != null) {
                        listener.onComplete();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (listener != null) {
                        if (e instanceof ConnectException
                                || e instanceof SocketTimeoutException
                                || e instanceof TimeoutException
                                || e instanceof UnknownHostException
                                || e instanceof UnknownServiceException) {
                            listener.onError(ErrorCode.NETWORK_ERROR, R.string.net_error);
                        } else if (e instanceof JsonSyntaxException || e instanceof JsonParseException) {
                            listener.onError(ErrorCode.DATA_FORMAT_ERROR, R.string.net_error_data_format);
                        }
                        listener.onComplete();
                    }
                }

                @Override
                public void onNext(CommonApiResponse object) {
                    if (listener != null) {
                        if (object != null) {
                            listener.onSuccess(object);
                        }
                    }
                }
            };
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            listener.onStart();
                        }
                    })
                    .subscribe(subscriber);
            return subscriber;
        }
        return null;
    }

}
