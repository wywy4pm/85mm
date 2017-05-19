package com.arun.a85mm.helper;

import android.content.Context;

import com.arun.a85mm.activity.BaseActivity;
import com.arun.a85mm.activity.MainActivity;
import com.arun.a85mm.bean.ShowTopBean;

/**
 * Created by WY on 2017/5/19.
 */
public class ShowTopToastHelper {

    public static void showTopToastView(Context context, String showData) {
        if (context instanceof MainActivity) {
            ShowTopBean bean = new ShowTopBean(((MainActivity) context).getShowingTop(), showData);
            ((MainActivity) context).showTopToastView(bean);
        } else if (context instanceof BaseActivity) {
            ShowTopBean bean = new ShowTopBean(((BaseActivity) context).getShowingTop(), showData);
            ((BaseActivity) context).showTopToastView(bean);
        }
    }
}
