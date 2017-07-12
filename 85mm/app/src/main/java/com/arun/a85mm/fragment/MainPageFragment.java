package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.UserMainAdapter;
import com.arun.a85mm.bean.WorkListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy on 2017/7/12.
 */

public class MainPageFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private UserMainAdapter adapter;
    private List<WorkListBean> workList = new ArrayList<>();
    public static final String KEY_TYPE = "main_page_type";

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
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new UserMainAdapter(getActivity(), workList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {

    }

    public void setData(List<WorkListBean> works) {
        workList.clear();
        workList.addAll(works);
        adapter.notifyDataSetChanged();
    }
}
