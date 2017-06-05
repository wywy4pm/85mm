package com.arun.a85mm.presenter;

import android.content.Context;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.ProductModel;
import com.arun.a85mm.view.CommonView3;

/**
 * Created by wy on 2017/6/5.
 */

public class OneWorkPresenter extends BasePresenter<CommonView3> {
    public OneWorkPresenter(Context context) {
        super(context);
    }

    public void getOneWorkDetail(String workId) {
        addSubscriber(ProductModel.getInstance()
                .getSingleWork(workId, new RequestListenerImpl(getMvpView()) {
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null && data.code == ErrorCode.SUCCESS) {
                                getMvpView().refresh(0, data.body);
                            }
                        }
                    }
                }));
    }
}
