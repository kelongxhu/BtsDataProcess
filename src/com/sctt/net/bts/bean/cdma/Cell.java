package com.sctt.net.bts.bean.cdma;

public class Cell {

	private long intId;
	private String name;// С������
	private String bscName;// BSC����
	private long btsId;
	private String btsName;// ��վ����
	private int cityId; // ����ID
	private int countryId; // ����ID
	private long relateBts;
	
	
	private int cellId;
	private int ci;
	private int pn;
	private int lac;
	private int do_cell;

	// �����ֹ����ݱ�c_tco_pro_cell

	private double longitude;// ����
	private double latitude;// γ��

	// ����c_bts��
	private String vendor_btstype;

	// ͳ�������ֶ�

	private long pyBtsId;// �����վΨһ��ʶ

	private String circuitRoomOwnership;// ��վ��Ȩ
	private String transOwnership;// ��վ�����Ȩ
	private String isIndoor;// �Ƿ��ҷ�
	private String isRru;// �Ƿ���Զ
	private String isGf;// �Ƿ񹦷�
	private int isOr;// ����ֱ��վ����
	private int isRR;// ����ֱ��վ����
	private int isSR;// ��Ƶֱ��վ����
	private long wyBtsIntId;// ����վ��INT_ID
	private String serviceLevel;// ά���ȼ�
	//2014-10-5����
	private String highTrainFlag;//�������Ǳ�ʶ
	private Integer redLineFlag;//1=�����ڣ�2=������
	private boolean isTunel;//�Ƿ����
	
	private int deleteFlag;
	
	
	private int sourceCityId;
	private boolean isSpecial;//�Ƿ�����վ��
	private String judgeMsg;//����С��ԭ��

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

	public String getBtsName() {
		return btsName;
	}

	public void setBtsName(String btsName) {
		this.btsName = btsName;
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

	public long getPyBtsId() {
		return pyBtsId;
	}

	public void setPyBtsId(long pyBtsId) {
		this.pyBtsId = pyBtsId;
	}

	public String getVendor_btstype() {
		return vendor_btstype;
	}

	public void setVendor_btstype(String vendor_btstype) {
		this.vendor_btstype = vendor_btstype;
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

	public long getWyBtsIntId() {
		return wyBtsIntId;
	}

	public void setWyBtsIntId(long wyBtsIntId) {
		this.wyBtsIntId = wyBtsIntId;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getCityId() {
		return cityId;
	}

	public String getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public int getCellId() {
		return cellId;
	}

	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	public int getCi() {
		return ci;
	}

	public void setCi(int ci) {
		this.ci = ci;
	}

	public int getPn() {
		return pn;
	}

	public void setPn(int pn) {
		this.pn = pn;
	}

	public int getLac() {
		return lac;
	}

	public void setLac(int lac) {
		this.lac = lac;
	}

	public int getDo_cell() {
		return do_cell;
	}

	public void setDo_cell(int do_cell) {
		this.do_cell = do_cell;
	}

	public long getRelateBts() {
		return relateBts;
	}

	public void setRelateBts(long relateBts) {
		this.relateBts = relateBts;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
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

	public boolean isTunel() {
		return isTunel;
	}

	public void setTunel(boolean isTunel) {
		this.isTunel = isTunel;
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

	
	
	
	
	
	
	
	
	
	
	
	
	

	
}
