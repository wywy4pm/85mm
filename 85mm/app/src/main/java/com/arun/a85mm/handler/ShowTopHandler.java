package com.arun.a85mm.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.arun.a85mm.activity.MainActivity;
import com.arun.a85mm.bean.ShowTopBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.utils.FileUtils;

import java.io.File;

/**
 * Created by wy on 2017/5/5.
 */

public class ShowTopHandler extends Handler {
    private Context context;

    public ShowTopHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == Constant.WHAT_SHOW_TOP) {
            ShowTopBean bean = (ShowTopBean) msg.obj;
            ((MainActivity) context).showTopToastView(bean);
        }
    }
}