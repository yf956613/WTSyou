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
public interface IsPCDT {

  /** 省 */
  public String getProvince();

  public void setProvince(String province);

  /** 省编码 */
  public String getProvinceCode();

  public void setProvinceCode(String provinceCode);

  /** 市 */
  public String getCity();

  public void setCity(String city);

  /** 市编码 */
  public String getCityCode();

  public void setCityCode(String cityCode);

  /** 区县 */
  public String getDistrict();

  public void setDistrict(String county);

  /** 区县编码 */
  public String getDistrictCode();

  public void setDistrictCode(String districtCode);

  /** 乡镇街道 */
  public String getTownship();

  public void setTownship(String township);
}
