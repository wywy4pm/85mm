package com.arun.a85mm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.arun.a85mm.R;
import com.arun.a85mm.utils.OtherAppStartUtils;
import com.arun.a85mm.utils.SystemServiceUtils;

/**
 * Created by Administrator on 2018/1/3.
 */

public class BrowserLimitDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView copyWechat;
    private TextView openWechat;

    public BrowserLimitDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public BrowserLimitDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        if (win != null) {
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            win.setAttributes(lp);
        }
        setContentView(R.layout.dialog_browser_limit);
        setCanceledOnTouchOutside(false);
        copyWechat = (TextView) findViewById(R.id.copyWechat);
        openWechat = (TextView) findViewById(R.id.openWechat);
        copyWechat.setOnClickListener(this);
        openWechat.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openWechat:
                OtherAppStartUtils.jumpToWeChat(context);
                break;
            case R.id.copyWechat:
                SystemServiceUtils.copyText(context, context.getString(R.string.wechat_num));
                /*if (context instanceof MainActivity) {
                    ((MainActivity) context).showTop("复制成功");
                } else if (context instanceof BaseActivity) {
                    ((BaseActivity) context).showTop("复制成功");
                }*/
                Toast.makeText(context, "复制成功", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
