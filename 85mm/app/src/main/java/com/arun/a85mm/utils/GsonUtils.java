package com.arun.a85mm.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Created by WY on 2017/5/7.
 */
public class GsonUtils {
    private static Gson gson;

    public static void newIntense() {
        synchronized (GsonUtils.class) {
            if (gson == null) {
                gson = new GsonBuilder().create();
            }
        }
    }

    public static String toJson(Object object) {
        newIntense();
        String json = "";
        if (gson != null) {
            json = gson.toJson(object);
        }
        return json;
    }

    public static <T> T fromJson(String json) {
        newIntense();
        T t = null;
        if (gson != null) {
            t = gson.fromJson(json, new TypeToken<T>() {
            }.getType());
        }
        return t;
    }
}
