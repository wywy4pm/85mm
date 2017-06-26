package com.arun.a85mm.presenter;

import android.content.Context;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.UserModel;
import com.arun.a85mm.view.CommonView2;

/**
 * Created by wy on 2017/6/26.
 */

public class LoginPresenter extends BasePresenter<CommonView2> {

    public LoginPresenter(Context context) {
        super(context);
    }

    public void postLoginInfo(String openId, String headUrl, String nickName) {
        addSubscriber(UserModel.getInstance()
                .postLoginInfo(openId, headUrl, nickName, new RequestListenerImpl(getMvpView()) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null && data != null && data.code == ErrorCode.SUCCESS) {
                            getMvpView().refresh(data);
                        }
                    }
                }));
    }
}
