package com.qingye.wtsyou.model;

import zuo.biao.library.model.EntityBase;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pm89 on 2018/3/6.
 */

public class EntityContent<T extends Serializable> extends EntityBase {

    public List<T> content;

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

}
