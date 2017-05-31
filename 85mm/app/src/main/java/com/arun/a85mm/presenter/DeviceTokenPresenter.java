package com.arun.a85mm.presenter;

import android.content.Context;
import android.util.Log;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.SettingModel;
import com.arun.a85mm.view.CommonView2;

/**
 * Created by wy on 2017/5/31.
 */

public class DeviceTokenPresenter extends BasePresenter<CommonView2> {

    public DeviceTokenPresenter(Context context) {
        super(context);
    }

    public void postDeviceToken(String uid, String deviceId, String deviceToken) {
        addSubscriber(SettingModel.getInstance()
                .postDeviceToken(uid, deviceId, deviceToken, new RequestListenerImpl(getMvpView()) {
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (data != null && data.code == ErrorCode.SUCCESS) {
                            Log.d("TAG", data.toString());
                        }
                    }
                }));
    }
}
