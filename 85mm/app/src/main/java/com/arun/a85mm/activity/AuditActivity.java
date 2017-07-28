package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.AuditListAdapter;
import com.arun.a85mm.bean.AuditBean;
import com.arun.a85mm.bean.AuditInfoBean;
import com.arun.a85mm.bean.AuditItemBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.helper.ConfigHelper;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.presenter.AuditPresenter;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.FullyGridLayoutManager;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.view.CommonView4;

import java.util.ArrayList;
import java.util.List;

public class AuditActivity extends BaseActivity implements CommonView4<AuditBean> {

    private TextView image_right;
    //private AutoLineLinearLayout layout_tags;
    private SwipeToLoadLayout swipeToLoad;
    private RecyclerView recyclerView;
    private AuditListAdapter auditListAdapter;
    private List<AuditItemBean> auditWorks = new ArrayList<>();
    //private List<AuditInfoBean.TagItemBean> tags;
    private int auditSortType = 0;//0：按时间，1：按热度
    public String lastWorkId;
    private AuditPresenter auditPresenter;

    private String searchName;
    private boolean isHaveMore = true;
    private String lastSearchName;
    private int lastSearchType;

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
        setTitleBar();
        setTitle("审核");
        setRight();
        setRefresh(swipeToLoad);
        setRecyclerViewScrollListener(recyclerView);
    }

    public void setTitleBar() {
        RelativeLayout layout_title_right_bar = (RelativeLayout) findViewById(R.id.layout_title_right_bar);
        if (layout_title_right_bar != null) {
            layout_title_right_bar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestData();
                }
            });
        }
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
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.recordUserAction(this, EventConstant.OPEN_AUDIT);
        }

        auditPresenter = new AuditPresenter(this);
        auditPresenter.attachView(this);
        requestData();
    }

    private void addHead(List<AuditInfoBean.TagItemBean> tags) {
        if (tags != null && tags.size() > 0) {
            String selectName = SharedPreferencesUtils.getConfigString(this, SharedPreferencesUtils.KEY_AUDIT_SELECT_TAG);
            if (TextUtils.isEmpty(selectName)) {
                setSearchName(tags.get(0).searchName);
            } else {
                setSearchName(selectName);
            }
        }

        //创建头部
        auditListAdapter.setTags(tags);
        AuditItemBean bean = new AuditItemBean();
        bean.type = AuditListAdapter.TYPE_AUDIT_HEAD;
        auditWorks.add(bean);
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public void requestData() {
        if (auditPresenter != null) {
            if ((!TextUtils.isEmpty(lastSearchName) && !lastSearchName.equals(searchName))
                    || lastSearchType != auditSortType) {
                clearAndAddHead();
            }

            recyclerView.scrollToPosition(0);
            setLoading(true);
            lastWorkId = "";
            isHaveMore = true;
            auditPresenter.getAuditWorkList(searchName, auditSortType, lastWorkId);
            SharedPreferencesUtils.setConfigString(this, SharedPreferencesUtils.KEY_AUDIT_SELECT_TAG, searchName);

            lastSearchName = searchName;
            lastSearchType = auditSortType;
        }
    }

    private void loadMore() {
        if (auditPresenter != null) {
            setLoading(true);
            auditPresenter.getAuditWorkList(searchName, auditSortType, lastWorkId);

            lastSearchName = searchName;
            lastSearchType = auditSortType;
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
                    onEvent(type, worksList.get(i).workId);
                }
            }
        }
    }

    @Override
    public void refresh(AuditBean data) {
        if (data != null) {
            if (data.workList != null) {
                if (auditWorks.size() == 0) {
                    if (data.auditInfo != null) {
                        addHead(data.auditInfo.tags);
                    }
                } else {
                    if ((!(!TextUtils.isEmpty(lastSearchName) && !lastSearchName.equals(searchName)))
                            || lastSearchType == auditSortType) {
                        clearAndAddHead();
                    }
                }
                formatData(data.workList);
                auditListAdapter.notifyDataSetChanged();
            } else {
                auditListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void clearAndAddHead() {
        if (auditWorks.size() > 0) {
            AuditItemBean bean = auditWorks.get(0);
            auditWorks.clear();
            auditWorks.add(bean);
        }
    }

    @Override
    public void refreshMore(AuditBean data) {
        if (data != null) {
            if (data.workList != null) {
                formatData(data.workList);
                auditListAdapter.notifyDataSetChanged();
            }
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
                    bean.workId = work.id;
                    bean.workTitle = work.title;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (auditPresenter != null) {
            auditPresenter.detachView();
        }
    }
}
