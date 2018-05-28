package com.qingye.wtsyou.model;

import zuo.biao.library.base.BaseModel;

/**
 * Created by pm89 on 2018/3/21.
 */

public class User extends BaseModel {
    String accountType;//用户类型
    String loginId;//登录id
    String passWord;//登录密码

    @Override
    protected boolean isCorrect() {
        return true;
    }
}
