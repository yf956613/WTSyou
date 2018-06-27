package com.qingye.wtsyou.model;

import java.io.Serializable;

import zuo.biao.library.base.BaseModel;

/**
 * Created by pm89 on 2018/5/9.
 */

public class SupportType implements Serializable {
    private String uuid;
    public String created;
    public String modified;
    public String creator;
    public String modifier;
    public String name;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
