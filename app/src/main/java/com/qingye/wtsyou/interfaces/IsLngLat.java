/**
 * 版权所有©，厦门走云网络科技有限公司，2015，所有权利保留。
 * 
 * 项目名：	storm-common-api
 * 文件名：	HasCoord.java
 * 模块说明：	
 * 修改历史：
 * 2015年6月6日 - subinzhu - 创建。
 */
package com.qingye.wtsyou.interfaces;

import java.math.BigDecimal;

/**
 * @author subinzhu
 *
 */
public interface IsLngLat {
  /** 经度 */
  public BigDecimal getLng();

  public void setLng(BigDecimal lng);

  /** 纬度 */
  public BigDecimal getLat();

  public void setLat(BigDecimal lat);
}
