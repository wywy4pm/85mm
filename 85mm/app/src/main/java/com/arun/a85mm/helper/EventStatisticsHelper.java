package com.arun.a85mm.helper;

import android.content.Context;

import com.arun.a85mm.bean.ActionBean;
import com.arun.a85mm.presenter.EventPresenter;
import com.arun.a85mm.utils.AppUtils;
import com.arun.a85mm.utils.DeviceUtils;
import com.arun.a85mm.utils.GsonUtils;
import com.arun.a85mm.view.MvpView;

import java.util.List;

/**
 * Created by WY on 2017/5/7.
 */
public class EventStatisticsHelper implements MvpView {
    private EventPresenter presenter;

    @SuppressWarnings("unchecked")
    public EventStatisticsHelper(Context context) {
        presenter = new EventPresenter(context);
        presenter.attachView(this);
    }

    /**
     * 用户行为记录 actionList
     */
    public void recordUserAction(Context context, List<ActionBean> actionList) {
        String uid = "";
        String deviceId = DeviceUtils.getMobileIMEI(context);
        String appVersion = AppUtils.getAppVersion(context);
        String osVersion = String.valueOf(DeviceUtils.getMobileSDK());
        String deviceModel = DeviceUtils.getMobileModel();
        String actionListJson = GsonUtils.toJson(actionList);
        if (presenter != null) {
            presenter.recordUserAction(uid, deviceId, appVersion, osVersion, deviceModel, actionListJson);
        }
        //recordUserAction(uid, deviceId, appVersion, osVersion, deviceModel, actionListJson);
    }


    @Override
    public void onError(String error, String tag) {

    }

    public void detachView() {
        if (presenter != null) {
            presenter.detachView();
        }
    }

}
