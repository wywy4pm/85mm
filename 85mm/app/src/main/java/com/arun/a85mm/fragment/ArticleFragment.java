package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.ArticleListAdapter;
import com.arun.a85mm.bean.ArticleListResponse;
import com.arun.a85mm.refresh.ShootRefreshView;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy on 2017/4/13.
 */

public class ArticleFragment extends BaseFragment {

    private SwipeToLoadLayout swipeToLoadLayout;
    private ShootRefreshView swipe_refresh_header;
    private RecyclerView recyclerView;
    private ArticleListAdapter articleListAdapter;
    private List<ArticleListResponse.ArticleListBean> articles = new ArrayList<>();

    public static ArticleFragment newIntense() {
        ArticleFragment articleFragment = new ArticleFragment();
        return articleFragment;
    }

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        return R.layout.fragment_layout;
    }

    @Override
    protected void initView() {
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoad);
        swipe_refresh_header = (ShootRefreshView) findViewById(R.id.swipe_refresh_header);
        setRefreshView(swipeToLoadLayout, swipe_refresh_header);
        recyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        articleListAdapter = new ArticleListAdapter(getActivity(), articles);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(articleListAdapter);
    }

    @Override
    protected void initData() {

    }
}
