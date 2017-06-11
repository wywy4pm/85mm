package com.arun.a85mm.helper;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.arun.a85mm.MMApplication;
import com.arun.a85mm.listener.UploadImageListener;

/**
 * Created by wy on 2017/6/10.
 */

public class OssUploadImageHelper {

    public static void uploadImage(final String uploadFilePath, final UploadImageListener uploadImageListener) {
        String uid = "";
        if (AppHelper.getInstance().getAppConfig() != null) {
            uid = AppHelper.getInstance().getAppConfig().uid;
        }

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(MMApplication.OSS_BUCKET_NAME,
                MMApplication.OSS_UPLOAD_IMAGE_FOLDER + uid + System.currentTimeMillis() + ".jpg", uploadFilePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = MMApplication.oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                /*Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());*/

                String imageUrl
                        = MMApplication.OSS_BUCKET_ENDPOINT
                        + MMApplication.OSS_UPLOAD_IMAGE_FOLDER
                        + uploadFilePath;

                if (uploadImageListener != null) {
                    uploadImageListener.uploadSuccess(imageUrl);
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }
}
