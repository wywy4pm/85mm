package com.arun.a85mm.helper;

import android.app.Notification;
import android.content.Context;

import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by wy on 2017/5/31.
 */

public class PushHelper {

    public static void setPushNotification(PushAgent mPushAgent) {

        UmengMessageHandler umengAdHandler = new UmengMessageHandler() {

            @Override
            public Notification getNotification(Context context, UMessage uMessage) {

                return super.getNotification(context, uMessage);
            }
        };
        mPushAgent.setMessageHandler(umengAdHandler);


        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {

            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }
}
