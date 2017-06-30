package com.arun.a85mm.helper;

import com.arun.a85mm.bean.UserInfo;

/**
 * Created by wy on 2017/6/30.
 */

public class UserManager {
    private volatile static UserManager userManager;
    private UserInfo userInfo;
    private boolean isLogin;

    public static UserManager getInstance() {
        if (userManager == null) {
            synchronized (UserManager.class) {
                if (userManager == null) {
                    userManager = new UserManager();
                }
            }
        }
        return userManager;
    }

    /*public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }*/

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
