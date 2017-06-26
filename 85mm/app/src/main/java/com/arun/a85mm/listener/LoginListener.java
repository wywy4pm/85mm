package com.arun.a85mm.listener;

import com.arun.a85mm.bean.UserInfo;

/**
 * Created by wy on 2017/6/26.
 */

public interface LoginListener {
    void onLoginSuccess(UserInfo userInfo);

    void onLoginFailed();
}
