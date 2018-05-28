package com.qingye.wtsyou.model;

import com.qingye.wtsyou.basemodel.POI;

/**
 * Created by THINK on 2018/4/16.
 */

public class CreateVoteRequest {

    private String name;
    private String relevanceStar;
    private POI address;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelevanceStar() {
        return relevanceStar;
    }

    public void setRelevanceStar(String relevanceStar) {
        this.relevanceStar = relevanceStar;
    }

    public POI getAddress() {
        return address;
    }

    public void setAddress(POI address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
