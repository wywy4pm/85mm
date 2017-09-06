package com.arun.a85mm.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Message;
import android.text.TextUtils;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.ShowTopBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.handler.ShowTopHandler;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.FileUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

/**
 * Created by wy on 2017/5/5.
 */

public class SaveImageHelper {

    /*public void saveImageShowTop(final Context context, final String imageUrl, final int width, final int height,
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
                            showData = "图片保存失败，请开启sd卡存储权限";
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
    }*/

    public void saveImage(final Context context, final String imageUrl,
                          final ShowTopHandler showTopHandler, final boolean isShowingTop,
                          final String authorName) {
        final String uid = SharedPreferencesUtils.getUid(context);

        Glide.with(context).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Bitmap newBitmap = null;
                if (!TextUtils.isEmpty(uid) && uid.equals("4")) {
                    newBitmap = resource;
                } else {
                    String waterMask = "";
                    if (TextUtils.isEmpty(authorName)) {
                        waterMask = "App: 85mm";
                    } else {
                        waterMask = "Photo by: " + authorName + ", App: 85mm";
                    }
                    newBitmap = createWaterMaskBitmap(context, resource, waterMask);
                }
                saveToDisk(imageUrl, showTopHandler, isShowingTop, newBitmap);
            }
        });
    }

    private void saveToDisk(String imageUrl, ShowTopHandler showTopHandler, boolean isShowingTop, Bitmap bitmap) {
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
        boolean writtenToDisk = FileUtils.wirteBitmapToDisk(bitmap, fileName);
        String showData = "";
        if (writtenToDisk) {
            showData = "图片已保存至" + FileUtils.DIR_IMAGE_SAVE + File.separator + fileName;
        } else {
            showData = "图片保存失败，请开启sd卡存储权限";
        }
        Message message = new Message();
        message.what = Constant.WHAT_SHOW_TOP;
        message.obj = new ShowTopBean(isShowingTop, showData);
        if (showTopHandler != null) {
            showTopHandler.sendMessage(message);
        }
    }

    private static Bitmap createWaterMaskBitmap(Context context, Bitmap src, String text) {
        Bitmap newBitmap = null;
        if (src != null) {
            int width = src.getWidth();
            int height = src.getHeight();
            newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            //在画布 0，0坐标上开始绘制原始图片
            canvas.drawBitmap(src, 0, 0, null);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.WHITE);
            paint.setTextSize(DensityUtil.dp2px(context, 6));
            paint.setTypeface(Typeface.DEFAULT);
            Rect rect = new Rect();
            paint.getTextBounds(text, 0, text.length(), rect);
            int tw = rect.right - rect.left;
            int ty = rect.bottom - rect.top;
            canvas.drawText(text, width - tw - DensityUtil.dp2px(context, 5), height - ty, paint);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        }
        return newBitmap;
    }
}
