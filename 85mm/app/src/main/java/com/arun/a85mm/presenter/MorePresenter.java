package com.arun.a85mm.presenter;

import android.content.Context;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.SettingModel;
import com.arun.a85mm.view.CommonView3;

/**
 * Created by WY on 2017/6/4.
 */
public class MorePresenter extends BasePresenter<CommonView3> {
    public static final int TYPE_HIDE_READ = 0;

    public MorePresenter(Context context) {
        super(context);
    }

    public void setHideReadStatus( int type) {
        addSubscriber(SettingModel.getInstance()
                .setHideReadStatus(String.valueOf(type), new RequestListenerImpl(getMvpView()) {
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null && data.code == ErrorCode.SUCCESS) {
                            getMvpView().refresh(TYPE_HIDE_READ, null);
                        }
                    }
                }));
    }
}
