package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.TranslateAnimation;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.ArticleListAdapter;
import com.arun.a85mm.bean.ArticleListResponse;
import com.arun.a85mm.presenter.ArticleFragmentPresenter;
import com.arun.a85mm.refresh.OnRefreshListener;
import com.arun.a85mm.refresh.ShootRefreshView;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.view.CommonView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy on 2017/4/13.
 */

public class ArticleFragment extends BaseFragment implements CommonView<ArticleListResponse>{

    private SwipeToLoadLayout swipeToLoadLayout;
    private ShootRefreshView swipe_refresh_header;
    private RecyclerView recyclerView;
    private ArticleListAdapter articleListAdapter;
    private List<ArticleListResponse.ArticleListBean> articles = new ArrayList<>();
    private ArticleFragmentPresenter articleFragmentPresenter;
    private int currentPageNum = 1;

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
        recyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        articleListAdapter = new ArticleListAdapter(getActivity(), articles);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(articleListAdapter);

        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    @Override
    protected void initData() {
        articleFragmentPresenter = new ArticleFragmentPresenter(getActivity());
        articleFragmentPresenter.attachView(this);
        refreshData();
    }

    private void refreshData() {
        if (articleFragmentPresenter != null) {
            articleFragmentPresenter.getArticleListData(currentPageNum);
        }
    }

    @Override
    public void refresh(ArticleListResponse data) {
        swipeToLoadLayout.setRefreshing(false);
        if (data != null && data.articleList != null && data.articleList.size() > 0) {
            articles.clear();
            articles.addAll(data.articleList);
            articleListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String error, String tag) {
        swipeToLoadLayout.setRefreshing(false);
    }

}
