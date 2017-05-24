package com.arun.a85mm.helper;

import android.content.Context;

import com.arun.a85mm.bean.AppBean;

/**
 * Created by wy on 2017/5/24.
 */

public class AppHelper {
    private volatile static AppHelper instance = null;
    private static AppBean appBean;

    public static AppHelper getInstance() {
        if (instance == null)
            synchronized (AppHelper.class) {
                if (instance == null) {
                    instance = new AppHelper();
                }
            }
        return instance;
    }

    public void setAppConfig(Context context) {
        appBean = new AppBean(context);
    }

    public AppBean getAppConfig() {
        return appBean;
    }

}
