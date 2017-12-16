package com.arun.a85mm.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.andexert.library.RippleView;
import com.arun.a85mm.R;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.helper.ConfigHelper;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.utils.DensityUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by Administrator on 2017/11/30.
 */

public class WorkListItemHolder extends RecyclerView.ViewHolder {

    private static int screenWidth;
    private ImageView work_list_item_img;
    private RippleView rippleView;
    private View bg_line;

    public WorkListItemHolder(Context context, View rootView) {
        super(rootView);
        screenWidth = DensityUtil.getScreenWidth(context);
        rippleView = (RippleView) rootView.findViewById(R.id.rippleView);
        work_list_item_img = (ImageView) rootView.findViewById(R.id.work_list_item_img);
        bg_line = rootView.findViewById(R.id.bg_line);
        bg_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public void setData(final Context context, final WorkListItemBean bean, final WorkListBean workListBean, final OnImageClick onImageClick) {

        int imageHeight = 0;
        if (TextUtils.isEmpty(bean.url)) {
            work_list_item_img.setVisibility(View.GONE);
        } else {
            if (bean.width > 0) {
                work_list_item_img.setVisibility(View.VISIBLE);
                if (bean.height < bean.width) {
                    if (ConfigHelper.isShowWImage == Constant.VALUE_SHOW_WIMAGE) {
                        imageHeight = screenWidth;
                    } else {
                        imageHeight = (bean.height * screenWidth) / bean.width;
                    }
                } else {
                    imageHeight = (bean.height * screenWidth) / bean.width;
                }
                if (imageHeight > 8192) {
                    imageHeight = 8192;
                }
                if (work_list_item_img.getLayoutParams() != null) {
                    work_list_item_img.getLayoutParams().height = imageHeight;
                }
                Glide.with(context).load(bean.url).centerCrop()
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
        //final int finalSaveImageHeight = imageHeight;
        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.isLoad) {
                    rippleView.setRippleDuration(300);
                    String workId = "";
                    String authorName = "";
                    if (workListBean != null) {
                        workId = workListBean.id;
                        authorName = workListBean.authorName;
                    }
                    if (onImageClick != null) {
                        onImageClick.onCoverClick(workId, bean.url, authorName);
                    }
                } else {
                    rippleView.setRippleDuration(0);
                    Glide.with(context).load(bean.url).centerCrop()
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
        if (workListBean != null) {
            work_list_item_img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    rippleView.setRippleDuration(0);
                    if (onImageClick != null) {
                        onImageClick.onMoreLinkClick(workListBean.id, workListBean.sourceUrl, workListBean.uid);
                    }
                    return false;
                }
            });
        }
    }
}