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


/**
 * @author subinzhu
 *
 */
public interface IsPoi<LngLat extends IsLngLat, PCDT extends IsPCDT> {

  /** 位置坐标 */
  public LngLat getLngLat();

  public void setLngLat(LngLat lngLat);

  /** 省市区县 */
  public PCDT getPcdt();

  public void setPcdt(PCDT pcdt);

  /** POI名称 */
  public String getPoiName();

  public void setPoiName(String name);

  /** POI详细地址 */
  public String getPoiAddress();

  public void setPoiAddress(String poiAddress);

  /** 用户输入的地址 */
  public String getAddress();

  public void setAddress(String address);
}
