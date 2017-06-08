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

    public String sender;
    public String content;
    public String receiver;
    public int msgId;
    public String sendTime;
    public List<MessageItem> imageList;

}
