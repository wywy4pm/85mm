package com.arun.a85mm.model;

import com.arun.a85mm.bean.UserInfoBean;
import com.arun.a85mm.listener.CommonRequestListener;
import com.arun.a85mm.retrofit.RetrofitInit;

import rx.Subscriber;

/**
 * Created by wy on 2017/6/26.
 */

public class UserModel extends BaseModel {
    private volatile static UserModel instance;

    public static UserModel getInstance() {
        if (instance == null)
            synchronized (UserModel.class) {
                if (instance == null) {
                    instance = new UserModel();
                }
            }
        return instance;
    }

    public Subscriber postLoginInfo(String openId, String headUrl, String nickName, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().postLoginInfo(openId, headUrl, nickName), listener);
    }

    public Subscriber updateUserInfo(String name, String headerUrl, String description, String coverUrl, CommonRequestListener listener) {
        UserInfoBean userInfoBean = new UserInfoBean(name, headerUrl, description, coverUrl);
        return request(RetrofitInit.getApi().updateUserInfo(userInfoBean), listener);
    }
}
