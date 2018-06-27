package com.qingye.wtsyou.model;

import com.qingye.wtsyou.basemodel.IdName;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by pm89 on 2018/3/21.
 */

public class CrowdMoneyDetailed implements Serializable {

    private String uuid;
    private String created;
    private String modified;
    private String creator;
    private String modifier;
    private String id;
    private String userId;
    private BigDecimal total;
    private BigDecimal ticketsAmount;
    private BigDecimal price;
    private int qty;
    private String state;
    private IdName channel;
    private String payId;
    private String paymentState;
    private String refundState;
    private String activityId;
    private String skuId;
    private String reveiver;
    private String mobile;
    private int carrierFee;
    private int discountAmount;
    private String userName;
    private String photo;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTicketsAmount() {
        return ticketsAmount;
    }

    public void setTicketsAmount(BigDecimal ticketsAmount) {
        this.ticketsAmount = ticketsAmount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public IdName getChannel() {
        return channel;
    }

    public void setChannel(IdName channel) {
        this.channel = channel;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    public String getRefundState() {
        return refundState;
    }

    public void setRefundState(String refundState) {
        this.refundState = refundState;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getReveiver() {
        return reveiver;
    }

    public void setReveiver(String reveiver) {
        this.reveiver = reveiver;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getCarrierFee() {
        return carrierFee;
    }

    public void setCarrierFee(int carrierFee) {
        this.carrierFee = carrierFee;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
