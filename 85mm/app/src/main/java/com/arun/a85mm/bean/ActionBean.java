package com.arun.a85mm.bean;

/**
 * Created by WY on 2017/5/7.
 */
public class ActionBean {
    public int actionType;
    public String resourceId;
    public String remark;

    public ActionBean(int actionType, String resourceId, String remark) {
        this.actionType = actionType;
        this.resourceId = resourceId;
        this.remark = remark;
    }
}
