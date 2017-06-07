package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.MessageModel;
import com.arun.a85mm.view.CommonView4;

/**
 * Created by wy on 2017/6/7.
 */

public class MessagePresenter extends BasePresenter<CommonView4> {
    public MessagePresenter(Context context) {
        super(context);
    }

    public void getMessageList(String uid, int msgType, final String lastMsgId) {
        addSubscriber(MessageModel.getInstance()
                .getMessageList(uid, msgType, Integer.parseInt(lastMsgId), new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null && data != null) {
                            if (data.code == ErrorCode.SUCCESS) {
                                if (TextUtils.isEmpty(lastMsgId)) {
                                    getMvpView().refresh(data);
                                } else {
                                    getMvpView().refreshMore(data);
                                }
                            }
                        }
                    }
                }));
    }
}
