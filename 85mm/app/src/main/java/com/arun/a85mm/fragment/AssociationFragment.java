package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.AssociationAdapter;
import com.arun.a85mm.bean.AssociationBean;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.presenter.AssociationPresenter;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.view.CommonView4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy on 2017/6/24.
 */

public class AssociationFragment extends BaseFragment implements CommonView4<CommonApiResponse> {
    private RecyclerView recyclerView;
    private SwipeToLoadLayout swipeToLoad;
    private AssociationPresenter presenter;
    private List<AssociationBean> associationList = new ArrayList<>();
    private AssociationAdapter associationAdapter;
    private int start;
    private int dataType;

    public static AssociationFragment getInstance() {
        AssociationFragment associationFragment = new AssociationFragment();
        return associationFragment;
    }

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        return R.layout.layout_association;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        swipeToLoad = (SwipeToLoadLayout) findViewById(R.id.swipeToLoad);
        associationAdapter = new AssociationAdapter(getActivity(), associationList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(associationAdapter);

        setRefresh(swipeToLoad);
        setRecyclerViewScrollListener(recyclerView);
    }

    @Override
    protected void initData() {
        presenter = new AssociationPresenter(getActivity());
        presenter.attachView(this);
        refreshData();
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
    public void refresh(CommonApiResponse data) {
        start = data.start;
        if (data.body != null && data.body instanceof List) {
            associationList.clear();
            List<AssociationBean> list = (List<AssociationBean>) data.body;
            associationList.addAll(list);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void refreshMore(CommonApiResponse data) {
        start = data.start;
        if (data.body != null) {
            List<AssociationBean> list = (List<AssociationBean>) data.body;
            associationList.addAll(list);
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
}
