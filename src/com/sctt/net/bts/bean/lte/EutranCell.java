package com.sctt.net.bts.bean.lte;
/**
 * tco_pro_eutrancell_m��
 * @author _think
 *
 */
public class EutranCell {
	private Long intId;
	private String relateEnbUserLabel;//������վ����
	private String userLabel;//С������
	private String vendorName;
	private Long relatedEnbIntId;//������վID
	private String eci; //ECI
	
	//������Ϣ
	private String judgeMsg;//�жϴ���������Ϣ
	
	private int cityId; // ����ID
	private int countryId; // ����ID
	private String circuitRoomOwnership;// ��վ��Ȩ
	private String transOwnership;// ��վ�����Ȩ
	private String isIndoor;// �Ƿ��ҷ�
	private String isRru;// �Ƿ���Զ
	private String isGf;// �Ƿ񹦷�
	private int isOr;// ����ֱ��վ����
	private int isRR;// ����ֱ��վ����
	private int isSR;// ��Ƶֱ��վ����
	private long wyLteIntId;// ����վ��INT_ID
	private String serviceLevel;// ά���ȼ�
	//2014-10-5����
	private String highTrainFlag;//�������Ǳ�ʶ
	private Integer redLineFlag;//1=�����ڣ�2=������
	private int deleteFlag;
	private boolean isSpecial;//�Ƿ�����վ��
    private String siteTogether;
    private String cityName;//��������
	public Long getIntId() {
		return intId;
	}
	public void setIntId(Long intId) {
		this.intId = intId;
	}
	public String getRelateEnbUserLabel() {
		return relateEnbUserLabel;
	}
	public void setRelateEnbUserLabel(String relateEnbUserLabel) {
		this.relateEnbUserLabel = relateEnbUserLabel;
	}
	public String getUserLabel() {
		return userLabel;
	}
	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
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
	public String getIsIndoor() {
		return isIndoor;
	}
	public void setIsIndoor(String isIndoor) {
		this.isIndoor = isIndoor;
	}
	public String getIsRru() {
		return isRru;
	}
	public void setIsRru(String isRru) {
		this.isRru = isRru;
	}
	public String getIsGf() {
		return isGf;
	}
	public void setIsGf(String isGf) {
		this.isGf = isGf;
	}
	public int getIsOr() {
		return isOr;
	}
	public void setIsOr(int isOr) {
		this.isOr = isOr;
	}
	public int getIsRR() {
		return isRR;
	}
	public void setIsRR(int isRR) {
		this.isRR = isRR;
	}
	public int getIsSR() {
		return isSR;
	}
	public void setIsSR(int isSR) {
		this.isSR = isSR;
	}

	public String getServiceLevel() {
		return serviceLevel;
	}
	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
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
	public int getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public boolean isSpecial() {
		return isSpecial;
	}
	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}
    
	public String getSiteTogether() {
		return siteTogether;
	}
	public void setSiteTogether(String siteTogether) {
		this.siteTogether = siteTogether;
	}
	public Long getRelatedEnbIntId() {
		return relatedEnbIntId;
	}
	public void setRelatedEnbIntId(Long relatedEnbIntId) {
		this.relatedEnbIntId = relatedEnbIntId;
	}
	public long getWyLteIntId() {
		return wyLteIntId;
	}
	public void setWyLteIntId(long wyLteIntId) {
		this.wyLteIntId = wyLteIntId;
	}
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getEci() {
		return eci;
	}
	public void setEci(String eci) {
		this.eci = eci;
	}
	@Override
	public String toString() {
		return "EutranCell [intId=" + intId + ", relateEnbUserLabel="
				+ relateEnbUserLabel + ", userLabel=" + userLabel
				+ ", vendorName=" + vendorName + ", relatedEnbIntId="
				+ relatedEnbIntId + ", judgeMsg=" + judgeMsg + ", cityId="
				+ cityId + ", countryId=" + countryId
				+ ", circuitRoomOwnership=" + circuitRoomOwnership
				+ ", transOwnership=" + transOwnership + ", isIndoor="
				+ isIndoor + ", isRru=" + isRru + ", isGf=" + isGf + ", isOr="
				+ isOr + ", isRR=" + isRR + ", isSR=" + isSR + ", wyLteIntId="
				+ wyLteIntId + ", serviceLevel=" + serviceLevel
				+ ", highTrainFlag=" + highTrainFlag + ", redLineFlag="
				+ redLineFlag + ", deleteFlag=" + deleteFlag + ", isSpecial="
				+ isSpecial + ", siteTogether=" + siteTogether + "]";
	}
	
	
}
