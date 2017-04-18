package com.arun.a85mm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
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

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        WorkListHeadHolder workListHeadHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_list, parent, false);
            workListHeadHolder = new WorkListHeadHolder(convertView);
            convertView.setTag(workListHeadHolder);
        } else {
            workListHeadHolder = (WorkListHeadHolder) convertView.getTag();
        }
        if (workListHeadHolder.work_list_cover_img.getLayoutParams() != null) {
            workListHeadHolder.work_list_cover_img.getLayoutParams().height = works.get(groupPosition).coverHeight;
            workListHeadHolder.itemView.getLayoutParams().height = works.get(groupPosition).coverHeight;
        }
        Glide.with(contexts.get()).load(works.get(groupPosition).coverUrl).centerCrop().into(workListHeadHolder.work_list_cover_img);
        workListHeadHolder.work_list_cover_count.setText(String.valueOf(works.get(groupPosition).totalImageNum));
        Glide.with(contexts.get()).load(works.get(groupPosition).sourceLogo).centerCrop().into(workListHeadHolder.source_logo);
        workListHeadHolder.create_time.setText(works.get(groupPosition).createTime);
        /*workListHeadHolder.rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
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
        private ImageView work_list_cover_img, source_logo;
        private TextView work_list_cover_count, create_time;

        private WorkListHeadHolder(View itemView) {
            this.itemView = itemView;
            //rippleView = (RippleView) itemView.findViewById(R.id.rippleView);
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
