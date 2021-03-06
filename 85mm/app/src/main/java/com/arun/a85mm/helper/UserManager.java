package com.arun.a85mm.helper;

import com.arun.a85mm.bean.UserInfo;
import com.arun.a85mm.bean.UserInfoBean;

/**
 * Created by wy on 2017/6/30.
 */

public class UserManager {
    private volatile static UserManager userManager;
    private UserInfoBean userInfoBean;
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

    public UserInfoBean getUserInfoBean() {
        return userInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    public void setUserName(String userName) {
        if (userInfoBean != null) {
            userInfoBean.name = userName;
        }
    }

    public void setUserHead(String headUrl) {
        if (userInfoBean != null) {
            userInfoBean.headUrl = headUrl;
        }
    }

    public void setUserBrief(String brief) {
        if (userInfoBean != null) {
            userInfoBean.description = brief;
        }
    }

    public void setUserCover(String coverUrl) {
        if (userInfoBean != null) {
            userInfoBean.coverUrl = coverUrl;
        }
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isLogin() {
        return isLogin;
    }


}
