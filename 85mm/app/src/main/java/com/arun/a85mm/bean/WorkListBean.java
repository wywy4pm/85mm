package com.arun.a85mm.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/5/5.
 */

public class WorkListBean implements Serializable {
    public String id;
    public String title;
    public String coverUrl;
    public int coverHeight;
    public int coverWidth;
    public int totalImageNum;
    public int downloadNum;
    public String authorName;
    public String authorHeadImg;
    public String authorPageUrl;
    public String sourceUrl;
    public String sourceLogo;
    public String sourceSite;
    public String createTime;
    public List<WorkListItemBean> imageList;
    public boolean isExpand;
    public boolean isCoverLoad;
    public int backgroundColor;
    public String description;
    public List<CommentsBean> commentList;
    public String uid;

    public int browseNum;
    public String date;
    public String searchDate;
    public int allDownloadNum;
    public int leftWorkNum;
    //public int start;
    public int workNum;
    public boolean isTitle;
    public boolean isBottom;


    /**
     * clickNum : 0
     * copy : 04-24 14:56
     * createDate : 1493017012000
     * id : 1111
     * imageList : []
     * module : 0
     * title : 侧面
     * uid : 0
     */

    public int clickNum;
    public String copy;
    public long createDate;
    public int module;
    public List<String> workTags;

    public String type;
}
