package com.arun.a85mm.model;

import com.arun.a85mm.listener.CommonRequestListener;
import com.arun.a85mm.retrofit.RetrofitInit;

import rx.Subscriber;

/**
 * Created by wy on 2017/5/22.
 */

public class SettingModel extends BaseModel {
    private volatile static SettingModel instance;

    public static SettingModel getInstance() {
        if (instance == null)
            synchronized (SettingModel.class) {
                if (instance == null) {
                    instance = new SettingModel();
                }
            }
        return instance;
    }

    public Subscriber queryConfig(String deviceId, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().queryConfig(deviceId), listener);
    }
}
