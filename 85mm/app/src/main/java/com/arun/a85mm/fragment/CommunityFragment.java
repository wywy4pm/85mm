package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.MainActivity;
import com.arun.a85mm.adapter.CommunityAdapter;
import com.arun.a85mm.bean.CommunityResponse;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.helper.DialogHelper;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.presenter.CommunityPresenter;
import com.arun.a85mm.refresh.OnRefreshListener;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.NetUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.view.CommonView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WY on 2017/4/14.
 */
public class CommunityFragment extends BaseFragment implements CommonView<CommunityResponse>, OnImageClick {
    public ExpandableListView expandableListView;
    public SwipeToLoadLayout swipeToLoadLayout;
    public ImageView not_network_image;
    public TextView not_network_text;
    public TextView not_network_btn;
    private List<WorkListBean> worksList = new ArrayList<>();
    private CommunityPresenter communityPresenter;
    private boolean isHaveMore = true;
    private String lastWorkDate;
    private static final String TAG = "CommunityFragment";
    private ImageView next_group_img;
    private CommunityAdapter communityAdapter;

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
        communityAdapter = new CommunityAdapter(getActivity(), worksList, true);
        expandableListView.setAdapter(communityAdapter);
        communityAdapter.setOnImageClick(this);
        communityAdapter.setEventListener(this);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        setExpandableListViewCommon(expandableListView, next_group_img, worksList);
    }

    @Override
    protected void initData() {
        communityPresenter = new CommunityPresenter(getActivity());
        communityPresenter.attachView(this);
        refreshData();
    }

    private void refreshData() {
        currentGroupPosition = 0;
        isSingleExpand = false;
        collapseGroup(expandableListView, worksList);

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
        refreshData();
    }

    @Override
    public void refresh(CommunityResponse data) {
        if (data != null && data.goodsList != null && data.goodsList.size() > 0) {
            //SharedPreferencesUtils.saveUid(getActivity(), data.uid);
            worksList.clear();
            formatData(data.goodsList);
        }
    }

    @Override
    public void refreshMore(CommunityResponse data) {
        if (data != null && data.goodsList != null && data.goodsList.size() > 0) {
            formatData(data.goodsList);
        }
    }

    private void formatData(List<CommunityResponse.GoodsListBean> goodsList) {
        if (goodsList != null && goodsList.size() > 0) {
            for (int k = 0; k < goodsList.size(); k++) {
                CommunityResponse.GoodsListBean goodsListBean = goodsList.get(k);
                if (goodsListBean != null && goodsListBean.workList != null && goodsListBean.workList.size() > 0) {
                    List<WorkListBean> workList = goodsListBean.workList;
                    for (int i = 0; i < workList.size(); i++) {
                        if (i == 0) {
                            workList.get(i).isTitle = true;
                            workList.get(i).date = goodsListBean.date;
                            workList.get(i).browseNum = goodsListBean.browseNum;
                            workList.get(i).workNum = goodsListBean.workNum;
                            workList.get(i).allDownloadNum = goodsListBean.downloadNum;
                        }
                        if (i == workList.size() - 1) {
                            workList.get(i).isBottom = true;
                            workList.get(i).date = goodsListBean.date;
                            workList.get(i).start = goodsListBean.start;
                            workList.get(i).leftWorkNum = goodsListBean.leftWorkNum;
                        }

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
                            lastWorkDate = workList.get(i).date;
                        }
                    }
                    preLoadChildFirstImage(workList);
                    worksList.addAll(workList);
                }
            }
        }
        communityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefreshComplete() {
        setLoading(false);
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    @Override
    public void onError(String error, String tag) {
        if (swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.setRefreshing(false);
        }
        showNetWorkErrorView(expandableListView);
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
    public void onCoverClick(String workId,String coverUrl, int width, int height) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).saveImageShowTop(workId,coverUrl, width, height);
        }
    }

    @Override
    public void onMoreLinkClick(String workId,String sourceUrl) {
        DialogHelper.showBottomSourceLink(getActivity(), sourceUrl,workId,eventStatisticsHelper);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (communityPresenter != null) {
            communityPresenter.detachView();
        }
    }
}
