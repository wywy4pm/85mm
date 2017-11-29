package com.arun.a85mm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.utils.OtherAppStartUtils;
import com.arun.a85mm.utils.SystemServiceUtils;

/**
 * Created by Administrator on 2017/11/29.
 */

public class RewardDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView text_tips;
    private TextView openWechat;
    private int type;
    private int leftCoins;
    public static final int TYPE_NO_COINS = 0;
    public static final int TYPE_NO_ENOUGH_COINS = 1;

    public RewardDialog(Context context) {
        super(context);
        this.context = context;
    }

    public RewardDialog(Context context, int themeResId, int type, int leftCoins) {
        super(context, themeResId);
        this.context = context;
        this.type = type;
        this.leftCoins = leftCoins;
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
        setContentView(R.layout.layout_reward_tip_dialog);
        initView();
    }

    private void initView() {
        text_tips = (TextView) findViewById(R.id.text_tips);
        openWechat = (TextView) findViewById(R.id.openWechat);
        findViewById(R.id.close).setOnClickListener(this);
        openWechat.setOnClickListener(this);

        if (type == TYPE_NO_COINS) {
            text_tips.setText(context.getResources().getString(R.string.no_coins));
        } else if (type == TYPE_NO_ENOUGH_COINS) {
            text_tips.setText(context.getResources().getString(R.string.no_enough_coins, leftCoins));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                dismiss();
                break;
            case R.id.openWechat:
                SystemServiceUtils.copyText(context, context.getString(R.string.wechat_num));
                OtherAppStartUtils.jumpToWeChat(context);
                dismiss();
                break;
        }
    }
}
