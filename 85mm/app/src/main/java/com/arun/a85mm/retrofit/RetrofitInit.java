package com.arun.a85mm.retrofit;

import com.arun.a85mm.bean.AppBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.helper.AppHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInit {

    private static OkHttpClient client;

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constant.API_BASE_URL)
            .client(getClient())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    /*static class GzipRequsetInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
                return chain.proceed(originalRequest);
            }

            Request compressedRequest = originalRequest.newBuilder()
                    .header("Content-Encoding", "gzip")
                    .method(originalRequest.method(), gzip(originalRequest.body()))
                    .build();
            return chain.proceed(compressedRequest);
        }

        private RequestBody gzip(final RequestBody body) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return body.contentType();
                }

                @Override
                public long contentLength() throws IOException {
                    return -1;
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                    body.writeTo(gzipSink);
                    gzipSink.close();
                }
            };
        }
    }*/

    public static OkHttpClient getClient() {
        if (client == null) {

            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    String appVersion = "1.0.0";
                    String osType = "android";
                    if (getAppBean() != null) {
                        appVersion = getAppBean().appVersion;
                        osType = getAppBean().osType;
                    }
                    Request originalRequest = chain.request();
                    Request authorised = originalRequest.newBuilder()
                            .addHeader("appVersion", appVersion)
                            .addHeader("osType", osType)
                            .build();
                    return chain.proceed(authorised);
                }
            };

            client = new OkHttpClient
                    .Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
        }
        return client;
    }

    private RetrofitInit() {

    }

    private static RetrofitApi retrofitApi;

    public static RetrofitApi getApi() {

        if (retrofitApi == null) {
            retrofitApi = retrofit.create(RetrofitApi.class);
        }
        return retrofitApi;
    }

    public static <T> T createApi(Class<T> mClass) {
        return retrofit.create(mClass);
    }

    private static AppBean getAppBean() {
        return AppHelper.getInstance().getAppConfig();
    }

}
