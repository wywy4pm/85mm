package com.arun.a85mm.adapter;

import android.content.Context;
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
import com.arun.a85mm.activity.WebViewActivity;
import com.arun.a85mm.bean.ProductListResponse;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by wy on 2017/4/18.
 */

public class ProductListAdapter extends BaseExpandableListAdapter {
    private WeakReference<Context> contexts;
    private List<ProductListResponse.WorkListBean> works;
    //private List<ProductListResponse.WorkListBean> worksCopy;
    private int screenWidth;

    public ProductListAdapter(Context context, List<ProductListResponse.WorkListBean> works) {
        contexts = new WeakReference<>(context);
        this.works = works;
        //his.worksCopy = worksCopy;
        screenWidth = DensityUtil.getScreenWidth(context);
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
        WorkListHeadHolder workListHeadHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_list, parent, false);
            workListHeadHolder = new WorkListHeadHolder(convertView);
            convertView.setTag(workListHeadHolder);
        } else {
            workListHeadHolder = (WorkListHeadHolder) convertView.getTag();
        }
        final WorkListHeadHolder headHolder = workListHeadHolder;
        final ProductListResponse.WorkListBean bean = works.get(groupPosition);
        //final ProductListResponse.WorkListBean copyBean = worksCopy.get(groupPosition);
        if (!bean.isExpand) {
            headHolder.work_list_cover_count.setVisibility(View.VISIBLE);
            headHolder.layout_source.setVisibility(View.VISIBLE);
            Glide.with(contexts.get()).load(bean.coverUrl).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(headHolder.work_list_cover_img);

        } else {
            headHolder.work_list_cover_count.setVisibility(View.GONE);
            headHolder.layout_source.setVisibility(View.GONE);
        }
        int saveImageHeight = 0;
        if (bean.coverWidth > 0) {
            int imageHeight = (bean.coverHeight * screenWidth) / bean.coverWidth;
            saveImageHeight = imageHeight;
            if (headHolder.work_list_cover_img.getLayoutParams() != null && headHolder.itemView.getLayoutParams() != null) {
                headHolder.work_list_cover_img.getLayoutParams().height = imageHeight;
                headHolder.itemView.getLayoutParams().height = imageHeight;
            }
        }

        headHolder.work_list_cover_count.setText(String.valueOf(bean.totalImageNum));
        Glide.with(contexts.get()).load(bean.sourceLogo).centerCrop().into(headHolder.source_logo);
        headHolder.create_time.setText(bean.createTime);

        //Glide.with(contexts.get()).load(bean.coverUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(headHolder.work_list_cover_img);

        /*headHolder.work_list_cover_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headHolder.work_list_cover_count.setVisibility(View.GONE);
                headHolder.layout_source.setVisibility(View.GONE);
                //copyBean.isExpand = true;
                bean.isExpand = true;
                if (onImageClick != null) {
                    onImageClick.onCountClick(groupPosition);
                }
            }
        });*/
        if (!bean.isExpand) {
            workListHeadHolder.rippleView.setRippleDuration(0);
        } else {
            workListHeadHolder.rippleView.setRippleDuration(300);
        }
        final WorkListHeadHolder finalWorkListHeadHolder = workListHeadHolder;

        final int finalImageHeight = saveImageHeight;
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
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        WorkListItemHolder workListItemHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_list_item, parent, false);
            workListItemHolder = new WorkListItemHolder(convertView);
            convertView.setTag(workListItemHolder);
        } else {
            workListItemHolder = (WorkListItemHolder) convertView.getTag();
        }
        final ProductListResponse.WorkListBean.WorkListItemBean bean = works.get(groupPosition).workDetail.get(childPosition);
        int detailSize = works.get(groupPosition).workDetail.size();
        int saveImageHeight = 0;
        final WorkListItemHolder finalWorkListItemHolder = workListItemHolder;
        if (TextUtils.isEmpty(bean.imageUrl)) {
            workListItemHolder.work_list_item_img.setVisibility(View.GONE);
        } else {
            if (bean.width > 0) {
                workListItemHolder.work_list_item_img.setVisibility(View.VISIBLE);
                int imageHeight = (bean.height * screenWidth) / bean.width;
                saveImageHeight = imageHeight;
                if (workListItemHolder.work_list_item_img.getLayoutParams() != null) {
                    workListItemHolder.work_list_item_img.getLayoutParams().height = imageHeight;
                }

                Glide.with(contexts.get()).load(bean.imageUrl).centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(finalWorkListItemHolder.work_list_item_img);
            }
        }
        if (childPosition == detailSize - 1) {
            workListItemHolder.work_list_item_title.setVisibility(View.VISIBLE);
            workListItemHolder.work_list_item_author.setVisibility(View.VISIBLE);
            workListItemHolder.work_list_item_title.setText(bean.workTitle);
            workListItemHolder.author_name.setText(bean.authorName);

            Glide.with(contexts.get()).load(bean.authorHeadImg).centerCrop()
                    .bitmapTransform(new GlideCircleTransform(contexts.get())).into(workListItemHolder.author_image);
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
        } else {
            workListItemHolder.work_list_item_title.setVisibility(View.GONE);
            workListItemHolder.work_list_item_author.setVisibility(View.GONE);
        }
        final int finalSaveImageHeight = saveImageHeight;
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
        return convertView;
    }

    private static class WorkListHeadHolder {
        private View itemView;
        private RippleView rippleView;
        private RelativeLayout layout_source;
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

        private WorkListItemHolder(View rootView) {
            rippleView = (RippleView) rootView.findViewById(R.id.rippleView);
            work_list_item_img = (ImageView) rootView.findViewById(R.id.work_list_item_img);
            work_list_item_title = (TextView) rootView.findViewById(R.id.work_list_item_title);
            work_list_item_author = (RelativeLayout) rootView.findViewById(R.id.work_list_item_author);
            author_image = (ImageView) rootView.findViewById(R.id.author_image);
            author_name = (TextView) rootView.findViewById(R.id.author_name);
            author_more = (ImageView) rootView.findViewById(R.id.author_more);
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
        return true;
    }
}
