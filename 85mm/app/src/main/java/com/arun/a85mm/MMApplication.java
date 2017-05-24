package com.arun.a85mm;

import android.app.Application;

import com.arun.a85mm.helper.AppHelper;
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
    }
}
