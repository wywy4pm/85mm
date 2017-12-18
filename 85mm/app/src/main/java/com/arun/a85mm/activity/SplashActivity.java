package com.arun.a85mm.activity;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.AllUserBodyBean;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.ConfigBean;
import com.arun.a85mm.bean.GuidePageBean;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.helper.AppHelper;
import com.arun.a85mm.helper.CommunityListCacheManager;
import com.arun.a85mm.helper.ConfigHelper;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.helper.ObjectAnimatorManager;
import com.arun.a85mm.helper.UrlJumpHelper;
import com.arun.a85mm.helper.UserManager;
import com.arun.a85mm.presenter.SettingPresenter;
import com.arun.a85mm.utils.BitmapUtils;
import com.arun.a85mm.utils.CacheUtils;
import com.arun.a85mm.utils.DateUtils;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.GsonUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.view.CommonView3;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.umeng.message.PushAgent;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity implements CommonView3 {
    public ImageView cover_Image;
    public TextView text_author;
    public LinearLayout layout_author;
    public ImageView image_85mm;
    public TextView text_slogan;
    public RelativeLayout activity_splash;
    public int screenHeight;
    private SettingPresenter settingPresenter;

    private boolean isJumpToWebView;
    private boolean isShowCache;
    private String type;
    private Map<String, String> map;
    private EventStatisticsHelper helper;
    private boolean isAnimationEnd;
    private boolean isConfigComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        PushAgent.getInstance(this).onAppStart();
        if (getWindow() != null) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        initView();
        initData();
    }

    private void initView() {
        cover_Image = (ImageView) findViewById(R.id.cover_Image);
        text_author = (TextView) findViewById(R.id.text_author);
        layout_author = (LinearLayout) findViewById(R.id.layout_author);
        image_85mm = (ImageView) findViewById(R.id.image_85mm);
        text_slogan = (TextView) findViewById(R.id.text_slogan);
        activity_splash = (RelativeLayout) findViewById(R.id.activity_splash);
        screenHeight = DensityUtil.getScreenHeight(SplashActivity.this);
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        helper = new EventStatisticsHelper(this);
        helper.recordUserAction(this, EventConstant.APP_START);

        if (settingPresenter == null) {
            settingPresenter = new SettingPresenter(this);
            settingPresenter.attachView(this);
            settingPresenter.getWorkMix();
            //settingPresenter.getUserInfo();
        }
        showSplash();
        if (getIntent() != null && getIntent().getExtras() != null) {
            type = getIntent().getExtras().getString(UrlJumpHelper.KEY_JUMP_APP);
            map = (Map<String, String>) getIntent().getExtras().getSerializable(UrlJumpHelper.KEY_JUMP_MAP);
        }

        ConfigHelper.isShowWImage = SharedPreferencesUtils.getConfigInt(this, SharedPreferencesUtils.KEY_WIDTH_IMAGE);
    }

    private void showAnimator() {
        final int durationTranslate = 800;
        final int durationAlpha = 500;

        final float firstStartOffsetY = 0;
        float firstOffset = image_85mm.getMeasuredHeight();
        float firstEndOffsetY = firstStartOffsetY + firstOffset;

        ObjectAnimatorManager.translationY(image_85mm, firstStartOffsetY, firstEndOffsetY, durationTranslate, null);
        ObjectAnimatorManager.translationY(text_slogan, firstStartOffsetY, firstEndOffsetY, durationTranslate, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                layout_author.setVisibility(View.VISIBLE);
                ObjectAnimatorManager.alpha(layout_author, 0f, 1f, durationAlpha, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        final float secondStartOffsetY = image_85mm.getMeasuredHeight();
                        final float secondOffset = text_slogan.getMeasuredHeight() + text_slogan.getPaddingBottom();
                        final float secondEndOffset = secondStartOffsetY + secondOffset;

                        ObjectAnimatorManager.translationY(layout_author, 0, secondOffset, durationTranslate, null);

                        ObjectAnimatorManager.translationY(image_85mm, secondStartOffsetY, secondEndOffset, durationTranslate, null);
                        ObjectAnimatorManager.translationY(text_slogan, secondStartOffsetY, secondEndOffset + DensityUtil.dp2px(SplashActivity.this, 18), durationTranslate, new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                ObjectAnimatorManager.alpha(text_slogan, 1f, 0f, durationAlpha, null);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                isAnimationEnd = true;
                                if (isConfigComplete) {
                                    jumpToMain(true);
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        });
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void refresh(int type, Object data) {
        if (type == SettingPresenter.TYPE_CONFIG) {
            if (data instanceof ConfigBean) {
                final ConfigBean config = (ConfigBean) data;
                SharedPreferencesUtils.saveUid(this, config.uid);
                if (AppHelper.getInstance().getAppConfig() != null) {
                    AppHelper.getInstance().getAppConfig().setUid(config.uid);
                }
                if (config.hideRead != null) {
                    SharedPreferencesUtils.setConfigInt(this, SharedPreferencesUtils.KEY_HIDE_READ_ENABLED, config.hideRead.hideReadEnable);
                    SharedPreferencesUtils.setConfigInt(this, SharedPreferencesUtils.KEY_HIDE_READ_OPENED, config.hideRead.hideReadOpen);
                }

                ConfigHelper.userTags = config.userTagList;
                ConfigHelper.menuList = config.menuList;
                //ConfigHelper.customMenuList = config.customMenuList;
                SharedPreferencesUtils.setConfigInt(this, SharedPreferencesUtils.KEY_NEW_MESSAGE, config.hasNewMsg);
                UserManager.getInstance().setLogin(config.isLogin == 1);
                UserManager.getInstance().setUserInfoBean(config.userInfo);
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                CacheUtils.saveBitmap(SplashActivity.this, CacheUtils.KEY_BITMAP_CONFIG, BitmapUtils.createBitmapByUrl(config.morePageImage));
                            }
                        }
                ).start();
                if (config.guideList != null) {
                    List<GuidePageBean> list = config.guideList;
                    if (list.size() == 2) {
                        CacheUtils.saveObject(this, CacheUtils.KEY_OBJECT_PRODUCT_RESPONSE, (Serializable) list);
                    }
                    isConfigComplete = true;
                    if (!isShowCache) {
                        if (list.size() > 0 && list.get(0) != null) {
                            show(list.get(0));
                        } else {
                            jumpToMain(false);
                        }
                    } else {
                        if (isAnimationEnd) {
                            jumpToMain(false);
                        }
                    }
                }
            }
        } else if (type == SettingPresenter.TYPE_WORKS) {
            if (data instanceof CommonApiResponse) {
                CommunityListCacheManager.setCommonApiResponse((CommonApiResponse) data);
            }
        } /*else if (type == SettingPresenter.TYPE_USER_INFO) {
            if (data instanceof AllUserBodyBean) {
                AllUserBodyBean bean = (AllUserBodyBean) data;
                if (bean.userInfo != null) {
                    ConfigHelper.userInfoBean = bean.userInfo;
                }
            }
        }*/
    }

    private void show(final GuidePageBean bean) {
        Glide.with(this).load(bean.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.color.splash_bg)
                .error(R.color.splash_bg)
                .centerCrop()
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        //jumpToMain(false);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        showAnimator();
                        return false;
                    }
                })
                .into(cover_Image);
        text_author.setText(bean.author);
        layout_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConfigComplete) {
                    if (helper != null) {
                        helper.recordUserAction(SplashActivity.this, EventConstant.CLICK_SPLASH);
                    }
                    isJumpToWebView = true;
                    WebViewActivity.jumpToWebViewActivity(SplashActivity.this, bean.linkUrl, WebViewActivity.TYPE_SPLASH);
                    finish();
                }
            }
        });
    }

    @Override
    public void onError(int errorType, String errorMsg) {

    }

    @Override
    public void onError(int errorType, @StringRes int errorMsg) {
        //Toast.makeText(this, "网络异常，请退出重试", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRefreshComplete() {

    }

    @SuppressWarnings("unchecked")
    private void showSplash() {
        if (CacheUtils.getObject(this, CacheUtils.KEY_OBJECT_PRODUCT_RESPONSE) != null
                && CacheUtils.getObject(this, CacheUtils.KEY_OBJECT_PRODUCT_RESPONSE) instanceof List) {
            List<GuidePageBean> list = (List<GuidePageBean>)
                    CacheUtils.getObject(this, CacheUtils.KEY_OBJECT_PRODUCT_RESPONSE);
            if (list.size() == 2) {
                GuidePageBean first = list.get(0);
                GuidePageBean second = list.get(1);
                if (DateUtils.isToday(first.date)) {
                    show(first);
                    isShowCache = true;
                } else if (DateUtils.isToday(second.date)) {
                    show(second);
                    isShowCache = true;
                }
                if (settingPresenter != null) {
                    settingPresenter.queryConfig();
                }
            }
        } else {
            if (settingPresenter != null) {
                settingPresenter.queryConfig();
            }
        }
    }

    private synchronized void jumpToMain(boolean isWait) {
        if (isWait) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    inMain();
                }
            }, 500);
        } else {
            inMain();
        }
    }

    private void inMain() {
        if (!isJumpToWebView) {
            MainActivity.jumpToMain(SplashActivity.this, type, map);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (settingPresenter != null) {
            settingPresenter.detachView();
        }
        if (helper != null) {
            helper.detachView();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
