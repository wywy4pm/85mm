package com.arun.a85mm.service;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.arun.a85mm.bean.PushCustomBean;
import com.arun.a85mm.helper.UrlJumpHelper;
import com.arun.a85mm.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.common.AgooConstants;
import org.json.JSONObject;

import static com.taobao.accs.ACCSManager.mContext;

/**
 * Created by wy on 2017/6/5.
 */

public class PushIntentService extends UmengMessageService {

    @Override
    public void onMessage(Context context, Intent intent) {
        try {
            //可以通过MESSAGE_BODY取得消息体
            String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            // 对完全自定义消息的处理方式，点击或者忽略
            //完全自定义消息的点击统计
            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);

            // 使用完全自定义消息来开启应用服务进程的示例代码
            // 首先需要设置完全自定义消息处理方式
            if (!TextUtils.isEmpty(msg.custom)) {
                PushCustomBean pushCustomBean = GsonUtils.fromJson(msg.custom, new TypeToken<PushCustomBean>() {
                }.getType());
                if (pushCustomBean != null) {
                    String pageUrl = pushCustomBean.pageUrl;
                    String pageTitle = pushCustomBean.pageTitle;
                    UrlJumpHelper.urlJumpTo(context, pageUrl, pageTitle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
