package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by wy on 2017/5/8.
 */

public class ActionRequest {
    public String uid;
    public String deviceId;
    public String appVersion;
    public String osVersion;
    public String deviceModel;
    public List<ActionBean> actionList;

    public ActionRequest(String uid, String deviceId, String appVersion, String osVersion, String deviceModel, List<ActionBean> actionList) {
        this.uid = uid;
        this.deviceId = deviceId;
        this.appVersion = appVersion;
        this.osVersion = osVersion;
        this.deviceModel = deviceModel;
        this.actionList = actionList;
    }
}
