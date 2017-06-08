package com.arun.a85mm.model;

import com.arun.a85mm.bean.AppBean;
import com.arun.a85mm.bean.ProductListResponse;
import com.arun.a85mm.helper.AppHelper;
import com.arun.a85mm.listener.CommonRequestListener;
import com.arun.a85mm.retrofit.RetrofitInit;

import rx.Subscriber;

/**
 * Created by wy on 2017/5/22.
 */

public class ProductModel extends BaseModel {
    private volatile static ProductModel instance;

    public static ProductModel getInstance() {
        if (instance == null)
            synchronized (ProductModel.class) {
                if (instance == null) {
                    instance = new ProductModel();
                }
            }
        return instance;
    }

    public Subscriber getWorksList( String lastWorkId, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getWorksList( lastWorkId), listener);
    }

    public Subscriber getWorksGoods(String lastDate, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getWorksGoods(lastDate), listener);
    }

    public Subscriber getWorksOneDayLeft(String date, int start, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getWorksOneDayLeft( date, start), listener);
    }

    public Subscriber getSingleWork(String workId, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getSingleWork(workId), listener);
    }

}
