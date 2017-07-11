package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.GuidePageBean;
import com.arun.a85mm.bean.UserInfo;
import com.arun.a85mm.bean.UserInfoBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.helper.LoginHelper;
import com.arun.a85mm.helper.UserManager;
import com.arun.a85mm.listener.LoginListener;
import com.arun.a85mm.presenter.LoginPresenter;
import com.arun.a85mm.utils.CacheUtils;
import com.arun.a85mm.utils.DateUtils;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.view.CommonView2;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener, CommonView2, LoginListener {

    public ImageView login_close;
    public ImageView back_image;
    /*public TextView wechat_text;
    public ImageView wechat_icon;*/
    private RelativeLayout layout_wechat;
    private LoginPresenter presenter;
    private String userName;
    private String headUrl;

    public static void jumpToLoginForResult(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        ((Activity) context).startActivityForResult(intent, Constant.REQUEST_CODE_ASSOCIATE_LOGIN);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initView() {
        login_close = (ImageView) findViewById(R.id.login_close);
        back_image = (ImageView) findViewById(R.id.back_image);
        /*wechat_text = (TextView) findViewById(R.id.wechat_text);
        wechat_icon = (ImageView) findViewById(R.id.wechat_icon);*/
        layout_wechat = (RelativeLayout) findViewById(R.id.layout_wechat);
        login_close.setOnClickListener(this);
        layout_wechat.setOnClickListener(this);

        if (login_close.getLayoutParams() != null && login_close.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) login_close.getLayoutParams()).setMargins(DensityUtil.dp2px(this, 23), DensityUtil.dp2px(this, 5) + DensityUtil.getStatusHeight(this), 0, 0);
        }
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        if (CacheUtils.getObject(this, CacheUtils.KEY_OBJECT_PRODUCT_RESPONSE) != null
                && CacheUtils.getObject(this, CacheUtils.KEY_OBJECT_PRODUCT_RESPONSE) instanceof List) {
            List<GuidePageBean> list = (List<GuidePageBean>)
                    CacheUtils.getObject(this, CacheUtils.KEY_OBJECT_PRODUCT_RESPONSE);
            if (list.size() == 2) {
                GuidePageBean first = list.get(0);
                GuidePageBean second = list.get(1);
                if (DateUtils.isToday(first.date)) {
                    show(first);
                } else if (DateUtils.isToday(second.date)) {
                    show(second);
                }
            }
        }
        if (presenter == null) {
            presenter = new LoginPresenter(this);
            presenter.attachView(this);
        }
    }

    private void show(final GuidePageBean bean) {
        Glide.with(this).load(bean.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.color.splash_bg)
                .error(R.color.splash_bg)
                .centerCrop()
                .crossFade()
                .into(back_image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_close:
                onBackPressed();
                break;
            case R.id.layout_wechat:
                LoginHelper.login(this, this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoginSuccess(UserInfo userInfo) {
        if (userInfo != null) {
            postUserInfo(userInfo);
        }
    }

    private void postUserInfo(UserInfo userInfo) {
        if (presenter != null) {
            userName = userInfo.nickName;
            headUrl = userInfo.headUrl;
            presenter.postLoginInfo(userInfo.openId, headUrl, userName);
        }
    }

    @Override
    public void onLoginFailed() {
        //showTop("登录失败");
    }

    @Override
    public void refresh(Object data) {
        if (UserManager.getInstance() != null) {
            UserManager.getInstance().setLogin(true);
            UserInfoBean userInfoBean = new UserInfoBean(userName, headUrl, "", "");
            UserManager.getInstance().setUserInfoBean(userInfoBean);
        }
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
