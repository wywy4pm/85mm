package com.arun.a85mm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/5/5.
 */

public class WorkListItemBean implements Serializable {
    public String url;
    public int height;
    public int width;
    public int backgroundColor;

    public String id;
    public String workTitle;
    public String description;
    public String authorName;
    public String authorHeadImg;
    public String authorPageUrl;
    public String sourceUrl;
    public List<CommentsBean> comments;
    public AmountInfoBean productInfo;
    public List<String> workTags;

    public boolean isLoad;

    public String createTime;
    public String type;

}
