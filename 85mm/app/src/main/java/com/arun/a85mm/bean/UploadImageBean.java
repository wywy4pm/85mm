package com.arun.a85mm.bean;

/**
 * Created by wy on 2017/6/8.
 */

public class UploadImageBean {
    public boolean isUpload;
    public String imageUrl;

    public UploadImageBean(boolean isUpload, String imageUrl) {
        this.isUpload = isUpload;
        this.imageUrl = imageUrl;
    }
}
