package com.arun.a85mm.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
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
import com.arun.a85mm.activity.FragmentCommonActivity;
import com.arun.a85mm.activity.MainActivity;
import com.arun.a85mm.activity.WebViewActivity;
import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.fragment.TagWorkFragment;
import com.arun.a85mm.helper.ConfigHelper;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.listener.EventListener;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.listener.OnTagWorkListener;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.arun.a85mm.utils.GlideRoundTransform;
import com.arun.a85mm.utils.NetUtils;
import com.arun.a85mm.widget.AutoLineLinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2017/5/5.
 */

public class CommunityAdapter extends BaseExpandableListAdapter {
    private WeakReference<Context> contexts;
    private List<WorkListBean> workList;
    private int screenWidth;
    private boolean isCommunity;
    private Resources resources;

    public CommunityAdapter(Context context, List<WorkListBean> workList, boolean isCommunity) {
        contexts = new WeakReference<>(context);
        this.workList = workList;
        screenWidth = DensityUtil.getScreenWidth(context);
        resources = context.getResources();
        this.isCommunity = isCommunity;
    }

    public OnImageClick onImageClick;

    public void setOnImageClick(OnImageClick onImageClick) {
        this.onImageClick = onImageClick;
    }

    public EventListener eventListener;

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public OnTagWorkListener onTagWorkListener;

    public void setOnTagWorkListener(OnTagWorkListener onTagWorkListener) {
        this.onTagWorkListener = onTagWorkListener;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        WorkListHeadHolder workListHeadHolder = null;
        convertView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_goods_list, parent, false);
        workListHeadHolder = new WorkListHeadHolder(convertView);

        final WorkListHeadHolder headHolder = workListHeadHolder;
        final WorkListBean bean = workList.get(groupPosition);

        //作品浏览
        if (eventListener != null) {
            if (groupPosition >= 2) {
                WorkListBean previousBean = workList.get(groupPosition - 2);
                if (previousBean != null) {
                    int type = -1;
                    if (isCommunity) {
                        type = EventConstant.WORK_BROWSE_HOTEST;
                    } else {
                        type = EventConstant.WORK_BROWSE_ONEDAY;
                    }
                    eventListener.onEvent(type, previousBean.id);
                }
            }
        }

