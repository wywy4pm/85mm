package com.arun.a85mm.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.arun.a85mm.R;
import com.arun.a85mm.helper.ShowTopToastHelper;

import java.util.List;

/**
 * Created by WY on 2017/5/19.
 */
public class OtherAppStartUtils {

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void jumpToWeChat(Context context) {
        if (isWeixinAvilible(context)) {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } else {
            ShowTopToastHelper.showTopToastView(context, "您暂未安装或不支持微信", 0);
            //Toast.makeText(context, "您暂未安装或不支持微信", Toast.LENGTH_SHORT).show();
        }
    }
}
