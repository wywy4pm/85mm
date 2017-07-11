package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.AuditListAdapter;
import com.arun.a85mm.adapter.UserMainAdapter;
import com.arun.a85mm.bean.AuthorInfo;
import com.arun.a85mm.bean.UserInfo;
import com.arun.a85mm.bean.UserMainPageBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.presenter.UserMainPagePresenter;
import com.arun.a85mm.utils.DrawableUtils;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.view.CommonView4;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class UserMainActivity extends BaseActivity implements CommonView4<UserMainPageBean> {
    public ImageView image_back;
    public TextView title;
    public ImageView image_right;
    public ImageView user_head_image;
    public TextView user_name;
    public TextView user_brief;
    public SlidingTabLayout tabLayout;
    public RelativeLayout user_head_bg;
    public RecyclerView recyclerView;
    public RelativeLayout activity_user_main;
    private UserMainPagePresenter presenter;
    private UserMainAdapter adapter;
    private List<WorkListBean> workList = new ArrayList<>();
    public static final String KEY_AUTHOR_ID = "authorId";
    private String authorId;

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
        initView();
        initData();
    }

    private void initView() {
        this.image_back = (ImageView) findViewById(R.id.image_back);
        this.title = (TextView) findViewById(R.id.title);
        this.image_right = (ImageView) findViewById(R.id.image_right);
        this.user_head_image = (ImageView) findViewById(R.id.user_head_image);
        this.user_name = (TextView) findViewById(R.id.user_name);
        this.user_brief = (TextView) findViewById(R.id.user_brief);
        this.tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
        this.user_head_bg = (RelativeLayout) findViewById(R.id.user_head_bg);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.activity_user_main = (RelativeLayout) findViewById(R.id.activity_user_main);

        adapter = new UserMainAdapter(this, workList);
        setImageRight(image_right, R.mipmap.message_white);
        setCommonShow();
    }

    private void initData() {
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
                user_name.setText(authorInfo.nickName);
                user_brief.setText(authorInfo.description);
            }
        }
    }

    @Override
    public void refreshMore(UserMainPageBean data) {

    }

    @Override
    public void refresh(int type, Object data) {

    }
}
