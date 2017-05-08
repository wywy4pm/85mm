package com.arun.a85mm.helper;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

import com.arun.a85mm.bean.ShowTopBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.handler.ShowTopHandler;
import com.arun.a85mm.listener.EventListener;
import com.arun.a85mm.utils.FileUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.File;

/**
 * Created by wy on 2017/5/5.
 */

public class SaveImageHelper {

    public void saveImageShowTop(final Context context, final String imageUrl, final int width, final int height,
                                 final ShowTopHandler showTopHandler, final boolean isShowingTop) {
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
                            fileName = System.currentTimeMillis() + ".jpg";
                        } else {
                            if (!fileName.contains(".")) {
                                fileName += ".jpg";
                            } else if (fileName.contains(".") && fileName.contains("?")) {
                                String[] strings = fileName.split("\\?", 2);
                                fileName = strings[0];
                            }
                        }
                        boolean writtenToDisk = FileUtils.writeFileToDisk(context, cacheFile, fileName);
                        String showData = "";
                        if (writtenToDisk) {
                            showData = "图片已保存至" + FileUtils.DIR_IMAGE_SAVE + File.separator + fileName;
                        } else {
                            showData = "图片保存失败";
                        }
                        Message message = new Message();
                        message.what = Constant.WHAT_SHOW_TOP;
                        message.obj = new ShowTopBean(isShowingTop, showData);
                        if (showTopHandler != null) {
                            showTopHandler.sendMessage(message);
                        }
                    }
                }
        ).start();
    }
}
