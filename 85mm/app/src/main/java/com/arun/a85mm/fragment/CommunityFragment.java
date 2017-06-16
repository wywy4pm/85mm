package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.MainActivity;
import com.arun.a85mm.adapter.CommunityAdapter;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.CommunityResponse;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.helper.CommunityListCacheManager;
import com.arun.a85mm.helper.DialogHelper;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.presenter.CommunityPresenter;
import com.arun.a85mm.refresh.OnRefreshListener;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.NetUtils;
import com.arun.a85mm.view.CommonView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WY on 2017/4/14.
 */
public class CommunityFragment extends BaseFragment implements CommonView<List<CommunityResponse.GoodsListBean>>, OnImageClick {
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
    private CommonApiResponse response;

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
        /*swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });*/
        setRefresh(swipeToLoadLayout);
        setExpandableListViewCommon(expandableListView, next_group_img, worksList);
        setHideReadTips();
    }

    @Override
    protected void initData() {
        communityPresenter = new CommunityPresenter(getActivity());
        communityPresenter.attachView(this);
        response = CommunityListCacheManager.getCommonApiResponse();
        refreshData();
    }

    private long requestTime;

    @SuppressWarnings("unchecked")
    private void refreshData() {
        requestTime = System.currentTimeMillis();
        currentGroupPosition = 0;
        isSingleExpand = false;
        collapseGroup(expandableListView, worksList);

        next_group_img.setVisibility(View.GONE);
        setHaveMore(true);
        if (NetUtils.isConnected(getActivity())) {
            hideNetWorkErrorView(expandableListView);
            if (communityPresenter != null) {
                lastWorkDate = "";
                if (response == null) {
                    setLoading(true);
                    communityPresenter.getWorksGoods(lastWorkDate);
                } else {
                    if (response.body != null) {
                        if (response.body instanceof List) {
                            List<CommunityResponse.GoodsListBean> goodsList = (List<CommunityResponse.GoodsListBean>) response.body;
                            worksList.clear();
                            formatData(goodsList);
                            response = null;
                        }
                    }
                }
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
            communityPresenter.getWorksGoods(lastWorkDate);
        }
    }

    @Override
    public void reloadData() {
        refreshData();
    }

    @Override
    public void refresh(List<CommunityResponse.GoodsListBean> data) {
        if (data != null && data.size() > 0) {
            worksList.clear();
            formatData(data);
        }
        Log.d("TAG", "system time refresh = " + (System.currentTimeMillis() - requestTime));
    }

    @Override
    public void refreshMore(List<CommunityResponse.GoodsListBean> data) {
        if (data != null && data.size() > 0) {
            formatData(data);
        }
    }

    private void formatData(List<CommunityResponse.GoodsListBean> goodsList) {
        int currentListAddCount = 0;
        if (goodsList != null && goodsList.size() > 0) {
            for (int k = 0; k < goodsList.size(); k++) {
                CommunityResponse.GoodsListBean goodsListBean = goodsList.get(k);
                if (goodsListBean != null && goodsListBean.workList != null && goodsListBean.workList.size() > 0) {
                    List<WorkListBean> workList = goodsListBean.workList;
                    for (int i = 0; i < workList.size(); i++) {
                        workList.get(i).backgroundColor = RandomColorHelper.getRandomColor();
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
                            workList.get(i).searchDate = goodsListBean.searchDate;
                            workList.get(i).start = goodsListBean.start;
                            workList.get(i).leftWorkNum = goodsListBean.leftWorkNum;
                        }

                        if (workList.get(i) != null && workList.get(i).workDetail != null && workList.get(i).workDetail.size() > 0) {
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
                            lastWorkDate = goodsListBean.searchDate;
                        }
                    }
                    if (NetUtils.isWifi(getActivity())) {
                        preLoadChildFirstImage(workList);
                    }

                    currentListAddCount += workList.size();
                    worksList.addAll(workList);
                } else if (goodsListBean != null) {
                    WorkListBean bean = new WorkListBean();

                    bean.isTitle = true;
                    bean.date = goodsListBean.date;
                    bean.browseNum = goodsListBean.browseNum;
                    bean.workNum = goodsListBean.workNum;
                    bean.allDownloadNum = goodsListBean.downloadNum;

                    bean.isBottom = true;
                    bean.date = goodsListBean.date;
                    bean.searchDate = goodsListBean.searchDate;
                    bean.start = goodsListBean.start;
                    bean.leftWorkNum = goodsListBean.leftWorkNum;

                    lastWorkDate = goodsListBean.searchDate;

                    currentListAddCount += 1;
                    worksList.add(bean);
                }
            }
        }

        setCurrentResponseCount(currentListAddCount);
        communityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefreshComplete() {
        setLoading(false);
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    /*@Override
    public void onError(String error, String tag) {
        super.onError(error, tag);
    }*/

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

    @Override
    public void onMoreLinkClick(String workId, String sourceUrl) {
        DialogHelper.showBottomSourceLink(getActivity(), sourceUrl, workId, eventStatisticsHelper);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (communityPresenter != null) {
            communityPresenter.detachView();
        }
    }

}
