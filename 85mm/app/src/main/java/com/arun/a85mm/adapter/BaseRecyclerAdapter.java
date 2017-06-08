package com.arun.a85mm.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by wy on 2017/6/8.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    public List<T> list;

    public BaseRecyclerAdapter(List<T> list) {
        this.list = list;
    }

    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
