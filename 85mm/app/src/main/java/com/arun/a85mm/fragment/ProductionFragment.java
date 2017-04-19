package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.andexert.library.RippleView;
import com.arun.a85mm.R;
import com.arun.a85mm.adapter.ProductListAdapter;
import com.arun.a85mm.bean.ProductListResponse;
import com.arun.a85mm.presenter.ProductFragmentPresenter;
import com.arun.a85mm.refresh.OnRefreshListener;
import com.arun.a85mm.refresh.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WY on 2017/4/14.
 */
public class ProductionFragment extends BaseFragment {

    private SwipeToLoadLayout swipeToLoadLayout;
    private ExpandableListView expandableListView;
    private ProductListAdapter productListAdapter;
    private List<ProductListResponse.WorkListBean> workList = new ArrayList<>();
    private ProductFragmentPresenter productFragmentPresenter;
    private boolean isHaveMore = true;
    private String userId;
    private String lastWorkId;

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        return R.layout.layout_producation;
    }

    @Override
    protected void initView() {
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoad);
        expandableListView = (ExpandableListView) findViewById(R.id.swipe_target);
        /*LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(productListAdapter);
        setRecyclerViewScrollListener(recyclerView);*/
        productListAdapter = new ProductListAdapter(getActivity(), workList);
        expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(productListAdapter);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        initdata();
        productListAdapter.notifyDataSetChanged();
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                parent.expandGroup(groupPosition,false);
                return true;
            }
        });
    }

    private void refreshData() {
        //productFragmentPresenter.getProductListData(userId, lastWorkId);
    }

    private void loadMore() {

    }

    @Override
    protected void initData() {
        /*productFragmentPresenter = new ProductFragmentPresenter(getActivity());
        productFragmentPresenter.attachView(this);*/
    }

    private void initdata() {
        List<ProductListResponse.WorkListBean> works = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ProductListResponse.WorkListBean workListBean = new ProductListResponse.WorkListBean();
            workListBean.workId = "123456";
            workListBean.workTitle = "呵呵哈哈哈或或或或或或或";
            workListBean.coverHeight = 900;
            workListBean.coverUrl = "http://img2.fengniao.com/product/157_700x2000/616/ce0x0nKrIpsHI.jpg";
            workListBean.sourceLogo = "http://www.qdaily.com/images/missing_face.png";
            workListBean.createTime = "10小时前";
            workListBean.totalImageNum = i + 1;
            List<ProductListResponse.WorkListBean.WorkListItemBean> workListItems = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                ProductListResponse.WorkListBean.WorkListItemBean bean = new ProductListResponse.WorkListBean.WorkListItemBean();
                bean.imageUrl = "http://img.qdaily.com/uploads/20170406161328XKrjP2zMbCHn4leB.jpg-WebpWebW640";
                if (j == i) {
                    bean.authorHeadImg = "http://www.qdaily.com/images/missing_face.png";
                    bean.authorName = "'arun";
                }
                workListItems.add(bean);
            }
            workListBean.workDetail = workListItems;
            works.add(workListBean);
        }
        workList.addAll(works);
    }

    @Override
    public void reloadData() {

    }

    public void setHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
    }
}
