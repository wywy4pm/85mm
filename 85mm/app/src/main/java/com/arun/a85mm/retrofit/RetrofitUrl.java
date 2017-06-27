package com.arun.a85mm.retrofit;

public class RetrofitUrl {
    public static final String V1 = "/v1";
    //public static final String V2 = "/v2";
    public static final String ARTICLE = "/article";
    public static final String WORKS = "/works";
    public static final String CONFIG = "/config";
    public static final String USER = "/user";

    public static final String ARTICLE_LIST = ARTICLE + V1 + "/list";
    public static final String ARTICLE_DETAIL = ARTICLE + V1 + "/detail";

    public static final String WORKS_LIST = WORKS + V1 + "/list";
    public static final String WORKS_GOODS = WORKS + V1 + "/goods";
    public static final String WORKS_ONE_DAY_LEFT = WORKS + V1 + "/onedayGoods";
    public static final String WORKS_SINGLE_DETAIL = WORKS + "/single";
    public static final String WORKS_AUDIT = WORKS + "/getAuditList";
    public static final String WORKS_COMMUNITY_LIST = WORKS + "/communityList";

    public static final String USER_ACTION = USER + V1 + "/action";
    public static final String USER_DEVICE_TOKEN = USER + "/addToken";
    public static final String USER_HIDE_SWITCH = USER + "/readSwitch";
    public static final String USER_MESSAGE_LIST = USER + "/msgList";
    public static final String USER_ADD_MESSAGE = USER + "/addMsg";
    public static final String USER_THIRD_LOGIN = USER + "/thirdLogin";
    public static final String USER_ADD_COMMENT = USER + "/addComment";
    public static final String USER_ADD_WORK = USER + "/addWork";

    public static final String CONFIG_QUERY = CONFIG + V1 + "/query";


}
