package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by wy on 2017/5/5.
 */

public class WorkListBean {
    public String workId;
    public String workTitle;
    public String coverUrl;
    public int coverHeight;
    public int coverWidth;
    public int totalImageNum;
    public String authorName;
    public String authorHeadImg;
    public String authorPageUrl;
    public String sourceUrl;
    public String sourceLogo;
    public String sourceSite;
    public String createTime;
    public List<WorkListItemBean> workDetail;
    public boolean isExpand;
    public boolean isCoverLoad;

    public int browseNum;
    public String date;
    public int downloadNum;
    public int leftWorkNum;
    public int start;
    public int workNum;
}
