package com.arun.a85mm.bean.request;

/**
 * Created by wy on 2017/6/26.
 */

public class AddCommentRequest {

    public String workId;
    public String comment;

    public AddCommentRequest(String workId, String comment) {
        this.workId = workId;
        this.comment = comment;
    }

}
