package com.qingye.wtsyou.model;

/**
 * Created by pm89 on 2018/5/9.
 */

public class SupportTypeItem extends SupportType {
    private SupportType supportType;
    private Boolean isSelector = false;

    public SupportType getSupportType() {
        return supportType;
    }

    public void setSupportType(SupportType supportType) {
        this.supportType = supportType;
    }

    public Boolean getSelector() {
        return isSelector;
    }

    public void setSelector(Boolean selector) {
        isSelector = selector;
    }
}
