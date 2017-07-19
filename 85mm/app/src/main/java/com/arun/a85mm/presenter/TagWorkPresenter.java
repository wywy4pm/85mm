package com.arun.a85mm.presenter;

import android.content.Context;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.fragment.TagWorkFragment;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.TagModel;
import com.arun.a85mm.view.CommonView;

/**
 * Created by wy on 2017/7/19.
 */

public class TagWorkPresenter extends BasePresenter<CommonView> {
    public TagWorkPresenter(Context context) {
        super(context);
    }

    public void getTagWorkList(String tagName, final int start) {
        addSubscriber(TagModel.getInstance()
                .getTagWorkList(tagName, start, new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null && data != null) {
                            if (data.code == ErrorCode.SUCCESS) {
                                ((TagWorkFragment) getMvpView()).setStart(data.start);
                                if (start == 0) {
                                    getMvpView().refresh(data.body);
                                } else {
                                    getMvpView().refreshMore(data.body);
                                }
                            } else if (data.code == ErrorCode.NO_DATA) {
                                ((TagWorkFragment) getMvpView()).setHaveMore(false);
                            }
                        }
                    }
                }));
    }
}
