package com.arun.a85mm.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.arun.a85mm.R;
import com.arun.a85mm.activity.BaseActivity;
import com.arun.a85mm.activity.WebViewActivity;
import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.helper.ConfigHelper;
import com.arun.a85mm.listener.EventListener;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.listener.OnTagWorkListener;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.arun.a85mm.utils.GlideRoundTransform;
import com.arun.a85mm.utils.NetUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.widget.AutoLineLinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy on 2017/4/18.
 */

public class ProductListAdapter extends BaseExpandableListAdapter {
    private WeakReference<Context> contexts;
    private List<WorkListBean> works;
    private int screenWidth;
    private boolean isHaveAuditTips = false;

    public ProductListAdapter(Context context, List<WorkListBean> works) {
        contexts = new WeakReference<>(context);
        this.works = works;
        screenWidth = DensityUtil.getScreenWidth(context);
    }

    public OnTagWorkListener onTagWorkListener;

    public void setOnTagWorkListener(OnTagWorkListener onTagWorkListener) {
        this.onTagWorkListener = onTagWorkListener;
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
        workListHeadHolder = new WorkListHeadHolder(contexts.get(), convertView);

       /* if (ConfigHelper.tipsPosition > 0 && ConfigHelper.tipsPosition == groupPosition + 1) {
            isHaveAuditTips = true;
            workListHeadHolder.layout_auditing_tips.setVisibility(View.VISIBLE);
            workListHeadHolder.layout_auditing_tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuditActivity.jumpToAudit(contexts.get());
                }
            });
        } else {
            isHaveAuditTips = false;
            workListHeadHolder.layout_auditing_tips.setVisibility(View.GONE);
        }*/

        final WorkListHeadHolder headHolder = workListHeadHolder;
        final WorkListBean bean = works.get(groupPosition);

        //作品浏览
        if (eventListener != null) {
            if (groupPosition >= 2) {
                WorkListBean previousBean = works.get(groupPosition - 2);
                if (previousBean != null) {
                    eventListener.onEvent(EventConstant.WORK_BROWSE_NEWEST, previousBean.id);
                }
            }
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
                    /*if (isHaveAuditTips) {
                        headHolder.itemView.getLayoutParams().height = imageHeight + DensityUtil.dp2px(contexts.get(), 67);
                    } else {
                        headHolder.itemView.getLayoutParams().height = imageHeight;
                    }*/
                }
                Glide.with(contexts.get()).load(bean.coverUrl).centerCrop()
                        .placeholder(bean.backgroundColor).error(bean.backgroundColor)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .override(screenWidth, imageHeight).into(headHolder.work_list_cover_img);

                headHolder.work_list_cover_count.setVisibility(View.VISIBLE);
                headHolder.layout_source.setVisibility(View.VISIBLE);
                headHolder.shadow.setVisibility(View.VISIBLE);
                headHolder.work_list_cover_count.setText(String.valueOf(bean.totalImageNum));
                Glide.with(contexts.get())
                        .load(bean.sourceLogo)
                        .centerCrop()
                        .transform(new GlideRoundTransform(contexts.get(), 5))
                        .into(headHolder.source_logo);
                headHolder.create_time.setText(bean.createTime);

            } else {
                if (headHolder.work_list_cover_img.getLayoutParams() != null && headHolder.itemView.getLayoutParams() != null) {
                    headHolder.work_list_cover_img.getLayoutParams().height = imageHeight;
                    headHolder.itemView.getLayoutParams().height = imageHeight;
                   /* if (isHaveAuditTips) {
                        headHolder.itemView.getLayoutParams().height = imageHeight + DensityUtil.dp2px(contexts.get(), 67);
                    } else {
                        headHolder.itemView.getLayoutParams().height = imageHeight;
                    }*/
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
                        }

                        headHolder.work_list_cover_count.setVisibility(View.VISIBLE);
                        headHolder.layout_source.setVisibility(View.VISIBLE);
                        headHolder.shadow.setVisibility(View.VISIBLE);
                        headHolder.work_list_cover_count.setText(String.valueOf(bean.totalImageNum));
                        Glide.with(contexts.get()).load(bean.sourceLogo).centerCrop().
                                transform(new GlideRoundTransform(contexts.get(), 5)).into(headHolder.source_logo);
                        headHolder.create_time.setText(bean.createTime);

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
        workListHeadHolder.work_list_cover_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.isExpand) {
                    finalWorkListHeadHolder.rippleView.setRippleDuration(300);
                    if (onImageClick != null) {
                        onImageClick.onCoverClick(bean.id, bean.coverUrl, screenWidth, finalImageHeight);
                    }
                } else {
                    if (bean.isCoverLoad) {
                        bean.isExpand = true;
                        if (onImageClick != null) {
                            onImageClick.onCountClick(groupPosition);
                        }
                        //作品点击展开
                        if (eventListener != null) {
                            eventListener.onEvent(EventConstant.WORK_CLICK_EXPAND, bean.id);
                        }
                    } else {//加载异常时点击重新加载
                        if (headHolder.work_list_cover_img.getLayoutParams() != null && headHolder.itemView.getLayoutParams() != null) {
                            headHolder.work_list_cover_img.getLayoutParams().height = finalImageHeight;
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
                                }


                                headHolder.work_list_cover_count.setVisibility(View.VISIBLE);
                                headHolder.layout_source.setVisibility(View.VISIBLE);
                                headHolder.shadow.setVisibility(View.VISIBLE);
                                headHolder.work_list_cover_count.setText(String.valueOf(bean.totalImageNum));
                                Glide.with(contexts.get()).load(bean.sourceLogo).centerCrop()
                                        .transform(new GlideRoundTransform(contexts.get(), 5))
                                        .into(headHolder.source_logo);
                                headHolder.create_time.setText(bean.createTime);

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
                    onImageClick.onMoreLinkClick(bean.id, bean.sourceUrl);
                    Log.d("TAG", "onMoreLinkClick = " + bean.sourceUrl);
                }
                return false;
            }
        });
        if (!TextUtils.isEmpty(SharedPreferencesUtils.getUid(contexts.get()))
                && SharedPreferencesUtils.getUid(contexts.get()).equals("4")) {
            final GestureDetector gestureDetector = new GestureDetector(contexts.get(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    Log.d("TAG", "IMAGE  onSingleTapConfirmed ");
                    if (eventListener != null) {
                        eventListener.onEvent(EventConstant.WORK_BAD_COMMNET, bean.id);
                    }
                    return super.onSingleTapConfirmed(e);
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d("TAG", "IMAGE onDoubleTap ");
                    if (eventListener != null) {
                        eventListener.onEvent(EventConstant.WORK_REPORT, bean.id);
                    }
                    return super.onDoubleTap(e);
                }
            });

            workListHeadHolder.create_time.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });

        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        WorkListItemHolder workListItemHolder = null;
        convertView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_list_item, parent, false);
        workListItemHolder = new WorkListItemHolder(convertView);

        final WorkListBean workGroup = works.get(groupPosition);
        final List<WorkListItemBean> workListBean = workGroup.imageList;
        final WorkListItemBean bean = workListBean.get(childPosition);
        int detailSize = works.get(groupPosition).imageList.size();
        final WorkListItemHolder finalWorkListItemHolder = workListItemHolder;
        int imageHeight = 0;
        if (TextUtils.isEmpty(bean.url)) {
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
                Glide.with(contexts.get()).load(bean.url).centerCrop()
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
                                    Glide.with(contexts.get()).load(workListBean.get(i).url).downloadOnly(workListBean.get(i).width, workListBean.get(i).height);
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
                        onImageClick.onMoreLinkClick(workGroup.id, bean.sourceUrl);
                    }
                }
            });
            workListItemHolder.work_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contexts.get() instanceof BaseActivity) {
                        ((BaseActivity) contexts.get()).shareWorkDetail(workGroup);
                    }
                }
            });
            workListItemHolder.work_list_item_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClick != null) {
                        onImageClick.onMoreLinkClick(workGroup.id, bean.sourceUrl);
                    }
                }
            });

            if (workGroup.workTags != null && workGroup.workTags.size() > 0) {
                workListItemHolder.layout_work_tags.setVisibility(View.VISIBLE);
                workListItemHolder.layout_work_tags.removeAllViews();
                for (int i = 0; i < workGroup.workTags.size(); i++) {
                    View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_tag_item_gray, workListItemHolder.layout_work_tags, false);
                    TextView work_tag_name = (TextView) itemView.findViewById(R.id.work_tag_name);
                    work_tag_name.setText(workGroup.workTags.get(i));
                    workListItemHolder.layout_work_tags.addView(itemView);
                }
            } else {
                workListItemHolder.layout_work_tags.setVisibility(View.GONE);
            }

            if (ConfigHelper.userTags != null && ConfigHelper.userTags.size() > 0) {
                workListItemHolder.layout_user_tags.setVisibility(View.VISIBLE);
                workListItemHolder.user_tags.removeAllViews();
                final List<UserTagBean> userTags = new ArrayList<>();
                userTags.addAll(ConfigHelper.userTags);
                for (int i = 0; i < userTags.size(); i++) {
                    final UserTagBean tagBean = userTags.get(i);
                    if (tagBean != null && tagBean.isShow == 1) {
                        tagBean.tagType = 0;
                        final TextView myTag = new TextView(contexts.get());
                        myTag.setTextColor(contexts.get().getResources().getColor(R.color.text_right_tips));
                        myTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                        myTag.setText(tagBean.name);
                        myTag.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onTagWorkListener != null) {
                                    onTagWorkListener.onClickMyTag(tagBean, workGroup.id);
                                }
                                tagBean.tagType = tagBean.tagType == 0 ? 1 : 0;
                                myTag.setTextColor(contexts.get().getResources().getColor(tagBean.tagType == 1 ? R.color.more_yellow : R.color.text_right_tips));
                            }
                        });
                        workListItemHolder.user_tags.addView(myTag);
                    }
                }
            } else {
                workListItemHolder.layout_user_tags.setVisibility(View.GONE);
            }

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
                        onImageClick.onCoverClick(works.get(groupPosition).id, bean.url, screenWidth, finalSaveImageHeight);
                    }
                } else {
                    finalWorkListItemHolder.rippleView.setRippleDuration(0);
                    Glide.with(contexts.get()).load(bean.url).centerCrop()
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
                    onImageClick.onMoreLinkClick(workGroup.id, bean.sourceUrl);
                }
                return false;
            }
        });
        return convertView;
    }

    private static class WorkListHeadHolder {
        private RelativeLayout itemView;
        private RippleView rippleView;
        private RelativeLayout layout_source, shadow, layout_auditing_tips;
        private ImageView work_list_cover_img, source_logo;
        private TextView work_list_cover_count, create_time;

        private WorkListHeadHolder(Context context, View itemView) {
            this.itemView = (RelativeLayout) itemView;
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
        private ImageView work_share, author_more;
        private View bg_line;
        private AutoLineLinearLayout layout_work_tags;
        private LinearLayout layout_user_tags;
        private AutoLineLinearLayout user_tags;

        private WorkListItemHolder(View rootView) {
            rippleView = (RippleView) rootView.findViewById(R.id.rippleView);
            work_list_item_img = (ImageView) rootView.findViewById(R.id.work_list_item_img);
            work_list_item_title = (TextView) rootView.findViewById(R.id.work_list_item_title);
            work_list_item_author = (RelativeLayout) rootView.findViewById(R.id.work_list_item_author);
            author_image = (ImageView) rootView.findViewById(R.id.author_image);
            author_name = (TextView) rootView.findViewById(R.id.author_name);
            work_share = (ImageView) rootView.findViewById(R.id.work_share);
            author_more = (ImageView) rootView.findViewById(R.id.author_more);
            bg_line = rootView.findViewById(R.id.bg_line);
            bg_line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            layout_work_tags = (AutoLineLinearLayout) rootView.findViewById(R.id.layout_work_tags);
            layout_user_tags = (LinearLayout) rootView.findViewById(R.id.layout_user_tags);
            user_tags = (AutoLineLinearLayout) rootView.findViewById(R.id.user_tags);

        }
    }

    @Override
    public int getGroupCount() {
        return works.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return works.get(groupPosition).imageList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return works.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return works.get(groupPosition).imageList.get(childPosition);
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
