package com.arun.a85mm.helper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.IntegerRes;
import android.view.View;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.BaseActivity;
import com.arun.a85mm.activity.MainActivity;
import com.arun.a85mm.bean.ShowTopBean;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy on 2017/5/5.
 */

public class ObjectAnimatorHelper {

    private ObjectAnimator startAnimator;
    private ObjectAnimator endAnimator;
    private boolean isSetToastParent = false;
    private List<ShowTopBean> waitShowTops = new ArrayList<>();

    public ObjectAnimatorHelper() {
    }

    public ObjectAnimatorHelper(boolean isSetToastParent) {
        this.isSetToastParent = isSetToastParent;
    }

    public void managerShowTopView(final Activity activity, final TextView toastView, ShowTopBean showTopBean) {
        if (!showTopBean.isShowingTop) {
            showTopToastView(activity, toastView, showTopBean.showData, showTopBean.backgroundResId);
        } else {
            waitShowTops.add(showTopBean);
        }
    }

    public void showTopToastView(final Activity activity, final TextView toastView, String showData, @ColorRes int backgroundResId) {
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).setShowingTop(true);
        } else if (activity instanceof BaseActivity) {
            ((BaseActivity) activity).setShowingTop(true);
        }

        StatusBarUtils.setStatusBar(activity, true);
        if (isSetToastParent && toastView.getParent() != null) {
            ((View) toastView.getParent()).setVisibility(View.VISIBLE);
        }
        toastView.setVisibility(View.VISIBLE);
        toastView.setText(showData);
        if (backgroundResId > 0) {
            toastView.setBackgroundResource(backgroundResId);
        } else {
            toastView.setBackgroundResource(R.color.black);
        }
        startAnimator = ObjectAnimator.ofFloat(toastView, "translationY", -DensityUtil.getStatusHeight(activity), 0);
        startAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideTopToastView(activity, toastView);
                    }
                }, 500);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        startAnimator.setDuration(500).start();
    }

    public void hideTopToastView(final Activity activity, final TextView toastView) {

        endAnimator = ObjectAnimator.ofFloat(toastView, "translationY", 0, -DensityUtil.getStatusHeight(activity));
        endAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isSetToastParent && toastView.getParent() != null) {
                    ((View) toastView.getParent()).setVisibility(View.GONE);
                }
                toastView.setVisibility(View.GONE);
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).setShowingTop(false);
                } else if (activity instanceof BaseActivity) {
                    ((BaseActivity) activity).setShowingTop(false);
                }
                StatusBarUtils.setStatusBar(activity, false);
                if (waitShowTops != null && waitShowTops.size() > 0) {
                    ShowTopBean nextShow = waitShowTops.get(0);
                    showTopToastView(activity, toastView, nextShow.showData, nextShow.backgroundResId);
                    waitShowTops.remove(0);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        endAnimator.setDuration(500).start();
    }
}
