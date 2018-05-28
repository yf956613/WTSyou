package com.qingye.wtsyou.basemodel;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;

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
        switch (entityBase.getCode()) {
            case "401" :
                context.startActivity(MainActivity.createIntent(context));
                break;
        }
    }

    public void Success(Context context, EntityBase entityBase) {

    }

    public void Error(Context context, Exception e) {
        Toast.makeText(context, R.string.noReturn, Toast.LENGTH_SHORT).show();
    }
}
