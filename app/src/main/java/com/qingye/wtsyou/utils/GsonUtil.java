package com.qingye.wtsyou.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by pm89 on 2018/4/27.
 */

public class GsonUtil {
    private static Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public GsonUtil() {
    }

    public static Gson getGson() {
        return gson;
    }
}
