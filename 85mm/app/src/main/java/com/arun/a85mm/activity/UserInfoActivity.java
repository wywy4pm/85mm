package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.MMApplication;
import com.arun.a85mm.R;
import com.arun.a85mm.bean.UserInfoBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.helper.DialogHelper;
import com.arun.a85mm.helper.OssUploadImageHelper;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.helper.UserManager;
import com.arun.a85mm.listener.UploadImageListener;
import com.arun.a85mm.presenter.UserPresenter;
import com.arun.a85mm.utils.DrawableUtils;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.view.CommonView3;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener, CommonView3 {

    public RelativeLayout layout_user_head;
    public TextView text_user_name;
    public ImageView user_image;
    public RelativeLayout layout_user_name;
    public RelativeLayout layout_user_brief;
    public RelativeLayout layout_user_cover;
    public TextView exit_login;
    public UserInfoBean user;
    private String headerUrl;
    private String coverUrl;
    private UserPresenter userPresenter;

    public static void jumpToUserInfo(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
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
        exit_login.setOnClickListener(this);
        setTitle("个人信息");
        setBack();
        setCommonShow();
    }

    private void initData() {
        userPresenter = new UserPresenter(this);
        userPresenter.attachView(this);
        user = UserManager.getInstance().getUserInfoBean();
        if (user != null) {
            updateName(user.name);
            updateImage(user.headUrl);
        }
    }

    private void updateImage(String imageUrl) {
        GradientDrawable drawable = DrawableUtils.getHeadBgDrawable(user_image);
        Glide.with(this)
                .load(imageUrl)
                .placeholder(drawable)
                .error(drawable)
                .centerCrop()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        user_image.setImageResource(R.mipmap.default_avatar);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .bitmapTransform(new GlideCircleTransform(this))
                .into(user_image);
    }

    private void updateName(String userName) {
        text_user_name.setText(userName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_user_head:
                DialogHelper.showUploadImageBottom(this, Constant.REQUEST_CODE_ALBUM_HEAD);
                break;
            case R.id.layout_user_name:
                UpdateUserNameActivity.jumpToUpdateUserName(this);
                break;
            case R.id.layout_user_brief:
                UpdateUserBriefActivity.jumpToUpdateUserBrief(this);
                break;
            case R.id.layout_user_cover:
                DialogHelper.showUploadImageBottom(this, Constant.REQUEST_CODE_ALBUM_COVER);
                break;
            case R.id.exit_login:
                if (userPresenter != null) {
                    userPresenter.userLogout();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (requestCode == Constant.REQUEST_CODE_ALBUM_HEAD) {
                OssUploadImageHelper.uploadImage(this, uri, new UploadImageListener() {
                    @Override
                    public void uploadPrepare(String imageUrl) {
                        headerUrl = imageUrl;
                        if (userPresenter != null) {
                            userPresenter.updateUserInfo("", headerUrl, "", "");
                        }
                    }

                    @Override
                    public void uploadSuccess(String imageUrl) {
                    }
                });
            } else if (requestCode == Constant.REQUEST_CODE_ALBUM_COVER) {
                OssUploadImageHelper.uploadImage(this, uri, new UploadImageListener() {
                    @Override
                    public void uploadPrepare(String imageUrl) {
                        coverUrl = imageUrl;
                        if (userPresenter != null) {
                            userPresenter.updateUserInfo("", "", "", coverUrl);
                        }
                    }

                    @Override
                    public void uploadSuccess(String imageUrl) {
                    }
                });
            }
        }
    }

    @Override
    public void refresh(int type, Object data) {
        if (type == UserPresenter.TYPE_UPDATE_USER_HEAD) {
            UserManager.getInstance().setUserHead(headerUrl);
            updateImage(headerUrl);
        } else if (type == UserPresenter.TYPE_UPDATE_USER_COVER) {
            UserManager.getInstance().setUserCover(coverUrl);
            showTop("修改成功");
        } else if (type == UserPresenter.TYPE_LOG_OUT) {
            UserManager.getInstance().setUserInfoBean(null);
            UserManager.getInstance().setLogin(false);
            onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        user = UserManager.getInstance().getUserInfoBean();
        if (user != null) {
            updateName(user.name);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userPresenter != null) {
            userPresenter.detachView();
        }
    }
}
