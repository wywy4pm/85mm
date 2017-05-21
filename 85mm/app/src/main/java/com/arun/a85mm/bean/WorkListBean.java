package com.arun.a85mm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/5/5.
 */

public class WorkListBean implements Serializable{
    public String workId;
    public String workTitle;
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
    public List<WorkListItemBean> workDetail;
    public boolean isExpand;
    public boolean isCoverLoad;
    public int backgroundColor;

    public int browseNum;
    public String date;
    public String searchDate;
    public int allDownloadNum;
    public int leftWorkNum;
    public int start;
    public int workNum;
    public boolean isTitle;
    public boolean isBottom;
}
