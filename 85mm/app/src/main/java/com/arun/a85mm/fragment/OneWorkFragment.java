package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.BaseActivity;
import com.arun.a85mm.activity.FragmentCommonActivity;
import com.arun.a85mm.adapter.OneWorkAdapter;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.helper.DialogHelper;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.helper.UrlJumpHelper;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.presenter.OneWorkPresenter;
import com.arun.a85mm.view.CommonView3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2017/6/5.
 */

public class OneWorkFragment extends BaseFragment implements CommonView3, OnImageClick, View.OnClickListener {
    private OneWorkPresenter oneWorkPresenter;
    private String workId;
    private String sourceUrl;
    public ImageView author_image;
    public TextView author_name;
    public TextView author_create_time;
    public RelativeLayout work_list_item_author;
    public RecyclerView recyclerView;
    private LinearLayout bottom_view;
    private TextView over_size;
    private TextView recommend_new;
    private OneWorkAdapter oneWorkAdapter;
    public static final String KEY_AUDIT = "audit";
    public static final String TYPE_AUDIT = "1";
    private List<WorkListItemBean> workListItems = new ArrayList<>();
    private String type;

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        return R.layout.layout_one_work;
    }

    @Override
    protected void initView() {
        author_image = (ImageView) findViewById(R.id.author_image);
        author_name = (TextView) findViewById(R.id.author_name);
        author_create_time = (TextView) findViewById(R.id.author_create_time);
        work_list_item_author = (RelativeLayout) findViewById(R.id.work_list_item_author);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        bottom_view = (LinearLayout) findViewById(R.id.bottom_view);
        over_size = (TextView) findViewById(R.id.over_size);
        recommend_new = (TextView) findViewById(R.id.recommend_new);
        over_size.setOnClickListener(this);
        recommend_new.setOnClickListener(this);

        oneWorkAdapter = new OneWorkAdapter(getActivity(), workListItems);
        oneWorkAdapter.setOnImageClick(this);
        recyclerView.setAdapter(oneWorkAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {
        if (getArguments() != null) {
            Map<String, String> map = (Map<String, String>) getArguments().getSerializable(FragmentCommonActivity.EXTRAS);
            if (map != null) {
                workId = map.get(UrlJumpHelper.WORK_ID);
                type = map.get(KEY_AUDIT);
            }
        }

        if (TYPE_AUDIT.equals(type)) {
            bottom_view.setVisibility(View.VISIBLE);
        }
        oneWorkPresenter = new OneWorkPresenter(getActivity());
        oneWorkPresenter.attachView(this);
        oneWorkPresenter.getOneWorkDetail(workId);
    }

    @Override
    public void refresh(int type, Object data) {
        if (data instanceof WorkListBean) {
            WorkListBean bean = (WorkListBean) data;
            sourceUrl = bean.sourceUrl;
            if (getActivity() instanceof FragmentCommonActivity) {
                ((FragmentCommonActivity) getActivity()).setShowBottomRight(sourceUrl, workId);
            }
            //setAuthor(bean);
            oneWorkAdapter.setWorkListBean(bean);
            addHead(bean);
            addCoverImage(bean);
            addImage(bean.workDetail);
        }
    }

    private void addHead(WorkListBean bean) {
        WorkListItemBean itemHeadBean = new WorkListItemBean();
        itemHeadBean.type = OneWorkAdapter.DATA_TYPE_HEAD;
        itemHeadBean.authorHeadImg = bean.authorHeadImg;
        itemHeadBean.authorName = bean.authorName;
        itemHeadBean.createTime = bean.createTime;
        workListItems.add(itemHeadBean);
    }

    private void addCoverImage(WorkListBean bean) {
        WorkListItemBean itemBean = new WorkListItemBean();
        itemBean.type = OneWorkAdapter.DATA_TYPE_IMAGE;
        itemBean.imageUrl = bean.coverUrl;
        itemBean.width = bean.coverWidth;
        itemBean.height = bean.coverHeight;
        itemBean.backgroundColor = RandomColorHelper.getRandomColor();
        workListItems.add(itemBean);
    }

    private void addImage(List<WorkListItemBean> list) {
        for (int i = 0; i < list.size(); i++) {
            WorkListItemBean bean = list.get(i);
            bean.type = OneWorkAdapter.DATA_TYPE_IMAGE;
            bean.backgroundColor = RandomColorHelper.getRandomColor();
        }
        workListItems.addAll(list);
        oneWorkAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCountClick(int groupPosition) {

    }

    @Override
    public void onCoverClick(String workId, String coverUrl, int width, int height) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).saveImageShowTop(workId, coverUrl, width, height);
        }
    }

    @Override
    public void onMoreLinkClick(String workId, String sourceUrl) {
        DialogHelper.showBottomSourceLink(getActivity(), sourceUrl, workId, eventStatisticsHelper);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recommend_new:
                if (eventStatisticsHelper != null) {
                    eventStatisticsHelper.recordUserAction(getActivity(), EventConstant.WORK_AUDIT_RECOMMEND, workId, "");
                }
                break;
            case R.id.over_size:
                if (eventStatisticsHelper != null) {
                    eventStatisticsHelper.recordUserAction(getActivity(), EventConstant.WORK_SCALE_OVER, workId, "");
                }
                break;
        }
    }
}
