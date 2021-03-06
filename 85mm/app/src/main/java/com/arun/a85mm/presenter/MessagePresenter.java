package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.MessageListBean;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.MessageFragment;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.MessageModel;
import com.arun.a85mm.view.CommonView4;

import java.util.List;

/**
 * Created by wy on 2017/6/7.
 */

public class MessagePresenter extends BasePresenter<CommonView4> {
    public MessagePresenter(Context context) {
        super(context);
    }

    public void getMessageList(int msgType, final int lastMsgId) {
        addSubscriber(MessageModel.getInstance()
                .getMessageList(msgType, lastMsgId, new RequestListenerImpl(getMvpView()) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null && data != null) {
                            if (data.code == ErrorCode.SUCCESS) {
                                if (lastMsgId == 0) {
                                    getMvpView().refresh(((MessageListBean) data.body).msgList);
                                } else {
                                    getMvpView().refreshMore(((MessageListBean) data.body).msgList);
                                }
                            } else if (data.code == ErrorCode.NO_DATA) {
                                if (lastMsgId == 0) {
                                    ((MessageFragment) getMvpView()).setNoData();
                                } else {
                                    ((MessageFragment) getMvpView()).setHaveMore(false);
                                }
                            }
                        }
                    }
                }));
    }
}