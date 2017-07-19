package com.arun.a85mm.presenter;

import android.content.Context;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.TagModel;
import com.arun.a85mm.view.CommonView3;

import java.util.List;

/**
 * Created by wy on 2017/7/19.
 */

public class UserTagPresenter extends BasePresenter<CommonView3> {
    public UserTagPresenter(Context context) {
        super(context);
    }

    public void updateUserTag(List<UserTagBean> tagList) {
        addSubscriber(TagModel.getInstance()
                .updateUserTag(tagList, new RequestListenerImpl(getMvpView()) {
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null &&
                                data != null && data.code == ErrorCode.SUCCESS) {
                            getMvpView().refresh(0, data);
                        }
                    }
                }));
    }
}
