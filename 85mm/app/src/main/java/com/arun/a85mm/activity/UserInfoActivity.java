package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.UserInfoBean;
import com.arun.a85mm.helper.UserManager;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    public RelativeLayout layout_user_head;
    public TextView text_user_name;
    public ImageView user_image;
    public RelativeLayout layout_user_name;
    public RelativeLayout layout_user_brief;
    public RelativeLayout layout_user_cover;
    public TextView exit_login;
    public UserInfoBean user;

    public static void jumpToUserInfo(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        initData();
    }

    private void initView() {
        layout_user_head = (RelativeLayout) findViewById(R.id.layout_user_head);
        text_user_name = (TextView) findViewById(R.id.text_user_name);
        user_image = (ImageView) findViewById(R.id.user_image);
        layout_user_name = (RelativeLayout) findViewById(R.id.layout_user_name);
        layout_user_brief = (RelativeLayout) findViewById(R.id.layout_user_brief);
        layout_user_cover = (RelativeLayout) findViewById(R.id.layout_user_cover);
        exit_login = (TextView) findViewById(R.id.exit_login);
        layout_user_head.setOnClickListener(this);
        layout_user_name.setOnClickListener(this);
        layout_user_brief.setOnClickListener(this);
        layout_user_cover.setOnClickListener(this);
        setTitle("个人信息");
        setBack();
    }

    private void initData() {
        user = UserManager.getInstance().getUserInfoBean();
        if (user != null) {
            text_user_name.setText(user.name);
            Glide.with(this).load(user.headerUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.mipmap.default_avatar)
                    .error(R.mipmap.default_avatar)
                    .centerCrop().bitmapTransform(new GlideCircleTransform(this)).into(user_image);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_user_head:

                break;
            case R.id.layout_user_name:
                UpdateUserNameActivity.jumpToUpdateUserName(this);
                break;
            case R.id.layout_user_brief:

                break;
            case R.id.layout_user_cover:

                break;
            case R.id.exit_login:

                break;
        }
    }
}
