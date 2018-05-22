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

import com.qingye.wtsyou.interfaces.IsPCDT;

import java.io.Serializable;
import java.util.Objects;

/**
 * 省市区县乡镇街道
 * 
 * @author subinzhu
 *
 */
public class PCDT implements IsPCDT, Serializable {
  private static final long serialVersionUID = 1021254385050930447L;

  private String province;
  private String provinceCode;
  private String city;
  private String cityCode;
  private String district;
  private String districtCode;
  private String township;

  /** 省 */
  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  /** 省编码 */
  public String getProvinceCode() {
    return provinceCode;
  }

  public void setProvinceCode(String provinceCode) {
    this.provinceCode = provinceCode;
  }

  /** 市 */
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  /** 市编码 */
  public String getCityCode() {
    return cityCode;
  }

  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

  /** 区县 */
  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  /** 区县编码 */
  public String getDistrictCode() {
    return districtCode;
  }

  public void setDistrictCode(String districtCode) {
    this.districtCode = districtCode;
  }

  /** 乡镇街道 */
  public String getTownship() {
    return township;
  }

  public void setTownship(String township) {
    this.township = township;
  }

  public static PCDT newInstance(IsPCDT source) {
    if (source == null) {
      return null;
    }

    PCDT target = new PCDT();
    target.setProvince(source.getProvince());
    target.setProvinceCode(source.getProvinceCode());
    target.setCity(source.getCity());
    target.setCityCode(source.getCityCode());
    target.setDistrict(source.getDistrict());
    target.setDistrictCode(source.getDistrictCode());
    target.setTownship(source.getTownship());
    return target;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof PCDT)) {
      return false;
    }
    PCDT other = (PCDT) obj;
    if (!Objects.equals(provinceCode, other.getProvinceCode())) {
      return false;
    }
    if (!Objects.equals(cityCode, other.getCityCode())) {
      return false;
    }
    if (!Objects.equals(districtCode, other.getDistrictCode())) {
      return false;
    }
    if (!Objects.equals(township, other.getTownship())) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(province == null ? "" : province);
    sb.append(city == null ? "" : city);
    sb.append(district == null ? "" : district);
    sb.append(township == null ? "" : township);
    return super.toString();
  }

}
