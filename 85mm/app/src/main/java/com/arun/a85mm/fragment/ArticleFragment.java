package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.ArticleListAdapter;
import com.arun.a85mm.bean.ArticleListBean;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.presenter.ArticleFragmentPresenter;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.NetUtils;
import com.arun.a85mm.view.CommonView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy on 2017/4/13.
 */

public class ArticleFragment extends BaseFragment implements CommonView<List<ArticleListBean>> {

    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView;
    private ArticleListAdapter articleListAdapter;
    private List<ArticleListBean> articles = new ArrayList<>();
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
    public void refresh(List<ArticleListBean> data) {
        if (data != null && data.size() > 0) {
            articles.clear();
            formatData(data);
            articles.addAll(data);
            articleListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshMore(List<ArticleListBean> data) {
        if (data != null && data.size() > 0) {
            formatData(data);
            articles.addAll(data);
            articleListAdapter.notifyDataSetChanged();
        }
    }

    private void formatData(List<ArticleListBean> data) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).backgroundColor = RandomColorHelper.getRandomColor();
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
        if (!isHaveMore) {
            setLeftArticleBrowse(EventConstant.ARTICLE_BROWSE, articles);
        }
    }

    public void setLeftArticleBrowse(int type, List<ArticleListBean> articleList) {
        if (articleList.size() >= 4) {
            for (int i = articleList.size() - 1; i >= articleList.size() - 4; i--) {
                if (articleList.get(i) != null) {
                    onEvent(EventStatisticsHelper.createOneActionList(type, articleList.get(i).id, ""));
                }
            }
        }
    }

}
