package com.arun.a85mm.presenter;

import android.content.Context;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.CommonWorkListBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.MainPageFragment;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.UserModel;
import com.arun.a85mm.view.CommonView4;

import java.util.List;

/**
 * Created by wy on 2017/7/11.
 */

public class UserMainPagePresenter extends BasePresenter<CommonView4> {
    public UserMainPagePresenter(Context context) {
        super(context);
    }

    public void getMainPage(String authorId) {
        addSubscriber(UserModel.getInstance()
                .getUserMainPage(authorId, new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null) {
                                if (data.code == ErrorCode.SUCCESS) {
                                    getMvpView().refresh(data.body);
                                }
                            }
                        }
                    }
                }));
    }

    public void getMoreMainPage(String lastWorkId, int type) {
        addSubscriber(UserModel.getInstance()
                .getUserMainPageMore(lastWorkId, type, new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null) {
                                if (data.code == ErrorCode.SUCCESS) {
                                    List<WorkListBean> workList = ((CommonWorkListBean) data.body).workList;
                                    getMvpView().refresh(workList);
                                } else if (data.code == ErrorCode.NO_DATA) {
                                    ((MainPageFragment) getMvpView()).setHaveMore(false);
                                }
                            }
                        }
                    }
                }));
    }
}
