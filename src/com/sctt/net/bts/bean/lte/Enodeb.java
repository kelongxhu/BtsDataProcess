package com.sctt.net.bts.bean.lte;

public class Enodeb {
	private Long intId;
	private String userLabel;
	private String venderName;
	private Integer enbId;
	private double longitude;
	private double latitude;
	

	//����һ������վ��ID
	private long relatedWyLteBtsId;
	//����һ��
	private int cityId;// ����ID
	private int countryId;// ����ID
	private String circuitRoomOwnership;
	private String transOwnership;
	private String highTrainFlag;//�������Ǳ�ʶ
	private Integer redLineFlag;//1=�����ڣ�2=������	
	private boolean isSpecial;//�Ƿ�����վ��
	private String judgeMsg;//����bbu���ж�ԭ��
	private Integer deleteFlag;//ɾ����ʶ
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
	
	public Integer getEnbId() {
		return enbId;
	}
	public void setEnbId(Integer enbId) {
		this.enbId = enbId;
	}
	
	public long getRelatedWyLteBtsId() {
		return relatedWyLteBtsId;
	}
	public void setRelatedWyLteBtsId(long relatedWyLteBtsId) {
		this.relatedWyLteBtsId = relatedWyLteBtsId;
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
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	
	
}
