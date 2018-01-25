package com.arun.a85mm.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.util.List;

/**
 * Created by WY on 2017/5/7.
 */
public class AppUtils {

    /**
     * 获取当前应用程序的包名
     *
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getAppPackageName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }

    /**
     * 获取单个App图标
     **/
    public static Drawable getAppIcon(Context context) throws PackageManager.NameNotFoundException {
        PackageManager pManager = context.getPackageManager();
        Drawable icon = pManager.getApplicationIcon(getAppPackageName(context));
        return icon;
    }

    /**
     * 获取单个App名称
     **/
    public static String getAppName(Context context) throws PackageManager.NameNotFoundException {
        PackageManager pManager = context.getPackageManager();
        ApplicationInfo appInfo = pManager.getApplicationInfo(getAppPackageName(context), 0);
        String appName = pManager.getApplicationLabel(appInfo).toString();
        return appName;
    }

    /**
     * 获取单个App版本号
     **/
    public static String getAppVersion(Context context) {
        try {
            PackageManager pManager = context.getPackageManager();
            PackageInfo packageInfo = pManager.getPackageInfo(getAppPackageName(context), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取单个App的所有权限
     **/
    public static String[] getAppPermission(Context context) throws PackageManager.NameNotFoundException {
        PackageManager pManager = context.getPackageManager();
        PackageInfo packageInfo = pManager.getPackageInfo(getAppPackageName(context), PackageManager.GET_PERMISSIONS);
        String[] permission = packageInfo.requestedPermissions;
        return permission;
    }

    /**
     * 获取单个App的签名
     **/
    public static String getAppSignature(Context context) throws PackageManager.NameNotFoundException {
        PackageManager pManager = context.getPackageManager();
        PackageInfo packageInfo = pManager.getPackageInfo(getAppPackageName(context), PackageManager.GET_SIGNATURES);
        String allSignature = packageInfo.signatures[0].toCharsString();
        return allSignature;
    }

    /**
     * 方法描述：判断某一应用是否正在运行
     * Created by cafeting on 2017/2/4.
     *
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为debug模式
     *
     * @param context
     * @return
     */
    public static boolean isApkDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void restartApp(Context context) {
        Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    /**
     * @param file
     * @return
     * @Description 安装apk
     */
    public static void installApk(Context context, File file) {
        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            Uri apkUri = FileProvider.getUriForFile(context, "com.shawpoo.app.fileprovider", file); //与manifest中定义的provider中的authorities="com.shawpoo.app.fileprovider"保持一致
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);*/

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static boolean isServiceRunning(Context context,String className){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(500);
        if (serviceList.size() < 1){
            return false;
        }
        Log.e("service", "isServiceRunning: "+serviceList.size());
        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName componentName = serviceInfo.service;
            if (componentName.getClassName().equals(className)) {
                Log.e("name",componentName.getClassName());
                return true;
            }
        }
        return false;
    }

}
