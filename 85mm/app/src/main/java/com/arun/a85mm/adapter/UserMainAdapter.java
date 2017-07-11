package com.arun.a85mm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.OneWorkActivity;
import com.arun.a85mm.bean.AuditItemBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.helper.UrlJumpHelper;
import com.arun.a85mm.utils.DensityUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2017/7/11.
 */

public class UserMainAdapter extends BaseRecyclerAdapter<WorkListBean> {
    public UserMainAdapter(Context context, List<WorkListBean> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_audit_work_item, parent, false);
        return new UserMainHolder(contexts.get(), itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserMainHolder) {
            UserMainHolder userMainHolder = (UserMainHolder) holder;
            userMainHolder.setData(contexts.get(), getItem(position));
        }
    }

    private static class UserMainHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageView work_image;
        private TextView work_count;
        private int widthHeight;

        private UserMainHolder(Context context, View itemView) {
            super(itemView);
            this.itemView = itemView;
            work_image = (ImageView) itemView.findViewById(R.id.work_image);
            work_count = (TextView) itemView.findViewById(R.id.work_count);
            widthHeight = (DensityUtil.getScreenWidth(context)) / 2;
        }

        private void setData(final Context context, final WorkListBean bean) {

            work_image.getLayoutParams().height = widthHeight;
            work_image.getLayoutParams().width = widthHeight;
            Glide.with(context)
                    .load(bean.coverUrl)
                    .placeholder(bean.backgroundColor)
                    .error(bean.backgroundColor)
                    .override(widthHeight, widthHeight)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .into(work_image);

            work_count.setText(bean.totalImageNum + "");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> map = new HashMap<>();
                    map.put(UrlJumpHelper.WORK_ID, bean.workId);
                    map.put(OneWorkActivity.KEY_TYPE, Constant.TYPE_COMMUNITY);

                    OneWorkActivity.jumpToOneWorkActivity(context, OneWorkActivity.FRAGMENT_ONE_WORK, bean.workTitle, map, OneWorkActivity.BACK_MODE_COM);
                }
            });
        }
    }
}
