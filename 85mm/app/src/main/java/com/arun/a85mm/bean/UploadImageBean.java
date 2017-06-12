package com.arun.a85mm.bean;

import android.content.Context;
import android.net.Uri;

import com.arun.a85mm.MMApplication;
import com.arun.a85mm.utils.FileUtils;

/**
 * Created by wy on 2017/6/8.
 */

public class UploadImageBean {
    public boolean isUpload;
    public Uri imageUri;
    public String imageUrl;
    public String key;

    public UploadImageBean(boolean isUpload, Uri imageUri) {
        this.isUpload = isUpload;
        this.imageUri = imageUri;
    }

    public UploadImageBean(boolean isUpload, Uri imageUri, String key) {
        this.isUpload = isUpload;
        this.imageUri = imageUri;
        this.key = key;
    }

}
