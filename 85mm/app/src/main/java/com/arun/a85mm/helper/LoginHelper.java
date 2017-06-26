package com.arun.a85mm.helper;

import android.app.Activity;
import android.content.Context;

import com.arun.a85mm.bean.UserInfo;
import com.arun.a85mm.listener.LoginListener;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by wy on 2017/6/26.
 */

public class LoginHelper {
    public static void login(Context context, final LoginListener listener) {
        UMShareAPI.get(context).getPlatformInfo((Activity) context, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                UserInfo userInfo = new UserInfo();
                if (map.containsKey("openid")) {
                    userInfo.openId = map.get("openid");
                }
                if (map.containsKey("iconurl")) {
                    userInfo.headUrl = map.get("iconurl");
                }
                if (map.containsKey("name")) {
                    userInfo.nickName = map.get("name");
                }

                if (listener != null) {
                    listener.onLoginSuccess(userInfo);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if (listener != null) {
                    listener.onLoginFailed();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }
}
