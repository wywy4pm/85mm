package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.MainActivity;
import com.arun.a85mm.adapter.CommunityAdapter;
import com.arun.a85mm.bean.ColumnBean;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.GoodsListBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.bean.WorkMixBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.event.UpdateProductEvent;
import com.arun.a85mm.helper.CommunityListCacheManager;
import com.arun.a85mm.helper.DialogHelper;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.helper.UrlJumpHelper;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.presenter.CommunityPresenter;
import com.arun.a85mm.refresh.OnRefreshListener;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.NetUtils;
import com.arun.a85mm.view.CommonView;
import com.arun.a85mm.view.CommonView4;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WY on 2017/4/14.
 */
public class CommunityFragment extends BaseFragment implements CommonView4<List<GoodsListBean>>, OnImageClick {
    public ExpandableListView expandableListView;
    public SwipeToLoadLayout swipeToLoadLayout;
    public ImageView not_network_image;
    public TextView not_network_text;
    public TextView not_network_btn;
    private List<WorkListBean> worksList = new ArrayList<>();
    private CommunityPresenter communityPresenter;
    private boolean isHaveMore = true;
    private String lastWorkDate;
    private static final String TAG = "CommunityFragment";
    private ImageView next_group_img;
    private CommunityAdapter communityAdapter;
    private CommonApiResponse response;
    private LinearLayout headView;

