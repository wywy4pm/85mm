package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.BaseActivity;
import com.arun.a85mm.activity.FragmentCommonActivity;
import com.arun.a85mm.adapter.CommunityAdapter;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.helper.DialogHelper;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.listener.OnTagWorkListener;
import com.arun.a85mm.presenter.CommunityPresenter;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.NetUtils;
import com.arun.a85mm.view.CommonView;
import com.arun.a85mm.view.CommonView4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WY on 2017/5/7.
 */
public class LeftWorksFragment extends BaseFragment implements OnImageClick, CommonView4<List<WorkListBean>>, OnTagWorkListener {

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
    private String originLastWorkId;
    private String lastWorkId;
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
        communityAdapter.setOnTagWorkListener(this);

        setRefresh(swipeToLoadLayout);
        setExpandableListViewCommon(expandableListView, next_group_img, workLists);
        setHideReadTips();
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
                lastWorkId = map.get(Constant.INTENT_WORKS_LEFT_START);
                originLastWorkId = lastWorkId;
            }
        }
        refreshData();
    }

    private void refreshData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                expandableListView.setSelectedGroup(0);
            }
        }, 50);

        currentGroupPosition = 0;
        isSingleExpand = false;
        collapseGroup(expandableListView, workLists);

        next_group_img.setVisibility(View.GONE);
        setHaveMore(true);
        if (NetUtils.isConnected(getActivity())) {
            hideNetWorkErrorView(expandableListView);
            if (communityPresenter != null) {
                setLoading(true);
                lastWorkId = originLastWorkId;
                communityPresenter.getWorksLeft(date, lastWorkId, true);
            }
        } else {
            if (swipeToLoadLayout.isRefreshing()) {
                swipeToLoadLayout.setRefreshing(false);
            }
            showNetWorkErrorView(expandableListView);
        }
    }

    private void formatData(List<WorkListBean> workList) {
        setCurrentResponseCount(workList.size());
        for (int i = 0; i < workList.size(); i++) {
            if (workList.get(i) != null && workList.get(i).imageList != null && workList.get(i).imageList.size() > 0) {
                workList.get(i).backgroundColor = RandomColorHelper.getRandomColor();
                for (int j = 0; j < workList.get(i).imageList.size(); j++) {
                    workList.get(i).imageList.get(j).backgroundColor = RandomColorHelper.getRandomColor();
                    if (j == workList.get(i).imageList.size() - 1) {
                        if (workList.get(i).imageList.get(j) != null) {
                            workList.get(i).imageList.get(j).authorHeadImg = workList.get(i).authorHeadImg;
                            workList.get(i).imageList.get(j).authorName = workList.get(i).authorName;
                            workList.get(i).imageList.get(j).authorPageUrl = workList.get(i).authorPageUrl;
                            workList.get(i).imageList.get(j).workTitle = workList.get(i).title;
                            workList.get(i).imageList.get(j).description = workList.get(i).description;
                            workList.get(i).imageList.get(j).sourceUrl = workList.get(i).sourceUrl;
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
                itemBean.workTitle = workList.get(i).title;
                itemBean.description = workList.get(i).description;
                itemBean.sourceUrl = workList.get(i).sourceUrl;
                items.add(itemBean);
                workList.get(i).imageList = items;
            }

            if (i == workList.size() - 1) {
                lastWorkId = workList.get(i).id;
            }
        }
        if (NetUtils.isWifi(getActivity())) {
            preLoadChildFirstImage(workList);
        }

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
            communityPresenter.getWorksLeft(date, lastWorkId, false);
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
    public void onCoverClick(String workId, String coverUrl, String authorName) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).saveImageShowTop(workId, coverUrl, authorName);
        }
    }

    @Override
    public void onMoreLinkClick(String workId, String sourceUrl, String authorUid) {
        DialogHelper.showBottom(getActivity(), Constant.TYPE_WORK, sourceUrl, workId, authorUid, eventStatisticsHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void refresh(List<WorkListBean> data) {
        if (data != null && data.size() > 0) {
            workLists.clear();
            formatData(data);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void refreshMore(List<WorkListBean> data) {
        if (data != null && data.size() > 0) {
            formatData(data);
        }
    }

    @Override
    public void refresh(int type, Object data) {
        if (type == CommunityPresenter.TYPE_TAG_WORK) {
            if (data instanceof UserTagBean) {
                showTop("打标成功");
            }
        }
    }

    public void setHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
        if (!isHaveMore) {
            setLeftWorkBrowse(EventConstant.WORK_BROWSE_ONEDAY, workLists);
        }
    }

    @Override
    public void onRefreshComplete() {
        setLoading(false);
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (communityPresenter != null) {
            communityPresenter.detachView();
        }
    }

    @Override
    public void onClickMyTag(UserTagBean tagBean, String workId) {
        if (communityPresenter != null) {
            communityPresenter.tagWork(tagBean, workId);
        }
    }

    @Override
    public void onUserAward(String workId) {

    }

    public void resetUserTag(UserTagBean tagBean) {
        tagBean.tagType = tagBean.tagType == 1 ? 0 : 1;
        communityAdapter.notifyDataSetChanged();
    }
}
