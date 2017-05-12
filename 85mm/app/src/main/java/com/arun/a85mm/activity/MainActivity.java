package com.arun.a85mm.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.ActionBean;
import com.arun.a85mm.bean.ConfigResponse;
import com.arun.a85mm.bean.ShowTopBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.fragment.ArticleFragment;
import com.arun.a85mm.fragment.CommunityFragment;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.handler.ShowTopHandler;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.helper.ObjectAnimatorHelper;
import com.arun.a85mm.helper.SaveImageHelper;
import com.arun.a85mm.presenter.SettingPresenter;
import com.arun.a85mm.utils.ACache;
import com.arun.a85mm.utils.CacheUtils;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.DeviceUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.view.CommonView2;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CommonView2 {

    private RelativeLayout activity_main;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    private String[] titles = new String[]{"最新", "最热", "文章"};
    private List<Fragment> list = new ArrayList<>();
    private TextView topCommonView;
    private TextView toastView;
    private ProductionFragment productionFragment;
    private long mExitTime;
    private boolean isShowingTop;
    private SaveImageHelper saveImageHelper;
    private ShowTopHandler showTopHandler;
    private ObjectAnimatorHelper objectAnimatorHelper;
    private EventStatisticsHelper eventStatisticsHelper;
    private SettingPresenter settingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initView() {
        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }
        });
        tabLayout.setViewPager(viewPager, titles);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewPager.setCurrentItem(1);
        setSaveImage();
        eventStatisticsHelper = new EventStatisticsHelper(this);
    }

    private void initData() {
        productionFragment = new ProductionFragment();
        CommunityFragment communityFragment = new CommunityFragment();
        ArticleFragment articleFragment = ArticleFragment.newIntense();
        list.add(productionFragment);
        list.add(communityFragment);
        list.add(articleFragment);
        if (settingPresenter == null) {
            settingPresenter = new SettingPresenter(this);
            settingPresenter.attachView(this);
            settingPresenter.queryConfig(DeviceUtils.getMobileIMEI(this));
        }
    }

    private void setSaveImage() {
        initToastView();
        saveImageHelper = new SaveImageHelper();
        showTopHandler = new ShowTopHandler(this);
        objectAnimatorHelper = new ObjectAnimatorHelper();
    }

    private void initToastView() {
        toastView = (TextView) findViewById(R.id.toastView);
        topCommonView = (TextView) findViewById(R.id.topCommonView);
        if (toastView.getLayoutParams() != null && topCommonView.getLayoutParams() != null) {
            toastView.getLayoutParams().height = DensityUtil.getStatusHeight(this);
            topCommonView.getLayoutParams().height = DensityUtil.getStatusHeight(this);
        }
    }

    public void showTop(String showData) {
        if (showTopHandler != null) {
            Message message = new Message();
            message.what = Constant.WHAT_SHOW_TOP;
            message.obj = new ShowTopBean(isShowingTop, showData);
            if (showTopHandler != null) {
                showTopHandler.sendMessage(message);
            }
        }
    }

    public void saveImageShowTop(String workId, String coverUrl, int width, int height) {
        if (saveImageHelper != null && showTopHandler != null) {
            if (!TextUtils.isEmpty(workId)) {
                onActionEvent(EventConstant.WORK_IMAGE_DOWNLOAD, EventStatisticsHelper.createOneActionList(EventConstant.WORK_IMAGE_DOWNLOAD, workId, coverUrl));
            }
            saveImageHelper.saveImageShowTop(this, coverUrl, width, height, showTopHandler, isShowingTop);
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

    public void onActionEvent(int type, List<ActionBean> actionList) {
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.recordUserAction(this, type, actionList);
        }
    }

    public void moreClick(View view) {
        MoreSettingActivity.jumpToMoreSettingActivity(this);
    }

    @Override
    public void refresh(Object data) {
        if (data instanceof ConfigResponse) {
            ConfigResponse config = (ConfigResponse) data;
            SharedPreferencesUtils.saveUid(this, config.uid);
            CacheUtils.saveObject(this, CacheUtils.KEY_OBJECT_CONFIG, (Serializable) config.copyWrite);
        }
    }

    @Override
    public void onError(String error, String tag) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (settingPresenter != null) {
            settingPresenter.detachView();
        }
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.detachView();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //Toast.makeText(this, R.string.text_exit_app, Toast.LENGTH_SHORT).show();
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
