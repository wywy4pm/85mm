package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by wy on 2017/6/16.
 */

public class AuditInfoBean {
    public int tipsPosition;
    public List<TagItemBean> tags;

    public static class TagItemBean {
        public String showName;
        public String searchName;
    }
}
