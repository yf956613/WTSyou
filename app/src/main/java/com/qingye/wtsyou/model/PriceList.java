package com.qingye.wtsyou.model;

import java.io.Serializable;
import java.math.BigDecimal;

import zuo.biao.library.base.BaseModel;

/**
 * Created by pm89 on 2018/5/10.
 */

public class PriceList implements Serializable {
    private String uuid;
    private String created;
    private String modified;
    private String creator;
    private String modifier;
    private String activityId;
    private double price;
    private int number;
    private int surplus;
    private int ticketNumber;
    private int ticketSurplus;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSurplus() {
        return surplus;
    }

    public void setSurplus(int surplus) {
        this.surplus = surplus;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public int getTicketSurplus() {
        return ticketSurplus;
    }

    public void setTicketSurplus(int ticketSurplus) {
        this.ticketSurplus = ticketSurplus;
    }
}
