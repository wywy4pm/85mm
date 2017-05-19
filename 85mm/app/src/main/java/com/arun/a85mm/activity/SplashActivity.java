package com.arun.a85mm.activity;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.ConfigResponse;
import com.arun.a85mm.bean.ProductListResponse;
import com.arun.a85mm.helper.ObjectAnimatorManager;
import com.arun.a85mm.presenter.SettingPresenter;
import com.arun.a85mm.utils.CacheUtils;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.DeviceUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.view.CommonView2;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.Serializable;

public class SplashActivity extends AppCompatActivity implements CommonView2 {
    public ImageView cover_Image;
    public TextView text_author;
    public LinearLayout layout_author;
    public ImageView image_85mm;
    public TextView text_slogan;
    public RelativeLayout activity_splash;
    public int screenHeight;
    private SettingPresenter settingPresenter;
    private ProductListResponse response;

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
            settingPresenter.queryConfig(DeviceUtils.getMobileIMEI(this));
            settingPresenter.getProductListData(SharedPreferencesUtils.getUid(this), DeviceUtils.getMobileIMEI(this), "'");
        }
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
                                        MainActivity.jumpToMain(SplashActivity.this, response);
                                        SplashActivity.this.finish();
                                    }
                                }, 3000);
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

    @Override
    public void refresh(Object data) {
        if (data instanceof ConfigResponse) {
            ConfigResponse config = (ConfigResponse) data;
            SharedPreferencesUtils.saveUid(this, config.uid);
            //CacheUtils.saveObject(this, CacheUtils.KEY_OBJECT_CONFIG, (Serializable) config.copyWrite);
            SharedPreferencesUtils.setMoreImage(this, config.morePageImage);
            if (config.guidePage != null) {
                final ConfigResponse.GuidePageBean bean = config.guidePage;
                Glide.with(this).load(bean.imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.color.splash_bg)
                        .error(R.color.splash_bg)
                        .centerCrop()
                        .crossFade()
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
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
                        WebViewActivity.jumpToWebViewActivity(SplashActivity.this, bean.linkUrl);
                    }
                });
            }
        } else if (data instanceof ProductListResponse) {
            response = (ProductListResponse) data;
        }
    }

    @Override
    public void onError(String error, String tag) {
        Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (settingPresenter != null) {
            settingPresenter.detachView();
        }
    }
}
