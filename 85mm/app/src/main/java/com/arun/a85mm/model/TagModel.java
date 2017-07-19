package com.arun.a85mm.model;

import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.listener.CommonRequestListener;
import com.arun.a85mm.retrofit.RetrofitInit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by wy on 2017/7/19.
 */

public class TagModel extends BaseModel {
    private volatile static TagModel instance;

    public static TagModel getInstance() {
        if (instance == null)
            synchronized (TagModel.class) {
                if (instance == null) {
                    instance = new TagModel();
                }
            }
        return instance;
    }

    public Subscriber updateUserTag(List<UserTagBean> tagList, CommonRequestListener listener) {
        Map<String, List<UserTagBean>> map = new HashMap<>();
        map.put("tagList", tagList);
        return request(RetrofitInit.getApi().updateUserTag(map), listener);
    }

    public Subscriber getTagWorkList(String tagName, int start, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getTagWorkList(tagName, start), listener);
    }
}
