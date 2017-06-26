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
    public static final int TYPE_DETAIL = 0;
    public static final int TYPE_ADD_COMMENT = 1;

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
                                getMvpView().refresh(TYPE_DETAIL, data.body);
                            }
                        }
                    }
                }));
    }

    public void addComment(String workId, String comment) {
        addSubscriber(ProductModel.getInstance()
                .addComment(workId, comment, new RequestListenerImpl(getMvpView()) {
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null && data != null && data.code == ErrorCode.SUCCESS) {
                            getMvpView().refresh(TYPE_ADD_COMMENT, data.body);
                        }
                    }
                }));
    }
}
