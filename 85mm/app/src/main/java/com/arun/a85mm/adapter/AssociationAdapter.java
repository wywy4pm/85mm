package com.arun.a85mm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.AssociationBean;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by wy on 2017/6/24.
 */

public class AssociationAdapter extends BaseRecyclerAdapter<AssociationBean> {
    private int screenWidth;

    public AssociationAdapter(Context context, List<AssociationBean> list) {
        super(context, list);
        screenWidth = DensityUtil.getScreenWidth(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_association_item, parent, false);
        return new AssociationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AssociationHolder) {
            AssociationHolder associationHolder = (AssociationHolder) holder;
            associationHolder.setData(contexts.get(), getItem(position), screenWidth);
        }
    }

    private static class AssociationHolder extends RecyclerView.ViewHolder {
        public ImageView author_image;
        public TextView author_name;
        public TextView author_create_time;
        public RelativeLayout work_list_item_author;
        public ImageView cover_Image;
        public TextView community_title;
        public TextView community_detail;
        public RelativeLayout comment_head;
        public LinearLayout layout_comment;
        public RelativeLayout layout_list_comment;

        private AssociationHolder(View itemView) {
            super(itemView);
            this.author_image = (ImageView) itemView.findViewById(R.id.author_image);
            this.author_name = (TextView) itemView.findViewById(R.id.author_name);
            this.author_create_time = (TextView) itemView.findViewById(R.id.author_create_time);
            this.work_list_item_author = (RelativeLayout) itemView.findViewById(R.id.work_list_item_author);
            this.cover_Image = (ImageView) itemView.findViewById(R.id.cover_Image);
            this.community_title = (TextView) itemView.findViewById(R.id.community_title);
            this.community_detail = (TextView) itemView.findViewById(R.id.community_detail);
            this.comment_head = (RelativeLayout) itemView.findViewById(R.id.comment_head);
            this.layout_comment = (LinearLayout) itemView.findViewById(R.id.layout_comment);
            this.layout_list_comment = (RelativeLayout) itemView.findViewById(R.id.layout_list_comment);
        }

        private void setData(Context context, AssociationBean bean, int screenWidth) {
            Glide.with(context).load(bean.authorHeadImg).centerCrop()
                    .bitmapTransform(new GlideCircleTransform(context)).into(author_image);
            author_name.setText(bean.authorName);
            author_create_time.setText(bean.createTime);

            int imageHeight = 0;
            if (bean.coverHeight < bean.coverWidth) {
                imageHeight = screenWidth;
            } else {
                imageHeight = (bean.coverHeight * screenWidth) / bean.coverWidth;
            }
            if (imageHeight > 8192) {
                imageHeight = 8192;
            }
            cover_Image.getLayoutParams().height = imageHeight;
            Glide.with(context).load(bean.coverUrl).override(screenWidth, imageHeight).centerCrop().into(cover_Image);

            community_title.setText(bean.workTitle);
            community_detail.setText(bean.description);
            for (int i = 0; i < 3; i++) {
                View commentView = LayoutInflater.from(context).inflate(R.layout.list_commnet_item, layout_comment, false);
                layout_comment.addView(commentView);
            }
            /*if (bean.comments != null && bean.comments.size() > 0) {
                layout_list_comment.setVisibility(View.VISIBLE);
                layout_comment.removeAllViews();
                for (int i = 0; i < bean.comments.size(); i++) {
                    View commentView = LayoutInflater.from(context).inflate(R.layout.list_commnet_item, layout_comment, false);
                    layout_comment.addView(commentView);
                }
            } else {
                layout_list_comment.setVisibility(View.GONE);
            }*/
        }
    }
}
