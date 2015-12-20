package com.sctt.net.bts.analyse.cdma;

import java.util.HashMap;
import java.util.Map;

public class InitInstance {
	private static InitInstance instance = null;

	private Map<String, Integer> cityMap = new HashMap<String, Integer>();
	
	private Map<String,Integer> lteCityMap=new HashMap<String,Integer>();

	private InitInstance() {

	}

	public static InitInstance getInstance() {
		if (instance == null) {
			synchronized (InitInstance.class) {
				if (instance == null) {
					instance = new InitInstance();
				}
			}
		}
		return instance;
	}

	/**
	 * 初始化原网管和先系统本地网编号对应关系
	 */
	public void initCityMap() {
		cityMap.put("-1517652455", 10006);
		cityMap.put("80450634", 10004);
		cityMap.put("-947426178", 10008);
		cityMap.put("1263125286", 10003);
		cityMap.put("1031539022", 10005);
		cityMap.put("-1975087318", 10009);
		cityMap.put("-1109568583", 10010);
		cityMap.put("408057399", 10007);
		cityMap.put("203856069", 10002);
	}
	
	/**
	 * 初始化原网管和先系统本地网编号对应关系
	 */
	public void initLteCityMap() {
		lteCityMap.put("黔东南", 10006);
		lteCityMap.put("安顺", 10004);
		lteCityMap.put("毕节", 10008);
		lteCityMap.put("遵义", 10003);
		lteCityMap.put("黔南", 10005);
		lteCityMap.put("六盘水", 10009);
		lteCityMap.put("黔西南", 10010);
		lteCityMap.put("铜仁", 10007);
		lteCityMap.put("贵阳", 10002);
	}

	/**
	 * 获取地市wy_city编号
	 * 
	 * @param cityId
	 * @return
	 */
	public Integer getCityId(String cityId) {
		return cityMap.get(cityId);
	}

	public Integer getLteCityId(String cityName) {
		return lteCityMap.get(cityName);
	}
	
	
}
