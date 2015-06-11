package com.sctt.net.bts.bean;

public class Country {

	private int id;
	private int cityId;
	private String contryName;// 区县
	private String cityName;// 地市
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getContryName() {
		return contryName;
	}

	public void setContryName(String contryName) {
		this.contryName = contryName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}
