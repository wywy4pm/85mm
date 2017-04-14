package com.arun.a85mm.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.arun.a85mm.refresh.ShootRefreshView;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

/**
 * Created by wy on 2017/4/13.
 */

public abstract class BaseFragment extends Fragment {
    protected Activity thisInstance;
    protected View rootView;
    private LayoutInflater inflater;
    private int layoutId;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            this.inflater = inflater;
            thisInstance = getActivity();
            layoutId = preparedCreate(savedInstanceState);
            int themeId = getTheme();
            if (themeId > 0) {
                final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), themeId);
                inflater = inflater.cloneInContext(contextThemeWrapper);
            }
            rootView = inflater.inflate(layoutId, null);
            initView();
            initData();

        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;

    }


    public int getTheme() {
        return -1;
    }

    public View findViewById(int id) {
        return rootView.findViewById(id);
    }

    protected abstract int preparedCreate(Bundle savedInstanceState);

    protected abstract void initView();

    protected abstract void initData();
}
