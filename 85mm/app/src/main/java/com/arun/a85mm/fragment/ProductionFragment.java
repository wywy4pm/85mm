package com.arun.a85mm.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.MainActivity;
import com.arun.a85mm.activity.WebViewActivity;
import com.arun.a85mm.adapter.ProductListAdapter;
import com.arun.a85mm.bean.ProductListResponse;
import com.arun.a85mm.presenter.ProductFragmentPresenter;
import com.arun.a85mm.refresh.OnRefreshListener;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.retrofit.RetrofitApi;
import com.arun.a85mm.retrofit.RetrofitInit;
import com.arun.a85mm.utils.FileUtils;
import com.arun.a85mm.utils.NetUtils;
import com.arun.a85mm.view.CommonView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by WY on 2017/4/14.
 */
public class ProductionFragment extends BaseFragment implements ProductListAdapter.OnImageClick, CommonView<ProductListResponse> {

    private SwipeToLoadLayout swipeToLoadLayout;
    private ExpandableListView expandableListView;
    private ProductListAdapter productListAdapter;
    private List<ProductListResponse.WorkListBean> workLists = new ArrayList<>();
    private ProductFragmentPresenter productFragmentPresenter;
    private boolean isHaveMore = true;
    private String userId;
    private String lastWorkId;
    private boolean isSaveImage;
    private static final String TAG = "ProductionFragment";
    private ImageView next_group_img;
    private int currentGroupPosition;
    private boolean isSingleExpand;
    private int count = 0;

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        return R.layout.layout_producation;
    }

    @Override
    protected void initView() {
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoad);
        expandableListView = (ExpandableListView) findViewById(R.id.swipe_target);
        next_group_img = (ImageView) findViewById(R.id.next_group_img);
        productListAdapter = new ProductListAdapter(getActivity(), workLists);
        expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(productListAdapter);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        //setAbListViewScrollListener(expandableListView);
        expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                synchronized (ProductionFragment.this) {
                    if (view.getLastVisiblePosition() >= view.getCount() - 1) {
                        if (!isLoading) {
                            setLoadMore();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /*Log.d(TAG, "firstVisibleItem = " + firstVisibleItem);
                Log.d(TAG, "visibleItemCount = " + visibleItemCount);
                Log.d(TAG, "totalItemCount = " + totalItemCount);*/
                //Log.d(TAG, "last = " + listView.getLastVisiblePosition());
                int lastVisiblePosition = listView.getLastVisiblePosition();
                int currentChildCount = 0;
                if (workLists != null && workLists.size() > 0) {
                    if (workLists.get(currentGroupPosition) != null && workLists.get(currentGroupPosition).workDetail != null) {
                        currentChildCount = workLists.get(currentGroupPosition).workDetail.size();
                    }
                    int currentRangeMin = currentGroupPosition;
                    int currentRangeMax = currentGroupPosition + currentChildCount;
                    if (isSingleExpand && lastVisiblePosition >= currentGroupPosition
                            && currentRangeMin <= firstVisibleItem
                            && currentRangeMax >= lastVisiblePosition
                            && currentChildCount > 4) {
                        next_group_img.setVisibility(View.VISIBLE);
                    } else {
                        next_group_img.setVisibility(View.GONE);
                        isSingleExpand = false;
                    }
                } else {
                    next_group_img.setVisibility(View.GONE);
                    isSingleExpand = false;
                }
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                isSingleExpand = true;
                if (workLists.get(groupPosition) != null && workLists.get(groupPosition).workDetail != null && workLists.get(groupPosition).totalImageNum > 5) {
                    if (groupPosition < workLists.size() - 1) {
                        currentGroupPosition = groupPosition;
                        Log.d(TAG, "currentGroupPosition = " + currentGroupPosition);
                        next_group_img.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        productListAdapter.setOnImageClick(this);
        next_group_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentGroupPosition < workLists.size()) {
                    Log.d(TAG, "currentPosition = " + (currentGroupPosition + 1));
                    int currentPosition = currentGroupPosition + 1;
                    expandableListView.setSelectedGroup(currentPosition);
                }
                next_group_img.setVisibility(View.GONE);
                isSingleExpand = false;
            }
        });
        /*expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                parent.expandGroup(groupPosition, false);
                return true;
            }
        });*/
    }

    @Override
    protected void initData() {
        productFragmentPresenter = new ProductFragmentPresenter(getActivity());
        productFragmentPresenter.attachView(this);
        refreshData();
    }

    private void refreshData() {
        setHaveMore(true);
        if (NetUtils.isConnected(getActivity())) {
            hideNetWorkErrorView(expandableListView);
            if (productFragmentPresenter != null) {
                setLoading(true);
                lastWorkId = "";
                productFragmentPresenter.getProductListData(userId, lastWorkId);
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
            productFragmentPresenter.getProductListData(userId, lastWorkId);
        }
    }

    @Override
    public void refresh(ProductListResponse data) {
        if (data != null && data.workList != null && data.workList.size() > 0) {
            workLists.clear();
            formatData(data.workList);
        }
    }

    @Override
    public void refreshMore(ProductListResponse data) {
        if (data != null && data.workList != null && data.workList.size() > 0) {
            formatData(data.workList);
        }
    }

    @Override
    public void onError(String error, String tag) {

    }

    private void formatData(List<ProductListResponse.WorkListBean> workList) {
        for (int i = 0; i < workList.size(); i++) {
            if (workList.get(i) != null && workList.get(i).workDetail != null && workList.get(i).workDetail.size() > 0) {
                for (int j = 0; j < workList.get(i).workDetail.size(); j++) {
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
                List<ProductListResponse.WorkListBean.WorkListItemBean> items = new ArrayList<>();
                ProductListResponse.WorkListBean.WorkListItemBean itemBean = new ProductListResponse.WorkListBean.WorkListItemBean();
                itemBean.authorHeadImg = workList.get(i).authorHeadImg;
                itemBean.authorName = workList.get(i).authorName;
                itemBean.authorPageUrl = workList.get(i).authorPageUrl;
                itemBean.workTitle = workList.get(i).workTitle;
                itemBean.sourceUrl = workList.get(i).sourceUrl;
                items.add(itemBean);
                workList.get(i).workDetail = items;
            }
            if (i == workList.size() - 1) {
                lastWorkId = workList.get(i).workId;
            }
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
            expandableListView.expandGroup(groupPosition, false);
        }

    }

    @Override
    public void onCoverClick(String coverUrl) {
        if (!isSaveImage) {
            saveImage(coverUrl);
        } else {
            Toast.makeText(getActivity(), "当前有图片正在保存，请稍后...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMoreLinkClick(String sourceUrl) {
        showBottomDialog(sourceUrl);
    }

    public void setHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
    }

    public void setSaveImage(boolean isSaveImage) {
        this.isSaveImage = isSaveImage;
    }

    private void saveImage(String imageUrl) {
        setSaveImage(true);
        final String imageName = FileUtils.getFileName(imageUrl);
        RetrofitApi downloadService = RetrofitInit.getApi();
        Call<ResponseBody> call = downloadService.downLoadImage(imageUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    boolean writtenToDisk = FileUtils.writeResponseBodyToDisk(getActivity(), response.body(), imageName);
                    if (!TextUtils.isEmpty(imageName)) {
                        if (writtenToDisk) {
                            if (imageName.length() > 10) {
                                ((MainActivity) getActivity()).showTopToastView("图片保存成功：" + imageName.substring(imageName.length() - 10, imageName.length()));
                            } else {
                                ((MainActivity) getActivity()).showTopToastView("图片保存成功：" + imageName);
                            }
                        } else {
                            ((MainActivity) getActivity()).showTopToastView("图片保存失败");
                        }
                    } else {
                        ((MainActivity) getActivity()).showTopToastView("图片保存失败");
                    }
                } else {
                    Toast.makeText(getActivity(), "下载失败", Toast.LENGTH_SHORT).show();
                    setSaveImage(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "下载失败", Toast.LENGTH_SHORT).show();
                setSaveImage(false);
            }
        });
    }

    private void showBottomDialog(final String webUrl) {
        final Dialog dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
        LinearLayout root = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_bottom, null);
        root.findViewById(R.id.btn_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.jumpToWebViewActivity(getActivity(), webUrl);
                dialog.cancel();
            }
        });
        root.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.setContentView(root);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        Window dialogWindow = dialog.getWindow();
        //dialogWindow.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.x = 0; // 新位置X坐标
            lp.y = 0; // 新位置Y坐标
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogWindow.setAttributes(lp);
            dialog.show();
        }
    }
}
