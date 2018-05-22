package com.qingye.wtsyou.modle;

import com.qingye.wtsyou.basemodel.EntityBase;
import com.qingye.wtsyou.basemodel.POI;

import java.io.Serializable;
import java.math.BigDecimal;
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

    @Override
    protected boolean isCorrect() {
        return true;
    }
}
