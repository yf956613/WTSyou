package com.qingye.wtsyou.model;

import java.io.Serializable;

/**
 * Created by pm89 on 2018/4/27.
 */

public class FocusStars implements Serializable {

    private String uuid;
    private String created;
    private String modified;
    private String creator;
    private String modifier;
    private String name;
    private String photo;
    private String atlas;
    private String language;
    private String nationality;
    private String englishName;
    private String alias;
    private String constellation;
    private String birthDate;
    private String starIntroduce;
    private int funsCount;
    private int chatRoomCount;
    private int ranking;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAtlas() {
        return atlas;
    }

    public void setAtlas(String atlas) {
        this.atlas = atlas;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getStarIntroduce() {
        return starIntroduce;
    }

    public void setStarIntroduce(String starIntroduce) {
        this.starIntroduce = starIntroduce;
    }

    public int getFunsCount() {
        return funsCount;
    }

    public void setFunsCount(int funsCount) {
        this.funsCount = funsCount;
    }

    public int getChatRoomCount() {
        return chatRoomCount;
    }

    public void setChatRoomCount(int chatRoomCount) {
        this.chatRoomCount = chatRoomCount;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public boolean isIsFollow() {
        return isFollow;
    }

    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }
}
