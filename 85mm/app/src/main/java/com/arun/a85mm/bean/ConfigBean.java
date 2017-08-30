package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by wy on 2017/7/27.
 */

public class ConfigBean {
    /**
     * 开屏页数据
     */
    public List<GuidePageBean> guideList;
    /**
     * 分配的用户ID
     */
    public String uid;

    /**
     * 隐藏已读
     */
    public HideReadBean hideRead;

    /**
     * 是否有新消息，0：没有，1：有
     */
    public int hasNewMsg;

    /**
     * 是否已登录，0：未登录，1：已登录
     */
    public int isLogin;

    /**
     * 用户信息
     */
    public UserInfoBean userInfo;

    /**
     * 用户标签列表
     */
    public List<UserTagBean> userTagList;

    /**
     * 更多页面的介绍图片
     */
    public String morePageImage;

    /**
     * 导航栏菜单列表
     */
    public List<MenuListBean> menuList;

    /**
     * 自定义菜单列表
     */
    public List<MenuListBean> customMenuList;
}
