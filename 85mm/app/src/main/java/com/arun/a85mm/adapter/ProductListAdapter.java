package com.arun.a85mm.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.arun.a85mm.R;
import com.arun.a85mm.activity.AuditActivity;
import com.arun.a85mm.activity.WebViewActivity;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.helper.ConfigHelper;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.listener.EventListener;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.arun.a85mm.utils.GlideRoundTransform;
import com.arun.a85mm.utils.NetUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by wy on 2017/4/18.
 */

public class ProductListAdapter extends BaseExpandableListAdapter {
    private WeakReference<Context> contexts;
    private List<WorkListBean> works;
    private int screenWidth;

    public ProductListAdapter(Context context, List<WorkListBean> works) {
        contexts = new WeakReference<>(context);
        this.works = works;
        screenWidth = DensityUtil.getScreenWidth(context);
    }

    public OnImageClick onImageClick;

    public void setOnImageClick(OnImageClick onImageClick) {
        this.onImageClick = onImageClick;
    }

    public EventListener eventListener;

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override

    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        WorkListHeadHolder workListHeadHolder = null;
        convertView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_list, parent, false);
        workListHeadHolder = new WorkListHeadHolder(convertView);
        /*if (convertView == null) {
            convertView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_list, parent, false);
            workListHeadHolder = new WorkListHeadHolder(convertView);
            convertView.setTag(workListHeadHolder);
        } else {
            workListHeadHolder = (WorkListHeadHolder) convertView.getTag();
        }*/
        if (ConfigHelper.tipsPosition > 0 && ConfigHelper.tipsPosition == groupPosition + 1) {
            workListHeadHolder.layout_auditing_tips.setVisibility(View.VISIBLE);
            workListHeadHolder.layout_auditing_tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuditActivity.jumpToAudit(contexts.get());
                }
            });
        } else {
            workListHeadHolder.layout_auditing_tips.setVisibility(View.GONE);
        }

        final WorkListHeadHolder headHolder = workListHeadHolder;
        final WorkListBean bean = works.get(groupPosition);
        //作品浏览
        if (eventListener != null) {
            eventListener.onEvent(EventStatisticsHelper.createOneActionList(EventConstant.WORK_BROWSE_NEWEST, bean.workId, ""));
        }

        int imageHeight = 0;
        if (bean.coverWidth > 0) {
            if (bean.coverHeight < bean.coverWidth) {
                imageHeight = screenWidth;
            } else {
                imageHeight = (bean.coverHeight * screenWidth) / bean.coverWidth;
            }
            if (imageHeight > 8192) {
                imageHeight = 8192;
            }
            if (bean.isCoverLoad) {
                if (headHolder.work_list_cover_img.getLayoutParams() != null && headHolder.itemView.getLayoutParams() != null) {
                    headHolder.work_list_cover_img.getLayoutParams().height = imageHeight;
                    headHolder.itemView.getLayoutParams().height = imageHeight;
                }
                Glide.with(contexts.get()).load(bean.coverUrl).centerCrop()
                        .placeholder(bean.backgroundColor).error(bean.backgroundColor)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .override(screenWidth, imageHeight).into(headHolder.work_list_cover_img);

                if (!bean.isExpand) {
                    headHolder.work_list_cover_count.setVisibility(View.VISIBLE);
                    headHolder.layout_source.setVisibility(View.VISIBLE);
                    headHolder.shadow.setVisibility(View.VISIBLE);

                    headHolder.work_list_cover_count.setText(String.valueOf(bean.totalImageNum));
                    Glide.with(contexts.get()).load(bean.sourceLogo).centerCrop().
                            transform(new GlideRoundTransform(contexts.get(), 5)).into(headHolder.source_logo);
                    headHolder.create_time.setText(bean.createTime);
                } else {
                    headHolder.work_list_cover_count.setVisibility(View.GONE);
                    headHolder.layout_source.setVisibility(View.GONE);
                    headHolder.shadow.setVisibility(View.GONE);
                }
            } else {
                if (headHolder.work_list_cover_img.getLayoutParams() != null && headHolder.itemView.getLayoutParams() != null) {
                    headHolder.work_list_cover_img.getLayoutParams().height = imageHeight;
                    headHolder.itemView.getLayoutParams().height = imageHeight;
                }
                final int finalImageHeight = imageHeight;
                Glide.with(contexts.get()).load(bean.coverUrl).centerCrop()
                        .placeholder(bean.backgroundColor).error(bean.backgroundColor)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (headHolder.work_list_cover_img.getLayoutParams() != null && headHolder.itemView.getLayoutParams() != null) {
                            headHolder.work_list_cover_img.getLayoutParams().height = finalImageHeight;
                            headHolder.itemView.getLayoutParams().height = finalImageHeight;
                        }

                        if (!bean.isExpand) {
                            headHolder.work_list_cover_count.setVisibility(View.VISIBLE);
                            headHolder.layout_source.setVisibility(View.VISIBLE);
                            headHolder.shadow.setVisibility(View.VISIBLE);

                            headHolder.work_list_cover_count.setText(String.valueOf(bean.totalImageNum));
                            Glide.with(contexts.get()).load(bean.sourceLogo).centerCrop().
                                    transform(new GlideRoundTransform(contexts.get(), 5)).into(headHolder.source_logo);
                            headHolder.create_time.setText(bean.createTime);
                        } else {
                            headHolder.work_list_cover_count.setVisibility(View.GONE);
                            headHolder.layout_source.setVisibility(View.GONE);
                            headHolder.shadow.setVisibility(View.GONE);
                        }
                        bean.isCoverLoad = true;
                        return false;
                    }
                }).override(screenWidth, imageHeight).into(headHolder.work_list_cover_img);
            }
        }

        if (!bean.isExpand) {
            workListHeadHolder.rippleView.setRippleDuration(0);
        } else {
            workListHeadHolder.rippleView.setRippleDuration(300);
        }
        final WorkListHeadHolder finalWorkListHeadHolder = workListHeadHolder;
        if (bean.backgroundColor > 0) {
            workListHeadHolder.rippleView.setRippleColor(bean.backgroundColor);
        }

        final int finalImageHeight = imageHeight;
        workListHeadHolder.rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.isExpand) {
                    finalWorkListHeadHolder.rippleView.setRippleDuration(300);
                    if (onImageClick != null) {
                        onImageClick.onCoverClick(bean.workId, bean.coverUrl, screenWidth, finalImageHeight);
                    }
                } else {
                    if (bean.isCoverLoad) {
                        headHolder.work_list_cover_count.setVisibility(View.GONE);
                        headHolder.layout_source.setVisibility(View.GONE);
                        headHolder.shadow.setVisibility(View.GONE);
                        bean.isExpand = true;
                        if (onImageClick != null) {
                            onImageClick.onCountClick(groupPosition);
                        }
                        //作品点击展开
                        if (eventListener != null) {
                            eventListener.onEvent(EventStatisticsHelper.createOneActionList(EventConstant.WORK_CLICK_EXPAND, bean.workId, ""));
                        }
                    } else {//加载异常时点击重新加载
                        if (headHolder.work_list_cover_img.getLayoutParams() != null && headHolder.itemView.getLayoutParams() != null) {
                            headHolder.work_list_cover_img.getLayoutParams().height = finalImageHeight;
                            headHolder.itemView.getLayoutParams().height = finalImageHeight;
                        }
                        Glide.with(contexts.get()).load(bean.coverUrl).centerCrop()
                                .placeholder(bean.backgroundColor).error(bean.backgroundColor)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                if (headHolder.work_list_cover_img.getLayoutParams() != null && headHolder.itemView.getLayoutParams() != null) {
                                    headHolder.work_list_cover_img.getLayoutParams().height = finalImageHeight;
                                    headHolder.itemView.getLayoutParams().height = finalImageHeight;
                                }

                                if (!bean.isExpand) {
                                    headHolder.work_list_cover_count.setVisibility(View.VISIBLE);
                                    headHolder.layout_source.setVisibility(View.VISIBLE);
                                    headHolder.shadow.setVisibility(View.VISIBLE);

                                    headHolder.work_list_cover_count.setText(String.valueOf(bean.totalImageNum));
                                    Glide.with(contexts.get()).load(bean.sourceLogo).centerCrop()
                                            .transform(new GlideRoundTransform(contexts.get(), 5))
                                            .into(headHolder.source_logo);
                                    headHolder.create_time.setText(bean.createTime);
                                } else {
                                    headHolder.work_list_cover_count.setVisibility(View.GONE);
                                    headHolder.layout_source.setVisibility(View.GONE);
                                    headHolder.shadow.setVisibility(View.GONE);
                                }
                                bean.isCoverLoad = true;
                                return false;
                            }
                        }).override(screenWidth, finalImageHeight).into(headHolder.work_list_cover_img);
                    }
                }
            }
        });
        workListHeadHolder.work_list_cover_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finalWorkListHeadHolder.rippleView.setRippleDuration(0);
                if (onImageClick != null) {
                    onImageClick.onMoreLinkClick(bean.workId, bean.sourceUrl);
                    Log.d("TAG", "onMoreLinkClick = " + bean.sourceUrl);
                }
                return false;
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        WorkListItemHolder workListItemHolder = null;
        convertView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_list_item, parent, false);
        workListItemHolder = new WorkListItemHolder(convertView);
       /* if (convertView == null) {
            convertView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_list_item, parent, false);
            workListItemHolder = new WorkListItemHolder(convertView);
            convertView.setTag(workListItemHolder);
        } else {
            workListItemHolder = (WorkListItemHolder) convertView.getTag();
        }*/
        final WorkListBean workGroup = works.get(groupPosition);
        final List<WorkListItemBean> workListBean = workGroup.workDetail;
        final WorkListItemBean bean = workListBean.get(childPosition);
        int detailSize = works.get(groupPosition).workDetail.size();
        final WorkListItemHolder finalWorkListItemHolder = workListItemHolder;
        int imageHeight = 0;
        if (TextUtils.isEmpty(bean.imageUrl)) {
            workListItemHolder.work_list_item_img.setVisibility(View.GONE);
        } else {
            if (bean.width > 0) {
                workListItemHolder.work_list_item_img.setVisibility(View.VISIBLE);
                if (bean.height < bean.width) {
                    imageHeight = screenWidth;
                } else {
                    imageHeight = (bean.height * screenWidth) / bean.width;
                }
                if (imageHeight > 8192) {
                    imageHeight = 8192;
                }
                if (workListItemHolder.work_list_item_img.getLayoutParams() != null) {
                    workListItemHolder.work_list_item_img.getLayoutParams().height = imageHeight;
                }
                Glide.with(contexts.get()).load(bean.imageUrl).centerCrop()
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
                }).override(screenWidth, imageHeight).into(finalWorkListItemHolder.work_list_item_img);

                if (NetUtils.isWifi(contexts.get())) {
                    if (workListBean.size() > 1) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 1; i < workListBean.size(); i++) {
                                    Glide.with(contexts.get()).load(workListBean.get(i).imageUrl).downloadOnly(workListBean.get(i).width, workListBean.get(i).height);
                                }
                            }
                        });
                    }
                }
            }
        }
        if (childPosition == detailSize - 1) {
            workListItemHolder.work_list_item_title.setVisibility(View.VISIBLE);
            workListItemHolder.work_list_item_author.setVisibility(View.VISIBLE);
            workListItemHolder.bg_line.setVisibility(View.VISIBLE);
            workListItemHolder.work_list_item_title.setText(bean.workTitle);
            workListItemHolder.author_name.setText(bean.authorName);

            Glide.with(contexts.get()).load(bean.authorHeadImg).centerCrop()
                    .bitmapTransform(new GlideCircleTransform(contexts.get())).into(workListItemHolder.author_image);
            if (!TextUtils.isEmpty(bean.authorPageUrl)) {
                workListItemHolder.author_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebViewActivity.jumpToWebViewActivity(contexts.get(), bean.authorPageUrl);
                    }
                });
                workListItemHolder.author_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebViewActivity.jumpToWebViewActivity(contexts.get(), bean.authorPageUrl);
                    }
                });
            }

            workListItemHolder.work_list_item_author.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClick != null) {
                        onImageClick.onMoreLinkClick(workGroup.workId, bean.sourceUrl);
                    }
                }
            });
            workListItemHolder.work_list_item_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClick != null) {
                        onImageClick.onMoreLinkClick(workGroup.workId, bean.sourceUrl);
                    }
                }
            });
        } else {
            workListItemHolder.work_list_item_title.setVisibility(View.GONE);
            workListItemHolder.work_list_item_author.setVisibility(View.GONE);
            workListItemHolder.bg_line.setVisibility(View.GONE);
        }
        if (bean.backgroundColor > 0) {
            workListItemHolder.rippleView.setRippleColor(bean.backgroundColor);
        }
        final int finalSaveImageHeight = imageHeight;
        workListItemHolder.rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.isLoad) {
                    finalWorkListItemHolder.rippleView.setRippleDuration(300);
                    if (onImageClick != null) {
                        onImageClick.onCoverClick(works.get(groupPosition).workId, bean.imageUrl, screenWidth, finalSaveImageHeight);
                    }
                } else {
                    finalWorkListItemHolder.rippleView.setRippleDuration(0);
                    Glide.with(contexts.get()).load(bean.imageUrl).centerCrop()
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
                    }).override(screenWidth, finalSaveImageHeight).into(finalWorkListItemHolder.work_list_item_img);
                }
            }
        });
        workListItemHolder.work_list_item_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finalWorkListItemHolder.rippleView.setRippleDuration(0);
                if (onImageClick != null) {
                    onImageClick.onMoreLinkClick(workGroup.workId, bean.sourceUrl);
                }
                return false;
            }
        });
        return convertView;
    }

    private static class WorkListHeadHolder {
        private View itemView;
        private RippleView rippleView;
        private RelativeLayout layout_source, shadow, layout_auditing_tips;
        private ImageView work_list_cover_img, source_logo;
        private TextView work_list_cover_count, create_time;

        private WorkListHeadHolder(View itemView) {
            this.itemView = itemView;
            rippleView = (RippleView) itemView.findViewById(R.id.rippleView);
            layout_source = (RelativeLayout) itemView.findViewById(R.id.layout_source);
            work_list_cover_img = (ImageView) itemView.findViewById(R.id.work_list_cover_img);
            source_logo = (ImageView) itemView.findViewById(R.id.source_logo);
            work_list_cover_count = (TextView) itemView.findViewById(R.id.work_list_cover_count);
            create_time = (TextView) itemView.findViewById(R.id.create_time);
            shadow = (RelativeLayout) itemView.findViewById(R.id.shadow);
            layout_auditing_tips = (RelativeLayout) itemView.findViewById(R.id.layout_auditing_tips);
        }
    }

    private static class WorkListItemHolder {
        private ImageView work_list_item_img;
        private TextView work_list_item_title;
        public RelativeLayout work_list_item_author;
        private RippleView rippleView;
        private ImageView author_image;
        private TextView author_name;
        private ImageView author_more;
        private View bg_line;

        private WorkListItemHolder(View rootView) {
            rippleView = (RippleView) rootView.findViewById(R.id.rippleView);
            work_list_item_img = (ImageView) rootView.findViewById(R.id.work_list_item_img);
            work_list_item_title = (TextView) rootView.findViewById(R.id.work_list_item_title);
            work_list_item_author = (RelativeLayout) rootView.findViewById(R.id.work_list_item_author);
            author_image = (ImageView) rootView.findViewById(R.id.author_image);
            author_name = (TextView) rootView.findViewById(R.id.author_name);
            author_more = (ImageView) rootView.findViewById(R.id.author_more);
            bg_line = rootView.findViewById(R.id.bg_line);
            bg_line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    @Override
    public int getGroupCount() {
        return works.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return works.get(groupPosition).workDetail.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return works.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return works.get(groupPosition).workDetail.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
