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
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.helper.DialogHelper;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.listener.OnImageClick;
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
public class ProductionFragment extends BaseFragment implements OnImageClick, CommonView<List<WorkListBean>> {

    private SwipeToLoadLayout swipeToLoadLayout;
    private ExpandableListView expandableListView;
    private ProductListAdapter productListAdapter;
    private List<WorkListBean> workLists = new ArrayList<>();
    private ProductFragmentPresenter productFragmentPresenter;
    private boolean isHaveMore = true;
    private String lastWorkId;
    private static final String TAG = "ProductionFragment";
    private ImageView next_group_img;

    public static ProductionFragment newInstance() {
        ProductionFragment productionFragment = new ProductionFragment();
        return productionFragment;
    }

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
        productListAdapter.setEventListener(this);
        /*swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });*/
        setRefresh(swipeToLoadLayout);
        setExpandableListViewCommon(expandableListView, next_group_img, workLists);
        setHideReadTips();
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
                productFragmentPresenter.getProductListData(lastWorkId);
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
            productFragmentPresenter.getProductListData(lastWorkId);
        }
    }

    @Override
    public void refresh(List<WorkListBean> data) {
        if (data != null && data.size() > 0) {
            //SharedPreferencesUtils.saveUid(getActivity(), data.uid);
            workLists.clear();
            formatData(data);
        }
    }

    @Override
    public void refreshMore(List<WorkListBean> data) {
        if (data != null && data.size() > 0) {
            formatData(data);
        }
    }

    /*@Override
    public void onError(String error, String tag) {
        showNetWorkErrorView(expandableListView);
    }*/

    private void formatData(List<WorkListBean> workList) {
        for (int i = 0; i < workList.size(); i++) {
            if (workList.get(i) != null && workList.get(i).workDetail != null && workList.get(i).workDetail.size() > 0) {
                workList.get(i).backgroundColor = RandomColorHelper.getRandomColor();
                if (workList.get(i).workDetail.size() <= 30) {
                    for (int j = 0; j < workList.get(i).workDetail.size(); j++) {
                        workList.get(i).workDetail.get(j).backgroundColor = RandomColorHelper.getRandomColor();
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
                        workList.get(i).workDetail.get(j).backgroundColor = RandomColorHelper.getRandomColor();
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
                if (workList.get(i) != null) {
                    workList.get(i).backgroundColor = RandomColorHelper.getRandomColor();
                }
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
        if (NetUtils.isWifi(getActivity())) {
            preLoadChildFirstImage(workList);
        }

        workLists.addAll(workList);
        productListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRefreshComplete() {
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
    public void onCoverClick(String workId, String coverUrl, int width, int height) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).saveImageShowTop(workId, coverUrl, width, height);
        }
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
    public void onMoreLinkClick(String workId, String sourceUrl) {
        DialogHelper.showBottomSourceLink(getActivity(), sourceUrl, workId, eventStatisticsHelper);
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
