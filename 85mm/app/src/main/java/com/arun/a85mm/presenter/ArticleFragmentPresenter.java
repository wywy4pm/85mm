package com.arun.a85mm.presenter;

import android.content.Context;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.ArticleFragment;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.ArticleModel;
import com.arun.a85mm.view.CommonView;

/**
 * Created by WY on 2017/4/13.
 */
public class ArticleFragmentPresenter extends BasePresenter<CommonView> {
    public ArticleFragmentPresenter(Context context) {
        super(context);
    }

    public void getArticleListData(final int pageNum) {
        addSubscriber(ArticleModel.getInstance()
                .getArticleListData(pageNum, new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null && data.code == ErrorCode.SUCCESS) {
                                if (pageNum == 1) {
                                    getMvpView().refresh(data.body);
                                } else if (pageNum > 1) {
                                    getMvpView().refreshMore(data.body);
                                }
                            } else {
                                ((ArticleFragment) getMvpView()).setHaveMore(false);
                            }
                        }
                    }
                }));
    }
}