    public static CommunityFragment newInstance() {
        CommunityFragment communityFragment = new CommunityFragment();
        return communityFragment;
    }

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return R.layout.layout_producation;
    }

    @Override
    protected void initView() {
        expandableListView = (ExpandableListView) findViewById(R.id.swipe_target);
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoad);
        not_network_image = (ImageView) findViewById(R.id.not_network_image);
        not_network_text = (TextView) findViewById(R.id.not_network_text);
        not_network_btn = (TextView) findViewById(R.id.not_network_btn);
        next_group_img = (ImageView) findViewById(R.id.next_group_img);
        communityAdapter = new CommunityAdapter(getActivity(), worksList, true);
        expandableListView.setAdapter(communityAdapter);
        communityAdapter.setOnImageClick(this);
        communityAdapter.setEventListener(this);
        addHeadView();

        setRefresh(swipeToLoadLayout);
        setExpandableListViewCommon(expandableListView, next_group_img, worksList);
        setHideReadTips();

    }

    private void addHeadView() {
        headView = new LinearLayout(getActivity());
        headView.setOrientation(LinearLayout.VERTICAL);
        headView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        expandableListView.addHeaderView(headView);
    }

    @Override
    protected void initData() {
        communityPresenter = new CommunityPresenter(getActivity());
        communityPresenter.attachView(this);
        response = CommunityListCacheManager.getCommonApiResponse();
        refreshData();
    }

    @SuppressWarnings("unchecked")
    public void refreshData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                expandableListView.setSelection(0);
            }
        }, 50);
        currentGroupPosition = 0;
        isSingleExpand = false;
        collapseGroup(expandableListView, worksList);
        next_group_img.setVisibility(View.GONE);
        setHaveMore(true);

        if (NetUtils.isConnected(getActivity())) {
            hideNetWorkErrorView(expandableListView);
            if (communityPresenter != null) {

                lastWorkDate = "";
                if (response == null) {
                    setLoading(true);
                    //communityPresenter.getWorksGoods(lastWorkDate);
                    communityPresenter.getWorkMix();
                } else {
                    if (response.body != null) {
                        if (response.body instanceof WorkMixBean) {
                            worksList.clear();
                            WorkMixBean workMixBean = (WorkMixBean) response.body;
                            setColumns(workMixBean.columns);
                            formatData(workMixBean.historyList);
                            response = null;
                        } else {
                            swipeToLoadLayout.setRefreshing(false);
                        }
                    } else {
                        swipeToLoadLayout.setRefreshing(false);
                    }
                }
            }
        } else {
            if (swipeToLoadLayout.isRefreshing()) {
                swipeToLoadLayout.setRefreshing(false);
            }
            showNetWorkErrorView(expandableListView);
        }
    }

    public void setHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
        if (!isHaveMore) {
            setLeftWorkBrowse(EventConstant.WORK_BROWSE_HOTEST, worksList);
        }
    }

    @Override
    public void setLoadMore() {
        if (isHaveMore) {
            loadMore();
        }
    }

    private void loadMore() {
        setLoading(true);
        if (communityPresenter != null) {
            communityPresenter.getWorksGoods(lastWorkDate);
        }
    }

    @Override
    public void reloadData() {
        refreshData();
    }

    @Override
    public void refresh(List<GoodsListBean> data) {
        /*if (data != null && data.size() > 0) {
            worksList.clear();
            formatData(data);
        }*/
    }

    @Override
    public void refresh(int type, Object data) {
        if (data != null && data instanceof WorkMixBean) {
            worksList.clear();
            WorkMixBean workMixBean = (WorkMixBean) data;
            setColumns(workMixBean.columns);
            formatData(workMixBean.historyList);
        }
    }

    @Override
    public void refreshMore(List<GoodsListBean> data) {
        if (data != null && data.size() > 0) {
            formatData(data);
        }
    }


    private void setColumns(final List<ColumnBean> columns) {
        if (columns != null && columns.size() > 0) {
            headView.removeAllViews();
            int imageWidthHeight = screenWidth / 3;

            for (int i = 0; i < columns.size(); i++) {
                View columnView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_work_column, headView, false);
                columnView.getLayoutParams().height = imageWidthHeight;
                if (i == 0) {
                    ((LinearLayout.LayoutParams) columnView.getLayoutParams()).setMargins(0, 0, 0, DensityUtil.dp2px(getActivity(), 5));
                }

                ImageView image1 = (ImageView) columnView.findViewById(R.id.image1);
                ImageView image2 = (ImageView) columnView.findViewById(R.id.image2);
                ImageView image3 = (ImageView) columnView.findViewById(R.id.image3);
                image1.getLayoutParams().width = imageWidthHeight;
                image2.getLayoutParams().width = imageWidthHeight;
                image3.getLayoutParams().width = imageWidthHeight;
                TextView text_big = (TextView) columnView.findViewById(R.id.text_big);
                TextView text_small = (TextView) columnView.findViewById(R.id.text_small);

                Glide.with(getActivity()).load(columns.get(i).images.get(0))
                        .placeholder(RandomColorHelper.getRandomColor())
                        .error(RandomColorHelper.getRandomColor())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .into(image1);
                Glide.with(getActivity()).load(columns.get(i).images.get(1))
                        .placeholder(RandomColorHelper.getRandomColor())
                        .error(RandomColorHelper.getRandomColor())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .into(image2);
                Glide.with(getActivity()).load(columns.get(i).images.get(2))
                        .placeholder(RandomColorHelper.getRandomColor())
                        .error(RandomColorHelper.getRandomColor())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .into(image3);
                text_big.setText(columns.get(i).text.get(0));
                text_small.setText(columns.get(i).text.get(1));
                final int finalI = i;
                columnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UrlJumpHelper.urlJumpTo(getActivity(), columns.get(finalI).linkUrl, columns.get(finalI).text.get(0));
                    }
                });

                headView.addView(columnView);
            }
        }
    }

    private void formatData(List<GoodsListBean> goodsList) {
        int currentListAddCount = 0;
        if (goodsList != null && goodsList.size() > 0) {
            for (int k = 0; k < goodsList.size(); k++) {
                GoodsListBean goodsListBean = goodsList.get(k);
                if (goodsListBean != null && goodsListBean.workList != null && goodsListBean.workList.size() > 0) {
                    List<WorkListBean> workList = goodsListBean.workList;
                    for (int i = 0; i < workList.size(); i++) {
                        workList.get(i).backgroundColor = RandomColorHelper.getRandomColor();
                        if (i == 0) {
                            workList.get(i).isTitle = true;
                            workList.get(i).date = goodsListBean.date;
                            workList.get(i).browseNum = goodsListBean.browseNum;
                            workList.get(i).workNum = goodsListBean.workNum;
                            workList.get(i).allDownloadNum = goodsListBean.downloadNum;
                        }
                        if (i == workList.size() - 1) {
                            workList.get(i).isBottom = true;
                            workList.get(i).date = goodsListBean.date;
                            workList.get(i).searchDate = goodsListBean.searchDate;
                            workList.get(i).start = goodsListBean.start;
                            workList.get(i).leftWorkNum = goodsListBean.leftWorkNum;
                        }

                        if (workList.get(i) != null && workList.get(i).workDetail != null && workList.get(i).workDetail.size() > 0) {
                            for (int j = 0; j < workList.get(i).workDetail.size(); j++) {
                                workList.get(i).workDetail.get(j).backgroundColor = RandomColorHelper.getRandomColor();
                                if (j == workList.get(i).workDetail.size() - 1) {
                                    if (workList.get(i).workDetail.get(j) != null) {
                                        workList.get(i).workDetail.get(j).authorHeadImg = workList.get(i).authorHeadImg;
                                        workList.get(i).workDetail.get(j).authorName = workList.get(i).authorName;
                                        workList.get(i).workDetail.get(j).authorPageUrl = workList.get(i).authorPageUrl;
                                        workList.get(i).workDetail.get(j).workTitle = workList.get(i).workTitle;
                                        workList.get(i).workDetail.get(j).sourceUrl = workList.get(i).sourceUrl;
                                    }
                                }
                            }
                        } else {
                            if (workList.get(i) != null) {
                                workList.get(i).backgroundColor = RandomColorHelper.getRandomColor();
                            }
                            List<WorkListItemBean> items = new ArrayList<>();
                            WorkListItemBean itemBean = new WorkListItemBean();
                            itemBean.authorHeadImg = workList.get(i).authorHeadImg;
                            itemBean.authorName = workList.get(i).authorName;
                            itemBean.authorPageUrl = workList.get(i).authorPageUrl;
                            itemBean.workTitle = workList.get(i).workTitle;
                            itemBean.sourceUrl = workList.get(i).sourceUrl;
                            items.add(itemBean);
                            workList.get(i).workDetail = items;
                        }
                        if (i == workList.size() - 1) {
                            lastWorkDate = goodsListBean.searchDate;
                        }
                    }
                    if (NetUtils.isWifi(getActivity())) {
                        preLoadChildFirstImage(workList);
                    }

                    currentListAddCount += workList.size();
                    worksList.addAll(workList);
                } else if (goodsListBean != null) {
                    WorkListBean bean = new WorkListBean();

                    bean.isTitle = true;
                    bean.date = goodsListBean.date;
                    bean.browseNum = goodsListBean.browseNum;
                    bean.workNum = goodsListBean.workNum;
                    bean.allDownloadNum = goodsListBean.downloadNum;

                    bean.isBottom = true;
                    bean.date = goodsListBean.date;
                    bean.searchDate = goodsListBean.searchDate;
                    bean.start = goodsListBean.start;
                    bean.leftWorkNum = goodsListBean.leftWorkNum;

                    lastWorkDate = goodsListBean.searchDate;

                    currentListAddCount += 1;
                    worksList.add(bean);
                }
            }
        }

        setCurrentResponseCount(currentListAddCount);
        communityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefreshComplete() {
        setLoading(false);
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    /*@Override
    public void onError(String error, String tag) {
        super.onError(error, tag);
    }*/

    @Override
    public void onCountClick(int groupPosition) {
        if (expandableListView != null) {
            if (!expandableListView.isGroupExpanded(groupPosition)) {
                expandableListView.expandGroup(groupPosition);
            }
        }
    }

    @Override
    public void onCoverClick(String workId, String coverUrl, int width, int height) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).saveImageShowTop(workId, coverUrl, width, height);
        }
    }

    @Override
    public void onMoreLinkClick(String workId, String sourceUrl) {
        //DialogHelper.showBottomSourceLink(getActivity(), sourceUrl, workId, eventStatisticsHelper);
        DialogHelper.showBottom(getActivity(), Constant.TYPE_WORK, sourceUrl, workId, "", eventStatisticsHelper);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setHideState(UpdateProductEvent event) {
        refreshData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (communityPresenter != null) {
            communityPresenter.detachView();
        }
    }

}
