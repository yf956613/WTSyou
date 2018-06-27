package com.qingye.wtsyou.manager;


import com.google.gson.Gson;

import java.util.List;

import zuo.biao.library.interfaces.OnHttpPageCallBack;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.model.EntityBase;

/**
 * Created by pm89 on 2018/5/30.
 */

public class HttpPageModel<T extends EntityBase, itemData> {

    private T data;
    private Class<T> cls;
    public static final int REFRESH = 666666;//刷新
    public static final int LOADMORE = 777777;

    public HttpPageModel(Class<T> cls) {
        this.cls = cls;
    }

    public void post(final String jsonStr, final String url, int requestCode, final OnHttpPageCallBack<T, itemData> callBack) {
        HttpManager.getInstance().postByJsonStr(jsonStr, url, requestCode, new OnHttpResponseListener() {

            @Override
            public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                responseCallBack(requestCode, resultJson, e, callBack, url);
            }
        });

    }

    public void get(final String url, int requestCode, final OnHttpPageCallBack<T, itemData> callBack) {
        get("", url, requestCode, callBack);
    }

    public void get(final String request, final String url, int requestCode, final OnHttpPageCallBack<T, itemData> callBack) {
        HttpManager.getInstance().getByJsonStr(request, url, requestCode, new OnHttpResponseListener() {
            @Override
            public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                responseCallBack(requestCode, resultJson, e, callBack, url);
            }
        });
    }

    private void responseCallBack(int requestCode, String resultJson, Exception e, OnHttpPageCallBack<T, itemData> callBack, String url) {
        if (resultJson != null) {

            T t = null;
            try {
                t = new Gson().fromJson(resultJson, cls);
            } catch (Exception e1) {
                e1.printStackTrace();
                Error(callBack, url, requestCode, e1);
                return;
            }

            data = t;

            String code = data.getCode();
            if ("200".equals(code)) {
                Success(callBack, url, requestCode, t);
            } else {
                CodeError(callBack, url, requestCode, t);
            }
        } else {
            Error(callBack, url, requestCode, e);
        }
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    private int page = 0;
    private int pageSize = 10;

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;

    }

    public void refreshPost(final String url, final OnHttpPageCallBack callBack) {
        String requestJsonStr = callBack.getRequestJsonStr(1, pageSize);
        post(requestJsonStr, url, REFRESH, callBack);
    }

    public void loadMorePost(final String url, final OnHttpPageCallBack<T, itemData> callBack) {
        String requestJsonStr = callBack.getRequestJsonStr(page + 1, pageSize);
        post(requestJsonStr, url, LOADMORE, callBack);
    }

    public void refreshGet(final String url, final OnHttpPageCallBack<T, itemData> callBack) {
        String requestJsonStr = callBack.getRequestJsonStr(1, pageSize);
        get(requestJsonStr, url, REFRESH, callBack);
    }

    public void loadMoreGet(final String url, final OnHttpPageCallBack<T, itemData> callBack) {
        String requestJsonStr = callBack.getRequestJsonStr(page + 1, pageSize);
        get(requestJsonStr, url, LOADMORE, callBack);
    }

    void Success(OnHttpPageCallBack<T, itemData> callback, String url, int RequestCode, T t) {
        callback.Success(url, RequestCode, t);
        List<itemData> list = callback.getList(getData());
        if (list == null) {
            callback.emptyPagingList();
            return;
        }
        switch (RequestCode) {
            case REFRESH:
                if (list.size() == 0) {
                    callback.emptyPagingList();
                } else {
                    page = 1;
                    callback.refreshSuccessPagingList(list);
                }
                break;
            case LOADMORE:
                if (list.size() == 0) {
                    callback.noMorePagingList();
                } else {
                    page++;
                    callback.loadMoreSuccessPagingList(list);
                }
                break;
        }
    }

    void Error(OnHttpPageCallBack<T, itemData> callback, String url, int RequestCode, Exception e) {
        callback.Error(url, RequestCode, e);
        pageError(callback, RequestCode);
    }

    void CodeError(OnHttpPageCallBack<T, itemData> callback, String url, int RequestCode, T t) {
        callback.CodeError(url, RequestCode, t);
        pageError(callback, RequestCode);
    }

    public void pageError(OnHttpPageCallBack<T, itemData> callback, int RequestCode) {
        switch (RequestCode) {
            case REFRESH:
                callback.refreshErrorPagingList();
                break;
            case LOADMORE:
                callback.loadMoreErrorPagingList();
                break;
        }
    }
}
