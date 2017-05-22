package com.arun.a85mm.activity;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.CommonApiResponse;
import com.arun.a85mm.bean.CommunityResponse;
import com.arun.a85mm.bean.ConfigResponse;
import com.arun.a85mm.helper.CommunityListCacheManager;
import com.arun.a85mm.helper.ObjectAnimatorManager;
import com.arun.a85mm.presenter.SettingPresenter;
import com.arun.a85mm.utils.CacheUtils;
import com.arun.a85mm.utils.DateUtils;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.DeviceUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.view.CommonView2;
import com.arun.a85mm.view.CommonView3;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.Serializable;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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

    private void initData() {
        if (settingPresenter == null) {
            settingPresenter = new SettingPresenter(this);
            settingPresenter.attachView(this);
            settingPresenter.getWorksGoods(SharedPreferencesUtils.getUid(this), DeviceUtils.getMobileIMEI(this), "");
        }
        showSplash();
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
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!isJumpToWebView) {
                                            MainActivity.jumpToMain(SplashActivity.this);
                                        }
                                        SplashActivity.this.finish();
                                    }
                                }, 1000);
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
            if (data instanceof CommonApiResponse) {
                CommonApiResponse config = (CommonApiResponse) data;
                SharedPreferencesUtils.saveUid(this, config.uid);
                SharedPreferencesUtils.setMoreImage(this, config.morePageImage);
                //CacheUtils.saveObject(this, CacheUtils.KEY_OBJECT_CONFIG, (Serializable) config.copyWrite);
                if (config.guidePage != null && config.guidePage instanceof List) {
                    List<ConfigResponse.GuidePageBean> list = (List<ConfigResponse.GuidePageBean>) config.guidePage;
                    if (list.size() == 2) {
                        CacheUtils.saveObject(this, CacheUtils.KEY_OBJECT_PRODUCT_RESPONSE, (Serializable) config.guidePage);
                    }
                    if (list.size() > 0 && list.get(0) != null) {
                        show(list.get(0));
                    } else {
                        errorIn();
                    }
                }
            }
        } else if (type == SettingPresenter.TYPE_WORKS) {
            if (data instanceof CommonApiResponse) {
                CommunityListCacheManager.setCommonApiResponse((CommonApiResponse) data);
            }
        }
    }

    private void show(final ConfigResponse.GuidePageBean bean) {
        Glide.with(this).load(bean.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.color.splash_bg)
                .error(R.color.splash_bg)
                .centerCrop()
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        errorIn();
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
        cover_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isJumpToWebView = true;
                WebViewActivity.jumpToWebViewActivity(SplashActivity.this, bean.linkUrl, String.valueOf(true));
            }
        });
    }

    @Override
    public void onError(int errorType, String errorMsg) {

    }

    @Override
    public void onError(int errorType, @StringRes int errorMsg) {
        errorIn();
    }

    @Override
    public void onRefreshComplete() {

    }

    @SuppressWarnings("unchecked")
    private void showSplash() {
        if (CacheUtils.getObject(this, CacheUtils.KEY_OBJECT_PRODUCT_RESPONSE) != null
                && CacheUtils.getObject(this, CacheUtils.KEY_OBJECT_PRODUCT_RESPONSE) instanceof List) {
            List<ConfigResponse.GuidePageBean> list = (List<ConfigResponse.GuidePageBean>)
                    CacheUtils.getObject(this, CacheUtils.KEY_OBJECT_PRODUCT_RESPONSE);
            if (list.size() == 2) {
                ConfigResponse.GuidePageBean first = list.get(0);
                ConfigResponse.GuidePageBean second = list.get(1);
                if (DateUtils.isToday(first.date)) {
                    show(first);
                } else if (DateUtils.isToday(second.date)) {
                    show(second);
                } else {
                    if (settingPresenter != null) {
                        settingPresenter.queryConfig(DeviceUtils.getMobileIMEI(this));
                    }
                }
            }
        } else {
            if (settingPresenter != null) {
                settingPresenter.queryConfig(DeviceUtils.getMobileIMEI(this));
            }
        }
    }

    private void errorIn() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.jumpToMain(SplashActivity.this);
                finish();
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (settingPresenter != null) {
            settingPresenter.detachView();
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
