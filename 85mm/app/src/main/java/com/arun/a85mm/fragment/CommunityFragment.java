package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
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
import com.arun.a85mm.presenter.CommunityPresenter;
import com.arun.a85mm.refresh.OnRefreshListener;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.NetUtils;
import com.arun.a85mm.view.CommonView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WY on 2017/4/14.
 */
public class CommunityFragment extends BaseFragment implements CommonView<CommunityResponse>, CommunityAdapter.OnImageClick {
    public ExpandableListView expandableListView;
    public SwipeToLoadLayout swipeToLoadLayout;
    public ImageView not_network_image;
    public TextView not_network_text;
    public TextView not_network_btn;
    private List<WorkListBean> worksList = new ArrayList<>();
    private CommunityPresenter communityPresenter;
    private boolean isHaveMore = true;
    private String userId;
    private String lastWorkDate;
    //private boolean isSaveImage;
    private static final String TAG = "CommunityFragment";
    private ImageView next_group_img;
    private int currentGroupPosition;
    private boolean isSingleExpand;
    private int count = 0;
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
        communityAdapter = new CommunityAdapter(getActivity(), worksList);
        expandableListView.setAdapter(communityAdapter);
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

            }

            @Override
            public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //分页处理
                if (listView.getCount() > 5) {
                    synchronized (CommunityFragment.this) {
                        if (listView.getLastVisiblePosition() >= listView.getCount() - 6) {
                            if (!isLoading) {
                                setLoadMore();
                            }
                        }
                    }

                }

                int currentGroupAllPosition = getCurrentGroupAllPosition(currentGroupPosition);
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
                        Log.d(TAG, "currentGroupPosition = " + currentGroupPosition);
                        next_group_img.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        communityAdapter.setOnImageClick(this);
        next_group_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentGroupPosition < worksList.size()) {
                    Log.d(TAG, "currentPosition = " + (currentGroupPosition + 1));
                    int currentPosition = currentGroupPosition + 1;
                    expandableListView.setSelectedGroup(currentPosition);
                }
                next_group_img.setVisibility(View.GONE);
                isSingleExpand = false;
            }
        });
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

    private void collapseGroup() {
        if (worksList != null && worksList.size() > 0) {
            for (int i = 0; i < worksList.size(); i++) {
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
        refreshData();
    }

    @Override
    public void refresh(CommunityResponse data) {
        if (data != null && data.goodsList != null && data.goodsList.size() > 0) {
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
                            workList.get(i).downloadNum = goodsListBean.downloadNum;
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

    private void preLoadChildFirstImage(final List<WorkListBean> workList) {
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
    public void onRefreshComplete() {
        setLoading(false);
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    @Override
    public void onError(String error, String tag) {

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
            saveImageHelper.saveImageShowTop(getActivity(), coverUrl, width, height, showTopHandler, isSaveImage);
        }*/
        ((MainActivity) getActivity()).saveImageShowTop(coverUrl, width, height);
    }

    @Override
    public void onMoreLinkClick(String sourceUrl) {
        DialogHelper.showBottomSourceLink(getActivity(), sourceUrl);
    }

}
