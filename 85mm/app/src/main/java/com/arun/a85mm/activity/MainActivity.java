package com.arun.a85mm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.CommonFragmentPagerAdapter;
import com.arun.a85mm.bean.ActionBean;
import com.arun.a85mm.bean.AppInfoBean;
import com.arun.a85mm.bean.MenuListBean;
import com.arun.a85mm.bean.ShowTopBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.dialog.BrowserLimitDialog;
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
import com.arun.a85mm.helper.UserManager;
import com.arun.a85mm.service.DownLoadApkService;
import com.arun.a85mm.utils.AppUtils;
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

import java.lang.reflect.Field;
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
    private List<MenuListBean> menuList;
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
        } else {
            judgeShowUpdate(ConfigHelper.appInfo);
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

        menuList = ConfigHelper.menuList;
        String lastSelectName = SharedPreferencesUtils.getConfigString(this, SharedPreferencesUtils.KEY_MAIN_TAB_POS);
        int lastSelectPos = 0;
        if (menuList != null) {
            titles = new String[menuList.size()];
            for (int i = 0; i < titles.length; i++) {
                if (menuList.get(i) != null) {
                    if (!TextUtils.isEmpty(menuList.get(i).showName)
                            && menuList.get(i).showName.equals(lastSelectName)) {
                        lastSelectPos = i;
                    }
                    titles[i] = menuList.get(i).showName;
                    int type = menuList.get(i).dataType;
                    if (type == -3 || type == -2 || type == -1 || type == 0 || type == 9 || type == 1) {
                        Fragment fragment = null;
                        if (type == -3) {
                            fragment = CommunityFragment.newInstance();
                        } else if (type == -2) {
                            fragment = ArticleFragment.newIntense();
                        } else if (type == -1) {
                            fragment = WebViewFragment.getInstance(menuList.get(i).url);
                        } else if (type == 0 || type == 9) {
                            fragment = ProductionFragment.newInstance(type, menuList.get(i).tagName);
                            /*if (type == 0) {
                                productionFragment = (ProductionFragment) fragment;
                            }*/
                        } else {
                            fragment = new LeftWorksFragment();
                        }
                        list.add(fragment);
                    } else if (type == 3) {
                        associationFragment = AssociationFragment.getInstance();
                        list.add(associationFragment);
                    }
                }
            }
        }

        viewPager.setAdapter(new CommonFragmentPagerAdapter(getSupportFragmentManager(), list));
        tabLayout.setViewPager(viewPager, titles);
        setCurrentTab(lastSelectPos);
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
                //int type = -1;
                if (menuList != null && position <= menuList.size()) {
                    MenuListBean bean = menuList.get(position);
                    if (bean != null) {
                        SharedPreferencesUtils.setConfigString(MainActivity.this, SharedPreferencesUtils.KEY_MAIN_TAB_POS, bean.showName);
                       /* int dataType = bean.dataType;
                        if (dataType == -3) {
                            type = EventConstant.CHANGE_WORK;
                        } else if (dataType == -2) {
                            type = EventConstant.CHANGE_ARTICLE;
                        } else if (dataType == 0) {
                            type = EventConstant.OPEN_LATEST;
                        } else if (dataType == 1) {
                            type = EventConstant.CHANGE_ASSOCIATION;
                        } else if (dataType == 9) {
                            type = EventConstant.CHANGE_AUDIT;
                        }*/
                        if (eventStatisticsHelper != null) {
                            eventStatisticsHelper.recordUserAction(MainActivity.this, EventConstant.TAB_CHANGE, "", bean.showName);
                        }
                    }
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

    /*private void initData() {
        //productionFragment = ProductionFragment.newInstance();
        CommunityFragment communityFragment = CommunityFragment.newInstance();
        associationFragment = AssociationFragment.getInstance();
        ArticleFragment articleFragment = ArticleFragment.newIntense();
        //list.add(productionFragment);
        list.add(communityFragment);
        list.add(associationFragment);
        list.add(articleFragment);
    }*/

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

    public void saveImageShowTop(String workId, String coverUrl, String authorName) {
        if (saveImageHelper != null && showTopHandler != null) {
            if (!TextUtils.isEmpty(workId)) {
                onActionEvent(EventConstant.WORK_IMAGE_DOWNLOAD, EventStatisticsHelper.createOneActionList(EventConstant.WORK_IMAGE_DOWNLOAD, workId, coverUrl));
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

    public void addWorkClick(View view) {
        if (UserManager.getInstance() != null) {
            if (UserManager.getInstance().isLogin()) {
                AddCommunityActivity.jumpToAddCommunityForResult(this);
            } else {
                LoginActivity.jumpToLoginForResult(this);
            }
        }
    }

    public void judgeShowUpdate(final AppInfoBean appInfoBean) {
        /*appInfoBean.update = 1;
        appInfoBean.forceUpdate = 0;*/
        if (appInfoBean != null && appInfoBean.update == 1) {//需要更新
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("版本更新");
            String forceUpdateDes = appInfoBean.forceUpdate == 1 ? "需强制更新，" : "";
            String updateDes = "发现新版本，" + forceUpdateDes + "是否更新?";
            builder.setMessage(updateDes);
            builder.setPositiveButton("暂不", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (appInfoBean.forceUpdate == 1) {//强制更新
                        finish();
                    }
                }
            });
            builder.setNegativeButton("更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (appInfoBean.forceUpdate == 1) {
                        try {//下面三句控制弹框的关闭
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(dialog, false);//false不关闭
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (!TextUtils.isEmpty(appInfoBean.downloadUrl)) {
                        if (!AppUtils.isServiceRunning(MainActivity.this, DownLoadApkService.class.getName())) {
                            DownLoadApkService.startDownLoadApk(MainActivity.this, appInfoBean.downloadUrl);
                            Toast.makeText(MainActivity.this, "更新安装包正在下载，请耐心等待", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "当前更新安装包已经在下载了", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            AlertDialog dialog = builder.create();
            if (appInfoBean.forceUpdate == 1) {//强制更新
                dialog.setCancelable(false);
            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
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
                /*if (associationFragment != null) {
                    associationFragment.setTagSelect(3);
                }*/
                /*if (productionFragment != null) {
                    productionFragment.refreshData();
                }*/
            }
        }
    }

    private void setCurrentTab(int currentTabPos) {
        TextViewUtils.setTextBold(tabLayout.getTitleView(currentTabPos), true);
        viewPager.setCurrentItem(currentTabPos);
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
