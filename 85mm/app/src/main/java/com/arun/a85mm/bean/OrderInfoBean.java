package com.arun.a85mm.bean;

import java.io.Serializable;

/**
 * Created by WY on 2017/11/29.
 */
public class OrderInfoBean implements Serializable{
    //订单类型，1：充值，2：打赏，3：被打赏
    public int orderType;
    public String workId;
    public int paidCoin;
    public String buyerUid;
    public String sellerUid;
    public int buyerLeftCoin;
    public String orderTime;
}
