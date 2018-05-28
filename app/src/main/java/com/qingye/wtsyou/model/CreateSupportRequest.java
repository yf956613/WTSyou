package com.qingye.wtsyou.model;

import java.math.BigDecimal;

/**
 * Created by THINK on 2018/4/16.
 */

public class CreateSupportRequest {

    private String activityName;
    private String activityIcon;
    private String name;
    private String detail;
    private String relevanceStar;
    private BigDecimal settingGoalsPrice;

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

    public String getRelevanceStar() {
        return relevanceStar;
    }

    public void setRelevanceStar(String relevanceStar) {
        this.relevanceStar = relevanceStar;
    }

    public BigDecimal getSettingGoalsPrice() {
        return settingGoalsPrice;
    }

    public void setSettingGoalsPrice(BigDecimal settingGoalsPrice) {
        this.settingGoalsPrice = settingGoalsPrice;
    }
}
