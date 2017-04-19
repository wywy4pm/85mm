package com.arun.a85mm.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * Created by wy on 2017/4/19.
 */

public class FileUtils {
    public static final String DIR_IMAGE_SAVE = Environment.getExternalStorageDirectory() + File.separator + "85mm";

    /**
     * 下载图片到本地目录
     *
     * @param context
     * @param body
     * @param file
     * @return
     */
    public static boolean writeResponseBodyToDisk(Context context, ResponseBody body, String file) {
        if (hasSdcard()) {
            try {
                File dir = new File(DIR_IMAGE_SAVE);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File futureStudioIconFile = new File(DIR_IMAGE_SAVE + File.separator + file);
                if (!futureStudioIconFile.exists()) {
                    futureStudioIconFile.createNewFile();
                }
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    byte[] fileReader = new byte[4096];
                    //long fileSize = body.contentLength();
                    long fileSizeDownloaded = 0;
                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(futureStudioIconFile);
                    while (true) {
                        int read = inputStream.read(fileReader);
                        if (read == -1) {
                            break;
                        }
                        outputStream.write(fileReader, 0, read);
                        fileSizeDownloaded += read;
                    }
                    outputStream.flush();
                    return true;
                } catch (IOException e) {
                    return false;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            } catch (IOException e) {
                return false;
            }
        } else {
            Toast.makeText(context, "请开启sd卡存储权限,方便保存图片", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
