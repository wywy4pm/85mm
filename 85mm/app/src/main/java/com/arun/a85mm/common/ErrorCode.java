package com.arun.a85mm.common;

/**
 * Created by WY on 2017/4/14.
 */
public class ErrorCode {
    public static final int NETWORK_ERROR = -100;//网络异常
    public static final int DATA_FORMAT_ERROR = -200;//数据解析异常
    public static final int SYSTEM_ERROR = -300;//系统异常
    public static final int SERVER_ERROR = -400;
    public static final int UNKNOWN_ERROR = -500;//未知错误

    public static final int SUCCESS = 200;//成功
    public static final int NO_DATA = 404;//无数据
    public static final int LACK_OF_PARAMS = 400;//缺少参数
    public static final int SERVER_INNER_ERROR = 500;//服务器内部错误

    // 1000：余额不足
    // 1001：已打赏
    public static final int AWARD_NO_ENOUGH = 1000;
    public static final int AWARD_DONE = 1001;
}
