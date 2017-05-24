package com.arun.a85mm.bean;

import android.content.Context;

import com.arun.a85mm.utils.AppUtils;
import com.arun.a85mm.utils.DeviceUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;

/**
 * Created by wy on 2017/5/24.
 */

public class AppBean {

    public String uid;
    public String deviceId;
    public String appVersion;
    public String osVersion;
    public String deviceModel;
    public String osType;

    public AppBean(Context context) {
        uid = SharedPreferencesUtils.getUid(context);
        deviceId = DeviceUtils.getMobileIMEI(context);
        appVersion = AppUtils.getAppVersion(context);
        osVersion = String.valueOf(DeviceUtils.getMobileSDK());
        deviceModel = DeviceUtils.getMobileModel();
    }
}
