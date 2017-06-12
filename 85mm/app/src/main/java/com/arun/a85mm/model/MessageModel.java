package com.arun.a85mm.model;

import com.arun.a85mm.bean.MessageItem;
import com.arun.a85mm.bean.request.AddMessageRequest;
import com.arun.a85mm.bean.request.MsgImgRequest;
import com.arun.a85mm.listener.CommonRequestListener;
import com.arun.a85mm.retrofit.RetrofitInit;

import java.util.List;

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

    public Subscriber addMessage(String sender, String receiver, String content, List<MsgImgRequest> imageList, CommonRequestListener listener) {
        AddMessageRequest request = new AddMessageRequest(sender, receiver, content, imageList);
        return request(RetrofitInit.getApi().addMessage(request), listener);
    }
}
