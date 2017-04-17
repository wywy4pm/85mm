package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.ArticleListAdapter;
import com.arun.a85mm.refresh.OnRefreshListener;
import com.arun.a85mm.refresh.ShootRefreshView;
import com.arun.a85mm.refresh.SwipeToLoadLayout;

/**
 * Created by WY on 2017/4/14.
 */
public class ProductionFragment extends BaseFragment {

    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView;

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        return R.layout.layout_producation;
    }

    @Override
    protected void initView() {
        /*swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoad);
        recyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        articleListAdapter = new ArticleListAdapter(getActivity(), articles);
        recyclerView.setAdapter(articleListAdapter);
        setRecyclerViewScrollListener(recyclerView);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });*/
    }

    @Override
    protected void initData() {

    }

    @Override
    public void reloadData() {

    }
}
