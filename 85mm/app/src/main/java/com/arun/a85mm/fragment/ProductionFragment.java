package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.MainActivity;
import com.arun.a85mm.adapter.ProductListAdapter;
import com.arun.a85mm.bean.ProductListResponse;
import com.arun.a85mm.presenter.ProductFragmentPresenter;
import com.arun.a85mm.refresh.OnRefreshListener;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.retrofit.RetrofitApi;
import com.arun.a85mm.retrofit.RetrofitInit;
import com.arun.a85mm.utils.FileUtils;
import com.arun.a85mm.utils.PermissionUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by WY on 2017/4/14.
 */
public class ProductionFragment extends BaseFragment implements ProductListAdapter.OnImageClick {

    private SwipeToLoadLayout swipeToLoadLayout;
    private ExpandableListView expandableListView;
    private ProductListAdapter productListAdapter;
    private List<ProductListResponse.WorkListBean> workList = new ArrayList<>();
    private ProductFragmentPresenter productFragmentPresenter;
    private boolean isHaveMore = true;
    private String userId;
    private String lastWorkId;
    private boolean isSaveImage;

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
                swipeToLoadLayout.setRefreshing(false);
            }
        });
        initdata();
        productListAdapter.setOnImageClick(this);
        productListAdapter.notifyDataSetChanged();
        /*expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                parent.expandGroup(groupPosition, false);
                return true;
            }
        });*/
    }

    @Override
    public void onCountClick(int groupPosition) {
        if (expandableListView != null) {
            expandableListView.expandGroup(groupPosition, false);
        }
    }

    @Override
    public void onCoverClick(String coverUrl, int groupPosition) {
        if (PermissionUtils.checkAlertWindowsPermission(getActivity())) {
            if (!isSaveImage) {
                saveImage(coverUrl);
            } else {
                Toast.makeText(getActivity(), "正在保存图片，请稍后...", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "请打开悬浮窗权限", Toast.LENGTH_SHORT).show();
        }

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
            //workListBean.coverUrl = "http://img.qdaily.com/uploads/20170406161328XKrjP2zMbCHn4leB.jpg-WebpWebW640";
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

    private void saveImage(String imageUrl) {
        setSaveImage(true);
        final String imageName = getFileName(imageUrl);
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
                                //Toast.makeText(getActivity(), imageName.substring(imageName.length() - 10, imageName.length()) + "  保存成功", Toast.LENGTH_SHORT).show();
                            } else {
                                ((MainActivity) getActivity()).showTopToastView("图片保存成功：" + imageName);
                                //Toast.makeText(getActivity(), imageName + "  保存成功", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ((MainActivity) getActivity()).showTopToastView("图片保存失败");
                            //Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ((MainActivity) getActivity()).showTopToastView("图片保存失败");
                    }
                } else {
                    //Log.d("TAG", "server contact failed");
                    Toast.makeText(getActivity(), "下载失败", Toast.LENGTH_SHORT).show();
                    isSaveImage = false;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e("TAG", "error");
                Toast.makeText(getActivity(), "下载失败", Toast.LENGTH_SHORT).show();
                isSaveImage = false;
            }
        });
    }

    public void setSaveImage(boolean isSaveImage) {
        this.isSaveImage = isSaveImage;
    }

    private String getFileName(String imageUrl) {
        String fileName = "";
        URL url = null;
        try {
            url = new URL(imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final URL finalUrl = url;
        if (finalUrl != null && !TextUtils.isEmpty(finalUrl.getFile())) {
            File file = new File(finalUrl.getFile());
            fileName = file.getName();
        }
        return fileName;
    }

}
