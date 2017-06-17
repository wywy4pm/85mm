package com.arun.a85mm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.utils.DensityUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by wy on 2017/6/16.
 */

public class AuditListAdapter extends BaseRecyclerAdapter<WorkListBean> {

    public AuditListAdapter(Context context, List<WorkListBean> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_audit_work_item, parent, false);
        return new AuditHolder(contexts.get(), itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AuditHolder) {
            AuditHolder auditHolder = (AuditHolder) holder;
            auditHolder.setData(contexts.get(), getItem(position));
        }
    }

    private static class AuditHolder extends RecyclerView.ViewHolder {
        private ImageView work_image;
        private TextView work_count;
        private int widthHeight;

        private AuditHolder(Context context, View itemView) {
            super(itemView);
            work_image = (ImageView) itemView.findViewById(R.id.work_image);
            work_count = (TextView) itemView.findViewById(R.id.work_count);
            widthHeight = (DensityUtil.getScreenWidth(context) - DensityUtil.dp2px(context, 10)) / 2;
        }

        private void setData(Context context, WorkListBean bean) {

            work_image.getLayoutParams().height = widthHeight;
            work_image.getLayoutParams().width = widthHeight;
            Glide.with(context)
                    .load(bean.coverUrl)
                    .override(widthHeight, widthHeight)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .into(work_image);
            work_count.setText(bean.totalImageNum + "");
        }
    }
}