package com.qingye.wtsyou.model;

import com.qingye.wtsyou.basemodel.IdName;
import com.qingye.wtsyou.basemodel.POI;

import java.io.Serializable;

/**
 * Created by pm89 on 2018/3/6.
 */

public class Ticket implements Serializable {

    private String uuid;
    private String created;
    private String modified;
    private String creator;
    private String modifier;
    private String id;
    private String userId;
    private double total;
    private double ticketsAmount;
    private double price;
    private int qty;
    private String state;
    private String cancelReason;
    private String remark;
    private String orderType;
    private IdName channel;
    private String payId;
    private String paymentState;
    private String payFailReason;
    private String refundId;
    private String refundState;
    private String refundReason;
    private String activityId;
    private String skuId;
    private String reveiver;
    private String mobile;
    private POI reveiveAddress;
    private String carrier;
    private String expressBillId;
    private double carrierFee;
    private String couponId;
    private int discountAmount;
    private String userName;
    private String photo;
    private ActivitySimple activitySimple;

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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTicketsAmount() {
        return ticketsAmount;
    }

    public void setTicketsAmount(double ticketsAmount) {
        this.ticketsAmount = ticketsAmount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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

    public String getPayFailReason() {
        return payFailReason;
    }

    public void setPayFailReason(String payFailReason) {
        this.payFailReason = payFailReason;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundState() {
        return refundState;
    }

    public void setRefundState(String refundState) {
        this.refundState = refundState;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
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

    public POI getReveiveAddress() {
        return reveiveAddress;
    }

    public void setReveiveAddress(POI reveiveAddress) {
        this.reveiveAddress = reveiveAddress;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getExpressBillId() {
        return expressBillId;
    }

    public void setExpressBillId(String expressBillId) {
        this.expressBillId = expressBillId;
    }

    public double getCarrierFee() {
        return carrierFee;
    }

    public void setCarrierFee(double carrierFee) {
        this.carrierFee = carrierFee;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
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

    public ActivitySimple getActivitySimple() {
        return activitySimple;
    }

    public void setActivitySimple(ActivitySimple activitySimple) {
        this.activitySimple = activitySimple;
    }
}
