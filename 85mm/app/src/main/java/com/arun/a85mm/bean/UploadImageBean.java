package com.arun.a85mm.bean;

import android.net.Uri;

/**
 * Created by wy on 2017/6/8.
 */

public class UploadImageBean {
    public boolean isUpload;
    public Uri imageUri;

    public UploadImageBean(boolean isUpload, Uri imageUri) {
        this.isUpload = isUpload;
        this.imageUri = imageUri;
    }
}
