/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * 
 * 项目名：	missyou-api
 * 文件名：	OrderApply.java
 * 模块说明：	
 * 修改历史：
 * 2018-4-8 - chenpeisi - 创建。
 */
package com.qingye.wtsyou.model;

import com.qingye.wtsyou.basemodel.IdName;
import com.qingye.wtsyou.basemodel.POI;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 订单申请参数|接口对象。
 * 
 * @author chenpeisi
 * 
 */
public class OrderApply implements Serializable {
  private static final long serialVersionUID = 6992869641866283411L;

  // 订单基础信息
  private String userId;
  private BigDecimal price;
  private BigDecimal qty;
  private String remark;
  // 支付信息
  private IdName channel;
  // 活动信息
  private String activityId;
  // 商品信息
  private String skuId;
  // 收货信息
  private String reveiver;
  private String mobile;
  private POI reveiveAddress;
  // 承运信息
  private BigDecimal carrierFee;
  // 优惠信息
  private String couponId;
  private BigDecimal discountAmount = BigDecimal.ZERO;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public IdName getChannel() {
    return channel;
  }

  public void setChannel(IdName channel) {
    this.channel = channel;
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

  public BigDecimal getCarrierFee() {
    return carrierFee;
  }

  public void setCarrierFee(BigDecimal carrierFee) {
    this.carrierFee = carrierFee;
  }

  public String getCouponId() {
    return couponId;
  }

  public void setCouponId(String couponId) {
    this.couponId = couponId;
  }

  public BigDecimal getDiscountAmount() {
    return discountAmount;
  }

  public void setDiscountAmount(BigDecimal discountAmount) {
    this.discountAmount = discountAmount;
  }
}
