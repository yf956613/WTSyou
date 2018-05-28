package com.qingye.wtsyou.model;

/**
 * Created by pm89 on 2018/3/21.
 */

public class Fans {

    private String uuid;
    private String created;
    private String modified;
    private String creator;
    private String modifier;
    private String userId;
    private String starUuid;
    private String starName;
    private String starPhoto;
    private String userName;
    private String userPhoto;
    private int ranking;
    private double loveCount;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStarUuid() {
        return starUuid;
    }

    public void setStarUuid(String starUuid) {
        this.starUuid = starUuid;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    public String getStarPhoto() {
        return starPhoto;
    }

    public void setStarPhoto(String starPhoto) {
        this.starPhoto = starPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public double getLoveCount() {
        return loveCount;
    }

    public void setLoveCount(double loveCount) {
        this.loveCount = loveCount;
    }
}
