package com.sctt.net.bts.bean;

public class Bts {
	private long intId;
	private String name;
	private double longitude;// 经度
	private double latitude;// 纬度
	private String bscName;
	private long btsId;
	private String vendorBtsType;
	private long relatedBsc;
	
	//冗余一个物理站点ID
	private long relatedWyBts;
	
	
	
	//冗余一下
	private int cityId;// 地市ID
	private int countryId;// 区县ID
	
	
	private int sourceCityId;
	private String highTrainFlag;//高铁覆盖标识
	private Integer redLineFlag;//1=红线内，2=红线外

	public long getIntId() {
		return intId;
	}

	public void setIntId(long intId) {
		this.intId = intId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getBscName() {
		return bscName;
	}

	public void setBscName(String bscName) {
		this.bscName = bscName;
	}

	public long getBtsId() {
		return btsId;
	}

	public void setBtsId(long btsId) {
		this.btsId = btsId;
	}

	public String getVendorBtsType() {
		return vendorBtsType;
	}

	public void setVendorBtsType(String vendorBtsType) {
		this.vendorBtsType = vendorBtsType;
	}

	public long getRelatedBsc() {
		return relatedBsc;
	}

	public void setRelatedBsc(long relatedBsc) {
		this.relatedBsc = relatedBsc;
	}

	public long getRelatedWyBts() {
		return relatedWyBts;
	}

	public void setRelatedWyBts(long relatedWyBts) {
		this.relatedWyBts = relatedWyBts;
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

	public int getSourceCityId() {
		return sourceCityId;
	}

	public void setSourceCityId(int sourceCityId) {
		this.sourceCityId = sourceCityId;
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

	
	
	
	
	
	
	

}
