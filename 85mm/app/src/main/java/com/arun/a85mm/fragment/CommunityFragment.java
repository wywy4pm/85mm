package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.CommunityResponse;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.handler.ShowTopHandler;
import com.arun.a85mm.helper.SaveImageHelper;
import com.arun.a85mm.presenter.CommunityPresenter;
import com.arun.a85mm.presenter.ProductFragmentPresenter;
import com.arun.a85mm.refresh.OnRefreshListener;
import com.arun.a85mm.refresh.ShootRefreshView;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.NetUtils;
import com.arun.a85mm.view.CommonView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WY on 2017/4/14.
 */
public class CommunityFragment extends BaseFragment implements CommonView<CommunityResponse>,SaveImageHelper.SaveImageCallBack {
    public ExpandableListView expandableListView;
    public SwipeToLoadLayout swipeToLoadLayout;
    public ImageView not_network_image;
    public TextView not_network_text;
    public TextView not_network_btn;
    private List<WorkListBean> goodsLists = new ArrayList<>();
    private CommunityPresenter communityPresenter;
    private boolean isHaveMore = true;
    private String userId;
    private String lastWorkDate;
    private boolean isSaveImage;
    private static final String TAG = "CommunityFragment";
    private ImageView next_group_img;
    private int currentGroupPosition;
    private boolean isSingleExpand;
    private int count = 0;
    private ShowTopHandler showTopHandler;
    private SaveImageHelper saveImageHelper;

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        return R.layout.layout_producation;
    }

    @Override
    protected void initView() {
        expandableListView = (ExpandableListView) findViewById(R.id.swipe_target);
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoad);
        not_network_image = (ImageView) findViewById(R.id.not_network_image);
        not_network_text = (TextView) findViewById(R.id.not_network_text);
        not_network_btn = (TextView) findViewById(R.id.not_network_btn);
        next_group_img = (ImageView) findViewById(R.id.next_group_img);

        expandableListView.setGroupIndicator(null);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                synchronized (CommunityFragment.this) {
                    if (view.getLastVisiblePosition() >= view.getCount() - 1) {
                        if (!isLoading) {
                            setLoadMore();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int currentGroupAllPosition = getCurrentGroupAllPosition(currentGroupPosition);
                int lastVisiblePosition = listView.getLastVisiblePosition();
                int currentChildCount = 0;
                if (goodsLists != null && goodsLists.size() > 0) {
                    if (goodsLists.get(currentGroupPosition) != null && goodsLists.get(currentGroupPosition).workDetail != null) {
                        currentChildCount = goodsLists.get(currentGroupPosition).workDetail.size();
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
                if (goodsLists.get(groupPosition) != null && goodsLists.get(groupPosition).workDetail != null && goodsLists.get(groupPosition).totalImageNum > 5) {
                    if (groupPosition < goodsLists.size() - 1) {
                        Log.d(TAG, "currentGroupPosition = " + currentGroupPosition);
                        next_group_img.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        //productListAdapter.setOnImageClick(this);
        next_group_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentGroupPosition < goodsLists.size()) {
                    Log.d(TAG, "currentPosition = " + (currentGroupPosition + 1));
                    int currentPosition = currentGroupPosition + 1;
                    expandableListView.setSelectedGroup(currentPosition);
                }
                next_group_img.setVisibility(View.GONE);
                isSingleExpand = false;
            }
        });
        showTopHandler = new ShowTopHandler(getActivity());
        saveImageHelper = new SaveImageHelper();
        saveImageHelper.setSaveImageCallBack(this);
    }

    @Override
    protected void initData() {
        communityPresenter = new CommunityPresenter(getActivity());
        communityPresenter.attachView(this);
        refreshData();
    }

    private void refreshData() {
        currentGroupPosition = 0;
        collapseGroup();
        isSingleExpand = false;
        next_group_img.setVisibility(View.GONE);
        setHaveMore(true);
        if (NetUtils.isConnected(getActivity())) {
            hideNetWorkErrorView(expandableListView);
            if (communityPresenter != null) {
                setLoading(true);
                lastWorkDate = "";
                communityPresenter.getWorksGoods(userId, deviceId, lastWorkDate);
            }
        } else {
            if (swipeToLoadLayout.isRefreshing()) {
                swipeToLoadLayout.setRefreshing(false);
            }
            showNetWorkErrorView(expandableListView);
        }
    }

    private int getCurrentGroupAllPosition(int groupPosition) {
        int currentGroupAllPosition = 0;
        if (goodsLists != null && goodsLists.size() > 0) {
            for (int i = 0; i < goodsLists.size(); i++) {
                if (i < groupPosition) {
                    if (goodsLists.get(i).isExpand) {
                        currentGroupAllPosition += goodsLists.get(i).totalImageNum;
                    } else {
                        currentGroupAllPosition += 1;
                    }
                }
            }
        }
        return currentGroupAllPosition;
    }

    private void collapseGroup() {
        if (goodsLists != null && goodsLists.size() > 0) {
            for (int i = 0; i < goodsLists.size(); i++) {
                if (expandableListView.isGroupExpanded(i)) {
                    expandableListView.collapseGroup(i);
                }
            }
        }
    }

    public void setHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
    }

    @Override
    public void setLoadMore() {
        if (isHaveMore) {
            loadMore();
        }
    }

    private void loadMore() {
        setLoading(true);
        if (communityPresenter != null) {
            communityPresenter.getWorksGoods(userId, deviceId, lastWorkDate);
        }
    }

    @Override
    public void reloadData() {

    }

    @Override
    public void refresh(CommunityResponse data) {

    }

    @Override
    public void refreshMore(CommunityResponse data) {

    }

    @Override
    public void onRefreshComplete() {

    }

    @Override
    public void onError(String error, String tag) {

    }

    @Override
    public void setSaveImage(boolean isSaveImage) {

    }
}
