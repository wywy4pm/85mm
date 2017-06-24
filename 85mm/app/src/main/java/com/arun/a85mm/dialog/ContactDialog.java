package com.arun.a85mm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.utils.OtherAppStartUtils;
import com.arun.a85mm.utils.SystemServiceUtils;

/**
 * Created by WY on 2017/6/4.
 */
public class ContactDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView text_copy;
    private TextView openWechat;

    public ContactDialog(Context context) {
        super(context);
        this.context = context;
    }

    public ContactDialog(Context context, int themeResId) {
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
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
        }
        setContentView(R.layout.layout_contact);
        initView();
    }

    private void initView() {
        findViewById(R.id.contact_close).setOnClickListener(this);
        text_copy = (TextView) findViewById(R.id.text_copy);
        openWechat = (TextView) findViewById(R.id.openWechat);
        text_copy.setOnClickListener(this);
        openWechat.setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
        text_copy.setText("点击复制");
    }

    public void copyWeChat() {
        SystemServiceUtils.copyText(context, context.getString(R.string.wechat_num));
        text_copy.setText("复制成功");
    }

    public void openWeChat() {
        OtherAppStartUtils.jumpToWeChat(context);
        cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_close:
                cancel();
                break;
            case R.id.openWechat:
                openWeChat();
                break;
            case R.id.text_copy:
                copyWeChat();
                break;
        }
    }
}
