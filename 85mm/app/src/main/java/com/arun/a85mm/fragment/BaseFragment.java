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

    int currentHeight = 0;
    float downY = 0;
    float currentY = 0;
    private boolean isDown;

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

    public void setRefreshView(final SwipeToLoadLayout swipeToLoadLayout, final ShootRefreshView swipe_refresh_header) {
        swipeToLoadLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isDown) {
                            downY = event.getY();
                            isDown = true;
                        }
                        Log.d("TAG", "downY = " + downY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        currentY = event.getY();
                        currentHeight = (int) ((int) (currentY - downY) * 0.5f);
                        swipe_refresh_header.setCurrentHeight(currentHeight);
                        Log.d("TAG", "currentY = " + downY);
                        break;
                }
                return false;
            }
        });
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToLoadLayout.setRefreshing(false);
            }
        });
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
