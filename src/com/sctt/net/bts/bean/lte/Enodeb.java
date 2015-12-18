package com.sctt.net.bts.bean.lte;

public class Enodeb {
	private Long intId;
	private String userLabel;
	private String venderName;
	private double longitude;
	private double latitude;
	

	//冗余一个物理站点ID
	private long relatedLteBtsId;
	//冗余一下
	private int cityId;// 地市ID
	private int countryId;// 区县ID
	private String highTrainFlag;//高铁覆盖标识
	private Integer redLineFlag;//1=红线内，2=红线外	
	private boolean isSpecial;//是否特殊站点
	public Long getIntId() {
		return intId;
	}
	public void setIntId(Long intId) {
		this.intId = intId;
	}
	public String getUserLabel() {
		return userLabel;
	}
	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}
	public String getVenderName() {
		return venderName;
	}
	public void setVenderName(String venderName) {
		this.venderName = venderName;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public long getRelatedLteBtsId() {
		return relatedLteBtsId;
	}
	public void setRelatedLteBtsId(long relatedLteBtsId) {
		this.relatedLteBtsId = relatedLteBtsId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public String getHighTrainFlag() {
		return highTrainFlag;
	}
	public void setHighTrainFlag(String highTrainFlag) {
		this.highTrainFlag = highTrainFlag;
	}
	public Integer getRedLineFlag() {
		return redLineFlag;
	}
	public void setRedLineFlag(Integer redLineFlag) {
		this.redLineFlag = redLineFlag;
	}
	public boolean isSpecial() {
		return isSpecial;
	}
	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}
	
	
}
