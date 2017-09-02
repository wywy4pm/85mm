package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.CommonFragmentPagerAdapter;
import com.arun.a85mm.bean.ActionBean;
import com.arun.a85mm.bean.MenuListBean;
import com.arun.a85mm.bean.ShowTopBean;
import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.event.UpdateMesDotEvent;
import com.arun.a85mm.fragment.ArticleFragment;
import com.arun.a85mm.fragment.AssociationFragment;
import com.arun.a85mm.fragment.CommunityFragment;
import com.arun.a85mm.fragment.LeftWorksFragment;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.fragment.WebViewFragment;
import com.arun.a85mm.handler.ShowTopHandler;
import com.arun.a85mm.helper.ConfigHelper;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.helper.ObjectAnimatorHelper;
import com.arun.a85mm.helper.SaveImageHelper;
import com.arun.a85mm.helper.ShareWindow;
import com.arun.a85mm.helper.UrlJumpHelper;
import com.arun.a85mm.utils.DataCleanManager;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.ShareParaUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.utils.TextViewUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    //private String[] titles = new String[]{"作品", "社区", "文章"};
    private String[] titles;
    private List<Fragment> list = new ArrayList<>();
    private TextView topCommonView;
    private TextView toastView;
    //private ProductionFragment productionFragment;
    private AssociationFragment associationFragment;
    private long mExitTime;
    private boolean isShowingTop;
    private SaveImageHelper saveImageHelper;
    private ShowTopHandler showTopHandler;
    private ObjectAnimatorHelper objectAnimatorHelper;
    private EventStatisticsHelper eventStatisticsHelper;
    public static final int INTENT_TYPE_PUSH_BACK = 1;
    private static String type;
    private static Map<String, String> map;
    private TextView dot_new_message;
    //public static final String KEY_RESPONSE = "productListResponse";

    public static void jumpToMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static void jumpToMain(Context context, String type, Map<String, String> map) {
        Intent intent = new Intent(context, MainActivity.class);
        if (!TextUtils.isEmpty(type)) {
            MainActivity.type = type;
        }
        if (map != null) {
            MainActivity.map = map;
        }
        context.startActivity(intent);
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
        StatusBarUtils.statusBarLightMode(this);
        EventBus.getDefault().register(this);
        //initData();
        init();
        setListener();
        DataCleanManager.clearOver50MBSize(this);

        if (!TextUtils.isEmpty(type) && map != null) {
            jumpToOther();
        }
    }

    private void jumpToOther() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UrlJumpHelper.JUMP_APP_WORK_DETAIL.equals(type)) {
                    //FragmentCommonActivity.jumpToFragmentCommonActivity(MainActivity.this, FragmentCommonActivity.FRAGMENT_ONE_WORK, map);
                    Map<String, String> map = new HashMap<>();
                    map.put(OneWorkActivity.KEY_TYPE, Constant.TYPE_PUSH);
                    OneWorkActivity.jumpToOneWorkActivity(MainActivity.this, OneWorkActivity.FRAGMENT_ONE_WORK, map);
                }
                type = "";
                map = null;
            }
        }, 500);
    }

    private void init() {
        tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        dot_new_message = (TextView) findViewById(R.id.dot_new_message);
        eventStatisticsHelper = new EventStatisticsHelper(this);

        List<MenuListBean> menuList = ConfigHelper.menuList;
        if (menuList != null) {
            titles = new String[menuList.size()];
            for (int i = 0; i < titles.length; i++) {
                if (menuList.get(i) != null) {
                    titles[i] = menuList.get(i).showName;
                    int type = menuList.get(i).dataType;
                    Fragment fragment = null;
                    if (type == -3) {
                        fragment = CommunityFragment.newInstance();
                    } else if (type == -2) {
                        fragment = ArticleFragment.newIntense();
                    } else if (type == -1) {
                        fragment = WebViewFragment.getInstance(menuList.get(i).url);
                    } else if (type == 0 || type == 9) {
                        fragment = ProductionFragment.newInstance(type, menuList.get(i).tagName);
                    } else if (type == 1) {
                        fragment = new LeftWorksFragment();
                    } else if (type == 3) {
                        fragment = AssociationFragment.getInstance();
                    }
                    list.add(fragment);
                }
            }
        }

        viewPager.setAdapter(new CommonFragmentPagerAdapter(getSupportFragmentManager(), list));
        tabLayout.setViewPager(viewPager, titles);
        TextViewUtils.setTextBold(tabLayout.getTitleView(0), true);
        viewPager.setCurrentItem(0);
        setSaveImage();
    }

    private void setListener() {
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (list != null && list.size() > 0
                        && position <= list.size() - 1) {
                    Fragment fragment = list.get(position);
                    if (fragment != null) {
                        if (fragment instanceof ProductionFragment) {
                            ProductionFragment productionFragment = (ProductionFragment) fragment;
                            productionFragment.refreshData();
                        } else if (fragment instanceof CommunityFragment) {
                            CommunityFragment communityFragment = (CommunityFragment) fragment;
                            communityFragment.refreshData();
                        } else if (fragment instanceof ArticleFragment) {
                            ArticleFragment articleFragment = (ArticleFragment) fragment;
                            articleFragment.refreshData();
                        } else if (fragment instanceof AssociationFragment) {
                            AssociationFragment associationFragment = (AssociationFragment) fragment;
                            associationFragment.refreshData();
                        }
                    }
                }
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
                    type = EventConstant.CHANGE_WORK;
                } else if (position == 1) {
                    type = EventConstant.CHANGE_ASSOCIATION;
                } else if (position == 2) {
                    type = EventConstant.CHANGE_ARTICLE;
                }
                if (eventStatisticsHelper != null) {
                    eventStatisticsHelper.recordUserAction(MainActivity.this, type);
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
    }

    private void initData() {
        //productionFragment = ProductionFragment.newInstance();
        CommunityFragment communityFragment = CommunityFragment.newInstance();
        associationFragment = AssociationFragment.getInstance();
        ArticleFragment articleFragment = ArticleFragment.newIntense();
        //list.add(productionFragment);
        list.add(communityFragment);
        list.add(associationFragment);
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
            eventStatisticsHelper.recordUserAction(MainActivity.this, EventConstant.CHANGE_MORE);
        }
        MoreSettingActivity.jumpToMoreSettingActivity(this);
    }

    public void messageClick(View view) {
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.recordUserAction(this, EventConstant.CHANGE_MESSAGE);
        }
        MessageCenterActivity.jumpToMessageCenter(this);
        dot_new_message.setVisibility(View.GONE);
        SharedPreferencesUtils.setConfigInt(this, SharedPreferencesUtils.KEY_NEW_MESSAGE, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreferencesUtils.getConfigInt(this, SharedPreferencesUtils.KEY_NEW_MESSAGE) == 1) {
            dot_new_message.setVisibility(View.VISIBLE);
        } else {
            dot_new_message.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateMsgDot(UpdateMesDotEvent event) {
        if (event != null && event.hasNewMsg == 1) {
            dot_new_message.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.detachView();
        }
        EventBus.getDefault().unregister(this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.REQUEST_CODE_ASSOCIATE_LOGIN) {
                AddCommunityActivity.jumpToAddCommunityForResult(this);
            } else if (requestCode == Constant.REQUEST_CODE_ASSOCIATE_MAIN) {//跳到最新
                if (associationFragment != null) {
                    associationFragment.setTagSelect(1);
                }
            }
        }
    }

    public void shareWorkDetail(WorkListBean workListBean) {
        ShareWindow.show(this, workListBean.title, ShareParaUtils.getWorkDetailShareDescription(workListBean.authorName),
                ShareParaUtils.getWorkDetailShareUrl(workListBean.id), workListBean.coverUrl, eventStatisticsHelper);
    }

    public void refreshComments(String commentId) {
        if (associationFragment != null) {
            associationFragment.refreshComments(commentId);
        }
    }

}
