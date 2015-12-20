package com.sctt.net.bts.bean.cdma;

public class Bts {
	private long intId;
	private String name;
	private double longitude;  // ����
	private double latitude;   // γ��
	private String bscName;
	private long btsId;
	private String vendorBtsType;
	private long relatedBsc;
	
	//����һ������վ��ID
	private long relatedWyBts;
	
	
	
	//����һ��
	private int cityId;// ����ID
	private int countryId;// ����ID
	
	private String circuitRoomOwnership;
	private String transOwnership;
	private int sourceCityId;
	private String highTrainFlag;//�������Ǳ�ʶ
	private Integer redLineFlag;//1=�����ڣ�2=������
	
	private boolean isSpecial;//�Ƿ�����վ��
	private String judgeMsg;//���������ж�ԭ��

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

	public boolean isSpecial() {
		return isSpecial;
	}

	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}

	public String getJudgeMsg() {
		return judgeMsg;
	}

	public void setJudgeMsg(String judgeMsg) {
		this.judgeMsg = judgeMsg;
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
    
}
