package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.ProductModel;
import com.arun.a85mm.model.SettingModel;
import com.arun.a85mm.model.UserModel;
import com.arun.a85mm.view.CommonView3;

/**
 * Created by wy on 2017/5/12.
 */

public class SettingPresenter extends BasePresenter<CommonView3> {

    public static final int TYPE_CONFIG = 1;
    public static final int TYPE_WORKS = 2;
    public static final int TYPE_USER_INFO = 3;

    public SettingPresenter(Context context) {
        super(context);
    }

    public void queryConfig() {
        addSubscriber(SettingModel.getInstance()
                .queryConfig(new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (data != null) {
                            if (data.code == ErrorCode.SUCCESS) {
                                if (getMvpView() != null) {
                                    getMvpView().refresh(TYPE_CONFIG, data.body);
                                }
                            }
                        }
                    }
                }));
    }

    public void getWorkMix() {
        addSubscriber(ProductModel.getInstance()
                .getWorkMix(new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null && data.code == ErrorCode.SUCCESS) {
                                getMvpView().refresh(TYPE_WORKS, data);
                            }
                        }
                    }
                }));
    }

    /*public void getUserInfo() {
        addSubscriber(UserModel.getInstance()
                .getUserInfo(new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null && data.code == ErrorCode.SUCCESS) {
                                getMvpView().refresh(TYPE_USER_INFO, data.body);
                            }
                        }
                    }
                }));
    }*/
}
