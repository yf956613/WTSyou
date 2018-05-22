package com.qingye.wtsyou.basemodel;

import zuo.biao.library.base.BaseModel;

/**
 * Created by pm89 on 2018/4/27.
 */

public class EntityBase extends BaseModel {

    private boolean success;
    private String code;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    protected boolean isCorrect() {
        return true;
    }
}
