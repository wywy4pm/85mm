package com.arun.a85mm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.ArticleDetailBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by WY on 2017/4/16.
 */
public class ArticleDetailAdapter extends RecyclerView.Adapter {
    private WeakReference<Context> contexts;
    private List<ArticleDetailBean> articleDetails;

    private static final int TYPE_FULL_IMAGE = 1;
    private static final int TYPE_BIG_TITLE = 2;
    private static final int TYPE_SMALL_TITLE = 3;
    private static final int TYPE_AUTHOR = 4;
    private static final int TYPE_PARAGRAPH = 5;

    public ArticleDetailAdapter(Context context, List<ArticleDetailBean> articleDetails) {
        contexts = new WeakReference<>(context);
        this.articleDetails = articleDetails;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FULL_IMAGE) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_single_iamge, parent, false);
            return new ImageHolder(contexts.get(), itemView);
        } else if (viewType == TYPE_BIG_TITLE) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_big_title, parent, false);
            return new BigTitleHolder(itemView);
        } else if (viewType == TYPE_SMALL_TITLE) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_small_title, parent, false);
            return new SmallTitleHolder(itemView);
        } else if (viewType == TYPE_AUTHOR) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_author, parent, false);
            return new AuthorHolder(itemView);
        } else if (viewType == TYPE_PARAGRAPH) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_paragraph, parent, false);
            return new ParagraphHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageHolder) {
            ImageHolder imageHolder = (ImageHolder) holder;
            imageHolder.setData(contexts.get(), articleDetails.get(position), position);
        } else if (holder instanceof BigTitleHolder) {
            BigTitleHolder bigTitleHolder = (BigTitleHolder) holder;
            bigTitleHolder.setData(articleDetails.get(position));
        } else if (holder instanceof AuthorHolder) {
            AuthorHolder authorHolder = (AuthorHolder) holder;
            authorHolder.setData(contexts.get(), articleDetails.get(position));
        } else if (holder instanceof SmallTitleHolder) {
            SmallTitleHolder smallTitleHolder = (SmallTitleHolder) holder;
            smallTitleHolder.setData(contexts.get(), articleDetails.get(position));
        } else if (holder instanceof ParagraphHolder) {
            ParagraphHolder paragraph = (ParagraphHolder) holder;
            paragraph.setData(contexts.get(), articleDetails.size(), articleDetails.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = -1;
        if (articleDetails.get(position) != null) {
            if (Constant.ARTICLE_TYPE_FULL_IMAGE.equals(articleDetails.get(position).componentType)) {
                type = TYPE_FULL_IMAGE;
            } else if (Constant.ARTICLE_TYPE_AUTHOR.equals(articleDetails.get(position).componentType)) {
                type = TYPE_AUTHOR;
            } else if (Constant.ARTICLE_TYPE_BIG_TITLE.equals(articleDetails.get(position).componentType)) {
                type = TYPE_BIG_TITLE;
            } else if (Constant.ARTICLE_TYPE_SMALL_TITLE.equals(articleDetails.get(position).componentType)) {
                type = TYPE_SMALL_TITLE;
            } else if (Constant.ARTICLE_TYPE_PARAGRAPH.equals(articleDetails.get(position).componentType)) {
                type = TYPE_PARAGRAPH;
            }
        }
        return type;
    }

    @Override
    public int getItemCount() {
        return articleDetails.size();
    }

    public static class ImageHolder extends RecyclerView.ViewHolder {
        public ImageView item_fullImage;

        public ImageHolder(Context context, View rootView) {
            super(rootView);
            item_fullImage = (ImageView) rootView.findViewById(R.id.item_fullImage);
        }

        private void setData(Context context, ArticleDetailBean bean, int position) {
            if (item_fullImage.getLayoutParams() != null) {
                int imageWidth = DensityUtil.getScreenWidth(context);
                int imageHeight = 0;
                if (position == 0) {
                    imageHeight = (int) (DensityUtil.getScreenWidth(context) * 0.56);
                } else {
                    imageHeight = (bean.imageHeight * DensityUtil.getScreenWidth(context)) / bean.imageWidth;
                }
                item_fullImage.getLayoutParams().height = imageHeight;
                item_fullImage.getLayoutParams().width = imageWidth;
                Glide.with(context)
                        .load(bean.imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .override(imageWidth, imageHeight)
                        .centerCrop()
                        .into(item_fullImage);
            }
        }
    }

    public static class SmallTitleHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public TextView item_small_title;

        public SmallTitleHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            item_small_title = (TextView) rootView.findViewById(R.id.item_small_title);
        }

        private void setData(Context context, ArticleDetailBean bean) {
            if (!TextUtils.isEmpty(bean.textColor)) {
                item_small_title.setTextColor(Color.parseColor(bean.textColor));
            } else {
                item_small_title.setTextColor(context.getResources().getColor(R.color.black));
            }
            if (item_small_title.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) item_small_title.getLayoutParams();
                if (Constant.ARTICLE_TITLE_ALIGN_LEFT.equals(bean.alignment)) {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                } else if (Constant.ARTICLE_TITLE_ALIGN_CENTER.equals(bean.alignment)) {
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                } else if (Constant.ARTICLE_TITLE_ALIGN_LEFT.equals(bean.alignment)) {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
            }

            item_small_title.setText(bean.text);
        }

    }

    public static class BigTitleHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public TextView item_big_title;

        public BigTitleHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            item_big_title = (TextView) rootView.findViewById(R.id.item_big_title);
        }

        private void setData(ArticleDetailBean bean) {
            //item_big_title.setTextColor(Color.parseColor(bean.textColor));
            item_big_title.setText(bean.text);
        }

    }

    public static class AuthorHolder extends RecyclerView.ViewHolder {
        public View rootView;
        private ImageView author_head_image;
        private TextView author_name;
        private TextView article_create_time;

        public AuthorHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            author_name = (TextView) rootView.findViewById(R.id.author_name);
            author_head_image = (ImageView) rootView.findViewById(R.id.author_head_image);
            article_create_time = (TextView) rootView.findViewById(R.id.article_create_time);
        }

        private void setData(Context context, ArticleDetailBean bean) {
            author_name.setText(bean.author);
            Glide.with(context).load(bean.authorHeadImg).transform(new GlideCircleTransform(context)).into(author_head_image);
            article_create_time.setText(bean.createTime);
        }

    }

    public static class ParagraphHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public TextView item_paragraph;

        public ParagraphHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            item_paragraph = (TextView) rootView.findViewById(R.id.item_paragraph);
        }

        private void setData(Context context, int size, ArticleDetailBean bean) {
            if (!TextUtils.isEmpty(bean.textColor)) {
                item_paragraph.setTextColor(Color.parseColor(bean.textColor));
            } else {
                item_paragraph.setTextColor(context.getResources().getColor(R.color.black));
            }
            item_paragraph.setText(bean.text);
           /* if (item_paragraph.getLayoutParams() != null && item_paragraph.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                if (getAdapterPosition() == size - 1) {
                    ((RelativeLayout.LayoutParams) item_paragraph.getLayoutParams()).setMargins(DensityUtil.dp2px(context, 18), DensityUtil.dp2px(context, 10), DensityUtil.dp2px(context, 18), DensityUtil.dp2px(context, 10));
                } else {
                    ((RelativeLayout.LayoutParams) item_paragraph.getLayoutParams()).setMargins(DensityUtil.dp2px(context, 18), DensityUtil.dp2px(context, 10), DensityUtil.dp2px(context, 18), 0);
                }
            }*/
        }

    }
}
