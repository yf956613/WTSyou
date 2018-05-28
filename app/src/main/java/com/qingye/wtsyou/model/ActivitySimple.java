package com.qingye.wtsyou.model;

import com.qingye.wtsyou.basemodel.POI;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pm89 on 2018/5/26.
 */

public class ActivitySimple implements Serializable {

    private String relevanceStar;
    private String activityName;
    private String activityPic;
    private String activityIcon;
    private String description;
    private String detail;
    private String state;
    private String activityType;
    private String activityProperty;
    private POI address;
    private String stadiumsId;
    private String relevanceStarName;
    private String activityTypeName;
    private String stadiumsName;
    private String startTimeStr;
    private String endTimeStr;
    private int chatroomNumber;
    private String activityPropertyName;
    private String activityStateName;

    public String getRelevanceStar() {
        return relevanceStar;
    }

    public void setRelevanceStar(String relevanceStar) {
        this.relevanceStar = relevanceStar;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityPic() {
        return activityPic;
    }

    public void setActivityPic(String activityPic) {
        this.activityPic = activityPic;
    }

    public String getActivityIcon() {
        return activityIcon;
    }

    public void setActivityIcon(String activityIcon) {
        this.activityIcon = activityIcon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityProperty() {
        return activityProperty;
    }

    public void setActivityProperty(String activityProperty) {
        this.activityProperty = activityProperty;
    }

    public POI getAddress() {
        return address;
    }

    public void setAddress(POI address) {
        this.address = address;
    }

    public String getStadiumsId() {
        return stadiumsId;
    }

    public void setStadiumsId(String stadiumsId) {
        this.stadiumsId = stadiumsId;
    }

    public String getRelevanceStarName() {
        return relevanceStarName;
    }

    public void setRelevanceStarName(String relevanceStarName) {
        this.relevanceStarName = relevanceStarName;
    }

    public String getActivityTypeName() {
        return activityTypeName;
    }

    public void setActivityTypeName(String activityTypeName) {
        this.activityTypeName = activityTypeName;
    }

    public String getStadiumsName() {
        return stadiumsName;
    }

    public void setStadiumsName(String stadiumsName) {
        this.stadiumsName = stadiumsName;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public int getChatroomNumber() {
        return chatroomNumber;
    }

    public void setChatroomNumber(int chatroomNumber) {
        this.chatroomNumber = chatroomNumber;
    }

    public String getActivityPropertyName() {
        return activityPropertyName;
    }

    public void setActivityPropertyName(String activityPropertyName) {
        this.activityPropertyName = activityPropertyName;
    }

    public String getActivityStateName() {
        return activityStateName;
    }

    public void setActivityStateName(String activityStateName) {
        this.activityStateName = activityStateName;
    }
}
