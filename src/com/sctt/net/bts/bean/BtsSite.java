package com.sctt.net.bts.bean;

public class BtsSite {
	private long intId;
	private String name;
	private double longitude;
	private double latitude;
	private String isIndoor;
	private String is_rru;
	private String bts_name;
	private String bsc_name;
	private long bts_id;
	private String circuitRoom_ownership;
	private String Trans_ownership;
	private String vendor_BtsType;
	private int cityId;// 地市ID
	private int countryId;// 区县ID
	private long related_bts;
	private long related_cells;
	
	private String serviceLevel;//维护等级
	private int deleteFlag;
	
	
	//冗余字段
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

	public String getIsIndoor() {
		return isIndoor;
	}

	public void setIsIndoor(String isIndoor) {
		this.isIndoor = isIndoor;
	}

	public String getIs_rru() {
		return is_rru;
	}

	public void setIs_rru(String is_rru) {
		this.is_rru = is_rru;
	}

	public String getBts_name() {
		return bts_name;
	}

	public void setBts_name(String bts_name) {
		this.bts_name = bts_name;
	}

	public String getBsc_name() {
		return bsc_name;
	}

	public void setBsc_name(String bsc_name) {
		this.bsc_name = bsc_name;
	}

	public long getBts_id() {
		return bts_id;
	}

	public void setBts_id(long bts_id) {
		this.bts_id = bts_id;
	}

	public String getCircuitRoom_ownership() {
		return circuitRoom_ownership;
	}

	public void setCircuitRoom_ownership(String circuitRoom_ownership) {
		this.circuitRoom_ownership = circuitRoom_ownership;
	}

	public String getTrans_ownership() {
		return Trans_ownership;
	}

	public void setTrans_ownership(String trans_ownership) {
		Trans_ownership = trans_ownership;
	}

	public String getVendor_BtsType() {
		return vendor_BtsType;
	}

	public void setVendor_BtsType(String vendor_BtsType) {
		this.vendor_BtsType = vendor_BtsType;
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

	public long getRelated_bts() {
		return related_bts;
	}

	public void setRelated_bts(long related_bts) {
		this.related_bts = related_bts;
	}

	public long getRelated_cells() {
		return related_cells;
	}

	public void setRelated_cells(long related_cells) {
		this.related_cells = related_cells;
	}

	public String getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
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
