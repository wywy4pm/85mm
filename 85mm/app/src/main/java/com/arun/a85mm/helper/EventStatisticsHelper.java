package com.arun.a85mm.helper;

import android.content.Context;
import android.support.annotation.StringRes;

import com.arun.a85mm.activity.MainActivity;
import com.arun.a85mm.bean.ActionBean;
import com.arun.a85mm.bean.ActionRequest;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.presenter.EventPresenter;
import com.arun.a85mm.utils.AppUtils;
import com.arun.a85mm.utils.DeviceUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.view.EventView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WY on 2017/5/7.
 */
public class EventStatisticsHelper implements EventView {
    private EventPresenter presenter;
    private Context context;

    @SuppressWarnings("unchecked")
    public EventStatisticsHelper(Context context) {
        presenter = new EventPresenter(context);
        presenter.attachView(this);
        this.context = context;
    }

    public static List<ActionBean> createOneActionList(int actionType, String resourceId, String remark) {
        List<ActionBean> actionList = new ArrayList<>();
        ActionBean actionBean = new ActionBean(actionType, resourceId, remark);
        actionList.add(actionBean);
        return actionList;
    }


    /**
     * 用户行为记录 actionList
     */
    public void recordUserAction(Context context, int type, List<ActionBean> actionList) {
        String uid = SharedPreferencesUtils.getUid(context);
        String deviceId = DeviceUtils.getMobileIMEI(context);
        String appVersion = AppUtils.getAppVersion(context);
        String osVersion = String.valueOf(DeviceUtils.getMobileSDK());
        String deviceModel = DeviceUtils.getMobileModel();

        ActionRequest actionRequest = new ActionRequest(uid, deviceId, appVersion, osVersion, deviceModel, actionList);
        if (presenter != null) {
            presenter.recordUserAction(type, actionRequest);
        }
    }

    @Override
    public void eventSuccess() {

    }

    @Override
    public void eventDone(int type) {
        if (context instanceof MainActivity) {
            ((MainActivity) context).showTop("操作成功");
        }
    }

    @Override
    public void onError(int errorType, String errorMsg) {

    }

    @Override
    public void onError(int errorType, @StringRes int errorMsg) {

    }

    @Override
    public void onRefreshComplete() {

    }

    public void detachView() {
        if (presenter != null) {
            presenter.detachView();
        }
    }

}
