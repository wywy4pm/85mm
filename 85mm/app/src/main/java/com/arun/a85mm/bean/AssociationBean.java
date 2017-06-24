package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by wy on 2017/6/24.
 */

public class AssociationBean {

    /**
     * workTitle : TEST
     * createTime : 6月23日 20:55
     * coverUrl : http://resources.link365.cn/images/3_1498027537330.jpg
     * description : TEST123
     * authorName : SHQ
     * coverWidth : 600
     * coverHeight : 900
     * workId : 43725
     * comments : [{"content":"85mm 真棒","createTime":"6月23日 21:21","authorName":"SHQ","commentId":4}]
     * totalImageNum : 2
     */

    public String workTitle;
    public String createTime;
    public String coverUrl;
    public String description;
    public String authorName;
    public String authorHeadImg;
    public int coverWidth;
    public int coverHeight;
    public int workId;
    public int totalImageNum;
    public List<CommentsBean> comments;

}
