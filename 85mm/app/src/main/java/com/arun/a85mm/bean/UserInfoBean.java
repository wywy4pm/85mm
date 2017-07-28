package com.arun.a85mm.bean;

/**
 * Created by wy on 2017/7/8.
 */

public class UserInfoBean {
    public String name;
    public String headUrl;
    public String description;
    public String coverUrl;

    public UserInfoBean(String name, String headUrl, String description, String coverUrl) {
        this.name = name;
        this.headUrl = headUrl;
        this.description = description;
        this.coverUrl = coverUrl;
    }

}
