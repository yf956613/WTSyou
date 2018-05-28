package zuo.biao.library.util;

import zuo.biao.library.interfaces.OnHttpCallBack;
import zuo.biao.library.model.EntityBase;

import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.manager.HttpManager;

/**
 * Created by pm89 on 2018/5/24.
 */

public class HttpModle <T extends EntityBase> {

    private T data;
    private Class <T> cls;

    public HttpModle(Class<T> cls) {
        this.cls = cls;
    }

    public void post(final String jsonStr,final String url,int requestCode,final OnHttpCallBack callBack) {
        HttpManager.getInstance().postByJsonStr(jsonStr, url, requestCode, new OnHttpResponseListener() {

            @Override
            public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                responseCallBack(requestCode, resultJson, e, callBack, url);
            }
        });

    }

    public void get(final String url,int requestCode,final OnHttpCallBack callBack) {
        get("", url, requestCode, callBack);
    }

    public void get(final String request,final String url,int requestCode,final OnHttpCallBack callBack) {
        HttpManager.getInstance().getByJsonStr(request, url, requestCode, new OnHttpResponseListener() {
            @Override
            public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                responseCallBack(requestCode, resultJson, e, callBack, url);
            }
        });
    }

    private void responseCallBack(int requestCode, String resultJson, Exception e, OnHttpCallBack callBack, String url) {
        if (e == null) {

            T t = null;
            try {
                t = JSON.parseObject(resultJson,cls);
            } catch (Exception e1) {
                e1.printStackTrace();
                callBack.Error(url, requestCode, e1);
                return;
            }

            data = t;

            String code = data.getCode();
            if ("200".equals(code)) {
                callBack.Success(url, requestCode, t);
            } else {
                callBack.CodeError(url, requestCode, t);
            }
        } else {

        }
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
