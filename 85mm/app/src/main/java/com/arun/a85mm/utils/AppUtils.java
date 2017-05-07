package com.arun.a85mm.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

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
}
