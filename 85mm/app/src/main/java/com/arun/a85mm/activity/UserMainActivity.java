package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.CommonFragmentPagerAdapter;
import com.arun.a85mm.bean.AuthorInfo;
import com.arun.a85mm.bean.UserMainPageBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.fragment.MainPageFragment;
import com.arun.a85mm.listener.AppBarStateChangeListener;
import com.arun.a85mm.presenter.UserMainPagePresenter;
import com.arun.a85mm.utils.DrawableUtils;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.utils.TextViewUtils;
import com.arun.a85mm.view.CommonView4;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class UserMainActivity extends BaseActivity implements CommonView4<UserMainPageBean> {
    public ImageView image_back;
    public TextView title;
    private View line;
    public ImageView image_right;
    public ImageView user_head_image;
    public TextView user_name;
    public TextView user_brief;
    public SlidingTabLayout tabLayout;
    public RelativeLayout user_head_bg;
    public ViewPager viewPager;
    private AppBarLayout appBarLayout;
    private UserMainPagePresenter presenter;

    public static final String KEY_AUTHOR_ID = "authorId";
    private String authorId;
    private String[] titles = new String[]{"发布", "下载"};
    private List<Fragment> list = new ArrayList<>();
    private MainPageFragment publishFragment;
    private MainPageFragment downloadFragment;
    public static final int TYPE_PUBLISH = 7;
    public static final int TYPE_DOWNLOAD = 8;
    private String nickName;

    public static void jumpToUserMain(Context context, String authorId) {
        Intent intent = new Intent(context, UserMainActivity.class);
        intent.putExtra(KEY_AUTHOR_ID, authorId);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
        initData();
        initView();
        setData();
    }

    private void initData() {
        publishFragment = MainPageFragment.getInstance(TYPE_PUBLISH);
        downloadFragment = MainPageFragment.getInstance(TYPE_DOWNLOAD);
        list.add(publishFragment);
        list.add(downloadFragment);
    }

    private void initView() {
        image_back = (ImageView) findViewById(R.id.image_back);
        title = (TextView) findViewById(R.id.title);
        title.setTextColor(getResources().getColor(R.color.charcoalgrey));
        line = findViewById(R.id.line);
        image_right = (ImageView) findViewById(R.id.image_right);
        user_head_image = (ImageView) findViewById(R.id.user_head_image);
        user_name = (TextView) findViewById(R.id.user_name);
        user_brief = (TextView) findViewById(R.id.user_brief);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
        user_head_bg = (RelativeLayout) findViewById(R.id.user_head_bg);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    title.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    title.setVisibility(View.VISIBLE);
                    title.setText(nickName);
                    line.setVisibility(View.VISIBLE);
                } else {
                    //中间状态
                    title.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);
                }
            }
        });

        image_right.setVisibility(View.VISIBLE);
        image_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageActivity.jumpToSendMessage(UserMainActivity.this, authorId);
            }
        });
        setImageRight(image_right, R.mipmap.message_center);
        setBack();
        setCommonShow();

        viewPager.setAdapter(new CommonFragmentPagerAdapter(getSupportFragmentManager(), list));
        tabLayout.setViewPager(viewPager, titles);
        TextViewUtils.setTextBold(tabLayout.getTitleView(0), true);
        viewPager.setCurrentItem(0);
        setListener();
    }

    private void setListener() {

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

    private void setData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            authorId = getIntent().getExtras().getString(KEY_AUTHOR_ID);
        }
        presenter = new UserMainPagePresenter(this);
        presenter.attachView(this);
        presenter.getMainPage(authorId);
    }

    @Override
    public void refresh(UserMainPageBean data) {
        if (data != null) {
            AuthorInfo authorInfo = data.authorInfo;
            if (authorInfo != null) {
                GradientDrawable gd = DrawableUtils.getHeadBgDrawable(user_head_image);
                Glide.with(this)
                        .load(authorInfo.headerUrl)
                        .placeholder(gd)
                        .error(gd)
                        .centerCrop()
                        .bitmapTransform(new GlideCircleTransform(this))
                        .into(user_head_image);
                nickName = authorInfo.nickName;
                user_name.setText(authorInfo.nickName);
                user_brief.setText(authorInfo.description);
            }
            publishFragment.setData(data.publishWorkList);
            //publishFragment.setAuthorId(authorId);

            downloadFragment.setData(data.downloadWorkList);
            //downloadFragment.setAuthorId(authorId);
        }
    }

    @Override
    public void refreshMore(UserMainPageBean data) {

    }

    @Override
    public void refresh(int type, Object data) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
