package com.arun.a85mm.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.ActionBean;
import com.arun.a85mm.bean.ShowTopBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.dialog.BrowserLimitDialog;
import com.arun.a85mm.fragment.BaseFragment;
import com.arun.a85mm.handler.ShowTopHandler;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.helper.ObjectAnimatorHelper;
import com.arun.a85mm.helper.SaveImageHelper;
import com.arun.a85mm.helper.ShareWindow;
import com.arun.a85mm.helper.ShowTopToastHelper;
import com.arun.a85mm.listener.EventListener;
import com.arun.a85mm.refresh.OnRefreshListener;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.DeviceUtils;
import com.arun.a85mm.utils.ShareParaUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.view.MvpView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * Created by WY on 2017/4/15.
 */
public abstract class BaseActivity extends AppCompatActivity implements SwipeBackActivityBase, MvpView, EventListener {
    private SwipeBackActivityHelper mHelper;
    private boolean isShowingTop;
    private SaveImageHelper saveImageHelper;
    private ShowTopHandler showTopHandler;
    private ObjectAnimatorHelper objectAnimatorHelper;
    public EventStatisticsHelper eventStatisticsHelper;
    public TextView topCommonView;
    public TextView toastView;
    public ImageView image_back;
    public TextView titleView;
    public String userId;
    public String deviceId;
    public boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        initCommon();
        StatusBarUtils.statusBarLightMode(this);
    }

    private void initCommon() {
        deviceId = DeviceUtils.getMobileIMEI(this);
        userId = SharedPreferencesUtils.getUid(this);
        eventStatisticsHelper = new EventStatisticsHelper(this);
    }

    public void setSaveImage(boolean isSetToastParent) {
        initToastView();
        saveImageHelper = new SaveImageHelper();
        showTopHandler = new ShowTopHandler(this);
        objectAnimatorHelper = new ObjectAnimatorHelper(isSetToastParent);
    }

    public void setCommonShow() {
        initToastView();
        objectAnimatorHelper = new ObjectAnimatorHelper();
    }

    public void showTop(String showData) {
        ShowTopBean showTopBean = new ShowTopBean(isShowingTop, showData);
        showTopToastView(showTopBean);
    }

    public void initToastView() {
        toastView = (TextView) findViewById(R.id.toastView);
        topCommonView = (TextView) findViewById(R.id.topCommonView);
        if (toastView != null && topCommonView != null
                && toastView.getLayoutParams() != null && topCommonView.getLayoutParams() != null) {
            toastView.getLayoutParams().height = DensityUtil.getStatusHeight(this);
            topCommonView.getLayoutParams().height = DensityUtil.getStatusHeight(this);
        }
    }

    public void saveImageShowTop(String workId, String coverUrl, String authorName) {
        if (saveImageHelper != null && showTopHandler != null) {
            if (!TextUtils.isEmpty(workId)) {
                onEvent(EventConstant.WORK_IMAGE_DOWNLOAD, workId, coverUrl);
            }
            saveImageHelper.saveImage(this, coverUrl, showTopHandler, isShowingTop, authorName);
        }
    }

    public void showTopToastView(ShowTopBean showTopBean) {
        if (showTopBean != null && objectAnimatorHelper != null) {
            objectAnimatorHelper.managerShowTopView(this, toastView, showTopBean);
        }
    }

    public void setShowingTop(boolean isShowingTop) {
        this.isShowingTop = isShowingTop;
    }

    public boolean getShowingTop() {
        return isShowingTop;
    }

    public void setImageRight(ImageView imageView, @DrawableRes int drawableResId) {
        if (imageView != null) {
            imageView.setImageResource(drawableResId);
        }
    }

    /*public void onActionEvent(int type, List<ActionBean> actionList) {
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.recordUserAction(this, type, actionList);
        }
    }*/

    @Override
    public void onEvent(int actionType) {
        onActionEvent(actionType, "", "");
    }

    @Override
    public void onEvent(int actionType, String resourceId) {
        onActionEvent(actionType, resourceId, "");
    }

    @Override
    public void onEvent(int actionType, String resourceId, String remark) {
        onActionEvent(actionType, resourceId, remark);
    }

    public void onActionEvent(int type, String resourceId, String remark) {
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.recordUserAction(this, type, resourceId, remark);
        }
    }

    public void setRecyclerViewScrollListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /*//得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                //得到lastChildView的bottom坐标值
                if(lastChildView != null){
                    int lastChildBottom = lastChildView.getBottom();
                    //得到RecyclerView的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                    int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                    int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                    synchronized (BaseActivity.this) {
                        if (lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                            if (!isLoading) {
                                setLoadMore();
                            }
                        }
                    }
                }*/
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                        int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
                        int visibleCount = linearLayoutManager.getChildCount();
                        int totalCount = linearLayoutManager.getItemCount();
                        int limitLoadMore = 0;
                        if (totalCount > 20) {
                            limitLoadMore = totalCount - 20;
                        } else {
                            limitLoadMore = totalCount;
                        }
                        synchronized (BaseActivity.this) {
                            if (firstVisiblePosition + visibleCount >= limitLoadMore) {
                                if (!isLoading) {
                                    setLoadMore();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void setRefresh(SwipeToLoadLayout swipeToLoadLayout) {
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh() {
                    onEvent(EventConstant.PULL_TO_REFRESH);
                    reloadData();
                }
            });
        }
    }


    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    protected void setLoadMore() {
    }

    protected void reloadData() {
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    public void setBack(View backView) {
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setBack() {
        image_back = (ImageView) findViewById(R.id.image_back);
        if (image_back != null) {
            setBack(image_back);
        }
    }

    public void setTitle(String title) {
        titleView = (TextView) findViewById(R.id.title);
        if (titleView != null) {
            titleView.setText(title);
        }
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.detachView();
        }
    }

    @Override
    public void onError(int errorType, String errorMsg) {

    }

    @Override
    public void onError(int errorType, @StringRes int errorMsg) {
        ShowTopToastHelper.showTopToastView(this, getString(errorMsg), R.color.red);
    }

    @Override
    public void onRefreshComplete() {

    }

    public void shareWorkDetail(WorkListBean workListBean) {
        ShareWindow.show(this, workListBean.title, ShareParaUtils.getWorkDetailShareDescription(workListBean.authorName),
                ShareParaUtils.getWorkDetailShareUrl(workListBean.id), workListBean.coverUrl, eventStatisticsHelper);
    }


    private BrowserLimitDialog browserLimitDialog;

    public void showBrowserDialog(Context context) {
        if (browserLimitDialog == null) {
            browserLimitDialog = new BrowserLimitDialog(context, R.style.CustomDialog);
        }
        if (!browserLimitDialog.isShowing()) {
            browserLimitDialog.show();
        }
    }
}
