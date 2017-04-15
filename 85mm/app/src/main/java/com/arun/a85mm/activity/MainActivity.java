package com.arun.a85mm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.arun.a85mm.R;
import com.arun.a85mm.fragment.ArticleFragment;
import com.arun.a85mm.fragment.CommunityFragment;
import com.arun.a85mm.fragment.ProducationFragment;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ArticleFragment.OnImageClickCallBack {

    private RelativeLayout activity_main;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    private String[] titles = new String[]{"作品", "社区", "文章"};
    private List<Fragment> list = new ArrayList<>();

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
    }

    private void initData() {
        /*for (int i = 0; i < titles.length; i++) {
            ArticleFragment articleFragment = ArticleFragment.newIntense();
            list.add(articleFragment);
        }*/
        ProducationFragment producationFragment = new ProducationFragment();
        CommunityFragment communityFragment = new CommunityFragment();
        ArticleFragment articleFragment = ArticleFragment.newIntense();
        list.add(producationFragment);
        list.add(communityFragment);
        list.add(articleFragment);
        articleFragment.setOnImageClickCallBack(this);
    }

    @Override
    public void onClickImage(View view, float x, float y, final String url) {
        if (view != null) {
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -y - tabLayout.getMeasuredHeight());
            translateAnimation.setFillAfter(true);
            translateAnimation.setDuration(1000);
            final ImageView imageView = new ImageView(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(view.getMeasuredWidth(), view.getMeasuredHeight());
            imageView.setLayoutParams(params);
            imageView.setX(x);
            imageView.setY(y);
            Glide.with(this).load(url).centerCrop().into(imageView);
            activity_main.addView(imageView);


            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Intent intent = new Intent(MainActivity.this, ArticleDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //intent.putExtra("'url", url);
                    MainActivity.this.startActivity(intent);
                    activity_main.removeView(imageView);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            imageView.setAnimation(translateAnimation);
        }

    }
}
