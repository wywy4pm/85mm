package com.arun.a85mm.helper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by wy on 2017/5/16.
 */

public class ObjectAnimatorManager {

    public static void translationY(View view, float startOffsetY, float endOffsetY, int duration, Animator.AnimatorListener listener) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", startOffsetY, endOffsetY);
        if (listener != null) {
            objectAnimator.addListener(listener);
        }
        objectAnimator.setDuration(duration).start();
    }

    public static void translationX(View view, float startOffsetX, float endOffsetX, int duration, Animator.AnimatorListener listener) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationX", startOffsetX, endOffsetX);
        if (listener != null) {
            objectAnimator.addListener(listener);
        }
        objectAnimator.setDuration(duration).start();
    }

    public static void alpha(View view, float startAlpha, float endAlpha, int duration, Animator.AnimatorListener listener) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", startAlpha, endAlpha);
        if (listener != null) {
            objectAnimator.addListener(listener);
        }
        objectAnimator.setDuration(duration).start();
    }

    /*public static void multiAnimator(int duration, Animator.AnimatorListener listener) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(duration).start();
    }*/
}
