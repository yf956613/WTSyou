package com.qingye.wtsyou.model;

/**
 * Created by pm89 on 2018/4/27.
 */

public class FocusStars {
    private String starUuid;
    private String starName;
    private String starPhoto;
    private int ranking;

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

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
}
