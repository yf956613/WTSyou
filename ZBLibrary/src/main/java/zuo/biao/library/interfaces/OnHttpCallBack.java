package zuo.biao.library.interfaces;

import zuo.biao.library.model.EntityBase;

/**
 * Created by pm89 on 2018/5/24.
 */

public interface OnHttpCallBack {

    void Success(String url, int RequestCode, EntityBase data);
    void Error(String url, int RequestCode, Exception e);
    void CodeError(String url, int RequestCode, EntityBase data);

}
