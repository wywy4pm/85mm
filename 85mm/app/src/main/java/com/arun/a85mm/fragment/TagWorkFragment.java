package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.BaseActivity;
import com.arun.a85mm.activity.FragmentCommonActivity;
import com.arun.a85mm.adapter.ProductListAdapter;
import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.event.UpdateProductEvent;
import com.arun.a85mm.helper.DialogHelper;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.listener.OnTagWorkListener;
import com.arun.a85mm.presenter.TagWorkPresenter;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.NetUtils;
import com.arun.a85mm.view.CommonView4;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2017/7/19.
 */

public class TagWorkFragment extends BaseFragment implements OnImageClick, CommonView4<List<WorkListBean>>, OnTagWorkListener {
    private SwipeToLoadLayout swipeToLoadLayout;
    private ExpandableListView expandableListView;
    private ProductListAdapter productListAdapter;
    private List<WorkListBean> workLists = new ArrayList<>();
    private TagWorkPresenter tagWorkPresenter;
    private boolean isHaveMore = true;
    private String lastWorkId;
    private static final String TAG = "TagWorkFragment";
    public static final String KEY_TAG_NAME = "tag_name";
    private ImageView next_group_img;
    private String tagName;

    public static TagWorkFragment newInstance() {
        TagWorkFragment tagWorkFragment = new TagWorkFragment();
        return tagWorkFragment;
    }

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        /*if (eventStatisticsHelper != null) {
            eventStatisticsHelper.recordUserAction(getActivity(), EventConstant.OPEN_LATEST);
        }*/
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
        productListAdapter.setOnTagWorkListener(this);

        setRefresh(swipeToLoadLayout);
        setExpandableListViewCommon(expandableListView, next_group_img, workLists);
        setHideReadTips();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {
        if (getArguments() != null
                && getArguments().containsKey(FragmentCommonActivity.EXTRAS)) {
            Map<String, String> map = (Map<String, String>) getArguments().getSerializable(FragmentCommonActivity.EXTRAS);
            if (map != null && map.containsKey(KEY_TAG_NAME)) {
                tagName = map.get(KEY_TAG_NAME);
            }
        }
        tagWorkPresenter = new TagWorkPresenter(getActivity());
        tagWorkPresenter.attachView(this);
        refreshData();
    }

    public void refreshData() {
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
            if (tagWorkPresenter != null) {
                setLoading(true);
                lastWorkId = "";
                tagWorkPresenter.getTagWorkList(lastWorkId, tagName);
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
        if (tagWorkPresenter != null) {
            tagWorkPresenter.getTagWorkList(lastWorkId, tagName);
        }
    }

    @Override
    public void refresh(List<WorkListBean> data) {
        if (data != null && data.size() > 0) {
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

    @Override
    public void refresh(int type, Object data) {
        if (type == TagWorkPresenter.TYPE_TAG_WORK) {
            if (data instanceof UserTagBean) {
                showTop("打标成功");
            }
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
    public void onCoverClick(String workId, String coverUrl,String authorName) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).saveImageShowTop(workId, coverUrl, authorName);
        }
    }

    @Override
    public void onMoreLinkClick(String workId, String sourceUrl) {
        DialogHelper.showBottom(getActivity(), Constant.TYPE_WORK, sourceUrl, workId, "", eventStatisticsHelper);
    }

    public void setHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
        if (!isHaveMore) {
            setLeftWorkBrowse(EventConstant.WORK_BROWSE_NEWEST, workLists);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setHideState(UpdateProductEvent event) {
        refreshData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (tagWorkPresenter != null) {
            tagWorkPresenter.detachView();
        }
    }

    @Override
    public void onClickMyTag(UserTagBean tagBean, String workId) {
        if (tagWorkPresenter != null) {
            tagWorkPresenter.tagWork(tagBean, workId);
        }
    }

    public void resetUserTag(UserTagBean tagBean) {
        tagBean.tagType = tagBean.tagType == 1 ? 0 : 1;
        productListAdapter.notifyDataSetChanged();
    }
}
