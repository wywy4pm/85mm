package com.arun.a85mm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.AmountWorkActivity;
import com.arun.a85mm.activity.BaseActivity;
import com.arun.a85mm.activity.FragmentCommonActivity;
import com.arun.a85mm.activity.MainActivity;
import com.arun.a85mm.adapter.ProductListAdapter;
import com.arun.a85mm.bean.AwardBodyBean;
import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.dialog.RewardDialog;
import com.arun.a85mm.event.DeleteWorkEvent;
import com.arun.a85mm.event.UpdateProductEvent;
import com.arun.a85mm.helper.DialogHelper;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.listener.OnTagWorkListener;
import com.arun.a85mm.presenter.OneWorkPresenter;
import com.arun.a85mm.presenter.ProductFragmentPresenter;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.NetUtils;
import com.arun.a85mm.view.CommonView4;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WY on 2017/4/14.
 */
public class ProductionFragment extends BaseFragment implements OnImageClick, CommonView4<List<WorkListBean>>, OnTagWorkListener {

    private SwipeToLoadLayout swipeToLoadLayout;
    private ExpandableListView expandableListView;
    private RelativeLayout layout_no_data;
    private ProductListAdapter productListAdapter;
    private List<WorkListBean> workLists = new ArrayList<>();
    private ProductFragmentPresenter productFragmentPresenter;
    private boolean isHaveMore = true;
    private String lastWorkId;
    //private static final String TAG = "ProductionFragment";
    public static final String INTENT_KEY_TYPE = "type";
    public static final String INTENT_KEY_TAG = "name";
    private ImageView next_group_img;
    private int dataType;
    private String tagName;

