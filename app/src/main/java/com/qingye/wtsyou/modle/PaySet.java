package com.qingye.wtsyou.modle;

import java.io.Serializable;

/**
 * Created by THINK on 2018/4/16.
 */

public class PaySet implements Serializable{

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
