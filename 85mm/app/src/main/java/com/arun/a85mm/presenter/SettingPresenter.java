package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.CommunityResponse;
import com.arun.a85mm.bean.ConfigResponse;
import com.arun.a85mm.bean.ProductListResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.CommunityFragment;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.ProductModel;
import com.arun.a85mm.model.SettingModel;
import com.arun.a85mm.retrofit.RetrofitInit;
import com.arun.a85mm.view.CommonView2;
import com.arun.a85mm.view.CommonView3;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wy on 2017/5/12.
 */

public class SettingPresenter extends BasePresenter<CommonView3> {

    public static final int TYPE_CONFIG = 1;
    public static final int TYPE_WORKS = 2;

    public SettingPresenter(Context context) {
        super(context);
    }

    public void queryConfig() {
        addSubscriber(SettingModel.getInstance()
                .queryConfig(new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (data != null) {
                            if (data.code == ErrorCode.SUCCESS) {
                                if (getMvpView() != null) {
                                    getMvpView().refresh(TYPE_CONFIG, data);
                                }
                            }
                        }
                    }
                }));
    }

    public void getWorksGoods(final String lastDate) {
        addSubscriber(ProductModel.getInstance()
                .getWorksGoods(lastDate, new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null && data.code == ErrorCode.SUCCESS) {
                                if (TextUtils.isEmpty(lastDate)) {
                                    getMvpView().refresh(TYPE_WORKS, data);
                                }
                            }
                        }
                    }
                }));
    }
}
