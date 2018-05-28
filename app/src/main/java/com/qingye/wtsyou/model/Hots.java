package com.qingye.wtsyou.model;

import zuo.biao.library.base.BaseModel;

/**
 * Created by pm89 on 2018/5/7.
 */

public class Hots extends BaseModel {
    private String uuid;
    private String created;
    private String modified;
    private String creator;
    private String modifier;
    private String activityId;
    private String state;
    private String activityName;
    private String activityIcon;
    private String activityProperty;
    private String activityState;
    private String activityStateName;

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

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

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

    public String getActivityProperty() {
        return activityProperty;
    }

    public void setActivityProperty(String activityProperty) {
        this.activityProperty = activityProperty;
    }

    public String getActivityState() {
        return activityState;
    }

    public void setActivityState(String activityState) {
        this.activityState = activityState;
    }

    public String getActivityStateName() {
        return activityStateName;
    }

    public void setActivityStateName(String activityStateName) {
        this.activityStateName = activityStateName;
    }

    @Override
    protected boolean isCorrect() {
        return true;
    }
}
