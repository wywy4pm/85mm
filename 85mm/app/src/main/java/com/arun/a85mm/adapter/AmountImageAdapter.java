package com.arun.a85mm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.viewholder.WorkListItemHolder;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2017/11/30.
 */

public class AmountImageAdapter extends BaseRecyclerAdapter<WorkListItemBean> {

    private OnImageClick onImageClick;

    public AmountImageAdapter(Context context, List<WorkListItemBean> list) {
        super(context, list);
    }

    public void setOnImageClick(OnImageClick onImageClick) {
        this.onImageClick = onImageClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_list_item, parent, false);
        return new WorkListItemHolder(contexts.get(), itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WorkListItemHolder) {
            WorkListItemHolder workListItemHolder = (WorkListItemHolder) holder;
            workListItemHolder.setData(contexts.get(), getItem(position), null, onImageClick);
        }
    }
}
