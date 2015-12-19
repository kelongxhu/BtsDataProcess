package com.sctt.net.bts.analyse.lte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sctt.net.bts.bean.lte.Enodeb;
import com.sctt.net.bts.bean.lte.LteBbu;

public class LteBbuStat {
	// bbu按站点分组
	Map<String, List<LteBbu>> bbuArrayMap = new HashMap<String, List<LteBbu>>();

	// 存储bbu设备
	Map<String, LteBbu> bbuMap = new HashMap<String, LteBbu>();
    /**
     * 统计数据
     * @param bts
     */
	public void bbuStat(Enodeb bts) {
		String btsName = bts.getUserLabel();
		String[] array = btsName.split("_");
		String name = array[0];// 物理名称
		int enbId=bts.getEnbId();
		// 生成bbu
		LteBbu lteBbu = new LteBbu();
		String bzKey = name+"_"+enbId;
		long intId = bzKey.hashCode();
		lteBbu.setIntId(intId);
		lteBbu.setName(name);
		lteBbu.setEnbBtsName(bts.getUserLabel());
		lteBbu.setRelateEnbBtsId(bts.getIntId());
		lteBbu.setWyLteBtsId(bts.getRelatedWyLteBtsId());
		lteBbu.setCityId(bts.getCityId());
		lteBbu.setCountryId(bts.getCountryId());
		lteBbu.setHightranFlag(bts.getHighTrainFlag());
		lteBbu.setRedlineFlag(bts.getRedLineFlag());
		lteBbu.setCircuitRoomOwnership(bts.getCircuitRoomOwnership());
		lteBbu.setTransOwnership(bts.getTransOwnership());
		lteBbu.setVendorName(bts.getVenderName());
		lteBbu.setEnbId(bts.getEnbId());
		// 组装
		List<LteBbu> bbuArray = bbuArrayMap.get(name);
		if (bbuArray == null) {
			bbuArray = new ArrayList<LteBbu>();
			bbuArray.add(lteBbu);
			bbuArrayMap.put(name, bbuArray);
		} else {
			bbuArray.add(lteBbu);
		}
	}
    /**
     * 收割数据
     */
	public void finishBbuStat() {
		for (String keyObj : bbuArrayMap.keySet()) {
			List<LteBbu> bbuList = bbuArrayMap.get(keyObj);
			long relateWyBts = 0;
			int bbuType = 0;
			//定义BBU的TYPE
			for (LteBbu bbu : bbuList) {
				String btsName = bbu.getEnbBtsName();
				String[] array = btsName.split("_");
				if ("BBU1".equals(array[1])) {
					if (btsName.contains("共站")) {
						// 标识该组与基站共站
						relateWyBts = bbu.getWyLteBtsId();
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
			// 判断是否共站
			for (LteBbu bbu : bbuList) {
				String btsName = bbu.getEnbBtsName();
				String[] array = btsName.split("_");
				if (array[1].length() > 3) {
					bbu.setBbuNo(array[1].substring(3));// BbuNo
				}
				bbu.setBuuType(bbuType);// 共站类型
				if (btsName.contains("共站")) {
					bbu.setIsShare(1);// 共站
					bbu.setWyLteBtsId(relateWyBts);
				} else {
					bbu.setIsShare(0);// 不共站
				}
				//IS_SHARE=0 BBU_TYPE=1       纯BBU
				//IS_SHARE=1 BBU_TYPE=2       共站BBU
				//IS_SHARE=1 BBU_TYPE=3      与物理站点共站
				bbuMap.put(bbu.getIntId() + "", bbu);
			}

		}
	}

	public Map<String, LteBbu> getBbuMap() {
		return bbuMap;
	}

}
