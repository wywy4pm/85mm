package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.arun.a85mm.bean.ShowTopBean;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.fragment.ArticleFragment;
import com.arun.a85mm.fragment.CommunityFragment;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.handler.ShowTopHandler;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.helper.ObjectAnimatorHelper;
import com.arun.a85mm.helper.SaveImageHelper;
import com.arun.a85mm.utils.DataCleanManager;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.TextViewUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout activity_main;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    //private int[] titleIds = new int[]{R.string.main_tab_1, R.string.main_tab_2, R.string.main_tab_3};
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
    public static final int INTENT_TYPE_PUSH_BACK = 1;
    //public static final String KEY_RESPONSE = "productListResponse";

    public static void jumpToMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static void jumpToMain(Context context, int type) {
        Intent intent = new Intent(context, MainActivity.class);
        if (INTENT_TYPE_PUSH_BACK == type) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushAgent.getInstance(this).onAppStart();
        initData();
        initView();
        DataCleanManager.clearOver50MBSize(this);
    }

    private void initView() {
        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        eventStatisticsHelper = new EventStatisticsHelper(this);
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
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int type = -1;
                if (position == 0) {
                    type = EventConstant.TAB_NEWEST;
                } else if (position == 1) {
                    type = EventConstant.TAB_HOTEST;
                } else if (position == 2) {
                    type = EventConstant.TAB_ARTICLE;
                }
                if (eventStatisticsHelper != null) {
                    eventStatisticsHelper.recordUserAction(MainActivity.this, type, EventStatisticsHelper.createOneActionList(type));
                }
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    if (i == position) {
                        TextViewUtils.setTextBold(tabLayout.getTitleView(i), true);
                    } else {
                        TextViewUtils.setTextBold(tabLayout.getTitleView(i), false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        TextViewUtils.setTextBold(tabLayout.getTitleView(1), true);
        viewPager.setCurrentItem(1);
        setSaveImage();
    }

    private void initData() {
        productionFragment = ProductionFragment.newInstance();
        CommunityFragment communityFragment = new CommunityFragment();
        ArticleFragment articleFragment = ArticleFragment.newIntense();
        list.add(productionFragment);
        list.add(communityFragment);
        list.add(articleFragment);
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
        ShowTopBean showTopBean = new ShowTopBean(isShowingTop, showData);
        showTopToastView(showTopBean);
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

    public boolean getShowingTop() {
        return isShowingTop;
    }

    public void onActionEvent(int type, List<ActionBean> actionList) {
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.recordUserAction(this, type, actionList);
        }
    }

    public void moreClick(View view) {
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.recordUserAction(MainActivity.this, EventConstant.TAB_MORE, EventStatisticsHelper.createOneActionList(EventConstant.TAB_MORE));
        }
        MoreSettingActivity.jumpToMoreSettingActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
