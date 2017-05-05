package com.arun.a85mm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.arun.a85mm.bean.WorkListBean;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by wy on 2017/5/5.
 */

public class CommunityAdapter extends BaseExpandableListAdapter {
    private WeakReference<Context> contexts;
    private List<WorkListBean> workList;

    public CommunityAdapter(Context context, List<WorkListBean> workList) {
        contexts = new WeakReference<>(context);
        this.workList = workList;
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
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
