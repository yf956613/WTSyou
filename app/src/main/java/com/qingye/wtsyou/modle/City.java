package com.qingye.wtsyou.modle;

import com.qingye.wtsyou.utils.PinYinUtil;

import zuo.biao.library.base.BaseModel;

/**
 * Created by pm89 on 2018/3/21.
 */

public class City extends BaseModel implements Comparable<City>{
    public String name;
    public String pinYin;
    public int isFirst;

    public City() {

    }

    public City(String name,int i,int isFirst) {
        this.name = name;
        this.isFirst = isFirst;
        //一开始就转化好拼音
        pinYin = PinYinUtil.getPinYin(name);
    }

    @Override
    public int compareTo(City another) {
        return pinYin.compareTo(another.pinYin);
    }

    public int getIsFirst() {
        return isFirst;
    }

    public void setIsFirst (int isFirst) {
        this.isFirst = isFirst;
    }

    @Override
    protected boolean isCorrect() {
        return true;
    }
}
