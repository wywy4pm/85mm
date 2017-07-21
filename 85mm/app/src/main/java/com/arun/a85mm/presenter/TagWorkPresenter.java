package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.TagWorkListBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.TagWorkFragment;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.TagModel;
import com.arun.a85mm.view.CommonView;

import java.util.List;

/**
 * Created by wy on 2017/7/19.
 */

public class TagWorkPresenter extends BasePresenter<CommonView> {
    public TagWorkPresenter(Context context) {
        super(context);
    }

    public void getTagWorkList(final String lastWorkId, String tagName) {
        addSubscriber(TagModel.getInstance()
                .getTagWorkList(lastWorkId, tagName, new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null && data != null) {
                            if (data.code == ErrorCode.SUCCESS) {
                                if (data.body != null && data.body instanceof TagWorkListBean) {
                                    List<WorkListBean> workList = ((TagWorkListBean) data.body).workList;
                                    if (TextUtils.isEmpty(lastWorkId)) {
                                        getMvpView().refresh(workList);
                                    } else {
                                        getMvpView().refreshMore(workList);
                                    }
                                }
                            } else if (data.code == ErrorCode.NO_DATA) {
                                ((TagWorkFragment) getMvpView()).setHaveMore(false);
                            }
                        }
                    }
                }));
    }
}
