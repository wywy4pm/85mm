package com.arun.a85mm.common;

/**
 * Created by WY on 2017/4/13.
 */
public class Constant {

    public static final String BASE_URL_TEST = "http://121.40.75.136:38888";
    public static final String BASE_URL_PROD = "http://api.link365.cn:38888";

    public static String API_BASE_URL = BASE_URL_PROD;
    public static String WEB_BASE_URL = BASE_URL_PROD;

    public static final String PATH_WORK_DETAIL = "/works/detail";
    public static final String PATH_AUDIT_LIST = "/audit/list";
    public static final String PATH_WORK_LATEST = "/works/latest";

    public static final String PATH_ORDER_LIST = "/order/list";

    /**
     * intent key
     */
    public static final String INTENT_TYPE = "type";
    public static final String INTENT_TITLE = "key";
    public static final String INTENT_DATA = "data";

    public static final String INTENT_ARTICLE_ID = "article_id";
    public static final String INTENT_ARTICLE_HEAD_IMAGE = "article_head_image";
    public static final String INTENT_ARTICLE_IMAGE_POSITIONX = "position_x";
    public static final String INTENT_ARTICLE_IMAGE_POSITIONY = "position_y";

    public static final String INTENT_WEB_URL = "'url";
    public static final String INTENT_WEB_TYPE = "web_type";
    public static final String INTENT_WEB_ARGS = "web_args";

    public static final String INTENT_WORKS_LEFT_DATE = "left_date";
    public static final String INTENT_WORKS_LEFT_START = "left_start";

    public static final String STRING_TRUE = "true";
    public static final String STRING_FALSE = "'false";

    public static final String INTENT_ADD_AMOUNT = "add_amount";

    /**
     * data type
     */
    public static final String ARTICLE_TYPE_BIG_TITLE = "big_title";
    public static final String ARTICLE_TYPE_AUTHOR = "author";
    public static final String ARTICLE_TYPE_SMALL_TITLE = "title";
    public static final String ARTICLE_TYPE_FULL_IMAGE = "fullImage";
    public static final String ARTICLE_TYPE_PARAGRAPH = "paragraph";

    public static final String ARTICLE_TITLE_ALIGN_LEFT = "left";
    public static final String ARTICLE_TITLE_ALIGN_CENTER = "center";
    public static final String ARTICLE_TITLE_ALIGN_RIGHT = "right";

    public static final String MESSAGE_LIST_TOP = "msg_top";
    public static final String MESSAGE_LIST_IMAGE = "msg_img";

    /**
     * handler what
     */
    public static final int WHAT_SHOW_TOP = 1;


    /**
     * intent request code
     */
    public static final int REQUEST_CODE_ASSOCIATE_LOGIN = 1001;
    public static final int REQUEST_CODE_ASSOCIATE_MAIN = 1002;
    public static final int REQUEST_CODE_ALBUM_HEAD = 1003;
    public static final int REQUEST_CODE_ALBUM_COVER = 1004;
    public static final int REQUEST_CODE_ADD_AMOUNT = 1005;

    /**
     * bottom_dialog_type
     */
    public static final String TYPE_WORK = "0";
    public static final String TYPE_AUDIT = "1";
    public static final String TYPE_COMMUNITY = "2";
    public static final String TYPE_PUSH = "3";


    /**
     * server type
     * 1.生产环境
     * 2.测试环境
     */
    public static final int SERVER_TYPE_PROD = 0;
    public static final int SERVER_TYPE_TEST = 1;


    public static final int VALUE_SHOW_WIMAGE = 0;
    public static final int VALUE_SHOW_COMMON = 1;
}
