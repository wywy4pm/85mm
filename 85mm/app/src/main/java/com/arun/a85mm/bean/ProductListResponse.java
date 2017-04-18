package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by wy on 2017/4/18.
 */

public class ProductListResponse {
    public int code;
    public List<WorkListBean> workList;

    public static class WorkListBean {
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
        public String createTime;
        public List<WorkListItemBean> workDetail;

        public static class WorkListItemBean {
            public String imageUrl;
            public int height;
            public int width;

            public String workId;
            public String workTitle;
            public String authorName;
            public String authorHeadImg;
            public String authorPageUrl;
        }
    }
}
