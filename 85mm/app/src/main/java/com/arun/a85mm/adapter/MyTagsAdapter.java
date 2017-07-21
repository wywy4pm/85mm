package com.arun.a85mm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.FragmentCommonActivity;
import com.arun.a85mm.activity.TagsActivity;
import com.arun.a85mm.activity.UpdateUserNameActivity;
import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.fragment.TagWorkFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2017/7/18.
 */

public class MyTagsAdapter extends BaseRecyclerAdapter<UserTagBean> {

    public static final String DATA_TYPE_ITEM = "item";
    public static final String DATA_TYPE_BOTTOM = "bottom";
    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_BOTTOM = 1;
    private boolean isEdit;

    public MyTagsAdapter(Context context, List<UserTagBean> list) {
        super(context, list);
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_tag_item, parent, false);
            return new TagItemHolder(itemView);
        } else if (viewType == VIEW_TYPE_BOTTOM) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_add_tags, parent, false);
            return new TagAddBottomHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TagItemHolder) {
            TagItemHolder tagItemHolder = (TagItemHolder) holder;
            tagItemHolder.setData(contexts.get(), getItem(position), position, isEdit);
        } else if (holder instanceof TagAddBottomHolder) {
            TagAddBottomHolder tagAddBottomHolder = (TagAddBottomHolder) holder;
            tagAddBottomHolder.setData(contexts.get());
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = -1;
        if (getItem(position) != null) {
            if (DATA_TYPE_ITEM.equals(getItem(position).type)) {
                type = VIEW_TYPE_ITEM;
            } else if (DATA_TYPE_BOTTOM.equals(getItem(position).type)) {
                type = VIEW_TYPE_BOTTOM;
            }
        }
        return type;
    }

    public static class TagItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private TextView tag_name;
        private TextView btn_tag_delete;
        private TextView btn_tag_rename;
        private TextView btn_show_hide;
        private ImageView btn_tag_sort;
        private ImageView btn_tag_right;
        private UserTagBean bean;
        private Context context;
        private int position;

        private TagItemHolder(View rootView) {
            super(rootView);
            itemView = rootView;
            tag_name = (TextView) rootView.findViewById(R.id.tag_name);
            btn_tag_delete = (TextView) rootView.findViewById(R.id.btn_tag_delete);
            btn_tag_rename = (TextView) rootView.findViewById(R.id.btn_tag_rename);
            btn_show_hide = (TextView) rootView.findViewById(R.id.btn_show_hide);
            btn_tag_sort = (ImageView) rootView.findViewById(R.id.btn_tag_sort);
            btn_tag_right = (ImageView) rootView.findViewById(R.id.btn_tag_right);

            btn_tag_delete.setOnClickListener(this);
            btn_tag_rename.setOnClickListener(this);
            btn_show_hide.setOnClickListener(this);
            btn_tag_sort.setOnClickListener(this);
            btn_tag_right.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        private void setData(final Context context, final UserTagBean bean, int position, boolean isEdit) {
            this.context = context;
            this.bean = bean;
            this.position = position;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> map = new HashMap<>();
                    map.put(TagWorkFragment.KEY_TAG_NAME, bean.name);
                    FragmentCommonActivity.jumpToFragmentCommonActivity(context, FragmentCommonActivity.FRAGMENT_TAG_WORKS, bean.name, map);
                }
            });

            if (isEdit) {
                setIsEditMode();
            } else {
                setCommonMode();
            }
            btn_show_hide.setText(bean.isShow == 1 ? "隐藏" : "显示");
            tag_name.setText(bean.name);
        }

        private void setIsEditMode() {
            btn_tag_right.setVisibility(View.GONE);
            btn_tag_sort.setVisibility(View.VISIBLE);
            btn_show_hide.setVisibility(View.VISIBLE);
            btn_tag_rename.setVisibility(View.VISIBLE);
            btn_tag_delete.setVisibility(View.VISIBLE);
        }

        private void setCommonMode() {
            btn_tag_right.setVisibility(View.VISIBLE);
            btn_tag_sort.setVisibility(View.GONE);
            btn_show_hide.setVisibility(View.GONE);
            btn_tag_rename.setVisibility(View.GONE);
            btn_tag_delete.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_tag_delete:
                    if (context instanceof TagsActivity) {
                        ((TagsActivity) context).deleteTagData(bean.name);
                    }
                    break;
                case R.id.btn_tag_rename:
                    UpdateUserNameActivity.jumpToAddTag(context, bean, position);
                    break;
                case R.id.btn_show_hide:
                    bean.isShow = bean.isShow == 1 ? 0 : 1;
                    btn_show_hide.setText(bean.isShow == 1 ? "隐藏" : "显示");
                    break;
            }
        }
    }

    private static class TagAddBottomHolder extends RecyclerView.ViewHolder {
        private RelativeLayout layout_add_tag;

        public TagAddBottomHolder(View itemView) {
            super(itemView);
            layout_add_tag = (RelativeLayout) itemView.findViewById(R.id.layout_add_tag);
        }

        private void setData(final Context context) {
            layout_add_tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateUserNameActivity.jumpToAddTag(context, null, 0);
                }
            });
        }
    }

}
