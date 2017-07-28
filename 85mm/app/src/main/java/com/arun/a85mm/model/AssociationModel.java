package com.arun.a85mm.model;

import com.arun.a85mm.bean.request.AddCommunityRequest;
import com.arun.a85mm.bean.request.MsgImgRequest;
import com.arun.a85mm.listener.CommonRequestListener;
import com.arun.a85mm.retrofit.RetrofitInit;

import java.util.List;

import rx.Subscriber;

/**
 * Created by wy on 2017/6/24.
 */

public class AssociationModel extends BaseModel {
    private volatile static AssociationModel instance;

    public static AssociationModel getInstance() {
        if (instance == null)
            synchronized (AssociationModel.class) {
                if (instance == null) {
                    instance = new AssociationModel();
                }
            }
        return instance;
    }

    /*public Subscriber getCommunityList(int start, int dataType, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getCommunityList(start, dataType), listener);
    }*/

    public Subscriber getCommunityList(String lastWorkId, int type, CommonRequestListener listener) {
        return request(RetrofitInit.getApi().getWorkList(lastWorkId, "", "", type), listener);
    }

    public Subscriber addCommunity(String title, String description, List<MsgImgRequest> imageList, CommonRequestListener listener) {
        AddCommunityRequest addCommunityRequest = new AddCommunityRequest(title, description, imageList);
        return request(RetrofitInit.getApi().addCommunity(addCommunityRequest), listener);
    }
}
