package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by wy on 2017/6/7.
 */

public class MessageItemBean {

    /**
     * sender : 4
     * content : test3
     * imageList : [{"height":994,"imageUrl":"http://85mm.oss-cn-hangzhou.aliyuncs.com/images/2.jpg","width":1242}]
     * receiver : 80
     * msgId : 6
     */

    public String senderId;
    public String senderName;
    public String senderHeadImg;
    public String content;
    public String receiverId;
    public String receiverName;
    public String receiverHeadImg;
    public int id;
    public String sendTime;
    public List<MessageItem> imageList;

    public int backgroundColor;
}
