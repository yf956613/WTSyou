package com.qingye.wtsyou.interfaces;

import zuo.biao.library.model.EntityBase;

/**
 * Created by pm89 on 2018/5/24.
 */

public interface OnHttpCallBack <T extends EntityBase> {

    void Success(String url, int RequestCode, T t);
    void Error(String url, int RequestCode, Exception e);
    void CodeError(String url, int RequestCode, T t);

}
