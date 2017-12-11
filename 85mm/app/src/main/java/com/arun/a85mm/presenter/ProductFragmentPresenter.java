package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.activity.AmountWorkActivity;
import com.arun.a85mm.activity.OneWorkActivity;
import com.arun.a85mm.bean.AwardBodyBean;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.CommonWorkListBean;
import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.ProductModel;
import com.arun.a85mm.model.TagModel;
import com.arun.a85mm.model.UserModel;
import com.arun.a85mm.view.CommonView4;

import java.util.List;

/**
 * Created by wy on 2017/4/18.
 */

public class ProductFragmentPresenter extends BasePresenter<CommonView4> {
    public static final int TYPE_TAG_WORK = 0;
    public static final int TYPE_USER_AWARD = 1;

    public ProductFragmentPresenter(Context context) {
        super(context);
    }

    /*public void getProductListData(final String lastWorkId) {
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
    }*/

    public void getProductListData(int dataType, String tagName, final String lastWorkId) {
        addSubscriber(ProductModel.getInstance()
                .getWorksList(dataType, tagName, lastWorkId, new RequestListenerImpl(getMvpView()) {

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
                                ((ProductionFragment) getMvpView()).setNoDataView();
                            }
                        }
                    }
                }));
    }

    public void tagWork(final UserTagBean tagBean, String workId) {
        addSubscriber(TagModel.getInstance()
                .tagWork(tagBean.name, workId, tagBean.tagType, new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null && data != null) {
                            if (data.code == ErrorCode.SUCCESS) {
                                getMvpView().refresh(TYPE_TAG_WORK, tagBean);
                            }
                        }
                    }

                    @Override
                    public void onError(int errorType, int errorMsg) {
                        super.onError(errorType, errorMsg);
                        if (getMvpView() != null) {
                            if (getMvpView() instanceof ProductionFragment) {
                                ((ProductionFragment) getMvpView()).resetUserTag(tagBean);
                            }
                        }
                    }
                }));
    }

    public void userAward(final String workId) {
        addSubscriber(UserModel.getInstance()
                .userAward(workId, new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null && getMvpView() instanceof ProductionFragment
                                && data != null) {
                            if (data.code == ErrorCode.SUCCESS) {
                                ((ProductionFragment) getMvpView()).setWorkId(workId);
                                getMvpView().refresh(TYPE_USER_AWARD, data.body);
                            } else if (data.code == ErrorCode.AWARD_DONE) {
                                ((ProductionFragment) getMvpView()).setWorkId(workId);
                                ((ProductionFragment) getMvpView()).jumpToAmountWork((AwardBodyBean) data.body, AmountWorkActivity.TYPE_COMMON);
                            } else if (data.code == ErrorCode.AWARD_NO_ENOUGH) {
                                ((ProductionFragment) getMvpView()).noEnoughCoins((AwardBodyBean) data.body);
                            }
                        }
                    }
                }));
    }
}
