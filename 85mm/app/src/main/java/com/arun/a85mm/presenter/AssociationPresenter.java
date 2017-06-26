package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.activity.AuditActivity;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.AssociationFragment;
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

    public void getCommunityList(final int start, int dataType) {
        addSubscriber(AssociationModel.getInstance()
                .getCommunityList(start, dataType, new RequestListenerImpl(getMvpView()) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data.code == ErrorCode.SUCCESS) {
                                ((AssociationFragment) getMvpView()).start = data.start;
                                if (start == 0) {
                                    getMvpView().refresh(data.body);
                                } else {
                                    getMvpView().refreshMore(data.body);
                                }
                            } else if (data.code == ErrorCode.NO_DATA) {
                                if (start == 0) {
                                    getMvpView().refresh(null);
                                } else {
                                    ((AuditActivity) getMvpView()).setHaveMore(false);
                                }
                            }
                        }
                    }
                }));
    }
}
