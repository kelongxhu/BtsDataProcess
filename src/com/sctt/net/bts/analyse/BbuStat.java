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
	// bbu��վ�����
	Map<String, List<WyBbu>> bbuArrayMap = new HashMap<String, List<WyBbu>>();

	// �洢bbu�豸
	Map<String, WyBbu> bbuMap = new HashMap<String, WyBbu>();
//	
//	// ��������Map
//	Map<String, WyWrongName> wrongMap = new HashMap<String, WyWrongName>();

	public void bbuStat(Bts bts) {
		String btsName = bts.getName();
		String[] array = btsName.split("_");
		String name = array[0];// ��������
		// ����bbu
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
		wyBbu.setDeleteFlag(0);//����BBU
		wyBbu.setCityId(bts.getCityId());
		wyBbu.setCountryId(bts.getCountryId());
		wyBbu.setHighTrainFlag(bts.getHighTrainFlag());
		wyBbu.setRedLineFlag(bts.getRedLineFlag());
		
		
		// ��װ
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
					if (btsName.contains("��վ")) {
						// ��ʶ�������վ��վ
						relateWyBts = bbu.getRelateWyBts();
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

			// ��ֵ�洢
			for (WyBbu bbu : bbuList) {
				String btsName = bbu.getBtsName();
				String[] array = btsName.split("_");
				if (array[1].length() > 3) {
					bbu.setBbuNo(array[1].substring(3));// BbuNo
				}
				bbu.setBbuType(bbuType);// ��վ����
				if (btsName.contains("��վ")) {
					bbu.setIsShare(1);// ��վ
					bbu.setRelateWyBts(relateWyBts);
				} else {
					bbu.setIsShare(0);// ����վ
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
