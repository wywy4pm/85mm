package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.activity.AuditActivity;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.CommonWorkListBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.AssociationFragment;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.AssociationModel;
import com.arun.a85mm.model.ProductModel;
import com.arun.a85mm.view.CommonView4;

import java.util.List;

/**
 * Created by wy on 2017/6/24.
 */

public class AssociationPresenter extends BasePresenter<CommonView4> {
    public AssociationPresenter(Context context) {
        super(context);
    }

    public void getCommunityList(final String lastId, int dataType) {
        addSubscriber(AssociationModel.getInstance()
                .getCommunityList(lastId, dataType, new RequestListenerImpl(getMvpView()) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data.code == ErrorCode.SUCCESS) {
                                List<WorkListBean> workList = ((CommonWorkListBean) data.body).workList;
                                if (TextUtils.isEmpty(lastId)) {
                                    getMvpView().refresh(workList);
                                } else {
                                    getMvpView().refreshMore(workList);
                                }
                            } else if (data.code == ErrorCode.NO_DATA) {
                                if (TextUtils.isEmpty(lastId)) {
                                    getMvpView().refresh(null);
                                } else {
                                    ((AssociationFragment) getMvpView()).setHaveMore(false);
                                }
                            }
                        }
                    }
                }));
    }

    /*public void getProductListData(final String lastWorkId) {
        addSubscriber(ProductModel.getInstance()
                .getWorksList(lastWorkId, new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null && data != null) {
                            if (data.code == ErrorCode.SUCCESS) {
                                if (data.body != null && data.body instanceof CommonWorkListBean) {
                                    List<WorkListBean> workList = ((CommonWorkListBean) data.body).workList;
                                    if (TextUtils.isEmpty(lastWorkId)) {
                                        getMvpView().refresh(workList);
                                    } else {
                                        getMvpView().refreshMore(workList);
                                    }
                                }
                            } else if (data.code == ErrorCode.NO_DATA) {
                                ((ProductionFragment) getMvpView()).setHaveMore(false);
                            }
                        }
                    }
                }));
    }*/

}
