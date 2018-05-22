/**
 * 版权所有©，厦门走云网络科技有限公司，2015，所有权利保留。
 * 
 * 项目名：	storm-common-api
 * 文件名：	Coord.java
 * 模块说明：	
 * 修改历史：
 * 2015年6月6日 - subinzhu - 创建。
 */
package com.qingye.wtsyou.basemodel;

import com.qingye.wtsyou.interfaces.IsLngLat;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 经纬度坐标
 * 
 * @author subinzhu
 *
 */
public class LngLat implements IsLngLat, Serializable {
  private static final long serialVersionUID = -228894081512078699L;

  protected BigDecimal lng;
  protected BigDecimal lat;

  public LngLat() {
  }

  public LngLat(BigDecimal lng, BigDecimal lat) {
    this.lng = lng;
    this.lat = lat;
  }

  /** 经度 */
  @Override
  public BigDecimal getLng() {
    return lng;
  }

  @Override
  public void setLng(BigDecimal lng) {
    this.lng = lng;
  }

  /** 纬度 */
  @Override
  public BigDecimal getLat() {
    return lat;
  }

  @Override
  public void setLat(BigDecimal lat) {
    this.lat = lat;
  }

  public static LngLat newInstance(IsLngLat source) {
    if (source == null)
      return null;

    return new LngLat(source.getLng(), source.getLat());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof LngLat)) {
      return false;
    }
    LngLat other = (LngLat) obj;
    if (lng != other.getLng()) {
      return false;
    }
    if (lat != other.getLat()) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return lng + "," + lat;
  }
}
