package com.arun.a85mm.listener;

import com.arun.a85mm.bean.ActionBean;

import java.util.List;

/**
 * Created by wy on 2017/5/8.
 */

public interface EventListener {
    void onEvent(List<ActionBean> actionList);
}
