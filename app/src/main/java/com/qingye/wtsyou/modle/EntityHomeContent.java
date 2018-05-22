package com.qingye.wtsyou.modle;

import com.qingye.wtsyou.basemodel.POI;

import java.util.Date;

import zuo.biao.library.base.BaseModel;

/**
 * Created by pm89 on 2018/4/27.
 */

public class EntityHomeContent extends BaseModel{

    private String uuid;
    private String created;
    private String modified;
    private String creator;
    private String modifier;
    private String relevanceStar;
    private String activityName;
    private String startTime;
    private String endTime;
    private String state;
    private String createUserId;
    private String activityType;
    private String activityProperty;
    private String activityId;
    private POI address;
    private String relevanceStarName;
    private String activityTypeName;
    private int chatroomNumber;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
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

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public POI getAddress() {
        return address;
    }

    public void setAddress(POI address) {
        this.address = address;
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

    public int getChatroomNumber() {
        return chatroomNumber;
    }

    public void setChatroomNumber(int chatroomNumber) {
        this.chatroomNumber = chatroomNumber;
    }

    @Override
    protected boolean isCorrect() {
        return true;
    }
}
