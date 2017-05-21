package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.bean.ProductListResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.retrofit.RetrofitInit;
import com.arun.a85mm.view.CommonView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wy on 2017/4/18.
 */

public class ProductFragmentPresenter extends BasePresenter<CommonView> {
    public ProductFragmentPresenter(Context context) {
        super(context);
    }

    public void getProductListData(String userId, String deviceId, final String lastWorkId) {
        Subscriber<ProductListResponse> subscriber = new Subscriber<ProductListResponse>() {
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
            public void onNext(ProductListResponse productListResponse) {
                if (getMvpView() != null) {
                    if (productListResponse != null) {
                        if (productListResponse.code == ErrorCode.SUCCESS) {
                            if (TextUtils.isEmpty(lastWorkId)) {
                                getMvpView().refresh(productListResponse);
                            } else {
                                getMvpView().refreshMore(productListResponse);
                            }
                        } else if (productListResponse.code == ErrorCode.NO_DATA) {
                            ((ProductionFragment) getMvpView()).setHaveMore(false);
                        } else {
                            getMvpView().onError(String.valueOf(productListResponse.code), null);
                        }
                    } else {
                        getMvpView().onError("", null);
                    }
                }
            }
        };

        addSubscriber(subscriber);
        RetrofitInit.getApi().getWorksList(userId, deviceId, lastWorkId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
