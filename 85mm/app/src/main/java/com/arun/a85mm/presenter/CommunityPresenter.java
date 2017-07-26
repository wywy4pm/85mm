package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.CommonWorkListBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.CommunityFragment;
import com.arun.a85mm.fragment.LeftWorksFragment;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.ProductModel;
import com.arun.a85mm.retrofit.RetrofitInit;
import com.arun.a85mm.view.CommonView;
import com.arun.a85mm.view.CommonView4;

import java.util.List;

/**
 * Created by WY on 2017/5/3.
 */
public class CommunityPresenter extends BasePresenter<CommonView4> {
    public CommunityPresenter(Context context) {
        super(context);
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
                                    getMvpView().refresh(data.body);
                                } else {
                                    getMvpView().refreshMore(data.body);
                                }
                            } else {
                                ((CommunityFragment) getMvpView()).setHaveMore(false);
                            }
                        }
                    }
                }));
    }

    public void getWorksLeft(final String date, String lastWorkId, final boolean isPullRefresh) {

        addSubscriber(ProductModel.getInstance()
                .getWorksOneDayLeft(date, lastWorkId, new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null && data.code == ErrorCode.SUCCESS) {
                                List<WorkListBean> workList = ((CommonWorkListBean) data.body).workList;
                                if (isPullRefresh) {
                                    getMvpView().refresh(workList);
                                } else {
                                    getMvpView().refreshMore(workList);
                                }
                            } else {
                                ((LeftWorksFragment) getMvpView()).setHaveMore(false);
                            }
                        }
                    }
                }));
    }

    public void getWorkMix() {
        addSubscriber(ProductModel.getInstance()
                .getWorkMix(new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
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
