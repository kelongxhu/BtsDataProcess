package com.sctt.net.common.util;

public enum WrongMsg{
	MISS("命名必有项有缺失或者错误"),
	OWNER_RIGHT("基站产权标识命名错误"),
	TRANS_RIGHT("传输产权标识命名错误"),
	RANK("维护等级命名错误"),
	GOFEN("功分信息标识命名错误"),
	ZF("直放站标识命名错误"),
	LY("非拉远基站，基站名称必须与分裂出来的站点名称一样"),
	CITY("站点名称中区县命名错误"),
	BBU_BTS_NOTEXIST("BBU1包含共站，站点中不能找到相同名称站点"),
	BBU_BHGZ("BBU2以上必须包含共站标识"),
	BBU_EXIST_GZ("BBU2以后必须存在共站"),
	BBU_FLAG_ERROR("BBU位命名错误"),
	ERROR("未知错误");
	private String wrongMsg;
	private WrongMsg(String wrongMsg){
		this.wrongMsg=wrongMsg;
	}
	
	public String getWrongMsg(){
	    return this.wrongMsg;
	}
	 
	
}
