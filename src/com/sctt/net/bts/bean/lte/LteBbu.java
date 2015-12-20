package com.sctt.net.bts.bean.lte;

public class LteBbu {
	private Long intId;
	private String name;
	private String bbuNo;// BBU编号
	private Integer enbId;//ENBID
	private Long relateEnbBtsId;
	private String enbBtsName;
	private String circuitRoomOwnership;
	private String transOwnership;
	private String vendorName;
	private Integer cityId;
	private Integer countryId;
	private Integer isShare;
	private Integer buuType;
	private Long WyLteBtsId;   //关联的物理站点
	private String hightranFlag;
	private Integer redlineFlag;
	private Integer deleteFlag;//删除标识
	private String siteTogether;//共站标识
	public Long getIntId() {
		return intId;
	}
	public void setIntId(Long intId) {
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
	
	public Integer getEnbId() {
		return enbId;
	}
	public void setEnbId(Integer enbId) {
		this.enbId = enbId;
	}
	public Long getRelateEnbBtsId() {
		return relateEnbBtsId;
	}
	public void setRelateEnbBtsId(Long relateEnbBtsId) {
		this.relateEnbBtsId = relateEnbBtsId;
	}
	public String getEnbBtsName() {
		return enbBtsName;
	}
	public void setEnbBtsName(String enbBtsName) {
		this.enbBtsName = enbBtsName;
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
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public Integer getIsShare() {
		return isShare;
	}
	public void setIsShare(Integer isShare) {
		this.isShare = isShare;
	}
	public Integer getBuuType() {
		return buuType;
	}
	public void setBuuType(Integer buuType) {
		this.buuType = buuType;
	}
	
	public Long getWyLteBtsId() {
		return WyLteBtsId;
	}
	public void setWyLteBtsId(Long wyLteBtsId) {
		WyLteBtsId = wyLteBtsId;
	}
	
	public String getHightranFlag() {
		return hightranFlag;
	}
	public void setHightranFlag(String hightranFlag) {
		this.hightranFlag = hightranFlag;
	}
	public Integer getRedlineFlag() {
		return redlineFlag;
	}
	public void setRedlineFlag(Integer redlineFlag) {
		this.redlineFlag = redlineFlag;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getSiteTogether() {
		return siteTogether;
	}
	public void setSiteTogether(String siteTogether) {
		this.siteTogether = siteTogether;
	}
	
	
}
