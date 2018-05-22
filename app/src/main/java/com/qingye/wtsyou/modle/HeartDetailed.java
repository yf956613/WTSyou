package com.qingye.wtsyou.modle;

/**
 * Created by pm89 on 2018/3/6.
 */

public class HeartDetailed {

    private String uuid;
    private String bizType;
    private int loveValue;
    private String occurTime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public int getLoveValue() {
        return loveValue;
    }

    public void setLoveValue(int loveValue) {
        this.loveValue = loveValue;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
    }
}
