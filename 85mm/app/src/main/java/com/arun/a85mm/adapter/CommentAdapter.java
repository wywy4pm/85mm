package com.arun.a85mm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.CommentsBean;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by wy on 2017/6/26.
 */

public class CommentAdapter extends BaseRecyclerAdapter<CommentsBean> {

    public CommentAdapter(Context context, List<CommentsBean> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.detail_comment_item, parent, false);
        return new CommentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentHolder) {
            CommentHolder commentHolder = (CommentHolder) holder;
            commentHolder.setData(contexts.get(), getItem(position));
        }
    }

    private static class CommentHolder extends RecyclerView.ViewHolder {
        private ImageView comment_head;
        private TextView comment_author;
        public TextView create_time;
        private TextView comment_detail;

        private CommentHolder(View rootView) {
            super(rootView);
            this.comment_head = (ImageView) rootView.findViewById(R.id.comment_head);
            this.comment_author = (TextView) rootView.findViewById(R.id.comment_author);
            this.create_time = (TextView) rootView.findViewById(R.id.create_time);
            this.comment_detail = (TextView) rootView.findViewById(R.id.comment_detail);
        }

        private void setData(Context context, CommentsBean bean) {
            Glide.with(context)
                    .load(bean.headUrl)
                    .bitmapTransform(new GlideCircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .into(comment_head);

            comment_author.setText(bean.authorName);
            create_time.setText(bean.createTime);
            comment_detail.setText(bean.content);
        }
    }
}
