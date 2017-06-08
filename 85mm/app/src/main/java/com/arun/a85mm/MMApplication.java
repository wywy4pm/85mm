package com.arun.a85mm;

import android.app.Application;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.arun.a85mm.helper.AppHelper;
import com.arun.a85mm.helper.PushHelper;
import com.arun.a85mm.presenter.DeviceTokenPresenter;
import com.arun.a85mm.service.PushIntentService;
import com.arun.a85mm.utils.DeviceUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by WY on 2017/5/14.
 */
public class MMApplication extends Application {

    //OSS的Bucket
    public static final String OSS_BUCKET_NAME = "85mm";

    //设置OSS数据中心域名或者cname域名
    public static final String OSS_BUCKET_ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com";
    //Key
    private static final String ACCESS_KEY_ID = "LTAI2NTBH0TVhoph";
    private static final String ACCESS_KEY_SECRET = "CF0bPVfcbFYY8SJqRUwHS4WBqMugrZ";

    public static OSS oss;

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
                if (TextUtils.isEmpty(SharedPreferencesUtils.getConfigString(
                        getApplicationContext(), SharedPreferencesUtils.KEY_DEVICE_TOKEN))) {
                    SharedPreferencesUtils.setConfigString(getApplicationContext(), SharedPreferencesUtils.KEY_DEVICE_TOKEN, deviceToken);

                    new DeviceTokenPresenter(getApplicationContext())
                            .postDeviceToken(deviceToken);
                }
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        PushHelper.setPushNotification(mPushAgent);
        //mPushAgent.setPushIntentServiceClass(PushIntentService.class);
        //初始化OSS配置
        initOSSConfig();
    }

    private void initOSSConfig() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(ACCESS_KEY_ID, ACCESS_KEY_SECRET);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        if (BuildConfig.DEBUG) {
            OSSLog.enableLog();
        }
        oss = new OSSClient(getApplicationContext(), OSS_BUCKET_ENDPOINT, credentialProvider, conf);
    }
}