    public static ProductionFragment newInstance(int dataType, String tagName) {
        ProductionFragment productionFragment = new ProductionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INTENT_KEY_TYPE, dataType);
        bundle.putString(INTENT_KEY_TAG, tagName);
        productionFragment.setArguments(bundle);
        return productionFragment;
    }

    public static ProductionFragment newInstance() {
        ProductionFragment productionFragment = new ProductionFragment();
        return productionFragment;
    }

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.recordUserAction(getActivity(), EventConstant.OPEN_LATEST);
        }
        return R.layout.layout_producation;
    }

    @Override
    protected void initView() {
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoad);
        expandableListView = (ExpandableListView) findViewById(R.id.swipe_target);
        next_group_img = (ImageView) findViewById(R.id.next_group_img);
        layout_no_data = (RelativeLayout) findViewById(R.id.layout_no_data);
        productListAdapter = new ProductListAdapter(getActivity(), workLists);
        expandableListView.setAdapter(productListAdapter);
        productListAdapter.setOnImageClick(this);
        productListAdapter.setEventListener(this);
        productListAdapter.setOnTagWorkListener(this);

        setRefresh(swipeToLoadLayout);
        setExpandableListViewCommon(expandableListView, next_group_img, workLists);
        setHideReadTips();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {
        if (getArguments() != null) {
            if (getArguments().containsKey(INTENT_KEY_TYPE)) {
                dataType = getArguments().getInt(INTENT_KEY_TYPE);
            }
            if (getArguments().containsKey(INTENT_KEY_TAG)) {
                tagName = getArguments().getString(INTENT_KEY_TAG);
            }
            if (getArguments().containsKey(FragmentCommonActivity.EXTRAS)) {
                Map<String, String> extras = (Map<String, String>) getArguments().get(FragmentCommonActivity.EXTRAS);
                if (extras != null && extras.containsKey(INTENT_KEY_TYPE)) {
                    dataType = Integer.parseInt(extras.get(INTENT_KEY_TYPE));
                }
            }
        }
        productFragmentPresenter = new ProductFragmentPresenter(getActivity());
        productFragmentPresenter.attachView(this);
        refreshData();
    }

    public void refreshData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                expandableListView.setSelectedGroup(0);
            }
        }, 50);

        currentGroupPosition = 0;
        isSingleExpand = false;
        collapseGroup(expandableListView, workLists);

        next_group_img.setVisibility(View.GONE);
        setHaveMore(true);
        if (NetUtils.isConnected(getActivity())) {
            hideNetWorkErrorView(expandableListView);
            if (productFragmentPresenter != null) {
                setLoading(true);
                lastWorkId = "";
                productFragmentPresenter.getProductListData(dataType, tagName, lastWorkId);
            }
        } else {
            if (swipeToLoadLayout.isRefreshing()) {
                swipeToLoadLayout.setRefreshing(false);
            }
            showNetWorkErrorView(expandableListView);
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
        if (productFragmentPresenter != null) {
            productFragmentPresenter.getProductListData(dataType, tagName, lastWorkId);
        }
    }

    @Override
    public void refresh(List<WorkListBean> data) {
        if (data != null && data.size() > 0) {
            layout_no_data.setVisibility(View.GONE);
            workLists.clear();
            formatData(data);
        }
    }

    @Override
    public void refreshMore(List<WorkListBean> data) {
        if (data != null && data.size() > 0) {
            formatData(data);
        }
    }

    @Override
    public void refresh(int type, Object data) {
        if (type == ProductFragmentPresenter.TYPE_TAG_WORK) {
            if (data instanceof UserTagBean) {
                showTop("打标成功");
            }
        } else if (type == ProductFragmentPresenter.TYPE_USER_AWARD) {
            if (data instanceof AwardBodyBean) {
                AwardBodyBean bean = (AwardBodyBean) data;
                jumpToAmountWork(bean, AmountWorkActivity.TYPE_PAY);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeOneWork(DeleteWorkEvent event) {
        if (event != null && !TextUtils.isEmpty(event.workId)) {
            if (workLists != null && workLists.size() > 0) {
                for (int i = 0; i < workLists.size(); i++) {
                    if (!TextUtils.isEmpty(event.workId)
                            && event.workId.equals(workLists.get(i).id)) {
                        workLists.remove(i);
                        break;
                    }
                }
                productListAdapter.notifyDataSetChanged();
            }
        }
    }

    private String workId;

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public void noEnoughCoins(AwardBodyBean bean) {
        int coins = 0;
        if (bean.productInfo != null) {
            coins = bean.leftCoin;
        }
        if (coins == 0) {
            showDialog(getActivity(), RewardDialog.TYPE_NO_COINS, coins);
        } else {
            showDialog(getActivity(), RewardDialog.TYPE_NO_ENOUGH_COINS, coins);
        }
    }

    public void jumpToAmountWork(AwardBodyBean awardBodyBean, int type) {
        String titleName = workId + "号收费内容";
        AmountWorkActivity.jumpToAmountWork(getActivity(), type, titleName, awardBodyBean);
    }

    private void showDialog(Context context, int type, int leftCoin) {
        RewardDialog rewardDialog = new RewardDialog(context, R.style.CustomDialog, type, leftCoin);
        rewardDialog.show();
    }

    private void formatData(List<WorkListBean> workList) {
        setCurrentResponseCount(workList.size());
        for (int i = 0; i < workList.size(); i++) {
            if (workList.get(i) != null && workList.get(i).imageList != null && workList.get(i).imageList.size() > 0) {
                workList.get(i).backgroundColor = RandomColorHelper.getRandomColor();
                for (int j = 0; j < workList.get(i).imageList.size(); j++) {
                    workList.get(i).imageList.get(j).backgroundColor = RandomColorHelper.getRandomColor();
                    if (j == workList.get(i).imageList.size() - 1) {
                        if (workList.get(i).imageList.get(j) != null) {
                            workList.get(i).imageList.get(j).authorHeadImg = workList.get(i).authorHeadImg;
                            workList.get(i).imageList.get(j).authorName = workList.get(i).authorName;
                            workList.get(i).imageList.get(j).authorPageUrl = workList.get(i).authorPageUrl;
                            workList.get(i).imageList.get(j).workTitle = workList.get(i).title;
                            workList.get(i).imageList.get(j).description = workList.get(i).description;
                            workList.get(i).imageList.get(j).sourceUrl = workList.get(i).sourceUrl;
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
                itemBean.workTitle = workList.get(i).title;
                itemBean.description = workList.get(i).description;
                itemBean.sourceUrl = workList.get(i).sourceUrl;
                items.add(itemBean);
                workList.get(i).imageList = items;
            }
            if (i == workList.size() - 1) {
                lastWorkId = workList.get(i).id;
            }
        }
        if (NetUtils.isWifi(getActivity())) {
            preLoadChildFirstImage(workList);
        }

        workLists.addAll(workList);
        productListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRefreshComplete() {
        setLoading(false);
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    @Override
    public void reloadData() {
        refreshData();
    }

    @Override
    public void onCountClick(int groupPosition) {
        if (expandableListView != null) {
            if (!expandableListView.isGroupExpanded(groupPosition)) {
                expandableListView.expandGroup(groupPosition);
            }
        }
    }

    @Override
    public void onCoverClick(String workId, String coverUrl, String authorName) {
        if (getActivity() != null) {
            if (getActivity() instanceof BaseActivity) {
                ((BaseActivity) getActivity()).saveImageShowTop(workId, coverUrl, authorName);
            } else if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).saveImageShowTop(workId, coverUrl, authorName);
            }
        }
    }

    /*//获取可视第一个group的position
    public int getFirstVisibleGroup() {
        int firstVis = expandableListView.getFirstVisiblePosition();
        long packedPosition = expandableListView.getExpandableListPosition(firstVis);
        int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
        return groupPosition;
    }

    //获取可视第一个child的position
    public int getFirstVisibleChild() {
        int firstVis = expandableListView.getFirstVisiblePosition();
        long packedPosition = expandableListView.getExpandableListPosition(firstVis);
        int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
        return childPosition;
    }*/

    @Override
    public void onMoreLinkClick(String workId, String sourceUrl, String authorUid) {
        String type = Constant.TYPE_WORK;
        if (dataType == 9) {
            type = Constant.TYPE_AUDIT;
        }
        DialogHelper.showBottom(getActivity(), type, sourceUrl, workId, authorUid, eventStatisticsHelper);
    }

    public void setHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
        if (!isHaveMore) {
            setLeftWorkBrowse(EventConstant.WORK_BROWSE_NEWEST, workLists);
        }
    }

    public void setNoDataView() {
        layout_no_data.setVisibility(View.VISIBLE);
        TextView text_no_data = (TextView) layout_no_data.findViewById(R.id.text_no_data);
        text_no_data.setText("没有更多了");
        expandableListView.setEmptyView(layout_no_data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setHideState(UpdateProductEvent event) {
        refreshData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (productFragmentPresenter != null) {
            productFragmentPresenter.detachView();
        }
    }

    @Override
    public void onClickMyTag(UserTagBean tagBean, String workId) {
        if (productFragmentPresenter != null) {
            productFragmentPresenter.tagWork(tagBean, workId);
        }
    }

    @Override
    public void onUserAward(String workId) {
        if (productFragmentPresenter != null) {
            productFragmentPresenter.userAward(workId);
        }
    }

    public void resetUserTag(UserTagBean tagBean) {
        tagBean.tagType = tagBean.tagType == 1 ? 0 : 1;
        productListAdapter.notifyDataSetChanged();
    }

}
