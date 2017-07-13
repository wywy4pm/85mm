package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.UserMainAdapter;
import com.arun.a85mm.bean.UserMainPageBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.presenter.UserMainPagePresenter;
import com.arun.a85mm.view.CommonView4;
import com.arun.a85mm.widget.ScrollableHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy on 2017/7/12.
 */

public class MainPageFragment extends BaseFragment implements CommonView4<UserMainPageBean> {

    private RecyclerView recyclerView;
    private RelativeLayout layout_no_data;
    private UserMainAdapter adapter;
    private List<WorkListBean> workList = new ArrayList<>();
    public static final String KEY_TYPE = "main_page_type";
    private int pageType;
    private UserMainPagePresenter presenter;
    private String lastWorkId;
    private String authorId;
    private boolean isHaveMore;

    public static MainPageFragment getInstance(int type) {
        MainPageFragment mainPageFragment = new MainPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        mainPageFragment.setArguments(bundle);
        return mainPageFragment;
    }

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        return R.layout.layout_main_page;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layout_no_data = (RelativeLayout) findViewById(R.id.layout_no_data);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new UserMainAdapter(getActivity(), workList);
        recyclerView.setAdapter(adapter);

        setRecyclerViewScrollListener(recyclerView);
    }

    @Override
    protected void initData() {
        if (getArguments() != null && getArguments().containsKey(KEY_TYPE)) {
            pageType = getArguments().getInt(KEY_TYPE);
        }
        presenter = new UserMainPagePresenter(getActivity());
        presenter.attachView(this);
    }


    public void setData(List<WorkListBean> works) {
        if (works != null && works.size() > 0) {
            setHaveData();
            formatData(works);
            workList.clear();
            workList.addAll(works);
            adapter.notifyDataSetChanged();
        } else {
            setNoData();
        }
    }

    private void formatData(List<WorkListBean> workList) {
        if (workList != null && workList.size() > 0) {
            for (int i = 0; i < workList.size(); i++) {
                if (workList.get(i) != null) {
                    workList.get(i).backgroundColor = RandomColorHelper.getRandomColor();
                    if (i == workList.size() - 1) {
                        lastWorkId = workList.get(i).workId;
                    }
                }
            }
        }
    }

    private void setHaveData() {
        layout_no_data.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void setNoData() {
        layout_no_data.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    @Override
    public void reloadData() {
        super.reloadData();
    }

    @Override
    public void setLoadMore() {
        if (isHaveMore) {
            if (presenter != null) {
                presenter.getMoreMainPage(authorId, lastWorkId, pageType);
            }
        }
    }

    @Override
    public void refresh(UserMainPageBean data) {
        if (data != null && data.workList != null && data.workList.size() > 0) {
            formatData(data.workList);
            workList.addAll(data.workList);
            adapter.notifyDataSetChanged();
        }
    }

    public void setHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
    }

    @Override
    public void refreshMore(UserMainPageBean data) {
    }

    @Override
    public void refresh(int type, Object data) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
