package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by WY on 2017/5/3.
 */
public class CommunityResponse {

    public int code;
    public String uid;
    public List<GoodsListBean> goodsList;

    public static class GoodsListBean {

        public int browseNum;
        public String date;
        public String searchDate;
        public int downloadNum;
        public int leftWorkNum;
        public int start;
        public int workNum;

        public List<WorkListBean> workList;

    }
}