        if (isCommunity) {
            if (bean.isTitle) {
                headHolder.layout_work_title.setVisibility(View.VISIBLE);
                headHolder.works_date.setText(bean.date);
                headHolder.count_works.setText(resources.getString(R.string.works_all_count, bean.workNum));
                headHolder.count_persons.setText(resources.getString(R.string.works_browse_count, bean.browseNum));
                headHolder.count_downloads.setText(resources.getString(R.string.works_download_count_all, bean.allDownloadNum));
            } else {
                headHolder.layout_work_title.setVisibility(View.GONE);
            }

            if (!bean.isExpand && bean.isBottom) {
                headHolder.layout_works_more.setVisibility(View.VISIBLE);
                if (groupPosition == workList.size() - 1) {
                    headHolder.bg_line.setVisibility(View.GONE);
                } else {
                    headHolder.bg_line.setVisibility(View.VISIBLE);
                }

                headHolder.query_more_works.setText(resources.getString(R.string.query_one_day_left_works, bean.date, bean.leftWorkNum));
                headHolder.layout_works_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jumpToLeftWorks(bean);
                    }
                });
            } else {
                headHolder.layout_works_more.setVisibility(View.GONE);
                headHolder.bg_line.setVisibility(View.GONE);
            }
        } else {
            headHolder.layout_work_title.setVisibility(View.GONE);
            headHolder.layout_works_more.setVisibility(View.GONE);
            headHolder.bg_line.setVisibility(View.GONE);
        }

        int imageHeight = 0;
        if (bean.coverWidth > 0) {
            if (bean.coverHeight < bean.coverWidth) {
                if (ConfigHelper.isShowWImage == Constant.VALUE_SHOW_WIMAGE) {
                    imageHeight = screenWidth;
                } else {
                    imageHeight = (bean.coverHeight * screenWidth) / bean.coverWidth;
                }
            } else {
                imageHeight = (bean.coverHeight * screenWidth) / bean.coverWidth;
            }
            if (imageHeight > 8192) {
                imageHeight = 8192;
            }
            if (bean.isCoverLoad) {
                if (headHolder.work_list_cover_img.getLayoutParams() != null && headHolder.itemImageView.getLayoutParams() != null) {
                    headHolder.work_list_cover_img.getLayoutParams().height = imageHeight;
                    headHolder.itemImageView.getLayoutParams().height = imageHeight;
                }
                Glide.with(contexts.get()).load(bean.coverUrl).centerCrop()
                        .placeholder(bean.backgroundColor).error(bean.backgroundColor)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .override(screenWidth, imageHeight).into(headHolder.work_list_cover_img);

                headHolder.work_list_cover_count.setVisibility(View.VISIBLE);
                headHolder.layout_source.setVisibility(View.VISIBLE);
                headHolder.shadow.setVisibility(View.VISIBLE);
                headHolder.work_list_cover_count.setText(String.valueOf(bean.totalImageNum));
                Glide.with(contexts.get()).load(bean.sourceLogo).centerCrop()
                        .transform(new GlideRoundTransform(contexts.get(), 5))
                        .into(headHolder.source_logo);
                headHolder.create_time.setText(bean.copy);//resources.getString(R.string.works_download_count_one, bean.downloadNum)
            } else {
                if (headHolder.work_list_cover_img.getLayoutParams() != null && headHolder.itemImageView.getLayoutParams() != null) {
                    headHolder.work_list_cover_img.getLayoutParams().height = imageHeight;
                    headHolder.itemImageView.getLayoutParams().height = imageHeight;
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
                        if (headHolder.work_list_cover_img.getLayoutParams() != null && headHolder.itemImageView.getLayoutParams() != null) {
                            headHolder.work_list_cover_img.getLayoutParams().height = finalImageHeight;
                            headHolder.itemImageView.getLayoutParams().height = finalImageHeight;
                        }

                        headHolder.work_list_cover_count.setVisibility(View.VISIBLE);
                        headHolder.layout_source.setVisibility(View.VISIBLE);
                        headHolder.shadow.setVisibility(View.VISIBLE);
                        headHolder.work_list_cover_count.setText(String.valueOf(bean.totalImageNum));
                        Glide.with(contexts.get()).load(bean.sourceLogo).centerCrop()
                                .transform(new GlideRoundTransform(contexts.get(), 5))
                                .into(headHolder.source_logo);
                        headHolder.create_time.setText(bean.copy);//resources.getString(R.string.works_download_count_one, bean.downloadNum)
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
                        onImageClick.onCoverClick(bean.id, bean.coverUrl, bean.authorName);
                    }
                } else {
                    if (bean.isCoverLoad) {
                        bean.isExpand = true;
                        if (onImageClick != null) {
                            onImageClick.onCountClick(groupPosition);
                        }
                        if (bean.isBottom) {
                            headHolder.layout_works_more.setVisibility(View.GONE);
                            headHolder.bg_line.setVisibility(View.GONE);
                        }
                        //作品点击展开
                        if (eventListener != null) {
                            eventListener.onEvent(EventConstant.WORK_CLICK_EXPAND, bean.id);
                        }
                    } else {//加载异常时点击重新加载
                        if (headHolder.work_list_cover_img.getLayoutParams() != null && headHolder.itemImageView.getLayoutParams() != null) {
                            headHolder.work_list_cover_img.getLayoutParams().height = finalImageHeight;
                            headHolder.itemImageView.getLayoutParams().height = finalImageHeight;
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
                                if (headHolder.work_list_cover_img.getLayoutParams() != null && headHolder.itemImageView.getLayoutParams() != null) {
                                    headHolder.work_list_cover_img.getLayoutParams().height = finalImageHeight;
                                    headHolder.itemImageView.getLayoutParams().height = finalImageHeight;
                                }


                                headHolder.work_list_cover_count.setVisibility(View.VISIBLE);
                                headHolder.layout_source.setVisibility(View.VISIBLE);
                                headHolder.shadow.setVisibility(View.VISIBLE);
                                headHolder.work_list_cover_count.setText(String.valueOf(bean.totalImageNum));
                                Glide.with(contexts.get()).load(bean.sourceLogo).centerCrop()
                                        .transform(new GlideRoundTransform(contexts.get(), 5))
                                        .into(headHolder.source_logo);
                                headHolder.create_time.setText(bean.copy);//resources.getString(R.string.works_download_count_one, bean.downloadNum)

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
                    onImageClick.onMoreLinkClick(bean.id, bean.sourceUrl, bean.uid);
                    Log.d("TAG", "onMoreLinkClick = " + bean.sourceUrl);
                }
                return false;
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        WorkListItemHolder workListItemHolder = null;
        convertView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_list_item, parent, false);
        workListItemHolder = new WorkListItemHolder(convertView);
        /*if (convertView == null) {
            convertView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_list_item, parent, false);
            workListItemHolder = new WorkListItemHolder(convertView);
            convertView.setTag(workListItemHolder);
        } else {
            workListItemHolder = (WorkListItemHolder) convertView.getTag();
        }*/
        final WorkListBean workGroup = workList.get(groupPosition);
        final List<WorkListItemBean> workListBean = workGroup.imageList;
        final WorkListItemBean bean = workListBean.get(childPosition);
        int detailSize = workList.get(groupPosition).imageList.size();
        final WorkListItemHolder finalWorkListItemHolder = workListItemHolder;
        int imageHeight = 0;
        if (TextUtils.isEmpty(bean.url)) {
            workListItemHolder.work_list_item_img.setVisibility(View.GONE);
        } else {
            if (bean.width > 0) {
                workListItemHolder.work_list_item_img.setVisibility(View.VISIBLE);
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
            if (groupPosition == workList.size() - 1) {
                workListItemHolder.bg_line.setVisibility(View.GONE);
            } else {
                workListItemHolder.bg_line.setVisibility(View.VISIBLE);
            }
            //workListItemHolder.bg_line.setVisibility(View.VISIBLE);
            workListItemHolder.work_list_item_title.setText(bean.workTitle);
            workListItemHolder.author_name.setText(bean.authorName);
            if (!TextUtils.isEmpty(bean.description)) {
                workListItemHolder.work_list_item_detail.setVisibility(View.VISIBLE);
                workListItemHolder.work_list_item_detail.setText(bean.description);
            } else {
                workListItemHolder.work_list_item_detail.setVisibility(View.GONE);
            }

            Glide.with(contexts.get()).load(bean.authorHeadImg).centerCrop()
                    .bitmapTransform(new GlideCircleTransform(contexts.get())).into(workListItemHolder.author_image);
            if (!TextUtils.isEmpty(bean.authorPageUrl)) {
                workListItemHolder.author_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (eventListener != null) {
                            eventListener.onEvent(EventConstant.WORK_CLICK_AUTHOR);
                        }
                        WebViewActivity.jumpToWebViewActivity(contexts.get(), bean.authorPageUrl);
                    }
                });
                workListItemHolder.author_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (eventListener != null) {
                            eventListener.onEvent(EventConstant.WORK_CLICK_AUTHOR);
                        }
                        WebViewActivity.jumpToWebViewActivity(contexts.get(), bean.authorPageUrl);
                    }
                });
            }

            workListItemHolder.work_list_item_author.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClick != null) {
                        onImageClick.onMoreLinkClick(workGroup.id, bean.sourceUrl, workGroup.uid);
                    }
                }
            });
            workListItemHolder.work_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contexts.get() instanceof MainActivity) {
                        ((MainActivity) contexts.get()).shareWorkDetail(workGroup);
                    } else if (contexts.get() instanceof BaseActivity) {
                        ((BaseActivity) contexts.get()).shareWorkDetail(workGroup);
                    }
                }
            });
            workListItemHolder.work_list_item_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClick != null) {
                        onImageClick.onMoreLinkClick(workGroup.id, bean.sourceUrl, workGroup.uid);
                    }
                }
            });
            workListItemHolder.work_list_item_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClick != null) {
                        onImageClick.onMoreLinkClick(workGroup.id, bean.sourceUrl, workGroup.uid);
                    }
                }
            });

            if (workGroup.workTags != null && workGroup.workTags.size() > 0) {
                workListItemHolder.layout_work_tags.setVisibility(View.VISIBLE);
                workListItemHolder.layout_work_tags.removeAllViews();
                for (int i = 0; i < workGroup.workTags.size(); i++) {
                    final String tagName = workGroup.workTags.get(i);
                    View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_tag_item_gray, workListItemHolder.layout_work_tags, false);
                    TextView work_tag_name = (TextView) itemView.findViewById(R.id.work_tag_name);
                    work_tag_name.setText(workGroup.workTags.get(i));
                    workListItemHolder.layout_work_tags.addView(itemView);
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, String> map = new HashMap<>();
                            map.put(TagWorkFragment.KEY_TAG_NAME, tagName);
                            FragmentCommonActivity.jumpToFragmentCommonActivity(contexts.get(), FragmentCommonActivity.FRAGMENT_TAG_WORKS, tagName, map);
                        }
                    });
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

            if (isCommunity) {
                if (workList.get(groupPosition).isBottom) {
                    workListItemHolder.layout_works_more.setVisibility(View.VISIBLE);
                    workListItemHolder.query_more_works.setText(resources.getString(R.string.query_one_day_left_works, workGroup.date, workGroup.leftWorkNum));
                    if (groupPosition == workList.size() - 1) {
                        workListItemHolder.bg_line.setVisibility(View.GONE);
                    } else {
                        workListItemHolder.bg_line.setVisibility(View.VISIBLE);
                    }

                    workListItemHolder.layout_works_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpToLeftWorks(workList.get(groupPosition));
                        }
                    });
                } else {
                    workListItemHolder.layout_works_more.setVisibility(View.GONE);
                }
            } else {
                workListItemHolder.bg_line.setVisibility(View.GONE);
                workListItemHolder.layout_works_more.setVisibility(View.GONE);
            }
        } else {
            workListItemHolder.work_list_item_title.setVisibility(View.GONE);
            workListItemHolder.work_list_item_detail.setVisibility(View.GONE);
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
                        onImageClick.onCoverClick(workList.get(groupPosition).id, bean.url, workList.get(groupPosition).authorName);
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
                    }).into(finalWorkListItemHolder.work_list_item_img);
                }
            }
        });
        workListItemHolder.work_list_item_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finalWorkListItemHolder.rippleView.setRippleDuration(0);
                if (onImageClick != null) {
                    onImageClick.onMoreLinkClick(workGroup.id, bean.sourceUrl, workGroup.uid);
                }
                return false;
            }
        });
        return convertView;
    }

    private void jumpToLeftWorks(WorkListBean bean) {
        if (eventListener != null) {
            eventListener.onEvent(EventConstant.WORK_CLICK_ONEDAY);
        }

        String title = resources.getString(R.string.one_day_left_works_title, bean.date, bean.leftWorkNum);
        Map<String, String> map = new HashMap<>();
        map.put(Constant.INTENT_WORKS_LEFT_DATE, bean.searchDate);
        map.put(Constant.INTENT_WORKS_LEFT_START, bean.id);
        FragmentCommonActivity.jumpToFragmentCommonActivity(contexts.get(),
                FragmentCommonActivity.FRAGMENT_LEFT_WORKS, title, map);
    }

    public static class WorkListHeadHolder {
        public TextView works_date;
        public TextView count_works;
        public TextView count_persons;
        public TextView count_downloads;
        public ImageView work_list_cover_img;
        public RippleView rippleView;
        public ImageView source_logo;
        public TextView create_time;
        public RelativeLayout layout_source;
        public TextView work_list_cover_count;
        public TextView query_more_works;
        public RelativeLayout itemImageView;
        private RelativeLayout layout_work_title;
        private RelativeLayout layout_works_more;
        private RelativeLayout shadow;
        private View bg_line;

        public WorkListHeadHolder(View rootView) {
            this.works_date = (TextView) rootView.findViewById(R.id.works_date);
            this.count_works = (TextView) rootView.findViewById(R.id.count_works);
            this.count_persons = (TextView) rootView.findViewById(R.id.count_persons);
            this.count_downloads = (TextView) rootView.findViewById(R.id.count_downloads);
            this.work_list_cover_img = (ImageView) rootView.findViewById(R.id.work_list_cover_img);
            this.rippleView = (RippleView) rootView.findViewById(R.id.rippleView);
            this.source_logo = (ImageView) rootView.findViewById(R.id.source_logo);
            this.create_time = (TextView) rootView.findViewById(R.id.create_time);
            this.layout_source = (RelativeLayout) rootView.findViewById(R.id.layout_source);
            this.work_list_cover_count = (TextView) rootView.findViewById(R.id.work_list_cover_count);
            this.query_more_works = (TextView) rootView.findViewById(R.id.query_more_works);
            this.itemImageView = (RelativeLayout) rootView.findViewById(R.id.itemImageView);
            this.layout_work_title = (RelativeLayout) rootView.findViewById(R.id.layout_work_title);
            this.layout_works_more = (RelativeLayout) rootView.findViewById(R.id.layout_works_more);
            this.shadow = (RelativeLayout) rootView.findViewById(R.id.shadow);
            this.bg_line = rootView.findViewById(R.id.bg_line);
            bg_line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            layout_work_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

    }

    private static class WorkListItemHolder {
        private ImageView work_list_item_img;
        private TextView work_list_item_title;
        private TextView work_list_item_detail;
        public RelativeLayout work_list_item_author;
        private RippleView rippleView;
        private ImageView author_image;
        private TextView author_name;
        private ImageView work_share, author_more;
        private RelativeLayout layout_works_more;
        private TextView query_more_works;
        private View bg_line;
        private AutoLineLinearLayout layout_work_tags;
        private LinearLayout layout_user_tags;
        private AutoLineLinearLayout user_tags;

        private WorkListItemHolder(View rootView) {
            rippleView = (RippleView) rootView.findViewById(R.id.rippleView);
            work_list_item_img = (ImageView) rootView.findViewById(R.id.work_list_item_img);
            work_list_item_title = (TextView) rootView.findViewById(R.id.work_list_item_title);
            work_list_item_detail = (TextView) rootView.findViewById(R.id.work_list_item_detail);
            work_list_item_author = (RelativeLayout) rootView.findViewById(R.id.work_list_item_author);
            author_image = (ImageView) rootView.findViewById(R.id.author_image);
            author_name = (TextView) rootView.findViewById(R.id.author_name);
            work_share = (ImageView) rootView.findViewById(R.id.work_share);
            author_more = (ImageView) rootView.findViewById(R.id.author_more);
            layout_works_more = (RelativeLayout) rootView.findViewById(R.id.layout_works_more);
            query_more_works = (TextView) rootView.findViewById(R.id.query_more_works);
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
        return workList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return workList.get(groupPosition).imageList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return workList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return workList.get(groupPosition).imageList.get(childPosition);
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
