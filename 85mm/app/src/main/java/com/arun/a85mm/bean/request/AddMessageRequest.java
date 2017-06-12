package com.arun.a85mm.bean.request;

import com.arun.a85mm.bean.MessageItem;

import java.util.List;

/**
 * Created by wy on 2017/6/12.
 */

public class AddMessageRequest {

    /**
     * 发件方uid
     */
    public String sender;

    /**
     * 收件方uid
     */
    public String receiver;

    /**
     * 消息内容
     */
    public String content;

    /**
     * 图片列表，由客户端直接上传阿里oss得到对于的图片地址
     */
    public List<MsgImgRequest> imageList;

    public AddMessageRequest(String sender, String receiver, String content, List<MsgImgRequest> imageList) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.imageList = imageList;
    }
}
