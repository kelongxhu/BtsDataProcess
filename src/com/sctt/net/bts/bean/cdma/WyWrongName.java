package com.sctt.net.bts.bean.cdma;

/**
 * 小区错误名称数据实体对象
 * 
 * @author Administrator
 * 
 */

public class WyWrongName {
	private long int_id;// 小区int_id
	private String cellName;// 小区名称
	private String bscName;// 基站名称
	private long btsId;// 基站ID
	private String btsName;//基站名称
	private int type;//错误类型
	private Integer cityId;//所在本地网
	private Integer netType;//网络类型
	private String wrongMsg;//错误原因
	
	
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
