package com.arun.a85mm;

import android.app.Application;

import com.arun.a85mm.helper.AppHelper;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by WY on 2017/5/14.
 */
public class MMApplication extends Application {

    {
        PlatformConfig.setWeixin("wxaa1d1954f46301df", "979486cc1c9736c83f974421282c753e");
        PlatformConfig.setQQZone("1106153512", "sKwAAUJpmHZPVDN2");
        PlatformConfig.setSinaWeibo("3732105978", "6aebd8c81c878394c19f98cde6be4da7", "http://sns.whalecloud.com/sina2/callback");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Config.DEBUG = true;
        UMShareAPI.get(this);
        AppHelper.getInstance().setAppConfig(getApplicationContext());
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.setDebugMode(false);
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }
}
