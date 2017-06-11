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

    public UploadImageBean(boolean isUpload, Uri imageUri) {
        this.isUpload = isUpload;
        this.imageUri = imageUri;
    }

    public UploadImageBean(boolean isUpload, Uri imageUri, Context context) {
        this.isUpload = isUpload;
        this.imageUri = imageUri;
        if (imageUri != null) {
            imageUrl = MMApplication.OSS_BUCKET_ENDPOINT
                    + MMApplication.OSS_UPLOAD_IMAGE_FOLDER
                    + FileUtils.getRealFilePathByUri(context, imageUri);
        }
    }

}
