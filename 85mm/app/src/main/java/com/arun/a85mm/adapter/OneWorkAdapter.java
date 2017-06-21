package com.arun.a85mm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.arun.a85mm.R;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * Created by wy on 2017/6/21.
 */

public class OneWorkAdapter extends BaseRecyclerAdapter<WorkListItemBean> {
    private static int screenWidth;
    private WorkListBean workListBean;
    private OnImageClick onImageClick;
    public static String DATA_TYPE_HEAD = "head";
    public static String DATA_TYPE_IMAGE = "image";
    private static int VIEW_TYPE_HEAD = 0;
    private static int VIEW_TYPE_IMAGE = 1;

    public OneWorkAdapter(Context context, List<WorkListItemBean> list) {
        super(context, list);
        screenWidth = DensityUtil.getScreenWidth(context);
    }

    public void setWorkListBean(WorkListBean workListBean) {
        this.workListBean = workListBean;
    }

    public void setOnImageClick(OnImageClick onImageClick) {
        this.onImageClick = onImageClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEAD) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_one_work_author, parent, false);
            return new OneWorkAuthorHolder(itemView);
        } else if (viewType == VIEW_TYPE_IMAGE) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_list_item, parent, false);
            return new WorkListItemHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WorkListItemHolder) {
            WorkListItemHolder workListItemHolder = (WorkListItemHolder) holder;
            workListItemHolder.setData(contexts.get(), getItem(position), workListBean, onImageClick);
        } else if (holder instanceof OneWorkAuthorHolder) {
            OneWorkAuthorHolder oneWorkAuthorHolder = (OneWorkAuthorHolder) holder;
            oneWorkAuthorHolder.setData(contexts.get(), getItem(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = -1;
        if (DATA_TYPE_HEAD.equals(list.get(position).type)) {
            type = VIEW_TYPE_HEAD;
        } else if (DATA_TYPE_IMAGE.equals(list.get(position).type)) {
            type = VIEW_TYPE_IMAGE;
        }
        return type;
    }

    private static class OneWorkAuthorHolder extends RecyclerView.ViewHolder {
        private ImageView author_image;
        private TextView author_name;
        private TextView author_create_time;

        private OneWorkAuthorHolder(View itemView) {
            super(itemView);
            author_image = (ImageView) itemView.findViewById(R.id.author_image);
            author_name = (TextView) itemView.findViewById(R.id.author_name);
            author_create_time = (TextView) itemView.findViewById(R.id.author_create_time);
        }

        private void setData(Context context, WorkListItemBean bean) {
            author_name.setText(bean.authorName);
            Glide.with(context).load(bean.authorHeadImg).centerCrop().bitmapTransform(new GlideCircleTransform(context)).into(author_image);
            author_create_time.setText(bean.createTime);
        }
    }

    private static class WorkListItemHolder extends RecyclerView.ViewHolder {
        private ImageView work_list_item_img;
        private RippleView rippleView;
        private View bg_line;

        private WorkListItemHolder(View rootView) {
            super(rootView);
            rippleView = (RippleView) rootView.findViewById(R.id.rippleView);
            work_list_item_img = (ImageView) rootView.findViewById(R.id.work_list_item_img);
            bg_line = rootView.findViewById(R.id.bg_line);
            bg_line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        private void setData(final Context context, final WorkListItemBean bean, final WorkListBean workListBean, final OnImageClick onImageClick) {

            int imageHeight = 0;
            if (TextUtils.isEmpty(bean.imageUrl)) {
                work_list_item_img.setVisibility(View.GONE);
            } else {
                if (bean.width > 0) {
                    work_list_item_img.setVisibility(View.VISIBLE);
                    if (bean.height < bean.width) {
                        imageHeight = screenWidth;
                    } else {
                        imageHeight = (bean.height * screenWidth) / bean.width;
                    }
                    if (imageHeight > 8192) {
                        imageHeight = 8192;
                    }
                    if (work_list_item_img.getLayoutParams() != null) {
                        work_list_item_img.getLayoutParams().height = imageHeight;
                    }
                    Glide.with(context).load(bean.imageUrl).centerCrop()
                            .placeholder(bean.backgroundColor).error(bean.backgroundColor)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            bean.isLoad = true;
                            return false;
                        }
                    }).override(screenWidth, imageHeight).into(work_list_item_img);
                }
            }

            if (bean.backgroundColor > 0) {
                rippleView.setRippleColor(bean.backgroundColor);
            }
            final int finalSaveImageHeight = imageHeight;
            rippleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.isLoad) {
                        rippleView.setRippleDuration(300);
                        if (onImageClick != null) {
                            onImageClick.onCoverClick(workListBean.workId, bean.imageUrl, screenWidth, finalSaveImageHeight);
                        }
                    } else {
                        rippleView.setRippleDuration(0);
                        Glide.with(context).load(bean.imageUrl).centerCrop()
                                .placeholder(bean.backgroundColor).error(bean.backgroundColor)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                bean.isLoad = true;
                                return false;
                            }
                        }).into(work_list_item_img);
                    }
                }
            });
            work_list_item_img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    rippleView.setRippleDuration(0);
                    if (onImageClick != null) {
                        onImageClick.onMoreLinkClick(workListBean.workId, workListBean.sourceUrl);
                    }
                    return false;
                }
            });
        }
    }
}
