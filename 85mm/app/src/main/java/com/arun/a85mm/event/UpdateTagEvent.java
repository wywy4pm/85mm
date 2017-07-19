package com.arun.a85mm.event;

/**
 * Created by wy on 2017/7/19.
 */

public class UpdateTagEvent {
    public String tagName;
    public boolean isAdd;

    public UpdateTagEvent(String tagName, boolean isAdd) {
        this.tagName = tagName;
        this.isAdd = isAdd;
    }

}
