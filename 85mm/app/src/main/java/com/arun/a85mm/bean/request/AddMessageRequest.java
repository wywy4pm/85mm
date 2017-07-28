package com.arun.a85mm.bean.request;

import java.util.List;

/**
 * Created by wy on 2017/6/12.
 */

public class AddMessageRequest {

    /**
     * 发件方uid
     */
    public String senderId;

    /**
     * 收件方uid
     */
    public String receiverId;

    /**
     * 消息内容
     */
    public String content;

    /**
     * 图片列表，由客户端直接上传阿里oss得到对于的图片地址
     */
    public List<MsgImgRequest> imageList;

    public AddMessageRequest(String sender, String receiver, String content, List<MsgImgRequest> imageList) {
        this.senderId = sender;
        this.receiverId = receiver;
        this.content = content;
        this.imageList = imageList;
    }
}
