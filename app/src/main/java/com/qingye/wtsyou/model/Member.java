package com.qingye.wtsyou.model;

import java.io.Serializable;

/**
 * Created by pm89 on 2018/3/6.
 */

public class Member implements Serializable {

    private String uuid;
    private String created;
    private String modified;
    private String creator;
    private String modifier;
    private String id;
    private String fullname;
    private String nickname;
    private Object mobile;
    private String sex;
    private String photo;
    private String autograph;
    private int level;
    private int expValue;
    private int loveCount;
    private int diamondCount;
    private int goldCount;
    private String birthday;
    private String backgruoud;
    private String areaName;
    private boolean isFollow;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Object getMobile() {
        return mobile;
    }

    public void setMobile(Object mobile) {
        this.mobile = mobile;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExpValue() {
        return expValue;
    }

    public void setExpValue(int expValue) {
        this.expValue = expValue;
    }

    public int getLoveCount() {
        return loveCount;
    }

    public void setLoveCount(int loveCount) {
        this.loveCount = loveCount;
    }

    public int getDiamondCount() {
        return diamondCount;
    }

    public void setDiamondCount(int diamondCount) {
        this.diamondCount = diamondCount;
    }

    public int getGoldCount() {
        return goldCount;
    }

    public void setGoldCount(int goldCount) {
        this.goldCount = goldCount;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBackgruoud() {
        return backgruoud;
    }

    public void setBackgruoud(String backgruoud) {
        this.backgruoud = backgruoud;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }
}
