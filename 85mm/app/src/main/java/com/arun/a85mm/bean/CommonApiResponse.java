package com.arun.a85mm.bean;

/**
 * Created by wy on 2017/5/22.
 */

public class CommonApiResponse<T> {
    public int start;
    public int code;
    public String uid;
    public String msg;
    public String morePageImage;
    public T body;
    public HideReadBean hideRead;

    /*public List<String> copyWrite;
    public T goodsList;
    public T workList;
    public T guidePage;
    public T article;
    public T articleList;*/
}
