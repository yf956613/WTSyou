package com.qingye.wtsyou.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.concurrent.TimeUnit;

/**
 * Created by pm89 on 2018/6/20.
 */

public class AliyunUtils {


    public static void aliyun(Callback callback, String side, String imgBase64){

        String jsonStr = "";
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject configure = new JSONObject();
            configure.put("side", side);
            jsonObject.put("configure", configure);

            jsonObject.put("image", imgBase64);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonStr = jsonObject.toString();

        http(callback, jsonStr);
    }

    public static void http(Callback callback,String jsonStr) {

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setWriteTimeout(100, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(100,TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(100,TimeUnit.SECONDS);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://dm-51.data.aliyun.com/rest/160601/ocr/ocr_idcard.json")
                .post(body)
                .addHeader("X-Ca-Key", "24915875")
                .addHeader("Authorization", "APPCODE f667cf22482a40f192c3a7cd620cf7b2")
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }
}
