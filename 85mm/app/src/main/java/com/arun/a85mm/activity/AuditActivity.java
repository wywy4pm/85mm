package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.AuditListAdapter;
import com.arun.a85mm.bean.ActionBean;
import com.arun.a85mm.bean.AuditInfoBean;
import com.arun.a85mm.bean.AuditItemBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.helper.ConfigHelper;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.listener.EventListener;
import com.arun.a85mm.presenter.AuditPresenter;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.FullyGridLayoutManager;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.view.CommonView4;
import com.arun.a85mm.widget.AutoLineLinearLayout;
import com.arun.a85mm.widget.NonScrollView;

import java.util.ArrayList;
import java.util.List;

public class AuditActivity extends BaseActivity implements CommonView4<List<WorkListBean>> {

    private TextView image_right;
    //private AutoLineLinearLayout layout_tags;
    private SwipeToLoadLayout swipeToLoad;
    private RecyclerView recyclerView;
    private AuditListAdapter auditListAdapter;
    private List<AuditItemBean> auditWorks = new ArrayList<>();
    //private List<AuditInfoBean.TagItemBean> tags;
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
        swipeToLoad = (SwipeToLoadLayout) findViewById(R.id.swipeToLoad);
        recyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        auditListAdapter = new AuditListAdapter(this, auditWorks);
        auditListAdapter.setEventListener(this);
        FullyGridLayoutManager gridLayoutManager = new FullyGridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
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
        if (SharedPreferencesUtils.getConfigInt(this, SharedPreferencesUtils.KEY_AUDIT_SELECT_SORT) == 0) {
            image_right.setText("按时间");
            auditSortType = 0;
        } else {
            image_right.setText("按热度");
            auditSortType = 1;
        }

        image_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auditSortType == 0) {
                    image_right.setText("按热度");
                    auditSortType = 1;
                    SharedPreferencesUtils.setConfigInt(AuditActivity.this, SharedPreferencesUtils.KEY_AUDIT_SELECT_SORT, auditSortType);
                    requestData();
                } else {
                    image_right.setText("按时间");
                    auditSortType = 0;
                    SharedPreferencesUtils.setConfigInt(AuditActivity.this, SharedPreferencesUtils.KEY_AUDIT_SELECT_SORT, auditSortType);
                    requestData();
                }
            }
        });
    }

    private void initData() {
        /*tags = ConfigHelper.tags;
        if (tags != null && tags.size() > 0) {
            String selectName = SharedPreferencesUtils.getConfigString(this, SharedPreferencesUtils.KEY_AUDIT_SELECT_TAG);
            if (TextUtils.isEmpty(selectName)) {
                searchName = tags.get(0).searchName;
            } else {
                searchName = selectName;
            }

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

                if ((!TextUtils.isEmpty(selectName) && selectName.equals(tags.get(i).searchName))
                        || (TextUtils.isEmpty(selectName) && i == 0)) {
                    tv.setSelected(true);
                    tv.setTextColor(getResources().getColor(R.color.white));
                }
                layout_tags.addView(tv);
            }
        }*/
        List<AuditInfoBean.TagItemBean> tags = ConfigHelper.tags;
        if (tags != null && tags.size() > 0) {
            String selectName = SharedPreferencesUtils.getConfigString(this, SharedPreferencesUtils.KEY_AUDIT_SELECT_TAG);
            if (TextUtils.isEmpty(selectName)) {
                setSearchName(tags.get(0).searchName);
            } else {
                setSearchName(selectName);
            }
        }

        //创建头部
        AuditItemBean bean = new AuditItemBean();
        bean.type = AuditListAdapter.TYPE_AUDIT_HEAD;
        auditWorks.add(bean);

        auditPresenter = new AuditPresenter(this);
        auditPresenter.attachView(this);
        requestData();
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public void requestData() {
        if (auditPresenter != null) {
            setLoading(true);
            start = 0;
            lastWorkId = "";
            isHaveMore = true;
            auditPresenter.getAuditWorkList(searchName, auditSortType, start, lastWorkId);

            SharedPreferencesUtils.setConfigString(this, SharedPreferencesUtils.KEY_AUDIT_SELECT_TAG, searchName);
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
        if (!haveMore) {
            setLeftWorkBrowse(EventConstant.WORK_BROWSE_AUDIT, auditWorks);
        }
    }

    public void setLeftWorkBrowse(int type, List<AuditItemBean> worksList) {
        if (worksList.size() >= 6) {
            for (int i = worksList.size() - 1; i >= worksList.size() - 6; i--) {
                if (worksList.get(i) != null) {
                    onEvent(EventStatisticsHelper.createOneActionList(type, worksList.get(i).workId, ""));
                }
            }
        }
    }

    @Override
    public void refresh(List<WorkListBean> data) {
        if (data != null) {
            AuditItemBean bean = auditWorks.get(0);
            auditWorks.clear();
            auditWorks.add(bean);
            formatData(data);
            auditListAdapter.notifyDataSetChanged();
        } else {
            AuditItemBean bean = auditWorks.get(0);
            auditWorks.clear();
            auditWorks.add(bean);
            auditListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshMore(List<WorkListBean> data) {
        if (data != null) {
            formatData(data);
            auditListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refresh(int type, Object data) {
    }

    private void formatData(List<WorkListBean> data) {
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i) != null) {
                    WorkListBean work = data.get(i);
                    AuditItemBean bean = new AuditItemBean();
                    bean.type = AuditListAdapter.TYPE_AUDIT_LIST;
                    bean.coverUrl = work.coverUrl;
                    bean.backgroundColor = RandomColorHelper.getRandomColor();
                    bean.totalImageNum = work.totalImageNum;
                    bean.workId = work.workId;
                    bean.workTitle = work.workTitle;
                    auditWorks.add(bean);
                }
            }
        }
    }

    @Override
    public void onRefreshComplete() {
        setLoading(false);
        if (swipeToLoad != null) {
            swipeToLoad.setRefreshing(false);
        }
    }

}
