package com.arun.a85mm.event;

/**
 * Created by wy on 2017/8/10.
 */

public class DeleteCommentEvent {
    public String commentId;

    public DeleteCommentEvent(String commentId) {
        this.commentId = commentId;
    }
}
