package zuo.biao.library.interfaces;

import android.content.Context;

import zuo.biao.library.model.EntityBase;

/**
 * Created by pm89 on 2018/5/24.
 */

public interface IErrorCodeTool {
    void Success(Context context, EntityBase entityBase);
    void Error(Context context, Exception e);
    void errorCode(Context context, EntityBase entityBase);
}
