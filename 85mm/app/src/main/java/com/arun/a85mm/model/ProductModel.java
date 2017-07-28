package com.arun.a85mm.model;

import com.arun.a85mm.bean.request.AddCommentRequest;
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

    public Subscriber getWorksList(String lastWorkId, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getWorkList(lastWorkId, "", "", 0), listener);
    }

    public Subscriber getWorksGoods(String lastDate, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getWorksGoods(lastDate), listener);
    }

    public Subscriber getWorksOneDayLeft(String date, String lastWorkId, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getWorkList(lastWorkId, date, "", 1), listener);
    }

    public Subscriber getSingleWork(String workId, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getSingleWork(workId), listener);
    }

    public Subscriber getAuditWorkList(String searchName, String orderType, String lastId, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getAuditWorkList(searchName, orderType, lastId), listener);
    }

    public Subscriber addComment(String workId, String comment, CommonRequestListener listener) {
        AddCommentRequest request = new AddCommentRequest(workId, comment);
        return request(RetrofitInit.getApi().addComment(request), listener);
    }

    public Subscriber getWorkMix(CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getWorkMix(), listener);
    }
}
