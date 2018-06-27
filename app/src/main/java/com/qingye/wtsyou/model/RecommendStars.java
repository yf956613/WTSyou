package com.qingye.wtsyou.model;

import java.io.Serializable;

import zuo.biao.library.base.BaseModel;

/**
 * Created by pm89 on 2018/4/27.
 */

public class RecommendStars implements Serializable {
    private String starUuid;
    private String starName;
    private String starPhoto;
    private String funsCount;
    private int chatRoomCount;
    private Boolean isFollow;

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

    public String getFunsCount() {
        return funsCount;
    }

    public void setFunsCount(String funsCount) {
        this.funsCount = funsCount;
    }

    public int getChatRoomCount() {
        return chatRoomCount;
    }

    public void setChatRoomCount(int chatRoomCount) {
        this.chatRoomCount = chatRoomCount;
    }

    public Boolean getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Boolean isFollow) {
        this.isFollow = isFollow;
    }

}
