package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by wy on 2017/5/22.
 */

public class CommonApiResponse<T> {
    public int start;
    public int code;
    public String uid;
    public String msg;
    public String morePageImage;
    public T body;

    /**
     * 分页最后一个作品ID
     */
    public String lastWorkId;


    public HideReadBean hideRead;
    /**
     * 是否有新消息，0：没有，1：有
     */
    public int hasNewMsg;

    /**
     * oss相关信息
     */
    //public OssInfoBean ossInfo;

    /**
     * 审核相关信息
     */
    public AuditInfoBean auditInfo;


    /**
     * 微信登录标识    是否已微信登陆，0：未登陆，1：已登陆
     */
    public int wechatLogin;

    /**
     * 用户信息
     */
    public UserInfoBean userInfo;

    /**
     * 用户标签列表
     */
    public List<UserTagBean> userTagList;

}
