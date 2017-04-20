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
import com.arun.a85mm.utils.NetUtils;
import com.arun.a85mm.view.CommonView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy on 2017/4/13.
 */

public class ArticleFragment extends BaseFragment implements CommonView<ArticleListResponse> {

    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView;
    private ArticleListAdapter articleListAdapter;
    private List<ArticleListResponse.ArticleListBean> articles = new ArrayList<>();
    private ArticleFragmentPresenter articleFragmentPresenter;
    private int currentPageNum;
    private boolean isHaveMore = true;

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
        });

    }

    @Override
    protected void initData() {
        articleFragmentPresenter = new ArticleFragmentPresenter(getActivity());
        articleFragmentPresenter.attachView(this);
        refreshData();
    }

    public void refreshData() {
        setHaveMore(true);
        currentPageNum = 1;
        if (NetUtils.isConnected(getActivity())) {
            hideNetWorkErrorView(recyclerView);
            if (articleFragmentPresenter != null) {
                articleFragmentPresenter.getArticleListData(currentPageNum);
            }
        } else {
            if (swipeToLoadLayout.isRefreshing()) {
                swipeToLoadLayout.setRefreshing(false);
            }
            showNetWorkErrorView(recyclerView);
        }
    }

    private void loadMore() {
        if (articleFragmentPresenter != null) {
            currentPageNum += 1;
            articleFragmentPresenter.getArticleListData(currentPageNum);
        }
    }

    @Override
    public void setLoadMore() {
        if (isHaveMore) {
            loadMore();
        }
    }

    @Override
    public void reloadData() {
        refreshData();
    }

    @Override
    public void refresh(ArticleListResponse data) {
        if (data != null && data.articleList != null && data.articleList.size() > 0) {
            articles.clear();
            articles.addAll(data.articleList);
            articleListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshMore(ArticleListResponse data) {
        if (data != null && data.articleList != null && data.articleList.size() > 0) {
            articles.addAll(data.articleList);
            articleListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefreshComplete() {
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    @Override
    public void onError(String error, String tag) {

    }

    public void setHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
    }
}
