package com.arun.a85mm.presenter;

import android.content.Context;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.request.MsgImgRequest;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.AssociationModel;
import com.arun.a85mm.view.CommonView3;

import java.util.List;

/**
 * Created by wy on 2017/6/27.
 */

public class AddCommunityPresenter extends BasePresenter<CommonView3> {
    public AddCommunityPresenter(Context context) {
        super(context);
    }

    public void addCommunity(String title, String description, List<MsgImgRequest> imageList) {
        addSubscriber(AssociationModel.getInstance()
                .addCommunity(title, description, imageList, new RequestListenerImpl(getMvpView()) {
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null && data != null && data.code == ErrorCode.SUCCESS) {
                            getMvpView().refresh(0, data);
                        }
                    }
                }));
    }
}
