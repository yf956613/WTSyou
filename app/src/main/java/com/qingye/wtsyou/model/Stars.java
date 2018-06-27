package com.qingye.wtsyou.model;

import java.io.Serializable;

/**
 * Created by pm89 on 2018/4/27.
 */

public class Stars implements Serializable {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
