package com.sctt.net.bts.bean.cdma;

public class WyBbu {
	private long intId;
	private String name;// ��������
	private String bbuNo;// BBU���
	private double longitude;// ����
	private double latitude;// γ��
	private String btsName;// ��վ����
	private String bscName;
	private long btsId;
	private String circuitRoomOwnership;//������Ȩ
	private String transOwnership;//�����Ȩ
	private String vendorBtsType;//�豸����
	private int cityId;//����ID
	private int countryId;//����ID
	private long relatedBts;
	private int isShare;//�Ƿ���0=������  1=����
	private int bbuType;//1=��BBU��2=��BBU��վBBU��3=��վ��վBBU
	private long relateWyBts;//��վ��վ����BBU��Ψһ��ʶ
	
	private String highTrainFlag;//�������Ǳ�ʶ
	private Integer redLineFlag;//1=�����ڣ�2=������
	
	
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
