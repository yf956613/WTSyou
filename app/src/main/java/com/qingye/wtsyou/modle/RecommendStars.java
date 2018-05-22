package com.qingye.wtsyou.modle;

import zuo.biao.library.base.BaseModel;

/**
 * Created by pm89 on 2018/4/27.
 */

public class RecommendStars extends BaseModel{
    private String starUuid;
    private String starName;
    private String starPhoto;
    private String funsCount;
    private String chatRoomCount;
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

    public String getChatRoomCount() {
        return chatRoomCount;
    }

    public void setChatRoomCount(String chatRoomCount) {
        this.chatRoomCount = chatRoomCount;
    }

    public Boolean getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Boolean isFollow) {
        this.isFollow = isFollow;
    }

    @Override
    protected boolean isCorrect() {
        return true;
    }
}
