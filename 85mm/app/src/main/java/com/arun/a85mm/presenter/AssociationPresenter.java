package com.arun.a85mm.presenter;

import android.content.Context;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.AssociationModel;
import com.arun.a85mm.view.CommonView4;

/**
 * Created by wy on 2017/6/24.
 */

public class AssociationPresenter extends BasePresenter<CommonView4> {
    public AssociationPresenter(Context context) {
        super(context);
    }

    public void getCommunityList(int start, int dataType) {
        addSubscriber(AssociationModel.getInstance()
                .getCommunityList(start, dataType, new RequestListenerImpl(getMvpView()) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            getMvpView().refresh(data);
                            /*if (data != null && data.code == ErrorCode.SUCCESS) {
                                getMvpView().refresh(data);
                            }*/
                        }
                    }
                }));
    }
}
