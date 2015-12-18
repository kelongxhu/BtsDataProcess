package com.sctt.net.bts.bean.cdma;

import java.util.Date;

public class WyBtsSpecial {
	private long intId;
	private String name;//名称
	private int cityId;// 地市ID
	private String bscName;
	private long btsId;
	private String btsName;
	private Date inTime;
	private Date updateTime;
	private int type;//1小区，2，基站
	private int netType;//网络类型 1=CDMA 2=LTE
	private int state;//1=新建未验收站，2=调测站 3=升级站
	private int deleteFlag;//删除标识
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
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
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
	public Date getInTime() {
		return inTime;
	}
	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNetType() {
		return netType;
	}
	public void setNetType(int netType) {
		this.netType = netType;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	

}
