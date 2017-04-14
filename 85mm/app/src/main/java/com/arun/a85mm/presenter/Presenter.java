package com.arun.a85mm.presenter;


import com.arun.a85mm.view.MvpView;

public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();

}
