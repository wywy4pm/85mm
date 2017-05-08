package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by WY on 2017/4/14.
 */
public class ArticleDetailResponse {


    public ArticleBean article;
    public int code;

    public static class ArticleBean {

        public String author;
        public String authorHeadImg;
        public String createTime;
        public int id;
        public String title;
        public List<ArticleDetailBean> contentComponents;

    }
}
