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
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.view.CommonView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy on 2017/4/13.
 */

public class ArticleFragment extends BaseFragment implements CommonView<List<ArticleListResponse.ArticleListBean>> {

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
        articleListAdapter.setEventListener(this);
        recyclerView.setAdapter(articleListAdapter);
        setRecyclerViewScrollListener(recyclerView);
        /*swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });*/
        setRefresh(swipeToLoadLayout);

    }

    @Override
    protected void initData() {
        articleFragmentPresenter = new ArticleFragmentPresenter(getActivity());
        articleFragmentPresenter.attachView(this);
        refreshData();
    }

    public void refreshData() {
        recyclerView.smoothScrollToPosition(0);
        setHaveMore(true);
        currentPageNum = 1;
        if (NetUtils.isConnected(getActivity())) {
            hideNetWorkErrorView(recyclerView);
            if (articleFragmentPresenter != null) {
                setLoading(true);
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
            setLoading(true);
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
    public void refresh(List<ArticleListResponse.ArticleListBean> data) {
        if (data != null && data.size() > 0) {
            //SharedPreferencesUtils.saveUid(getActivity(), data.uid);
            articles.clear();
            articles.addAll(data);
            articleListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshMore(List<ArticleListResponse.ArticleListBean> data) {
        if (data != null && data.size() > 0) {
            articles.addAll(data);
            articleListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefreshComplete() {
        setLoading(false);
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    public void setHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
    }

}
