package com.arun.a85mm.bean.request;

import com.arun.a85mm.bean.UploadImageBean;

import java.util.List;

/**
 * Created by wy on 2017/6/27.
 */

public class AddCommunityRequest {
    public String title;
    public String description;
    public List<MsgImgRequest> imageList;

    public AddCommunityRequest(String title, String description, List<MsgImgRequest> imageList) {
        this.title = title;
        this.description = description;
        this.imageList = imageList;
    }

}
