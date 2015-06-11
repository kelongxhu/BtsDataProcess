package com.sctt.net.bts.analyse;

import java.util.HashMap;
import java.util.Map;

public class InitInstance {
	private static InitInstance instance = null;

	private Map<String, Integer> cityMap = new HashMap<String, Integer>();

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
	 * ��ʼ��ԭ���ܺ���ϵͳ��������Ŷ�Ӧ��ϵ
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
	 * ��ȡ����wy_city���
	 * 
	 * @param cityId
	 * @return
	 */
	public Integer getCityId(String cityId) {
		return cityMap.get(cityId);
	}

}
