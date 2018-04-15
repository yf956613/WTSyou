package com.qingye.wtsyou.modle;

import zuo.biao.library.base.BaseModel;

/**
 * Created by pm89 on 2018/3/6.
 */

public class Message extends BaseModel {

    /**默认构造方法，JSON等解析时必须要有
     */
    public Message() {
        //default
    }
    public Message(long id) {
        this();
        this.id = id;
    }

    @Override
    protected boolean isCorrect() {
        return true;
    }
}