package com.qingye.wtsyou.model;

import java.io.Serializable;

/**
 * Created by pm89 on 2018/3/6.
 */

public class Card implements Serializable {

    private String uuid;
    private String created;
    private String modified;
    private String creator;
    private String modifier;
    private String title;
    private String detail;
    private String startReceiveTime;
    private String startTime;
    private String deadline;
    private String type;
    private double discountAmount;
    private double minAmount;
    private String restriction;
    private String state;
    private int couponNumber;
    private String surplus;
    private String scopes;
    private String typeName;
    private String stateName;
    private String userId;
    private String ruleId;
    private String couponState;
    private String userName;
    private String couponStateName;
    private String couponTypeName;
    private String activityProperty;
    private String activityId;
    private String userIds;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStartReceiveTime() {
        return startReceiveTime;
    }

    public void setStartReceiveTime(String startReceiveTime) {
        this.startReceiveTime = startReceiveTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(int couponNumber) {
        this.couponNumber = couponNumber;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getCouponState() {
        return couponState;
    }

    public void setCouponState(String couponState) {
        this.couponState = couponState;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCouponStateName() {
        return couponStateName;
    }

    public void setCouponStateName(String couponStateName) {
        this.couponStateName = couponStateName;
    }

    public String getCouponTypeName() {
        return couponTypeName;
    }

    public void setCouponTypeName(String couponTypeName) {
        this.couponTypeName = couponTypeName;
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

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }
}
