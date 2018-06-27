package com.qingye.wtsyou.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pm89 on 2018/3/6.
 */

public class Conversation implements Serializable {

    private String id;
    private String name;
    private String coverImage;
    private String backgroundImage;
    private String ownerId;
    private String ownerName;
    private String ownerPhoto;
    private int userMax;
    private String status;
    private String notice;
    private int userCount;
    private int allContribution;
    private String[] stars;
    private boolean topRecommend;
    private boolean hotRecommend;

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

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhoto() {
        return ownerPhoto;
    }

    public void setOwnerPhoto(String ownerPhoto) {
        this.ownerPhoto = ownerPhoto;
    }

    public int getUserMax() {
        return userMax;
    }

    public void setUserMax(int userMax) {
        this.userMax = userMax;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getAllContribution() {
        return allContribution;
    }

    public void setAllContribution(int allContribution) {
        this.allContribution = allContribution;
    }

    public String[] getStars() {
        return stars;
    }

    public void setStars(String[] stars) {
        this.stars = stars;
    }

    public boolean isTopRecommend() {
        return topRecommend;
    }

    public void setTopRecommend(boolean topRecommend) {
        this.topRecommend = topRecommend;
    }

    public boolean isHotRecommend() {
        return hotRecommend;
    }

    public void setHotRecommend(boolean hotRecommend) {
        this.hotRecommend = hotRecommend;
    }
}
