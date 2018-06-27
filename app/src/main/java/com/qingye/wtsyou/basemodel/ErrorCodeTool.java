package com.qingye.wtsyou.basemodel;

import android.content.Context;
import android.widget.Toast;

import com.qingye.wtsyou.R;

import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.model.EntityBase;

/**
 * Created by pm89 on 2018/5/24.
 */

public class ErrorCodeTool implements IErrorCodeTool {

    private static ErrorCodeTool instance;

    private ErrorCodeTool() {
    }

    public static ErrorCodeTool getInstance() {
        if (instance == null) {
            synchronized (ErrorCodeTool.class) {
                if (instance == null)
                    instance = new ErrorCodeTool();
            }
        }
        return instance;
    }

    @Override
    public void errorCode(Context context, EntityBase entityBase) {
        if (entityBase.getMessage() != null)
        Toast.makeText(context, entityBase.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void Success(Context context, EntityBase entityBase) {

    }

    public void Error(Context context, Exception e) {
        Toast.makeText(context, R.string.noReturn, Toast.LENGTH_SHORT).show();
    }
}
