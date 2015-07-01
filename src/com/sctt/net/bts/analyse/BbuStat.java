package com.sctt.net.bts.analyse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sctt.net.bts.bean.Bts;
import com.sctt.net.bts.bean.Country;
import com.sctt.net.bts.bean.WyBbu;
import com.sctt.net.bts.bean.WyWrongName;
import com.sctt.net.common.util.StringUtils;

public class BbuStat {
	// bbu按站点分组
	Map<String, List<WyBbu>> bbuArrayMap = new HashMap<String, List<WyBbu>>();

	// 存储bbu设备
	Map<String, WyBbu> bbuMap = new HashMap<String, WyBbu>();
//	
//	// 错误名称Map
//	Map<String, WyWrongName> wrongMap = new HashMap<String, WyWrongName>();

	public void bbuStat(Bts bts) {
		String btsName = bts.getName();
		String[] array = btsName.split("_");
		String name = array[0];// 物理名称
		// 生成bbu
		WyBbu wyBbu = new WyBbu();
		String bzKey = name + "_" + bts.getBtsId() + "_" + bts.getBscName();
		wyBbu.setIntId(bzKey.hashCode());
		wyBbu.setName(name);
		wyBbu.setLongitude(bts.getLongitude());
		wyBbu.setLatitude(bts.getLatitude());
		wyBbu.setBtsName(bts.getName());
		wyBbu.setBscName(bts.getBscName());
		wyBbu.setBtsId(bts.getBtsId());
		wyBbu.setVendorBtsType(bts.getVendorBtsType());
		wyBbu.setRelatedBts(bts.getIntId());
		wyBbu.setRelateWyBts(bts.getRelatedWyBts());
		wyBbu.setDeleteFlag(0);//在用BBU
		wyBbu.setCityId(bts.getCityId());
		wyBbu.setCountryId(bts.getCountryId());
		wyBbu.setHighTrainFlag(bts.getHighTrainFlag());
		wyBbu.setRedLineFlag(bts.getRedLineFlag());
		
		
		// 组装
		List<WyBbu> bbuArray = bbuArrayMap.get(name);
		if (bbuArray == null) {
			bbuArray = new ArrayList<WyBbu>();
			bbuArray.add(wyBbu);
			bbuArrayMap.put(name, bbuArray);
		} else {
			bbuArray.add(wyBbu);
		}
	}

	public void finishBbuStat() {
		for (String keyObj : bbuArrayMap.keySet()) {
			List<WyBbu> bbuList = bbuArrayMap.get(keyObj);
			long relateWyBts = 0;
			int bbuType = 0;
			for (WyBbu bbu : bbuList) {
				String btsName = bbu.getBtsName();
				String[] array = btsName.split("_");
				if ("BBU1".equals(array[1])) {
					if (btsName.contains("共站")) {
						// 标识该组与基站共站
						relateWyBts = bbu.getRelateWyBts();
						bbuType = 3;
					} else {
						// 标识该组与BBU共站
						if (bbuList.size() == 1) {
							bbuType = 1;// 纯BBU，旗下无其他BBU设备
						} else {
							bbuType = 2;// 旗下有其他BBU设备
						}
						relateWyBts = bbu.getIntId();
					}
					break;
				}
			}

			// 赋值存储
			for (WyBbu bbu : bbuList) {
				String btsName = bbu.getBtsName();
				String[] array = btsName.split("_");
				if (array[1].length() > 3) {
					bbu.setBbuNo(array[1].substring(3));// BbuNo
				}
				bbu.setBbuType(bbuType);// 共站类型
				if (btsName.contains("共站")) {
					bbu.setIsShare(1);// 共站
					bbu.setRelateWyBts(relateWyBts);
				} else {
					bbu.setIsShare(0);// 不共站
					String highTrainFlag=bbu.getHighTrainFlag();
					String circuitRoomOwnership="";
					String transOwnership="";
					if(null!=highTrainFlag&&!"".equals(highTrainFlag)){
						circuitRoomOwnership=array[3];
						transOwnership=array[4];
					}else{
						circuitRoomOwnership=array[2];
						transOwnership=array[3];
					}
					bbu.setCircuitRoomOwnership(circuitRoomOwnership);
					bbu.setTransOwnership(transOwnership);
				}
				bbuMap.put(bbu.getIntId() + "", bbu);
			}

		}
	}
	

	public Map<String, WyBbu> getBbuMap() {
		return bbuMap;
	}

	
	
	
}
