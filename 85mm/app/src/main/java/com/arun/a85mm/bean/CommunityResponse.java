package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by WY on 2017/5/3.
 */
public class CommunityResponse {

    public int code;
    public List<GoodsListBean> goodsList;

    public static class GoodsListBean {

        public int browseNum;
        public String date;
        public int downloadNum;
        public int leftWorkNum;
        public int start;
        public int workNum;

        public List<WorkListBean> workList;

        /*public static class WorkListBean {

            public String authorHeadImg;
            public String authorName;
            public String authorPageUrl;
            public int coverHeight;
            public String coverUrl;
            public int coverWidth;
            public String createTime;
            public int downloadNum;
            public String sourceLogo;
            public String sourceSite;
            public String sourceUrl;
            public int totalImageNum;
            public int workId;
            public String workTitle;
            public List<WorkDetailBean> workDetail;

            public static class WorkDetailBean {
                public int height;
                public String imageUrl;
                public int width;
            }
        }*/
    }
}
