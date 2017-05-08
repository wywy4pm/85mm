package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by WY on 2017/4/13.
 */
public class ArticleListResponse {

    public int code;
    public String uid;
    public List<ArticleListBean> articleList;

    public static class ArticleListBean {
        public String brief;
        public String headImage;
        public String id;
        public String title;
    }
}
