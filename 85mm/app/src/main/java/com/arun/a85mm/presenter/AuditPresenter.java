package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.activity.AuditActivity;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.ProductModel;
import com.arun.a85mm.view.CommonView4;

/**
 * Created by wy on 2017/6/16.
 */

public class AuditPresenter extends BasePresenter<CommonView4> {
    public AuditPresenter(Context context) {
        super(context);
    }

    public void getAuditWorkList(String searchName, int orderType, int start, final String lastWorkId) {
        addSubscriber(ProductModel.getInstance()
                .getAuditWorkList(searchName, String.valueOf(orderType), String.valueOf(start), lastWorkId,
                        new RequestListenerImpl(getMvpView()) {
                            @SuppressWarnings("unchecked")
                            @Override
                            public void onSuccess(CommonApiResponse data) {
                                if (getMvpView() != null && data != null) {
                                    if (data.code == ErrorCode.SUCCESS) {
                                        ((AuditActivity) getMvpView()).start = data.start;
                                        ((AuditActivity) getMvpView()).lastWorkId = data.lastWorkId;
                                        if (TextUtils.isEmpty(lastWorkId)) {
                                            getMvpView().refresh(data.body);
                                        } else {
                                            getMvpView().refreshMore(data.body);
                                        }
                                    } else if (data.code == ErrorCode.NO_DATA) {
                                        if (TextUtils.isEmpty(lastWorkId)) {
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
