package com.arun.a85mm.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.fragment.ArticleFragment;
import com.arun.a85mm.fragment.CommunityFragment;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.utils.DensityUtil;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private ViewGroup rootView;
    private RelativeLayout activity_main;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    private String[] titles = new String[]{"作品", "社区", "文章"};
    private List<Fragment> list = new ArrayList<>();
    private TextView textView;
    private WindowManager windowManager;
    private ProductionFragment productionFragment;

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

        textView = new TextView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.getStatusHeight(this));
        textView.setLayoutParams(params);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setBackgroundResource(R.color.black);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        //textView.setText("");
        textView.setVisibility(View.GONE);

        addManagerView();
    }

    private void addManagerView() {
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
        windowManager.addView(textView, param);
    }

    private void initData() {
        productionFragment = new ProductionFragment();
        CommunityFragment communityFragment = new CommunityFragment();
        ArticleFragment articleFragment = ArticleFragment.newIntense();
        list.add(productionFragment);
        list.add(communityFragment);
        list.add(articleFragment);
    }

    public void showTopToastView(String showName) {
        //initToastView();
        textView.setVisibility(View.VISIBLE);
        textView.setText(showName);
        ObjectAnimator animator = ObjectAnimator.ofFloat(textView, "translationY", -DensityUtil.getStatusHeight(this), 0);
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
                //hideTopToastView();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //toastView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(500).start();
    }

    public void hideTopToastView() {
        //initToastView();
        ObjectAnimator animator = ObjectAnimator.ofFloat(textView, "translationY", 0, -DensityUtil.getStatusHeight(this));
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setVisibility(View.GONE);
                productionFragment.setSaveImage(false);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //toastView.setVisibility(View.GONE);
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
        if (windowManager != null) {
            windowManager.removeView(textView);
        }
    }
}
