package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.BaseActivity;
import com.arun.a85mm.activity.FragmentCommonActivity;
import com.arun.a85mm.adapter.ImageAdapter;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.helper.DialogHelper;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.helper.UrlJumpHelper;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.presenter.OneWorkPresenter;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.arun.a85mm.view.CommonView3;
import com.arun.a85mm.widget.ListViewForScrollView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2017/6/5.
 */

public class OneWorkFragment extends BaseFragment implements CommonView3, OnImageClick {
    private OneWorkPresenter oneWorkPresenter;
    private String workId;
    private String sourceUrl;
    public ImageView author_image;
    public TextView author_name;
    public TextView author_create_time;
    public RelativeLayout work_list_item_author;
    public ListViewForScrollView listView;
    private ImageAdapter imageAdapter;
    private List<WorkListItemBean> workListItems = new ArrayList<>();

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
        listView = (ListViewForScrollView) findViewById(R.id.listView);
        imageAdapter = new ImageAdapter(getActivity(), workListItems);
        imageAdapter.setOnImageClick(this);
        listView.setAdapter(imageAdapter);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {
        if (getArguments() != null) {
            Map<String, String> map = (Map<String, String>) getArguments().getSerializable(FragmentCommonActivity.EXTRAS);
            if (map != null) {
                workId = map.get(UrlJumpHelper.WORK_ID);
            }
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
            setAuthor(bean);
            imageAdapter.setWorkListBean(bean);
            addImage(bean.workDetail);
        }
    }

    private void setAuthor(WorkListBean bean) {
        author_name.setText(bean.authorName);
        Glide.with(this).load(bean.authorHeadImg).centerCrop().bitmapTransform(new GlideCircleTransform(getActivity())).into(author_image);
        author_create_time.setText(bean.createTime);
    }

    private void addImage(List<WorkListItemBean> list) {
        for (int i = 0; i < list.size(); i++) {
            WorkListItemBean bean = list.get(i);
            bean.backgroundColor = RandomColorHelper.getRandomColor();
        }
        workListItems.addAll(list);
        imageAdapter.notifyDataSetChanged();
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
}
