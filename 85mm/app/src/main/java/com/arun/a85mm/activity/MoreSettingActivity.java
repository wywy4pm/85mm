package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.arun.a85mm.R;
import com.arun.a85mm.common.Constant;

public class MoreSettingActivity extends BaseActivity {

    public static void jumpToMoreSettingActivity(Context context) {
        Intent intent = new Intent(context, MoreSettingActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_setting);
    }
}
