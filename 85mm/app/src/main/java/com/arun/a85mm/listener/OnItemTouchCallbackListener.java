package com.arun.a85mm.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by wy on 2017/7/18.
 */

public interface OnItemTouchCallbackListener {
    boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target);
}
