package com.arun.a85mm.helper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.StatusBarUtils;

/**
 * Created by wy on 2017/5/5.
 */

public class ObjectAnimatorHelper {

    public static void showTopToastView(final Activity activity, final TextView toastView, String showName, final Fragment fragment) {
        StatusBarUtils.setStatusBar(activity, true);
        toastView.setVisibility(View.VISIBLE);
        toastView.setText(showName);
        ObjectAnimator animator = ObjectAnimator.ofFloat(toastView, "translationY", -DensityUtil.getStatusHeight(activity), 0);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideTopToastView(activity, toastView, fragment);
                    }
                }, 500);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //removeManagerView();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(500).start();
    }

    public static void hideTopToastView(final Activity activity, final TextView toastView, final Fragment fragment) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(toastView, "translationY", 0, -DensityUtil.getStatusHeight(activity));
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                toastView.setVisibility(View.GONE);
                if (fragment != null) {
                    if (fragment instanceof ProductionFragment) {
                        ProductionFragment productionFragment = (ProductionFragment) fragment;
                        productionFragment.setSaveImage(false);
                    }
                }
                StatusBarUtils.setStatusBar(activity, false);
                //removeManagerView();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //removeManagerView();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(500).start();
    }
}
