package com.arun.a85mm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/4/18.
 */

public class ProductListResponse implements Serializable{
    public int code;
    public String uid;
    public List<WorkListBean> workList;
}
