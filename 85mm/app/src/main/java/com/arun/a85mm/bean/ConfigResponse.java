package com.arun.a85mm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/5/12.
 */

public class ConfigResponse {
    public int code;
    public String uid;
    public List<String> copyWrite;
    public String morePageImage;

    public List<GuidePageBean> guidePage;

    public static class GuidePageBean implements Serializable{

        public String author;
        public String linkUrl;
        public String imageUrl;
        public long date;
    }
}
