package com.arun.a85mm;

import android.app.Application;

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
        //PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Config.DEBUG = true;
        UMShareAPI.get(this);
    }
}
