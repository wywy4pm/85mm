package com.arun.a85mm.listener;

/**
 * Created by wy on 2017/5/8.
 */

public interface EventListener {

    void onEvent(int actionType);

    void onEvent(int actionType, String resourceId);

    void onEvent(int actionType, String resourceId, String remark);
}
