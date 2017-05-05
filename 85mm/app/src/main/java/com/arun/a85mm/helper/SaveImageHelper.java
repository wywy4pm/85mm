package com.arun.a85mm.helper;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

import com.arun.a85mm.common.Constant;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.handler.ShowTopHandler;
import com.arun.a85mm.utils.FileUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.File;

/**
 * Created by wy on 2017/5/5.
 */

public class SaveImageHelper {

    public SaveImageCallBack saveImageCallBack;

    public interface SaveImageCallBack {
        void setSaveImage(boolean isSaveImage);
    }

    public void setSaveImageCallBack(SaveImageCallBack saveImageCallBack) {
        this.saveImageCallBack = saveImageCallBack;
    }

    public void saveImageShowTop(final Context context, final String imageUrl, final int width, final int height, final ShowTopHandler showTopHandler) {
        if (saveImageCallBack != null) {
            saveImageCallBack.setSaveImage(true);
        }
        //异步
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        FutureTarget<File> future = Glide.with(context)
                                .load(imageUrl)
                                .downloadOnly(width, height);
                        File cacheFile = null;
                        try {
                            cacheFile = future.get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String fileName = FileUtils.getFileName(imageUrl);
                        if (TextUtils.isEmpty(fileName)) {//没取到文件名，默认使用当前时间戳作为保存的文件名
                            fileName = String.valueOf(System.currentTimeMillis());
                        }
                        boolean writtenToDisk = FileUtils.writeFileToDisk(context, cacheFile, fileName);
                        if (writtenToDisk) {
                            Message message = new Message();
                            message.what = Constant.WHAT_SHOW_TOP_SUCCESS;
                            message.obj = fileName;
                            if (showTopHandler != null) {
                                showTopHandler.sendMessage(message);
                            }
                        } else {
                            if (showTopHandler != null) {
                                showTopHandler.sendEmptyMessage(Constant.WHAT_SHOW_TOP_FAILED);
                            }
                            if (saveImageCallBack != null) {
                                saveImageCallBack.setSaveImage(false);
                            }
                        }
                    }
                }
        ).start();
    }
}
