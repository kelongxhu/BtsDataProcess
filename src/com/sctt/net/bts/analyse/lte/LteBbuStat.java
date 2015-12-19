package com.sctt.net.bts.analyse.lte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sctt.net.bts.bean.lte.Enodeb;
import com.sctt.net.bts.bean.lte.LteBbu;

public class LteBbuStat {
	// bbu��վ�����
	Map<String, List<LteBbu>> bbuArrayMap = new HashMap<String, List<LteBbu>>();

	// �洢bbu�豸
	Map<String, LteBbu> bbuMap = new HashMap<String, LteBbu>();
    /**
     * ͳ������
     * @param bts
     */
	public void bbuStat(Enodeb bts) {
		String btsName = bts.getUserLabel();
		String[] array = btsName.split("_");
		String name = array[0];// ��������
		int enbId=bts.getEnbId();
		// ����bbu
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
		// ��װ
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
     * �ո�����
     */
	public void finishBbuStat() {
		for (String keyObj : bbuArrayMap.keySet()) {
			List<LteBbu> bbuList = bbuArrayMap.get(keyObj);
			long relateWyBts = 0;
			int bbuType = 0;
			//����BBU��TYPE
			for (LteBbu bbu : bbuList) {
				String btsName = bbu.getEnbBtsName();
				String[] array = btsName.split("_");
				if ("BBU1".equals(array[1])) {
					if (btsName.contains("��վ")) {
						// ��ʶ�������վ��վ
						relateWyBts = bbu.getWyLteBtsId();
						bbuType = 3;
					} else {
						// ��ʶ������BBU��վ
						if (bbuList.size() == 1) {
							bbuType = 1;// ��BBU������������BBU�豸
						} else {
							bbuType = 2;// ����������BBU�豸
						}
						relateWyBts = bbu.getIntId();
					}
					break;
				}
			}
			// �ж��Ƿ�վ
			for (LteBbu bbu : bbuList) {
				String btsName = bbu.getEnbBtsName();
				String[] array = btsName.split("_");
				if (array[1].length() > 3) {
					bbu.setBbuNo(array[1].substring(3));// BbuNo
				}
				bbu.setBuuType(bbuType);// ��վ����
				if (btsName.contains("��վ")) {
					bbu.setIsShare(1);// ��վ
					bbu.setWyLteBtsId(relateWyBts);
				} else {
					bbu.setIsShare(0);// ����վ
				}
				//IS_SHARE=0 BBU_TYPE=1       ��BBU
				//IS_SHARE=1 BBU_TYPE=2       ��վBBU
				//IS_SHARE=1 BBU_TYPE=3      ������վ�㹲վ
				bbuMap.put(bbu.getIntId() + "", bbu);
			}

		}
	}

	public Map<String, LteBbu> getBbuMap() {
		return bbuMap;
	}

}
