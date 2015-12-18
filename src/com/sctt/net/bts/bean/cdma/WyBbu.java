package com.sctt.net.bts.bean.cdma;

public class WyBbu {
	private long intId;
	private String name;// 物理名称
	private String bbuNo;// BBU编号
	private double longitude;// 经度
	private double latitude;// 纬度
	private String btsName;// 基站名称
	private String bscName;
	private long btsId;
	private String circuitRoomOwnership;//机房产权
	private String transOwnership;//传输产权
	private String vendorBtsType;//设备类型
	private int cityId;//地市ID
	private int countryId;//乡镇ID
	private long relatedBts;
	private int isShare;//是否共享，0=不共享  1=共享
	private int bbuType;//1=纯BBU，2=纯BBU共站BBU，3=基站共站BBU
	private long relateWyBts;//共站基站或者BBU的唯一标识
	
	private String highTrainFlag;//高铁覆盖标识
	private Integer redLineFlag;//1=红线内，2=红线外
	
	
	private int deleteFlag;
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
	public String getBbuNo() {
		return bbuNo;
	}
	public void setBbuNo(String bbuNo) {
		this.bbuNo = bbuNo;
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
	public String getBtsName() {
		return btsName;
	}
	public void setBtsName(String btsName) {
		this.btsName = btsName;
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
	public String getCircuitRoomOwnership() {
		return circuitRoomOwnership;
	}
	public void setCircuitRoomOwnership(String circuitRoomOwnership) {
		this.circuitRoomOwnership = circuitRoomOwnership;
	}
	public String getTransOwnership() {
		return transOwnership;
	}
	public void setTransOwnership(String transOwnership) {
		this.transOwnership = transOwnership;
	}
	public String getVendorBtsType() {
		return vendorBtsType;
	}
	public void setVendorBtsType(String vendorBtsType) {
		this.vendorBtsType = vendorBtsType;
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
	public int getIsShare() {
		return isShare;
	}
	public void setIsShare(int isShare) {
		this.isShare = isShare;
	}
	public int getBbuType() {
		return bbuType;
	}
	public void setBbuType(int bbuType) {
		this.bbuType = bbuType;
	}
	public long getRelateWyBts() {
		return relateWyBts;
	}
	public void setRelateWyBts(long relateWyBts) {
		this.relateWyBts = relateWyBts;
	}
	public long getRelatedBts() {
		return relatedBts;
	}
	public void setRelatedBts(long relatedBts) {
		this.relatedBts = relatedBts;
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
