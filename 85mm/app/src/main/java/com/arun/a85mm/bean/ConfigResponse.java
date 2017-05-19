package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by wy on 2017/5/12.
 */

public class ConfigResponse {
    public int code;
    public String uid;
    public List<String> copyWrite;
    public String morePageImage;

    public GuidePageBean guidePage;

    public static class GuidePageBean {

        public String author;
        public String linkUrl;
        public String imageUrl;
        public String nextImageUrl;
    }
}
