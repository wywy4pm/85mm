package com.arun.a85mm.helper;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.arun.a85mm.activity.MainActivity;
import com.arun.a85mm.activity.MoreSettingActivity;
import com.arun.a85mm.bean.PushCustomBean;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.utils.GsonUtils;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONObject;

import java.net.URL;

/**
 * Created by wy on 2017/5/31.
 */

public class PushHelper {

    public static void setPushNotification(PushAgent mPushAgent, final EventStatisticsHelper helper) {
        /*UmengMessageHandler umengAdHandler = new UmengMessageHandler() {

            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                return super.getNotification(context, uMessage);
            }
        };
        mPushAgent.setMessageHandler(umengAdHandler);*/

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                if (msg != null && !TextUtils.isEmpty(msg.custom)) {
                    try {
                        PushCustomBean pushCustomBean = GsonUtils.fromJson(msg.custom, new TypeToken<PushCustomBean>() {
                        }.getType());
                        if (pushCustomBean != null) {
                            String pageUrl = pushCustomBean.pageUrl;
                            String pageTitle = pushCustomBean.pageTitle;
                            if (helper != null) {
                                helper.recordUserAction(context, EventConstant.CLICK_PUSH, "", pageUrl);
                            }
                            UrlJumpHelper.urlJumpTo(context, pageUrl, pageTitle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }
}
