package com.sctt.net.bts.bean.cdma;

/**
 * С��������������ʵ�����
 * 
 * @author Administrator
 * 
 */

public class WyWrongName {
	private long int_id;// С��int_id
	private String cellName;// С������
	private String bscName;// ��վ����
	private long btsId;// ��վID
	private String btsName;//��վ����
	private int type;//��������
	private Integer cityId;//���ڱ�����
	private Integer netType;//��������
	private String wrongMsg;//����ԭ��
	
	
	private int deleteFlag;

	public long getInt_id() {
		return int_id;
	}

	public void setInt_id(long int_id) {
		this.int_id = int_id;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Integer getNetType() {
		return netType;
	}

	public void setNetType(Integer netType) {
		this.netType = netType;
	}

	public String getWrongMsg() {
		return wrongMsg;
	}

	public void setWrongMsg(String wrongMsg) {
		this.wrongMsg = wrongMsg;
	}
	

}
