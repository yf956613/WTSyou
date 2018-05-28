package com.qingye.wtsyou.model;

import zuo.biao.library.base.BaseModel;

/**
 * Created by pm89 on 2018/3/6.
 */

public class Conversation extends BaseModel {

    /**默认构造方法，JSON等解析时必须要有
     */
    public Conversation() {
        //default
    }
    public Conversation(long id) {
        this();
        this.id = id;
    }

    @Override
    protected boolean isCorrect() {
        return true;
    }
}
