package com.arun.a85mm.bean;

import java.io.Serializable;

/**
 * Created by wy on 2017/7/18.
 */

public class UserTagBean implements Serializable{
    public String id;
    public String name;

    /**
     * 0.未显示 1.显示
     */
    public int isShow;

    public String type;
}
