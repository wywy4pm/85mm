package com.arun.a85mm.helper;

import android.content.Context;
import android.os.Message;

import com.arun.a85mm.bean.ActionBean;
import com.arun.a85mm.bean.ActionRequest;
import com.arun.a85mm.bean.ShowTopBean;
import com.arun.a85mm.common.Constant;
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

    @SuppressWarnings("unchecked")
    public EventStatisticsHelper(Context context) {
        presenter = new EventPresenter(context);
        presenter.attachView(this);
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
    public void eventSuccess(int type) {
        if (type == EventConstant.WORK_REPORT) {
            /*Message message = new Message();
            message.what = Constant.WHAT_SHOW_TOP;
            message.obj = new ShowTopBean(isShowingTop, "举报成功");
            if (showTopHandler != null) {
                showTopHandler.sendMessage(message);
            }*/
        }
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
