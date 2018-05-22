package com.qingye.wtsyou.basemodel;

import java.io.Serializable;

/**
 * Created by THINK on 2018/4/16.
 */

public class IdName implements Serializable{

    private String id;
    private String name;

    public IdName() {
    }

    public IdName(String id, String name) {
        this.id = id;
        this.name = name;
    }

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
