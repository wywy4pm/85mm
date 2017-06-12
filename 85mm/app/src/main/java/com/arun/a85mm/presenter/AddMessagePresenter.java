package com.arun.a85mm.presenter;

import android.content.Context;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.MessageItem;
import com.arun.a85mm.bean.request.MsgImgRequest;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.MessageModel;
import com.arun.a85mm.view.CommonView3;

import java.util.List;

/**
 * Created by wy on 2017/6/12.
 */

public class AddMessagePresenter extends BasePresenter<CommonView3> {
    public AddMessagePresenter(Context context) {
        super(context);
    }

    public void addMessage(String sender, String receiver, String content, List<MsgImgRequest> imageList) {
        addSubscriber(MessageModel.getInstance()
                .addMessage(sender, receiver, content, imageList, new RequestListenerImpl(getMvpView()) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null && data != null) {
                            if (data.code == ErrorCode.SUCCESS) {
                                getMvpView().refresh(0, data);
                            }
                        }
                    }
                }));
    }
}
