package com.arun.a85mm.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import okhttp3.ResponseBody;

/**
 * Created by wy on 2017/4/19.
 */

public class FileUtils {
    public static final String DIR_IMAGE_SAVE = Environment.getExternalStorageDirectory() + File.separator + "85mm";
    //public static final String DIR_IMAGE_SAVE_FLYME = Environment.getExternalStorageDirectory() + File.separator + "DCIM";

    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 下载图片到本地目录
     *
     * @param context
     * @param body
     * @param fileName
     * @return
     */
    public static boolean writeResponseBodyToDisk(final Context context, ResponseBody body, String fileName) {
        if (hasSdcard()) {
            try {
                final File file = createDirAndFile(DIR_IMAGE_SAVE, fileName);

                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    byte[] fileReader = new byte[4096];
                    long fileSizeDownloaded = 0;
                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(file);
                    while (true) {
                        int read = inputStream.read(fileReader);
                        if (read == -1) {
                            break;
                        }
                        outputStream.write(fileReader, 0, read);
                        fileSizeDownloaded += read;
                    }
                    outputStream.flush();
                    //notifySystemImageUpdate(context, file, file2);
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

    public static boolean writeFileToDisk(final Context context, File file, String fileName) {
        if (hasSdcard()) {
            try {
                final File newFile = createDirAndFile(DIR_IMAGE_SAVE, fileName);

                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    byte[] fileReader = new byte[4096];
                    long fileSizeDownloaded = 0;
                    inputStream = new FileInputStream(file);
                    outputStream = new FileOutputStream(newFile);
                    while (true) {
                        int read = inputStream.read(fileReader);
                        if (read == -1) {
                            break;
                        }
                        outputStream.write(fileReader, 0, read);
                        fileSizeDownloaded += read;
                    }
                    outputStream.flush();
                    //notifySystemImageUpdate(context, file, file2);
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

    private static File createDirAndFile(String path, String fileName) {
        File dir = new File(path);
        File file = new File(path + File.separator + fileName);
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String getFileName(String imageUrl) {
        String fileName = "";
        URL url = null;
        try {
            url = new URL(imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final URL finalUrl = url;
        if (finalUrl != null && !TextUtils.isEmpty(finalUrl.getFile())) {
            File file = new File(finalUrl.getFile());
            fileName = file.getName();
        }
        return fileName;
    }

    public static String getFilePath(String imageUrl) {
        String filePath = "";
        URL url = null;
        try {
            url = new URL(imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final URL finalUrl = url;
        if (finalUrl != null && !TextUtils.isEmpty(finalUrl.getFile())) {
            File file = new File(finalUrl.getFile());
            filePath = file.getAbsolutePath();
        }
        return filePath;
    }

    public static void notifySystemImageUpdate(final Context context, File... file) {
        if (file.length > 0) {
            for (int i = 0; i < file.length; i++) {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file[i].getAbsolutePath())));//通知图库刷新
            }
        }
    }
}
