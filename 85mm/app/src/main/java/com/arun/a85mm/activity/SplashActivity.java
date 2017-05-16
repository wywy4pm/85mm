package com.arun.a85mm.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.helper.ObjectAnimatorManager;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.StatusBarUtils;

public class SplashActivity extends AppCompatActivity {
    public ImageView cover_Image;
    public TextView text_author;
    public LinearLayout layout_author;
    public ImageView image_85mm;
    public TextView text_slogan;
    public RelativeLayout activity_splash;
    public int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (getWindow() != null) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        initView();
        initData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAnimator();
            }
        }, 200);
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
    }

    private void showAnimator() {
        final int durationTranslate = 700;
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
}
