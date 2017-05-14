package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.BaseActivity;
import com.arun.a85mm.activity.FragmentCommonActivity;
import com.arun.a85mm.activity.MainActivity;
import com.arun.a85mm.adapter.CommunityAdapter;
import com.arun.a85mm.bean.LeftWorksResponse;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.common.Constant;
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
import java.util.Map;

/**
 * Created by WY on 2017/5/7.
 */
public class LeftWorksFragment extends BaseFragment implements OnImageClick, CommonView<LeftWorksResponse> {

    public ExpandableListView expandableListView;
    public SwipeToLoadLayout swipeToLoadLayout;
    public ImageView not_network_image;
    public TextView not_network_text;
    public TextView not_network_btn;
    public ImageView next_group_img;
    private CommunityAdapter communityAdapter;
    private List<WorkListBean> workLists = new ArrayList<>();
    private CommunityPresenter communityPresenter;
    private boolean isHaveMore = true;
    private String userId;
    private int originStart;
    private int start;
    private String date;

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        return R.layout.layout_producation;
    }

    @Override
    protected void initView() {
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.swipe_target);
        swipeToLoadLayout = (SwipeToLoadLayout) rootView.findViewById(R.id.swipeToLoad);
        not_network_image = (ImageView) rootView.findViewById(R.id.not_network_image);
        not_network_text = (TextView) rootView.findViewById(R.id.not_network_text);
        not_network_btn = (TextView) rootView.findViewById(R.id.not_network_btn);
        next_group_img = (ImageView) rootView.findViewById(R.id.next_group_img);
        communityAdapter = new CommunityAdapter(getActivity(), workLists, false);
        communityAdapter.setEventListener(this);
        expandableListView.setAdapter(communityAdapter);
        communityAdapter.setOnImageClick(this);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        setExpandableListViewCommon(expandableListView, next_group_img, workLists);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {
        communityPresenter = new CommunityPresenter(getActivity());
        communityPresenter.attachView(this);
        if (getArguments() != null) {
            Map<String, String> map = (Map<String, String>) getArguments().getSerializable(FragmentCommonActivity.EXTRAS);
            if (map != null) {
                date = map.get(Constant.INTENT_WORKS_LEFT_DATE);
                start = Integer.parseInt(map.get(Constant.INTENT_WORKS_LEFT_START));
                originStart = start;
            }
        }
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
            if (communityPresenter != null) {
                setLoading(true);
                start = originStart;
                communityPresenter.getWorksLeft(userId, deviceId, date, start, true);
            }
        } else {
            if (swipeToLoadLayout.isRefreshing()) {
                swipeToLoadLayout.setRefreshing(false);
            }
            showNetWorkErrorView(expandableListView);
        }
    }

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
        }

        preLoadChildFirstImage(workList);

        workLists.addAll(workList);
        communityAdapter.notifyDataSetChanged();

    }

    @Override
    public void reloadData() {
        refreshData();
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
            communityPresenter.getWorksLeft(userId, deviceId, date, start, false);
        }
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
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).saveImageShowTop(workId, coverUrl, width, height);
        }
    }

    @Override
    public void onMoreLinkClick(String workId,String sourceUrl) {
        DialogHelper.showBottomSourceLink(getActivity(), sourceUrl,workId,eventStatisticsHelper);
    }

    @Override
    public void refresh(LeftWorksResponse data) {
        if (data != null && data.workList != null && data.workList.size() > 0) {
            start = data.start;
            workLists.clear();
            formatData(data.workList);
        }
    }

    @Override
    public void refreshMore(LeftWorksResponse data) {
        if (data != null && data.workList != null && data.workList.size() > 0) {
            start = data.start;
            formatData(data.workList);
        }
    }

    public void setHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
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
}
