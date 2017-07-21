package com.arun.a85mm.event;

/**
 * Created by wy on 2017/7/19.
 */

public class UpdateTagEvent {
    public String tagName;
    public int position;
    public boolean isAdd;

    public UpdateTagEvent(String tagName, int position, boolean isAdd) {
        this.tagName = tagName;
        this.position = position;
        this.isAdd = isAdd;
    }

}
