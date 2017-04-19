package com.arun.a85mm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.arun.a85mm.R;
import com.arun.a85mm.bean.ProductListResponse;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by wy on 2017/4/18.
 */

public class ProductListAdapter extends BaseExpandableListAdapter {
    private WeakReference<Context> contexts;
    private List<ProductListResponse.WorkListBean> works;

    public ProductListAdapter(Context context, List<ProductListResponse.WorkListBean> works) {
        contexts = new WeakReference<>(context);
        this.works = works;
    }

    public OnImageClick onImageClick;

    public interface OnImageClick {
        void onCountClick(int groupPosition);

        void onCoverClick(String coverUrl, int groupPosition);
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
        if (!bean.isExpand) {
            headHolder.work_list_cover_count.setVisibility(View.VISIBLE);
            headHolder.layout_source.setVisibility(View.VISIBLE);
        } else {
            headHolder.work_list_cover_count.setVisibility(View.GONE);
            headHolder.layout_source.setVisibility(View.GONE);
        }
        if (headHolder.work_list_cover_img.getLayoutParams() != null) {
            headHolder.work_list_cover_img.getLayoutParams().height = bean.coverHeight;
            headHolder.itemView.getLayoutParams().height = bean.coverHeight;
        }
        Glide.with(contexts.get()).load(bean.coverUrl).centerCrop().into(headHolder.work_list_cover_img);
        headHolder.work_list_cover_count.setText(String.valueOf(bean.totalImageNum));
        Glide.with(contexts.get()).load(bean.sourceLogo).centerCrop().into(headHolder.source_logo);
        headHolder.create_time.setText(bean.createTime);

        headHolder.work_list_cover_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headHolder.work_list_cover_count.setVisibility(View.GONE);
                headHolder.layout_source.setVisibility(View.GONE);
                bean.isExpand = true;
                if (onImageClick != null) {
                    onImageClick.onCountClick(groupPosition);
                }
            }
        });
        workListHeadHolder.rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onImageClick != null) {
                    onImageClick.onCoverClick(bean.coverUrl,groupPosition);
                }
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
        ProductListResponse.WorkListBean.WorkListItemBean bean = works.get(groupPosition).workDetail.get(childPosition);
        Glide.with(contexts.get()).load(bean.imageUrl).centerCrop().into(workListItemHolder.work_list_item_img);
        workListItemHolder.work_list_item_title.setText(bean.workTitle);
        workListItemHolder.author_name.setText(bean.authorName);
        Glide.with(contexts.get()).load(bean.authorHeadImg).centerCrop()
                .bitmapTransform(new GlideCircleTransform(contexts.get())).into(workListItemHolder.author_image);
        workListItemHolder.author_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    public static class WorkListItemHolder {
        public ImageView work_list_item_img;
        public TextView work_list_item_title;
        public ImageView author_image;
        public TextView author_name;
        public ImageView author_more;

        public WorkListItemHolder(View rootView) {
            work_list_item_img = (ImageView) rootView.findViewById(R.id.work_list_item_img);
            work_list_item_title = (TextView) rootView.findViewById(R.id.work_list_item_title);
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
