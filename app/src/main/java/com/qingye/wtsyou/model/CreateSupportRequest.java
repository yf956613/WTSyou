package com.qingye.wtsyou.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by THINK on 2018/4/16.
 */

public class CreateSupportRequest {

    private String activityName;
    private String activityIcon;
    private String name;
    private String detail;
    private List<Id> relevanceStars;
    private BigDecimal settingGoalsPrice;
    private String[] chatingRooms;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityIcon() {
        return activityIcon;
    }

    public void setActivityIcon(String activityIcon) {
        this.activityIcon = activityIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<Id> getRelevanceStars() {
        return relevanceStars;
    }

    public void setRelevanceStars(List<Id> relevanceStars) {
        this.relevanceStars = relevanceStars;
    }

    public BigDecimal getSettingGoalsPrice() {
        return settingGoalsPrice;
    }

    public void setSettingGoalsPrice(BigDecimal settingGoalsPrice) {
        this.settingGoalsPrice = settingGoalsPrice;
    }

    public String[] getChatingRooms() {
        return chatingRooms;
    }

    public void setChatingRooms(String[] chatingRooms) {
        this.chatingRooms = chatingRooms;
    }
}
