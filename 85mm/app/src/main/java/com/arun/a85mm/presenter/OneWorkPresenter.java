package com.arun.a85mm.presenter;

import android.content.Context;

import com.arun.a85mm.activity.OneWorkActivity;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.bean.WorkDetailBean;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.ProductModel;
import com.arun.a85mm.model.TagModel;
import com.arun.a85mm.model.UserModel;
import com.arun.a85mm.view.CommonView3;

/**
 * Created by wy on 2017/6/5.
 */

public class OneWorkPresenter extends BasePresenter<CommonView3> {
    public static final int TYPE_DETAIL = 0;
    public static final int TYPE_ADD_COMMENT = 1;
    public static final int TYPE_TAG_WORK = 2;
    public static final int TYPE_LOG_OUT = 3;

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
                                if (data.body != null && data.body instanceof WorkDetailBean) {
                                    getMvpView().refresh(TYPE_DETAIL, ((WorkDetailBean) data.body).workInfo);
                                }
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
                            if (getMvpView() instanceof OneWorkActivity) {
                                ((OneWorkActivity) getMvpView()).resetUserTag(tagBean);
                            }
                        }
                    }
                }));
    }

    public void userLogout() {
        addSubscriber(UserModel.getInstance()
                .userLogout(new RequestListenerImpl(getMvpView()) {
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null && data.code == ErrorCode.SUCCESS) {
                                getMvpView().refresh(TYPE_LOG_OUT, data);
                            }
                        }
                    }
                }));
    }
}
