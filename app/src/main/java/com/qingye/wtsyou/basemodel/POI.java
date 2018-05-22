/**
 * 版权所有©，厦门走云网络科技有限公司，2015，所有权利保留。
 * 
 * 项目名：	storm-common-api
 * 文件名：	POI.java
 * 模块说明：	
 * 修改历史：
 * 2015年7月28日 - subinzhu - 创建。
 */
package com.qingye.wtsyou.basemodel;

import com.qingye.wtsyou.interfaces.IsPoi;

import java.io.Serializable;


/**
 * POI位置信息
 * 
 * @author subinzhu
 *
 */
public class POI implements IsPoi<LngLat, PCDT>, Serializable {
  private static final long serialVersionUID = -1126509463641747422L;

  private LngLat lngLat = new LngLat();
  private PCDT pcdt = new PCDT();
  private String poiName;
  private String poiAddress;
  private String address;

  /** 位置坐标 */
  public LngLat getLngLat() {
    return lngLat;
  }

  public void setLngLat(LngLat lngLat) {
    this.lngLat = lngLat;
  }

  /** 省市区县乡镇街道 */
  public PCDT getPcdt() {
    return pcdt;
  }

  public void setPcdt(PCDT pcdt) {
    this.pcdt = pcdt;
  }

  /** POI名称 */
  public String getPoiName() {
    return poiName;
  }

  public void setPoiName(String name) {
    this.poiName = name;
  }

  /** POI详细地址 */
  public String getPoiAddress() {
    return poiAddress;
  }

  public void setPoiAddress(String poiAddress) {
    this.poiAddress = poiAddress;
  }

  /** 用户输入的地址 */
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public static POI newInstance(IsPoi source) {
    if (source == null)
      return null;

    POI target = new POI();
    target.setLngLat(LngLat.newInstance(source.getLngLat()));
    target.setPcdt(PCDT.newInstance(source.getPcdt()));
    target.setPoiName(source.getPoiName());
    target.setPoiAddress(source.getPoiAddress());
    target.setAddress(source.getAddress());
    return target;
  }


  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(pcdt == null ? "" : pcdt.toString());
    sb.append(poiName == null ? "" : poiName);
    sb.append(poiAddress == null ? "" : "【" + poiAddress + "】");
    return super.toString();
  }

}
