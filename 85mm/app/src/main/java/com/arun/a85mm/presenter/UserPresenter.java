package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.UserModel;
import com.arun.a85mm.view.CommonView3;

/**
 * Created by wy on 2017/7/8.
 */

public class UserPresenter extends BasePresenter<CommonView3> {
    public static final int TYPE_UPDATE_USER_NAME = 1;
    public static final int TYPE_UPDATE_USER_HEAD = 2;
    public static final int TYPE_UPDATE_USER_DESCRIPTION = 3;
    public static final int TYPE_UPDATE_USER_COVER = 4;
    public static final int TYPE_LOG_OUT = 5;

    public UserPresenter(Context context) {
        super(context);
    }

    public void updateUserInfo(final String name, final String headerUrl, final String description, final String coverUrl) {
        addSubscriber(UserModel.getInstance()
                .updateUserInfo(name, headerUrl, description, coverUrl, new RequestListenerImpl(getMvpView()) {
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null && data.code == ErrorCode.SUCCESS) {
                                if (!TextUtils.isEmpty(name)) {
                                    getMvpView().refresh(TYPE_UPDATE_USER_NAME, data);
                                } else if (!TextUtils.isEmpty(headerUrl)) {
                                    getMvpView().refresh(TYPE_UPDATE_USER_HEAD, data);
                                } else if (!TextUtils.isEmpty(description)) {
                                    getMvpView().refresh(TYPE_UPDATE_USER_DESCRIPTION, data);
                                } else if (!TextUtils.isEmpty(coverUrl)) {
                                    getMvpView().refresh(TYPE_UPDATE_USER_COVER, data);
                                }
                            }
                        }
                    }
                }));
    }

    public void userLogout() {
        addSubscriber(UserModel.getInstance()
                .userLogout(new RequestListenerImpl(getMvpView()) {
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null && data.code == ErrorCode.SUCCESS) {
                                getMvpView().refresh(TYPE_LOG_OUT, data);
                            }
                        }
                    }
                }));
    }

    public void getUserMain(){

    }
}
