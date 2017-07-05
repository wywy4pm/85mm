package com.arun.a85mm.helper;

import android.text.TextUtils;

import com.arun.a85mm.MMApplication;
import com.arun.a85mm.R;
import com.arun.a85mm.common.ErrorCode;
import com.arun.a85mm.exception.ApiException;
import com.arun.a85mm.exception.ServerException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by wy on 2017/7/5.
 */

public class ExceptionHelper {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ApiException handleException(Throwable e) {
        ApiException ex = null;
        if (e instanceof HttpException) {
            //HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, httpException.code());
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    //ex.message = MMApplication.getInstance().getResources().getString(R.string.error_network);  //均视为网络错误
                    ex.errorMsg = R.string.error_network;
                    break;
            }
        } /*else if (e instanceof ServerException) {    //服务器返回的错误
            ServerException resultException = (ServerException) e;
            ex = new ApiException(resultException, resultException.code);
            //服务器返回的失败说明在data里的msg字段
            try {
                JSONObject jsonObject = new JSONObject(resultException.message);
                String msg = jsonObject.optString("msg");
                if (!TextUtils.isEmpty(msg)) {
                    ex.message = msg;
                } else {
                    ex.message = resultException.message;
                }
            } catch (JSONException e1) {
                ex.message = resultException.message;
            }
        } */ else if (e instanceof ConnectException
                || e instanceof UnknownHostException
                || e instanceof TimeoutException) {
            ex = new ApiException(e, ErrorCode.NETWORK_ERROR);
            //ex.message = MMApplication.getInstance().getResources().getString(R.string.error_network);  //均视为网络错误
            ex.errorMsg = R.string.error_network;
        } else if (e instanceof JsonSyntaxException
                || e instanceof JsonParseException
                || e instanceof JSONException) {
            ex = new ApiException(e, ErrorCode.DATA_FORMAT_ERROR);
            //ex.message = MMApplication.getInstance().getResources().getString(R.string.error_data_format);//均视为解析错误
            ex.errorMsg = R.string.error_data_format;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, ErrorCode.SERVER_ERROR);
            ex.errorMsg = R.string.error_server;
        } else {
            ex = new ApiException(e, ErrorCode.UNKNOWN_ERROR);
            //ex.message = MMApplication.getInstance().getResources().getString(R.string.error_unknown);//未知错误
            ex.errorMsg = R.string.error_unknown;
        }
        return ex;
    }
}
