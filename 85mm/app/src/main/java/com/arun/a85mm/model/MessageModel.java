package com.arun.a85mm.model;

import com.arun.a85mm.listener.CommonRequestListener;
import com.arun.a85mm.retrofit.RetrofitInit;

import rx.Subscriber;

/**
 * Created by wy on 2017/6/7.
 */

public class MessageModel extends BaseModel {
    private volatile static MessageModel instance;

    public static MessageModel getInstance() {
        if (instance == null)
            synchronized (MessageModel.class) {
                if (instance == null) {
                    instance = new MessageModel();
                }
            }
        return instance;
    }

    public Subscriber getMessageList(String uid, int msgType, int lastMsgId, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getMessageList(uid, msgType, lastMsgId), listener);
    }
}
