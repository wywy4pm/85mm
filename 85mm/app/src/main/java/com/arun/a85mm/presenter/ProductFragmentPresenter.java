package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.ProductModel;
import com.arun.a85mm.view.CommonView;

/**
 * Created by wy on 2017/4/18.
 */

public class ProductFragmentPresenter extends BasePresenter<CommonView> {
    public ProductFragmentPresenter(Context context) {
        super(context);
    }

    public void getProductListData(final String lastWorkId) {
        addSubscriber(ProductModel.getInstance()
                .getWorksList(lastWorkId, new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null) {
                                if (data.code == ErrorCode.SUCCESS) {
                                    if (TextUtils.isEmpty(lastWorkId)) {
                                        getMvpView().refresh(data.body);
                                    } else {
                                        getMvpView().refreshMore(data.body);
                                    }
                                } else if (data.code == ErrorCode.NO_DATA) {
                                    ((ProductionFragment) getMvpView()).setHaveMore(false);
                                } else {
                                    getMvpView().onError(data.code, null);
                                }
                            }
                        }
                    }
                }));
    }
}
