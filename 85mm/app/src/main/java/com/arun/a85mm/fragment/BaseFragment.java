package com.arun.a85mm.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.ActionBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.listener.EventListener;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.DeviceUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * Created by wy on 2017/4/13.
 */

public abstract class BaseFragment extends Fragment implements EventListener {
    protected Activity thisInstance;
    protected View rootView;
    private LayoutInflater inflater;
    private int layoutId;
    private View no_network;
    public boolean isLoading;
    public int screenWidth;
    public String userId;
    public String deviceId;
    public int currentGroupPosition;
    public boolean isSingleExpand;
    public EventStatisticsHelper eventStatisticsHelper;

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

            initCommon();

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

    private void initCommon() {
        screenWidth = DensityUtil.getScreenWidth(getActivity());
        deviceId = DeviceUtils.getMobileIMEI(getActivity());
        eventStatisticsHelper = new EventStatisticsHelper(getActivity());
        userId = SharedPreferencesUtils.getUid(getActivity());
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void showNetWorkErrorView(View view) {
        view.setVisibility(View.GONE);
        no_network = findViewById(R.id.no_network);
        if (no_network != null) {
            no_network.setVisibility(View.VISIBLE);
        }
        if (findViewById(R.id.not_network_btn) != null) {
            findViewById(R.id.not_network_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reloadData();
                }
            });
        }
    }

    public void hideNetWorkErrorView(View view) {
        view.setVisibility(View.VISIBLE);
        no_network = findViewById(R.id.no_network);
        if (no_network != null) {
            no_network.setVisibility(View.GONE);
        }
    }

    public void setRecyclerViewScrollListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到RecyclerView的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                synchronized (BaseFragment.this) {
                    if (lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                        if (!isLoading) {
                            setLoadMore();
                        }
                    }
                }
            }
        });
    }

    public void setExpandableListViewCommon(final ExpandableListView expandableListView, final ImageView next_group_img, final List<WorkListBean> worksList) {
        expandableListView.setFriction((float) (ViewConfiguration.getScrollFriction() * 0.3));
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //分页处理
                if (listView.getCount() > 9) {
                    synchronized (BaseFragment.this) {
                        if (listView.getLastVisiblePosition() >= listView.getCount() - 9) {
                            if (!isLoading) {
                                setLoadMore();
                            }
                        }
                    }
                }

                int currentGroupAllPosition = getCurrentGroupAllPosition(worksList, currentGroupPosition);
                int lastVisiblePosition = listView.getLastVisiblePosition();
                int currentChildCount = 0;
                if (worksList != null && worksList.size() > 0) {
                    if (worksList.get(currentGroupPosition) != null && worksList.get(currentGroupPosition).workDetail != null) {
                        currentChildCount = worksList.get(currentGroupPosition).workDetail.size();
                    }
                    //int currentRangeMin = currentGroupAllPosition;
                    int currentRangeMax = currentGroupAllPosition + currentChildCount;
                    if (isSingleExpand && lastVisiblePosition >= currentGroupAllPosition && currentChildCount > 4) {
                        if (currentChildCount > visibleItemCount && lastVisiblePosition <= currentRangeMax) {
                            next_group_img.setVisibility(View.VISIBLE);
                        } else {
                            if (currentChildCount <= visibleItemCount) {
                                next_group_img.setVisibility(View.VISIBLE);
                            } else {
                                next_group_img.setVisibility(View.GONE);
                                isSingleExpand = false;
                            }
                        }
                    }
                } else {
                    next_group_img.setVisibility(View.GONE);
                    isSingleExpand = false;
                }
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                isSingleExpand = true;
                currentGroupPosition = groupPosition;
                if (worksList.get(groupPosition) != null && worksList.get(groupPosition).workDetail != null && worksList.get(groupPosition).totalImageNum > 5) {
                    if (groupPosition < worksList.size() - 1) {
                        //Log.d(TAG, "currentGroupPosition = " + currentGroupPosition);
                        next_group_img.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        next_group_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentGroupPosition < worksList.size()) {
                    //Log.d(TAG, "currentPosition = " + (currentGroupPosition + 1));
                    int currentPosition = currentGroupPosition + 1;
                    expandableListView.setSelectedGroup(currentPosition);
                }
                next_group_img.setVisibility(View.GONE);
                isSingleExpand = false;
            }
        });
    }

    public int getCurrentGroupAllPosition(List<WorkListBean> worksList, int groupPosition) {
        int currentGroupAllPosition = 0;
        if (worksList != null && worksList.size() > 0) {
            for (int i = 0; i < worksList.size(); i++) {
                if (i < groupPosition) {
                    if (worksList.get(i).isExpand) {
                        currentGroupAllPosition += worksList.get(i).totalImageNum;
                    } else {
                        currentGroupAllPosition += 1;
                    }
                }
            }
        }
        return currentGroupAllPosition;
    }

    public void collapseGroup(ExpandableListView expandableListView, List<WorkListBean> worksList) {
        if (worksList != null && worksList.size() > 0) {
            for (int i = 0; i < worksList.size(); i++) {
                if (expandableListView.isGroupExpanded(i)) {
                    expandableListView.collapseGroup(i);
                }
            }
        }
    }

    public void preLoadChildFirstImage(final List<WorkListBean> workList) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (workList != null && workList.size() > 0) {
                    for (int i = 0; i < workList.size(); i++) {
                        WorkListBean workListBean = workList.get(i);
                        //int coverHeight = (workListBean.coverHeight * screenWidth) / workListBean.coverWidth;
                        Glide.with(getActivity()).load(workListBean.coverUrl).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                        if (workList.get(i) != null && workList.get(i).workDetail != null && workList.get(i).workDetail.size() > 0) {
                            WorkListItemBean bean = workList.get(i).workDetail.get(0);
                            if (bean != null) {
                                if (bean.width > 0) {
                                    //int imageHeight = (bean.height * screenWidth) / bean.width;
                                    Glide.with(getActivity()).load(bean.imageUrl).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                                    Log.d("TAG", "imageUrl = " + bean.imageUrl);
                                }
                            }
                        }
                    }
                }
            }
        });
    }


    @Override
    public void onEvent(List<ActionBean> actionList) {
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.recordUserAction(getActivity(), EventConstant.DEFAULT, actionList);
        }
    }

    public void setLoadMore() {

    }

    public void reloadData() {

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.detachView();
        }
    }
}
