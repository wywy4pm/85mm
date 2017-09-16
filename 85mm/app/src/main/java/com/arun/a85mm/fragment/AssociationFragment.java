package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.AddCommunityActivity;
import com.arun.a85mm.activity.LoginActivity;
import com.arun.a85mm.adapter.AssociationAdapter;
import com.arun.a85mm.bean.CommunityTagBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.event.DeleteCommentEvent;
import com.arun.a85mm.event.UpdateAssociateEvent;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.helper.UserManager;
import com.arun.a85mm.presenter.AssociationPresenter;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.view.CommonView4;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy on 2017/6/24.
 */

public class AssociationFragment extends BaseFragment implements CommonView4<List<WorkListBean>>, AssociationAdapter.OnTagClick {
    private RecyclerView recyclerView;
    private SwipeToLoadLayout swipeToLoad;
    //private RelativeLayout layout_no_data;
    private AssociationPresenter presenter;
    private List<WorkListBean> associationList = new ArrayList<>();
    private AssociationAdapter associationAdapter;
    private ImageView btn_add_community;
    public String lastWorkId;
    private int dataType = 2;
    private String[] tags = new String[]{"精选", "最新", "最热"};
    private int[] types = new int[]{2, 3, 4};
    private boolean isHaveMore = true;

    public static AssociationFragment getInstance() {
        AssociationFragment associationFragment = new AssociationFragment();
        return associationFragment;
    }

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return R.layout.layout_association;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        swipeToLoad = (SwipeToLoadLayout) findViewById(R.id.swipeToLoad);
        btn_add_community = (ImageView) findViewById(R.id.btn_add_community);
        //layout_no_data = (RelativeLayout) findViewById(R.id.layout_no_data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        associationAdapter = new AssociationAdapter(getActivity(), associationList, createHead(), eventStatisticsHelper);
        associationAdapter.setOnTagClick(this);
        recyclerView.setAdapter(associationAdapter);

        setRefresh(swipeToLoad);
        setRecyclerViewScrollListener(recyclerView);
        btn_add_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.getInstance() != null) {
                    if (UserManager.getInstance().isLogin()) {
                        AddCommunityActivity.jumpToAddCommunityForResult(getActivity());
                    } else {
                        LoginActivity.jumpToLoginForResult(getActivity());
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        addHeadTags();
        dataType = SharedPreferencesUtils.getConfigInt(getActivity(), SharedPreferencesUtils.KEY_ASSOCIATION_TAG);
        if (dataType == 0) {
            dataType = 2;
        }
        presenter = new AssociationPresenter(getActivity());
        presenter.attachView(this);
        refreshData();
    }

    private List<CommunityTagBean> createHead() {
        List<CommunityTagBean> tagsList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CommunityTagBean tagBean = new CommunityTagBean();
            tagBean.name = tags[i];
            tagBean.dataType = types[i];
            tagsList.add(tagBean);
        }
        return tagsList;
    }

    private void addHeadTags() {
        WorkListBean bean = new WorkListBean();
        bean.type = AssociationAdapter.DATA_TYPE_HEAD;
        associationList.add(bean);
    }

    public void scrollToTop() {
        if (recyclerView != null) {
            recyclerView.scrollToPosition(0);
        }
    }

    public void refreshData() {
        setLoading(true);
        recyclerView.scrollToPosition(0);
        lastWorkId = "";
        isHaveMore = true;
        presenter.getCommunityList(lastWorkId, dataType);
    }

    private void loadMore() {
        setLoading(true);
        presenter.getCommunityList(lastWorkId, dataType);
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

    @SuppressWarnings("unchecked")
    @Override
    public void refresh(List<WorkListBean> data) {
        if (data != null && data.size() > 0) {
            WorkListBean bean = associationList.get(0);
            associationList.clear();
            associationList.add(bean);

            formatData(data);
            associationList.addAll(data);
            associationAdapter.notifyDataSetChanged();
        } else {
            WorkListBean bean = associationList.get(0);
            associationList.clear();
            associationList.add(bean);
            associationAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void refreshMore(List<WorkListBean> data) {
        if (data != null && data.size() > 0) {
            formatData(data);
            associationList.addAll(data);
            associationAdapter.notifyDataSetChanged();
        }
    }

    private void formatData(List<WorkListBean> list) {
        for (int i = 0; i < list.size(); i++) {
            WorkListBean bean = list.get(i);
            if (bean != null) {
                bean.type = AssociationAdapter.DATA_TYPE_CONTENT;
                bean.backgroundColor = RandomColorHelper.getRandomColor();
            }
            if (i == list.size() - 1 && bean != null) {
                lastWorkId = bean.id;
            }
        }
    }

    @Override
    public void refresh(int type, Object data) {
    }

    @Override
    public void onRefreshComplete() {
        setLoading(false);
        if (swipeToLoad != null) {
            swipeToLoad.setRefreshing(false);
        }
    }

    @Override
    public void click(int dataType) {
        this.dataType = dataType;
        SharedPreferencesUtils.setConfigInt(getActivity(), SharedPreferencesUtils.KEY_ASSOCIATION_TAG, dataType);
        refreshData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (presenter != null) {
            presenter.detachView();
        }
    }

    public void setTagSelect(int position) {
        dataType = position;
        SharedPreferencesUtils.setConfigInt(getActivity(), SharedPreferencesUtils.KEY_ASSOCIATION_TAG, dataType);
        refreshData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateList(UpdateAssociateEvent event) {
        refreshData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteComment(DeleteCommentEvent event) {
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.recordUserAction(getActivity(), EventConstant.ASSOCIATION_COMMENT_DELETE, event.commentId);
        }
    }

    public void refreshComments(String commentId) {
        for (int i = 0; i < associationList.size(); i++) {
            if (associationList.get(i) != null && associationList.get(i).commentList != null) {
                for (int j = 0; j < associationList.get(i).commentList.size(); j++) {
                    String id = String.valueOf(associationList.get(i).commentList.get(j).id);
                    if (!TextUtils.isEmpty(commentId) && commentId.equals(id)) {
                        associationList.get(i).commentList.remove(j);
                        associationAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }

    public void setHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
    }
}
