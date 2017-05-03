package com.arun.a85mm.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Application;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arun.a85mm.R;
import com.arun.a85mm.fragment.ArticleFragment;
import com.arun.a85mm.fragment.CommunityFragment;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.PermissionUtils;
import com.arun.a85mm.utils.StatusBarUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private ViewGroup rootView;
    private RelativeLayout activity_main;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    private String[] titles = new String[]{"最新", "精选", "文章"};
    private List<Fragment> list = new ArrayList<>();
    private TextView topCommonView;
    private TextView toastView;
    private ProductionFragment productionFragment;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initView() {
        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        //toastView = (TextView) findViewById(R.id.toastView);
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
        initToastView();
    }

    private void initToastView() {
       /* textView = new TextView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.getStatusHeight(this));
        textView.setLayoutParams(params);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setBackgroundResource(R.color.black);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        textView.setVisibility(View.GONE);*/
        toastView = (TextView) findViewById(R.id.toastView);
        topCommonView = (TextView) findViewById(R.id.topCommonView);
        if (toastView.getLayoutParams() != null && topCommonView.getLayoutParams() != null) {
            toastView.getLayoutParams().height = DensityUtil.getStatusHeight(this);
            topCommonView.getLayoutParams().height = DensityUtil.getStatusHeight(this);
        }

        /*if (toastView != null) {
            toastView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    toastView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                }
            });
        }*/
    }

    /*public void addManagerView() {
        if (PermissionUtils.checkAlertWindowsPermission(this)) {
            if (windowManager == null) {
                windowManager = getWindowManager();
            }
            WindowManager.LayoutParams param = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    DensityUtil.getStatusHeight(this),
                    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                            | WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    PixelFormat.TRANSLUCENT);

            param.gravity = Gravity.TOP;
            param.y = 0;
            param.x = 0;
            windowManager.addView(toastView, param);
        }
    }

    public void removeManagerView() {
        if (PermissionUtils.checkAlertWindowsPermission(this)) {
            if (windowManager != null) {
                windowManager.removeView(toastView);
            }
        }
    }*/

    private void initData() {
        productionFragment = new ProductionFragment();
        CommunityFragment communityFragment = new CommunityFragment();
        ArticleFragment articleFragment = ArticleFragment.newIntense();
        list.add(productionFragment);
        list.add(communityFragment);
        list.add(articleFragment);
    }

    public void showTopToastView(String showName) {
        //addManagerView();
        StatusBarUtils.setStatusBar(this, true);
        toastView.setVisibility(View.VISIBLE);
        //toastView.setText("");
        toastView.setText(showName);
        ObjectAnimator animator = ObjectAnimator.ofFloat(toastView, "translationY", -DensityUtil.getStatusHeight(this), 0);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideTopToastView();
                    }
                }, 500);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //removeManagerView();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(500).start();
    }

    public void hideTopToastView() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(toastView, "translationY", 0, -DensityUtil.getStatusHeight(this));
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                toastView.setVisibility(View.GONE);
                productionFragment.setSaveImage(false);
                StatusBarUtils.setStatusBar(MainActivity.this, false);
                //removeManagerView();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //removeManagerView();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(500).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
