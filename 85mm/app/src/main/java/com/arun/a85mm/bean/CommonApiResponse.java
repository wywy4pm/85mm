package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by wy on 2017/5/22.
 */

public class CommonApiResponse<T> {
    public int start;
    public int code;
    //public String uid;
    //public String msg;
    public T body;

    /**
     * 分页最后一个作品ID
     */
    //public String lastWorkId;

    /**
     * oss相关信息
     */
    //public OssInfoBean ossInfo;

    /**
     * 审核相关信息
     */
    //public AuditInfoBean auditInfo;


}
