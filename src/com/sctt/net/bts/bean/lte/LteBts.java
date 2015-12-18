package com.sctt.net.bts.bean.lte;

public class LteBts {
	private Long intId;
	private String name;
	private double longitude;// 经度
	private double latitude;// 纬度
	private String isIndoor;
	private String isRru;
	private Long relateEnbIntId;
	private String enbName;
	private String circuitRoomOwnerShip;
	private String transOwnerShip;
	private String venderName;
	private String serviceLevel;
	private String hightrainFlag;
	private Integer redlineFlag;
	private Integer cityId;
	private Integer countryId;
	private String siteTogether;
	private Integer deleteFlag;//刪除标识

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

	public Long getRelateEnbIntId() {
		return relateEnbIntId;
	}

	public void setRelateEnbIntId(Long relateEnbIntId) {
		this.relateEnbIntId = relateEnbIntId;
	}

	public String getEnbName() {
		return enbName;
	}

	public void setEnbName(String enbName) {
		this.enbName = enbName;
	}

	public String getCircuitRoomOwnerShip() {
		return circuitRoomOwnerShip;
	}

	public void setCircuitRoomOwnerShip(String circuitRoomOwnerShip) {
		this.circuitRoomOwnerShip = circuitRoomOwnerShip;
	}

	public String getTransOwnerShip() {
		return transOwnerShip;
	}

	public void setTransOwnerShip(String transOwnerShip) {
		this.transOwnerShip = transOwnerShip;
	}

	public String getVenderName() {
		return venderName;
	}

	public void setVenderName(String venderName) {
		this.venderName = venderName;
	}

	public String getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public String getHightrainFlag() {
		return hightrainFlag;
	}

	public void setHightrainFlag(String hightrainFlag) {
		this.hightrainFlag = hightrainFlag;
	}

	
	public Integer getRedlineFlag() {
		return redlineFlag;
	}

	public void setRedlineFlag(Integer redlineFlag) {
		this.redlineFlag = redlineFlag;
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

	public String getSiteTogether() {
		return siteTogether;
	}

	public void setSiteTogether(String siteTogether) {
		this.siteTogether = siteTogether;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((intId == null) ? 0 : intId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LteBts other = (LteBts) obj;
		if (intId == null) {
			if (other.intId != null)
				return false;
		} else if (!intId.equals(other.intId))
			return false;
		return true;
	}
	
	
	
	

}
