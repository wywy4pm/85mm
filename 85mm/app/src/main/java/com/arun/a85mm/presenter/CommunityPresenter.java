package com.arun.a85mm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.CommunityResponse;
import com.arun.a85mm.bean.LeftWorksResponse;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.fragment.CommunityFragment;
import com.arun.a85mm.fragment.LeftWorksFragment;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.listener.RequestListenerImpl;
import com.arun.a85mm.model.ProductModel;
import com.arun.a85mm.retrofit.RetrofitInit;
import com.arun.a85mm.view.CommonView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by WY on 2017/5/3.
 */
public class CommunityPresenter extends BasePresenter<CommonView> {
    public CommunityPresenter(Context context) {
        super(context);
    }

    public void getWorksGoods(String userId, String deviceId, final String lastDate) {
        addSubscriber(ProductModel.getInstance()
                .getWorksGoods(userId, deviceId, lastDate, new RequestListenerImpl(getMvpView()) {

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
        /*Subscriber<CommunityResponse> subscriber = new Subscriber<CommunityResponse>() {
            @Override
            public void onCompleted() {
                if (getMvpView() != null) {
                    getMvpView().onRefreshComplete();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (getMvpView() != null) {
                    getMvpView().onError(ErrorCode.NETWORK_ERROR, null);
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onNext(CommunityResponse communityResponse) {
                if (getMvpView() != null) {
                    if (communityResponse != null && communityResponse.code == ErrorCode.SUCCESS) {
                        if (TextUtils.isEmpty(lastDate)) {
                            getMvpView().refresh(communityResponse);
                        } else {
                            getMvpView().refreshMore(communityResponse);
                        }
                    } else {
                        ((CommunityFragment) getMvpView()).setHaveMore(false);
                    }
                }
            }
        };

        addSubscriber(subscriber);
        RetrofitInit.getApi().getWorksGoods(userId, deviceId, lastDate).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);*/
    }

    public void getWorksLeft(String userId, String deviceId, final String date, int start, final boolean isPullRefresh) {

        addSubscriber(ProductModel.getInstance()
                .getWorksOneDayLeft(userId, deviceId, date, start, new RequestListenerImpl(getMvpView()) {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(CommonApiResponse data) {
                        if (getMvpView() != null) {
                            if (data != null && data.code == ErrorCode.SUCCESS) {
                                if (isPullRefresh) {
                                    getMvpView().refresh(data);
                                } else {
                                    getMvpView().refreshMore(data);
                                }
                            } else {
                                ((LeftWorksFragment) getMvpView()).setHaveMore(false);
                            }
                        }
                    }
                }));
        /*Subscriber<LeftWorksResponse> subscriber = new Subscriber<LeftWorksResponse>() {
            @Override
            public void onCompleted() {
                if (getMvpView() != null) {
                    getMvpView().onRefreshComplete();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (getMvpView() != null) {
                    getMvpView().onError(ErrorCode.NETWORK_ERROR, null);
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onNext(LeftWorksResponse leftWorksResponse) {
                if (getMvpView() != null) {
                    if (leftWorksResponse != null && leftWorksResponse.code == ErrorCode.SUCCESS) {
                        if (isPullRefresh) {
                            getMvpView().refresh(leftWorksResponse);
                        } else {
                            getMvpView().refreshMore(leftWorksResponse);
                        }
                    } else {
                        ((LeftWorksFragment) getMvpView()).setHaveMore(false);
                    }
                }
            }
        };

        addSubscriber(subscriber);
        RetrofitInit.getApi().getWorksOneDayLeft(userId, deviceId, date, start).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);*/
    }
}
