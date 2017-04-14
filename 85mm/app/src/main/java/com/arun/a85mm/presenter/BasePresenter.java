package com.arun.a85mm.presenter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.arun.a85mm.view.MvpView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;


public class BasePresenter<T extends MvpView> implements Presenter<T> {

    private T mMvpView;
    private Context context;
    private Dialog dialog;
    private List<Subscriber> subscribers;

    public BasePresenter(Context context) {
        this.context = context;
        subscribers = new ArrayList<>();
    }

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        for (Subscriber subscriber : subscribers) {
            subscriber.unsubscribe();
        }
        //cancelDialog();
        subscribers = null;
    }

    /**
     * 添加到列表，在detachView时取消订阅。在子类定义的subscriber需要手动调用添加
     *
     * @param subscriber
     */
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    /*private Dialog createLoadingDialog(Context pContext) {
        LayoutInflater inflater = LayoutInflater.from(pContext);
        View v = inflater.inflate(R.layout.loading_view, null);// 得到加载view

        Dialog loadingDialog = new Dialog(pContext, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
        loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //GsonRequest.this.cancel();
            }
        });

        loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    cancelDialog();
//                    ((Activity)context.get()).onKeyDown(keyCode,event);
                    return true;
                }
                return false;
            }
        });

//        ProgressBar progress_bg = (ProgressBar) v
//                .findViewById(R.id.progress_bg);
        // UIResize.setRelativeResizeUINew3(progress_bg, 65, 65);
        return loadingDialog;
    }*/

    /*protected void cancelDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    protected void showDialog() {
        if (dialog == null) {
            dialog = createLoadingDialog(context);
        }
        dialog.show();

    }*/

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }

    /*public void onErrorToast() {
        ToastUtils.makeText(context, context.getResources().getString(R.string.str_network_fail)).show();
    }

    //BaseLatestBean<T>,判断里面的状态码,处返回码0以外都抛出异常走onError回调
    public static class MapServerResponseFunc<T> implements Func1<BaseLatestBean<T>, BaseLatestBean<T>> {
        @Override
        public BaseLatestBean<T> call(BaseLatestBean<T> baseLatestBean) {
            //对返回码进行判断，如果不是0，则证明服务器端返回错误信息了，便根据跟服务器约定好的错误码去解析异常
            if (baseLatestBean.ret != ErrorCode.SUCCESS) {
                //如果服务器端有错误信息返回，那么抛出异常，让下面的方法去捕获异常做统一处理
                throw new Transformers.ServerException(baseLatestBean.ret, new Gson().toJson(baseLatestBean.data));
            }
            //服务器请求数据成功，返回里面的数据实体
            return baseLatestBean;
        }

    }

    public static class OnErrorHttpResponseFunc<T> implements Func1<Throwable, Observable<T>> {
        @Override
        public Observable<T> call(Throwable throwable) {
            //ExceptionEngine为处理异常的驱动器
            return Observable.error(Transformers.ExceptionEngine.handleException(throwable));
        }
    }

    public abstract class NewSubscriber<T> extends Subscriber<T> {
        @Override
        public void onError(Throwable e) {
            if (e instanceof Transformers.ApiException) {
                onError((Transformers.ApiException) e);
            } else {
                Transformers.ApiException apiException = new Transformers.ApiException(e, 121);
                apiException.message = "我是BasePresenter里的新的异常";
                onError(apiException);
            }
        }

        *//**
         * 错误回调
         *//*
        protected abstract void onError(Transformers.ApiException apiException);
    }*/


}
