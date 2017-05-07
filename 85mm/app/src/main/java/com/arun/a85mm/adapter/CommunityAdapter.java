package com.arun.a85mm.adapter;

import android.content.Context;
import android.content.res.Resources;
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
import com.arun.a85mm.activity.FragmentCommonActivity;
import com.arun.a85mm.activity.WebViewActivity;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.lang.ref.WeakReference;
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

    public interface OnImageClick {
        void onCountClick(int groupPosition);

        void onCoverClick(String coverUrl, int width, int height);

        void onMoreLinkClick(String sourceUrl);
    }

    public void setOnImageClick(OnImageClick onImageClick) {
        this.onImageClick = onImageClick;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_goods_list, parent, false);
        WorkListHeadHolder workListHeadHolder = new WorkListHeadHolder(view);
        final WorkListHeadHolder headHolder = workListHeadHolder;
        final WorkListBean bean = workList.get(groupPosition);

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
                headHolder.query_more_works.setText(resources.getString(R.string.query_one_day_left_works, bean.date, bean.leftWorkNum));
                headHolder.query_more_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jumpToLeftWorks(bean);
                    }
                });
            } else {
                headHolder.layout_works_more.setVisibility(View.GONE);
            }
        } else {
            headHolder.layout_work_title.setVisibility(View.GONE);
            headHolder.layout_works_more.setVisibility(View.GONE);
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
                if (headHolder.work_list_cover_img.getLayoutParams() != null && headHolder.itemImageView.getLayoutParams() != null) {
                    headHolder.work_list_cover_img.getLayoutParams().height = imageHeight;
                    headHolder.itemImageView.getLayoutParams().height = imageHeight;
                }
                Glide.with(contexts.get()).load(bean.coverUrl).centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(headHolder.work_list_cover_img);

                if (!bean.isExpand) {
                    headHolder.work_list_cover_count.setVisibility(View.VISIBLE);
                    headHolder.layout_source.setVisibility(View.VISIBLE);

                    headHolder.work_list_cover_count.setText(String.valueOf(bean.totalImageNum));
                    Glide.with(contexts.get()).load(bean.sourceLogo).centerCrop().into(headHolder.source_logo);
                    headHolder.create_time.setText(resources.getString(R.string.works_download_count_one, bean.downloadNum));
                } else {
                    headHolder.work_list_cover_count.setVisibility(View.GONE);
                    headHolder.layout_source.setVisibility(View.GONE);
                }
            } else {
                if (headHolder.work_list_cover_img.getLayoutParams() != null && headHolder.itemImageView.getLayoutParams() != null) {
                    headHolder.work_list_cover_img.getLayoutParams().height = imageHeight;
                    headHolder.itemImageView.getLayoutParams().height = imageHeight;
                }
                final int finalImageHeight = imageHeight;
                Glide.with(contexts.get()).load(bean.coverUrl).centerCrop()
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

                        if (!bean.isExpand) {
                            headHolder.work_list_cover_count.setVisibility(View.VISIBLE);
                            headHolder.layout_source.setVisibility(View.VISIBLE);

                            headHolder.work_list_cover_count.setText(String.valueOf(bean.totalImageNum));
                            Glide.with(contexts.get()).load(bean.sourceLogo).centerCrop().into(headHolder.source_logo);
                            headHolder.create_time.setText(resources.getString(R.string.works_download_count_one, bean.downloadNum));
                        } else {
                            headHolder.work_list_cover_count.setVisibility(View.GONE);
                            headHolder.layout_source.setVisibility(View.GONE);
                        }
                        return false;
                    }
                }).into(headHolder.work_list_cover_img);
                bean.isCoverLoad = true;
            }
        }

        if (!bean.isExpand) {
            workListHeadHolder.rippleView.setRippleDuration(0);
        } else {
            workListHeadHolder.rippleView.setRippleDuration(300);
        }
        final WorkListHeadHolder finalWorkListHeadHolder = workListHeadHolder;

        final int finalImageHeight = imageHeight;
        workListHeadHolder.rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.isExpand) {
                    finalWorkListHeadHolder.rippleView.setRippleDuration(300);
                    if (onImageClick != null) {
                        onImageClick.onCoverClick(bean.coverUrl, screenWidth, finalImageHeight);
                    }
                } else {
                    headHolder.work_list_cover_count.setVisibility(View.GONE);
                    headHolder.layout_source.setVisibility(View.GONE);
                    bean.isExpand = true;
                    if (onImageClick != null) {
                        onImageClick.onCountClick(groupPosition);
                    }
                    if (bean.isBottom) {
                        headHolder.layout_works_more.setVisibility(View.GONE);
                    }
                }
            }
        });
        workListHeadHolder.work_list_cover_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finalWorkListHeadHolder.rippleView.setRippleDuration(0);
                if (onImageClick != null) {
                    onImageClick.onMoreLinkClick(bean.sourceUrl);
                    Log.d("TAG", "onMoreLinkClick = " + bean.sourceUrl);
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        WorkListItemHolder workListItemHolder = null;
        View view = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_list_item, parent, false);
        workListItemHolder = new WorkListItemHolder(view);
        /*if (convertView == null) {
            convertView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_list_item, parent, false);
            workListItemHolder = new WorkListItemHolder(convertView);
            convertView.setTag(workListItemHolder);
        } else {
            workListItemHolder = (WorkListItemHolder) convertView.getTag();
        }*/
        final List<WorkListItemBean> workListBean = workList.get(groupPosition).workDetail;
        final WorkListItemBean bean = workListBean.get(childPosition);
        int detailSize = workList.get(groupPosition).workDetail.size();
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
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(finalWorkListItemHolder.work_list_item_img);

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
        if (childPosition == detailSize - 1) {
            workListItemHolder.work_list_item_title.setVisibility(View.VISIBLE);
            workListItemHolder.work_list_item_author.setVisibility(View.VISIBLE);
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
                        onImageClick.onMoreLinkClick(bean.sourceUrl);
                    }
                }
            });
            workListItemHolder.work_list_item_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClick != null) {
                        onImageClick.onMoreLinkClick(bean.sourceUrl);
                    }
                }
            });
            if (isCommunity) {
                if (workList.get(groupPosition).isBottom) {
                    workListItemHolder.layout_works_more.setVisibility(View.VISIBLE);
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
                workListItemHolder.layout_works_more.setVisibility(View.GONE);
            }
        } else {
            workListItemHolder.work_list_item_title.setVisibility(View.GONE);
            workListItemHolder.work_list_item_author.setVisibility(View.GONE);
        }
        final int finalSaveImageHeight = imageHeight;
        workListItemHolder.rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalWorkListItemHolder.rippleView.setRippleDuration(300);
                if (onImageClick != null) {
                    onImageClick.onCoverClick(bean.imageUrl, screenWidth, finalSaveImageHeight);
                }
            }
        });
        workListItemHolder.work_list_item_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finalWorkListItemHolder.rippleView.setRippleDuration(0);
                if (onImageClick != null) {
                    onImageClick.onMoreLinkClick(bean.sourceUrl);
                }
                return false;
            }
        });
        return view;
    }

    private void jumpToLeftWorks(WorkListBean bean) {
        String title = resources.getString(R.string.one_day_left_works_title, bean.date, bean.leftWorkNum);

        Map<String, String> map = new HashMap<>();
        map.put(Constant.INTENT_WORKS_LEFT_DATE, bean.date);
        map.put(Constant.INTENT_WORKS_LEFT_START, String.valueOf(bean.start));
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
        private RelativeLayout layout_works_more;

        private WorkListItemHolder(View rootView) {
            rippleView = (RippleView) rootView.findViewById(R.id.rippleView);
            work_list_item_img = (ImageView) rootView.findViewById(R.id.work_list_item_img);
            work_list_item_title = (TextView) rootView.findViewById(R.id.work_list_item_title);
            work_list_item_author = (RelativeLayout) rootView.findViewById(R.id.work_list_item_author);
            author_image = (ImageView) rootView.findViewById(R.id.author_image);
            author_name = (TextView) rootView.findViewById(R.id.author_name);
            author_more = (ImageView) rootView.findViewById(R.id.author_more);
            layout_works_more = (RelativeLayout) rootView.findViewById(R.id.layout_works_more);
        }
    }

    @Override
    public int getGroupCount() {
        return workList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return workList.get(groupPosition).workDetail.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return workList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return workList.get(groupPosition).workDetail.get(childPosition);
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
