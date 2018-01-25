package com.arun.a85mm.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.arun.a85mm.R;
import com.arun.a85mm.helper.ProgressResponseBody;
import com.arun.a85mm.listener.DownLoadListener;
import com.arun.a85mm.retrofit.RetrofitApi;
import com.arun.a85mm.retrofit.RetrofitInit;
import com.arun.a85mm.utils.AppUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/1/24.
 */

public class DownLoadApkService extends Service {

    private Notification notification;
    private RemoteViews contentView;

    private static final String INTENT_KEY_URL = "url";

    public static void startDownLoadApk(Context context, String url) {
        Intent intent = new Intent(context, DownLoadApkService.class);
        intent.putExtra(INTENT_KEY_URL, url);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setTicker("85mm安装包正在后台下载...").build();
        contentView = new RemoteViews(getPackageName(), R.layout.layout_notification_update);
        notification.contentView = contentView;
        /*contentView.setProgressBar(R.id.progressbar_notification, 100, 0, false);
        startForeground(R.id.progressbar_notification, notification);*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() != null && intent.getExtras().containsKey(INTENT_KEY_URL)) {
            String url = intent.getExtras().getString(INTENT_KEY_URL);
            downLoadApk(url);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void downLoadApk(final String downLoadUrl) {
        DownLoadListener<ResponseBody> downLoadListener = new DownLoadListener<ResponseBody>() {
            @Override
            public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                stopForeground(true);
                File apkFile = saveFile(response, downLoadUrl);
                AppUtils.installApk(DownLoadApkService.this, apkFile);
            }

            @Override
            public void onLoading(long progress, long total) {
                int pro = (int) (((double) progress / total) * 100);
                contentView.setProgressBar(R.id.progressbar_notification, 100, pro, false);
                startForeground(R.id.progressbar_notification, notification);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        };
        RetrofitInit.getDownLoadApi(downLoadListener).commonDownLoad(downLoadUrl).enqueue(downLoadListener);
    }

    /**
     * 通过IO流写入文件
     */
    public File saveFile(Response<ResponseBody> response, String url) {
        InputStream in = null;
        FileOutputStream out = null;
        byte[] buf = new byte[2048];
        int len;
        File file = null;
        try {
            File dir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            if (!dir.exists()) {// 如果文件不存在新建一个
                dir.mkdirs();
            }
            int index = url.lastIndexOf("/");
            String name = url.substring(index + 1);
            in = response.body().byteStream();
            file = new File(dir, name);
            out = new FileOutputStream(file);
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
