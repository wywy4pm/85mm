package com.arun.a85mm.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.arun.a85mm.R;

/**
 * Created by wy on 2017/5/17.
 */

public class ShareWindow {

    public static void show(final Activity context, final String title, final String description, final String shareUrl, final String shareImageUrl) {
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        final LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.layout_share_board, null);

        root.findViewById(R.id.layout_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareHelper.share(context, root.findViewById(R.id.layout_wechat), title, description, shareUrl, shareImageUrl);
                dialog.cancel();
            }
        });

        root.findViewById(R.id.layout_pengyouquan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareHelper.share(context, root.findViewById(R.id.layout_pengyouquan), title, description, shareUrl, shareImageUrl);
                dialog.cancel();
            }
        });

        root.findViewById(R.id.layout_sina).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareHelper.share(context, root.findViewById(R.id.layout_sina), title, description, shareUrl, shareImageUrl);
                dialog.cancel();
            }
        });

        root.findViewById(R.id.layout_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareHelper.share(context, root.findViewById(R.id.layout_qq), title, description, shareUrl, shareImageUrl);
                dialog.cancel();
            }
        });

        root.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.setContentView(root);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.x = 0; // 新位置X坐标
            lp.y = 0; // 新位置Y坐标
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogWindow.setAttributes(lp);
            dialog.show();
        }
    }
}
