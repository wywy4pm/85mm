package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.bean.ArticleListBody;
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

    public void getArticleListData(final String lastId) {
        addSubscriber(ArticleModel.getInstance()
                .getArticleListData(lastId, new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null && data.code == ErrorCode.SUCCESS) {
                                if (TextUtils.isEmpty(lastId)) {
                                    if (data.body instanceof ArticleListBody) {
                                        getMvpView().refresh(((ArticleListBody) data.body).articleList);
                                    }
                                } else {
                                    getMvpView().refreshMore(((ArticleListBody) data.body).articleList);
                                }
                            } else {
                                ((ArticleFragment) getMvpView()).setHaveMore(false);
                            }
                        }
                    }
                }));
    }
}
