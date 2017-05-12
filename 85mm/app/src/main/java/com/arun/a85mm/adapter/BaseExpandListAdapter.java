package com.arun.a85mm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2017/5/12.
 */

public abstract class BaseExpandListAdapter<P, C> extends BaseExpandableListAdapter {
    /*private WeakReference<Context> contexts;
    private List<P> parentList;
    //private Map<P, List<C>> map;

    public BaseExpandListAdapter(Context context, List<P> parentList) {
        contexts = new WeakReference<>(context);
        this.parentList = parentList;
    }

    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return parentList.get(groupPosition);
    }

    @Override
    public P getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }

    @Override
    public C getChild(int groupPosition, int childPosition) {
        return null;
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
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }*/
}
