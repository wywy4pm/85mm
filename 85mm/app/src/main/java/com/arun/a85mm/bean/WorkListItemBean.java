package com.arun.a85mm.bean;

import java.io.Serializable;

/**
 * Created by wy on 2017/5/5.
 */

public class WorkListItemBean implements Serializable{
    public String imageUrl;
    public int height;
    public int width;
    public int backgroundColor;

    public String workId;
    public String workTitle;
    public String authorName;
    public String authorHeadImg;
    public String authorPageUrl;
    public String sourceUrl;

    public boolean isLoad;
}
