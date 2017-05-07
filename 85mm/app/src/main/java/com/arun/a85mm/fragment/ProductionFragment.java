package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.MainActivity;
import com.arun.a85mm.adapter.ProductListAdapter;
import com.arun.a85mm.bean.ProductListResponse;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.helper.DialogHelper;
import com.arun.a85mm.presenter.ProductFragmentPresenter;
import com.arun.a85mm.refresh.OnRefreshListener;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.NetUtils;
import com.arun.a85mm.view.CommonView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WY on 2017/4/14.
 */
public class ProductionFragment extends BaseFragment implements ProductListAdapter.OnImageClick, CommonView<ProductListResponse> {

    private SwipeToLoadLayout swipeToLoadLayout;
    private ExpandableListView expandableListView;
    private ProductListAdapter productListAdapter;
    private List<WorkListBean> workLists = new ArrayList<>();
    private ProductFragmentPresenter productFragmentPresenter;
    private boolean isHaveMore = true;
    private String userId;
    private String lastWorkId;
    private static final String TAG = "ProductionFragment";
    private ImageView next_group_img;
   /* private int currentGroupPosition;
    private boolean isSingleExpand;*/

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        return R.layout.layout_producation;
    }

    @Override
    protected void initView() {
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoad);
        expandableListView = (ExpandableListView) findViewById(R.id.swipe_target);
        next_group_img = (ImageView) findViewById(R.id.next_group_img);
        productListAdapter = new ProductListAdapter(getActivity(), workLists);
        expandableListView.setAdapter(productListAdapter);
        productListAdapter.setOnImageClick(this);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        setExpandableListViewCommon(expandableListView, next_group_img, workLists);
        /*expandableListView.setGroupIndicator(null);
        expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //分页处理
                if (listView.getCount() > 5) {
                    synchronized (ProductionFragment.this) {
                        if (listView.getLastVisiblePosition() == listView.getCount() - 6) {
                            if (!isLoading) {
                                setLoadMore();
                            }
                        }
                    }
                }

                int currentGroupAllPosition = getCurrentGroupAllPosition(currentGroupPosition);
                int lastVisiblePosition = listView.getLastVisiblePosition();
                int currentChildCount = 0;
                if (workLists != null && workLists.size() > 0) {
                    if (workLists.get(currentGroupPosition) != null && workLists.get(currentGroupPosition).workDetail != null) {
                        currentChildCount = workLists.get(currentGroupPosition).workDetail.size();
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
                if (workLists.get(groupPosition) != null && workLists.get(groupPosition).workDetail != null && workLists.get(groupPosition).totalImageNum > 5) {
                    if (groupPosition < workLists.size() - 1) {
                        Log.d(TAG, "currentGroupPosition = " + currentGroupPosition);
                        next_group_img.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        next_group_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentGroupPosition < workLists.size()) {
                    Log.d(TAG, "currentPosition = " + (currentGroupPosition + 1));
                    int currentPosition = currentGroupPosition + 1;
                    expandableListView.setSelectedGroup(currentPosition);
                }
                next_group_img.setVisibility(View.GONE);
                isSingleExpand = false;
            }
        });*/
    }

    @Override
    protected void initData() {
        productFragmentPresenter = new ProductFragmentPresenter(getActivity());
        productFragmentPresenter.attachView(this);
        refreshData();
    }

    private void refreshData() {
        currentGroupPosition = 0;
        isSingleExpand = false;
        collapseGroup(expandableListView, workLists);

        next_group_img.setVisibility(View.GONE);
        setHaveMore(true);
        if (NetUtils.isConnected(getActivity())) {
            hideNetWorkErrorView(expandableListView);
            if (productFragmentPresenter != null) {
                setLoading(true);
                lastWorkId = "";
                productFragmentPresenter.getProductListData(userId, deviceId, lastWorkId);
            }
        } else {
            if (swipeToLoadLayout.isRefreshing()) {
                swipeToLoadLayout.setRefreshing(false);
            }
            showNetWorkErrorView(expandableListView);
        }
    }

    @Override
    public void setLoadMore() {
        if (isHaveMore) {
            loadMore();
        }
    }

    private void loadMore() {
        setLoading(true);
        if (productFragmentPresenter != null) {
            productFragmentPresenter.getProductListData(userId, deviceId, lastWorkId);
        }
    }

    @Override
    public void refresh(ProductListResponse data) {
        if (data != null && data.workList != null && data.workList.size() > 0) {
            workLists.clear();
            formatData(data.workList);
        }
    }

    @Override
    public void refreshMore(ProductListResponse data) {
        if (data != null && data.workList != null && data.workList.size() > 0) {
            formatData(data.workList);
        }
    }

    @Override
    public void onError(String error, String tag) {
        if (swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.setRefreshing(false);
        }
        showNetWorkErrorView(expandableListView);
    }

    private void formatData(List<WorkListBean> workList) {
        for (int i = 0; i < workList.size(); i++) {
            if (workList.get(i) != null && workList.get(i).workDetail != null && workList.get(i).workDetail.size() > 0) {
                if (workList.get(i).workDetail.size() <= 30) {
                    for (int j = 0; j < workList.get(i).workDetail.size(); j++) {
                        if (j == workList.get(i).workDetail.size() - 1) {
                            if (workList.get(i).workDetail.get(j) != null) {
                                workList.get(i).workDetail.get(j).authorHeadImg = workList.get(i).authorHeadImg;
                                workList.get(i).workDetail.get(j).authorName = workList.get(i).authorName;
                                workList.get(i).workDetail.get(j).authorPageUrl = workList.get(i).authorPageUrl;
                                workList.get(i).workDetail.get(j).workTitle = workList.get(i).workTitle;
                                workList.get(i).workDetail.get(j).sourceUrl = workList.get(i).sourceUrl;
                            }
                        }
                    }
                } else if (workList.get(i).workDetail.size() > 30) {
                    for (int j = 0; j < 30; j++) {
                        if (j == 29) {
                            if (workList.get(i).workDetail.get(j) != null) {
                                workList.get(i).workDetail.get(j).authorHeadImg = workList.get(i).authorHeadImg;
                                workList.get(i).workDetail.get(j).authorName = workList.get(i).authorName;
                                workList.get(i).workDetail.get(j).authorPageUrl = workList.get(i).authorPageUrl;
                                workList.get(i).workDetail.get(j).workTitle = workList.get(i).workTitle;
                                workList.get(i).workDetail.get(j).sourceUrl = workList.get(i).sourceUrl;
                            }
                        }
                    }
                }
            } else {
                List<WorkListItemBean> items = new ArrayList<>();
                WorkListItemBean itemBean = new WorkListItemBean();
                itemBean.authorHeadImg = workList.get(i).authorHeadImg;
                itemBean.authorName = workList.get(i).authorName;
                itemBean.authorPageUrl = workList.get(i).authorPageUrl;
                itemBean.workTitle = workList.get(i).workTitle;
                itemBean.sourceUrl = workList.get(i).sourceUrl;
                items.add(itemBean);
                workList.get(i).workDetail = items;
            }
            if (i == workList.size() - 1) {
                lastWorkId = workList.get(i).workId;
            }
        }

        preLoadChildFirstImage(workList);

        workLists.addAll(workList);
        productListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRefreshComplete() {
        //isFirstLoad = false;
        setLoading(false);
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    @Override
    public void reloadData() {
        refreshData();
    }

    @Override
    public void onCountClick(int groupPosition) {
        if (expandableListView != null) {
            if (!expandableListView.isGroupExpanded(groupPosition)) {
                expandableListView.expandGroup(groupPosition);
            }
        }
    }

    @Override
    public void onCoverClick(String coverUrl, int width, int height) {
        /*if (saveImageHelper != null && showTopHandler != null) {
            saveImageHelper.saveImageShowTop(getActivity(), coverUrl, width, height, showTopHandler, ((MainActivity) getActivity()).isSaveImage());
        }*/
        ((MainActivity) getActivity()).saveImageShowTop(coverUrl, width, height);
    }

    /*//获取可视第一个group的position
    public int getFirstVisibleGroup() {
        int firstVis = expandableListView.getFirstVisiblePosition();
        long packedPosition = expandableListView.getExpandableListPosition(firstVis);
        int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
        return groupPosition;
    }

    //获取可视第一个child的position
    public int getFirstVisibleChild() {
        int firstVis = expandableListView.getFirstVisiblePosition();
        long packedPosition = expandableListView.getExpandableListPosition(firstVis);
        int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
        return childPosition;
    }*/

    @Override
    public void onMoreLinkClick(String sourceUrl) {
        //showBottomDialog(sourceUrl);
        DialogHelper.showBottomSourceLink(getActivity(), sourceUrl);
    }

    public void setHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (productFragmentPresenter != null) {
            productFragmentPresenter.detachView();
        }
    }
}
