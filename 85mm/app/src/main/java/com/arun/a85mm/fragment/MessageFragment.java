package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.MessageAdapter;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.FullyLinearLayoutManager;
import com.arun.a85mm.view.CommonView4;

/**
 * Created by wy on 2017/6/7.
 */

public class MessageFragment extends BaseFragment implements CommonView4{
    public RecyclerView recyclerView;
    public SwipeToLoadLayout swipeToLoadLayout;
    private MessageAdapter messageAdapter;

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        return R.layout.layout_message;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoad);

        setRefresh(swipeToLoadLayout);
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //messageAdapter = new MessageAdapter(getActivity(),);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void refresh(Object data) {

    }

    @Override
    public void refreshMore(Object data) {

    }

    @Override
    public void refresh(int type, Object data) {

    }
}
