package com.qingye.wtsyou.modle;

import zuo.biao.library.base.BaseModel;

/**
 * Created by pm89 on 2018/5/12.
 */

public class QiniuMessage extends BaseModel {

    private String hash;
    private String key;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    protected boolean isCorrect() {
        return true;
    }
}
