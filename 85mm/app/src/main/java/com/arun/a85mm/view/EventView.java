package com.arun.a85mm.view;

import com.arun.a85mm.bean.ActionBean;

import java.util.List;

/**
 * Created by WY on 2017/5/11.
 */
public interface EventView extends MvpView {
    void eventSuccess(int hasNewMsg);

    void eventDone(int type);

    void eventDoneExtra(int type, List<ActionBean> actionList);
}
