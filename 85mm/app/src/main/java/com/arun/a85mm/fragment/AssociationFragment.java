package com.arun.a85mm.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.AddCommunityActivity;
import com.arun.a85mm.activity.LoginActivity;
import com.arun.a85mm.adapter.AssociationAdapter;
import com.arun.a85mm.bean.AssociationBean;
import com.arun.a85mm.bean.AuditItemBean;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.CommunityTagBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.event.UpdateAssociateEvent;
import com.arun.a85mm.helper.MatisseHelper;
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

public class AssociationFragment extends BaseFragment implements CommonView4<List<AssociationBean>>, AssociationAdapter.OnTagClick {
    private RecyclerView recyclerView;
    private SwipeToLoadLayout swipeToLoad;
    private AssociationPresenter presenter;
    private List<AssociationBean> associationList = new ArrayList<>();
    private AssociationAdapter associationAdapter;
    private ImageView btn_add_community;
    public int start;
    private int dataType = 0;
    private String[] tags = new String[]{"精选", "最新", "最热"};
    private int[] types = new int[]{0, 1, 2};

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        associationAdapter = new AssociationAdapter(getActivity(), associationList, createHead());
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
        AssociationBean bean = new AssociationBean();
        bean.type = AssociationAdapter.DATA_TYPE_HEAD;
        associationList.add(bean);
    }

    public void refreshData() {
        start = 0;
        presenter.getCommunityList(start, dataType);
    }

    private void loadMore() {
        presenter.getCommunityList(start, dataType);
    }

    @Override
    public void reloadData() {
        refreshData();
    }

    @Override
    public void setLoadMore() {
        loadMore();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void refresh(List<AssociationBean> data) {
        if (data != null && data.size() > 0) {
            AssociationBean bean = associationList.get(0);
            associationList.clear();
            associationList.add(bean);

            formatData(data);
            associationList.addAll(data);
            associationAdapter.notifyDataSetChanged();
        } else {
            AssociationBean bean = associationList.get(0);
            associationList.clear();
            associationList.add(bean);
            associationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshMore(List<AssociationBean> data) {
        if (data != null && data.size() > 0) {
            formatData(data);
            associationList.addAll(data);
            associationAdapter.notifyDataSetChanged();
        }
    }

    private void formatData(List<AssociationBean> list) {
        for (int i = 0; i < list.size(); i++) {
            AssociationBean bean = list.get(i);
            if (bean != null) {
                bean.type = AssociationAdapter.DATA_TYPE_CONTENT;
            }
        }
    }

    @Override
    public void refresh(int type, Object data) {
    }

    @Override
    public void onRefreshComplete() {
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
        recyclerView.scrollToPosition(0);
        refreshData();
    }
}
