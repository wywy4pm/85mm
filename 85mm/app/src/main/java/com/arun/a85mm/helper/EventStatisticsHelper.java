package com.arun.a85mm.helper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.BaseActivity;
import com.arun.a85mm.activity.MainActivity;
import com.arun.a85mm.bean.ActionBean;
import com.arun.a85mm.bean.request.ActionRequest;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.event.UpdateAssociateEvent;
import com.arun.a85mm.event.UpdateMesDotEvent;
import com.arun.a85mm.presenter.EventPresenter;
import com.arun.a85mm.utils.AppUtils;
import com.arun.a85mm.utils.DeviceUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.view.EventView;

import org.greenrobot.eventbus.EventBus;

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

    public static List<ActionBean> createOneActionList(int actionType) {
        List<ActionBean> actionList = new ArrayList<>();
        ActionBean actionBean = new ActionBean(actionType, "", "");
        actionList.add(actionBean);
        return actionList;
    }

    public static List<ActionBean> createOneActionList(int actionType, String resourceId, String remark) {
        List<ActionBean> actionList = new ArrayList<>();
        ActionBean actionBean = new ActionBean(actionType, resourceId, remark);
        actionList.add(actionBean);
        return actionList;
    }


    public void recordUserAction(Context context, int type, String resourceId, String remark) {
        recordUserAction(context, type, createOneActionList(type, resourceId, remark));
    }

    public void recordUserAction(Context context, int type) {
        recordUserAction(context, type, createOneActionList(type));
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
        String osType = context.getString(R.string.android);

        ActionRequest actionRequest = new ActionRequest(uid, deviceId, appVersion, osVersion, deviceModel, osType, actionList);
        if (presenter != null) {
            presenter.recordUserAction(type, actionRequest);
        }
    }

    @Override
    public void eventSuccess(int hasNewMsg) {
        if (hasNewMsg == 1) {
            SharedPreferencesUtils.setConfigInt(context, SharedPreferencesUtils.KEY_NEW_MESSAGE, hasNewMsg);
            EventBus.getDefault().post(new UpdateMesDotEvent(hasNewMsg));
        }
    }

    @Override
    public void eventDone(int type) {
        String tips = "";
        if (type == EventConstant.WORK_REPORT) {
            tips = context.getString(R.string.report);
        } else if (type == EventConstant.WORK_SCALE_OVER) {
            tips = context.getString(R.string.scale_over);
        } else if (type == EventConstant.WORK_BAD_COMMNET) {
            tips = context.getString(R.string.bad_comment);
        } else if (type == EventConstant.WORK_REPEAT) {
            tips = context.getString(R.string.repeat);
        } else if (type == EventConstant.WORK_SHOW_SEQ) {
            tips = context.getString(R.string.show_seq);
        } else if (type == EventConstant.WORK_AUDIT_RECOMMEND) {
            tips = context.getString(R.string.recommend_new);
        } else if (type == EventConstant.WORK_ASSOCIATION_RECOMMEND) {
            tips = context.getString(R.string.recommend_jingxuan);
        }
        if (!TextUtils.isEmpty(tips)) {
            if (context instanceof MainActivity) {
                ((MainActivity) context).showTop("[" + tips + "]" + "操作成功");
            } else if (context instanceof BaseActivity) {
                ((BaseActivity) context).showTop("[" + tips + "]" + "操作成功");
            }
        }
        if (type == EventConstant.WORK_ASSOCIATION_DELETE) {
            EventBus.getDefault().post(new UpdateAssociateEvent());
            ((Activity) context).onBackPressed();
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
