package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.AuditListAdapter;
import com.arun.a85mm.bean.AuditInfoBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.helper.ConfigHelper;
import com.arun.a85mm.presenter.AuditPresenter;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.FullyGridLayoutManager;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.view.CommonView4;
import com.arun.a85mm.widget.AutoLineLinearLayout;

import java.util.ArrayList;
import java.util.List;

public class AuditActivity extends BaseActivity implements CommonView4<List<WorkListBean>> {

    private TextView image_right;
    private AutoLineLinearLayout layout_tags;
    private SwipeToLoadLayout swipeToLoad;
    private RecyclerView recyclerView;
    private AuditListAdapter auditListAdapter;
    private List<WorkListBean> works = new ArrayList<>();
    private List<AuditInfoBean.TagItemBean> tags;
    private int auditSortType = 0;//0：按时间，1：按热度
    public int start;
    public String lastWorkId;
    private AuditPresenter auditPresenter;
    private String searchName;
    private boolean isHaveMore = true;

    public static void jumpToAudit(Context context) {
        Intent intent = new Intent(context, AuditActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
        setContentView(R.layout.activity_audit);
        initView();
        initData();
    }

    private void initView() {
        image_right = (TextView) findViewById(R.id.image_right);
        layout_tags = (AutoLineLinearLayout) findViewById(R.id.layout_tags);
        swipeToLoad = (SwipeToLoadLayout) findViewById(R.id.swipeToLoad);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        auditListAdapter = new AuditListAdapter(this, works);
        FullyGridLayoutManager gridLayoutManager = new FullyGridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(auditListAdapter);

        setBack();
        setTitle("审核");
        setRight();
        setRefresh(swipeToLoad);
        setRecyclerViewScrollListener(recyclerView);
    }

    private void setRight() {
        image_right.setVisibility(View.VISIBLE);
        image_right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        image_right.setTextColor(getResources().getColor(R.color.tab_select));
        ((RelativeLayout.LayoutParams) image_right.getLayoutParams()).setMargins(DensityUtil.dp2px(this, 12), 0, DensityUtil.dp2px(this, 12), 0);
        image_right.setText("按时间");
        image_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auditSortType == 0) {
                    image_right.setText("按热度");
                    auditSortType = 1;
                    requestData();
                } else {
                    image_right.setText("按时间");
                    auditSortType = 0;
                    requestData();
                }
            }
        });
    }

    private void initData() {
        tags = ConfigHelper.tags;
        if (tags != null && tags.size() > 0) {
            searchName = tags.get(0).searchName;
            for (int i = 0; i < tags.size(); i++) {
                final TextView tv = new TextView(this);
                tv.setTag(i);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                tv.setTextColor(getResources().getColor(R.color.charcoalgrey));
                tv.setPadding(DensityUtil.dp2px(this, 10), DensityUtil.dp2px(this, 5), DensityUtil.dp2px(this, 10), DensityUtil.dp2px(this, 5));
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundResource(R.drawable.selector_audit_flow_tags);
                final int finalI = i;
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv.setSelected(true);
                        tv.setTextColor(getResources().getColor(R.color.white));
                        resetSelect(tv.getTag());
                        searchName = tags.get(finalI).searchName;
                        requestData();
                    }
                });

                tv.setText(tags.get(i).showName);
                layout_tags.addView(tv);
            }
        }
        auditPresenter = new AuditPresenter(this);
        auditPresenter.attachView(this);
        requestData();
    }

    private void resetSelect(Object selectTag) {
        if (layout_tags.getChildCount() > 0) {
            for (int i = 0; i < layout_tags.getChildCount(); i++) {
                TextView textView = (TextView) layout_tags.getChildAt(i);
                if (textView != null && textView.getTag() != null
                        && !textView.getTag().equals(selectTag)
                        && textView.isSelected()) {
                    textView.setSelected(false);
                    textView.setTextColor(getResources().getColor(R.color.charcoalgrey));
                }
            }
        }
    }

    private void requestData() {
        if (auditPresenter != null) {
            setLoading(true);
            start = 0;
            lastWorkId = "";
            auditPresenter.getAuditWorkList(searchName, auditSortType, start, lastWorkId);
        }
    }

    private void loadMore() {
        if (auditPresenter != null) {
            setLoading(true);
            auditPresenter.getAuditWorkList(searchName, auditSortType, start, lastWorkId);
        }
    }

    @Override
    protected void reloadData() {
        requestData();
    }

    @Override
    public void setLoadMore() {
        if (isHaveMore) {
            loadMore();
        }
    }

    public void setHaveMore(boolean haveMore) {
        isHaveMore = haveMore;
    }

    @Override
    public void refresh(List<WorkListBean> data) {
        if (data != null) {
            works.clear();
            works.addAll(data);
            auditListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshMore(List<WorkListBean> data) {

    }

    @Override
    public void refresh(int type, Object data) {
    }

    @Override
    public void onRefreshComplete() {
        setLoading(false);
        if (swipeToLoad != null) {
            swipeToLoad.setRefreshing(false);
        }
    }
}
