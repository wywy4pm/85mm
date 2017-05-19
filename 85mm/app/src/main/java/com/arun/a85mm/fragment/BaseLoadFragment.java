package com.arun.a85mm.fragment;

/**
 * Created by wy on 2016/10/28.
 */

/**
 * 防止预加载
 */
public abstract class BaseLoadFragment extends BaseFragment {

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {
    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
}
