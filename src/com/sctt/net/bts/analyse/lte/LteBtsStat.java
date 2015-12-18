package com.sctt.net.bts.analyse.lte;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sctt.net.bts.bean.cdma.WyBtsSpecial;
import com.sctt.net.bts.bean.cdma.WyWrongName;
import com.sctt.net.bts.bean.lte.Enodeb;
import com.sctt.net.bts.bean.lte.EutranCell;
import com.sctt.net.bts.bean.lte.LteBts;
import com.sctt.net.common.util.AnalyseUtil;
import com.sctt.net.common.util.Constants;

public class LteBtsStat {
	private static Logger logger = Logger.getLogger("baseLog");
	// �洢����վ��Map,�ҷֺͷ��ҷ֣�keyΪint_id
	Map<String, LteBts> lteBtsMap = new HashMap<String, LteBts>();
	// �洢����վ�㣬key��Ϊ����վ������
	Map<String, LteBts> noIndoorMap = new HashMap<String, LteBts>();
	// ��������Map
	Map<String, WyWrongName> wrongMap = new HashMap<String, WyWrongName>();
	// ����״̬��վС��
	Map<String, WyBtsSpecial> btsSpecialMap = new HashMap<String, WyBtsSpecial>();
	// ����С��Map
	Map<String, EutranCell> cellMap = new HashMap<String, EutranCell>();
	/**
	 * ����С������
	 * 
	 * @param cell
	 *            С����Ϣ
	 * @return������վ���Ψһ��ʶ
	 */
	public void btsStat(EutranCell cell) {
		String cellName = cell.getUserLabel();
		String[] splitName = cellName.split("_");                                              
		String name = splitName[3];// ����վ������
		String btsKey = name;
		long btsKeyCode = btsKey.hashCode();
		LteBts lteBts = lteBtsMap.get(String.valueOf(btsKeyCode));
		if (lteBts == null) {
			lteBts = new LteBts();
			lteBts.setIntId(btsKeyCode);
			lteBts.setName(name);
			lteBts.setVenderName(cell.getVendorName());
			lteBts.setIsIndoor(cell.getIsIndoor());// �Ƿ��ҷ�
			lteBts.setIsRru(cell.getIsRru());// �Ƿ���Զ
			lteBts.setEnbName(cell.getRelateEnbUserLabel());
			lteBts.setCircuitRoomOwnerShip(cell.getCircuitRoomOwnership());
			lteBts.setTransOwnerShip(cell.getTransOwnership());// �����Ȩ
			lteBts.setServiceLevel(cell.getServiceLevel());// ά���ȼ�
			lteBts.setCityId(cell.getCityId());
			lteBts.setCountryId(cell.getCountryId());
			lteBts.setRelateEnbIntId(cell.getRelatedEnbIntId());
			lteBts.setHightrainFlag(cell.getHighTrainFlag());
			lteBts.setRedlineFlag(cell.getRedLineFlag());
			lteBts.setSiteTogether(cell.getSiteTogether());
			lteBtsMap.put(btsKeyCode + "", lteBts);
		}
		// �洢���ҷ�����վ��Map
		String isIndoor = cell.getIsIndoor();
		if ("��".equals(isIndoor)) {
			LteBts site = noIndoorMap.get(name);
			if (site == null) {
				site = new LteBts();
				site.setIntId(btsKeyCode);
				site.setName(name);
				noIndoorMap.put(name, site);
			}
		}
		cell.setWyBtsIntId(lteBts.getIntId());
		cell.setDeleteFlag(0);// ����С��
		cellMap.put(cell.getIntId() + "", cell);
	}

	/**
	 * ���Ӵ�������С������
	 * 
	 * @param cell
	 */
	public void addWrongCell(EutranCell cell) {
		WyWrongName wwN = new WyWrongName();
		wwN.setInt_id(cell.getIntId());
		wwN.setCellName(cell.getUserLabel());
		wwN.setBtsId(cell.getRelatedEnbIntId());
		wwN.setBtsName(cell.getRelateEnbUserLabel());
		wwN.setType(Constants.CELL);//С��
		wwN.setCityId(cell.getCityId());
		wwN.setNetType(Constants.LTE);
		wrongMap.put(cell.getIntId() + "", wwN);
	}

	public void addWrongBbu(Enodeb bbu) {
		WyWrongName wwN = new WyWrongName();
		wwN.setInt_id(bbu.getIntId());
		wwN.setCellName(bbu.getUserLabel());
		wwN.setCityId(bbu.getCityId());
		// wwN.setBtsName();
		wwN.setNetType(Constants.LTE);
		wwN.setType(Constants.BBU);//bbu
		wrongMap.put(bbu.getIntId() + "", wwN);
	}

	/**
	 * �������վ��С��
	 * @param cell
	 */
	public void addSpecialCell(EutranCell cell) {
		String name = cell.getUserLabel();
		String type = name.substring(name.length() - 2);
		WyBtsSpecial btsSpecical = new WyBtsSpecial();
		btsSpecical.setIntId(cell.getIntId());
		btsSpecical.setBtsName(cell.getRelateEnbUserLabel());
		btsSpecical.setBtsId(cell.getRelatedEnbIntId());
		btsSpecical.setCityId(cell.getCityId());
		btsSpecical.setName(cell.getUserLabel());
		btsSpecical.setDeleteFlag(0);
		btsSpecical.setInTime(new Date());
		btsSpecical.setNetType(Constants.LTE);// CDMA
		btsSpecical.setState(AnalyseUtil.getSpecicalState(type));
		btsSpecical.setType(Constants.CELL);
		btsSpecialMap.put(cell.getIntId()+"", btsSpecical);
	}
	
	/**
	 * �������վ���վ
	 * @param cell
	 */
	public void addSpecialBts(Enodeb bbu) {
		String name = bbu.getUserLabel();
		String type = name.substring(name.length() - 2);
		WyBtsSpecial btsSpecical = new WyBtsSpecial();
		btsSpecical.setIntId(bbu.getIntId());
		btsSpecical.setBtsName(bbu.getUserLabel());
		btsSpecical.setDeleteFlag(0);
		btsSpecical.setInTime(new Date());
		btsSpecical.setNetType(Constants.LTE);// CDMA
		btsSpecical.setState(AnalyseUtil.getSpecicalState(type));
		btsSpecical.setType(Constants.BBU);//վ��
		btsSpecialMap.put(bbu.getIntId()+"", btsSpecical);
	}

	public Map<String, LteBts> getLteBtsMap() {
		return lteBtsMap;
	}

	public Map<String, LteBts> getNoIndoorMap() {
		return noIndoorMap;
	}

	public Map<String, WyWrongName> getWrongMap() {
		return wrongMap;
	}

	public Map<String, WyBtsSpecial> getBtsSpecialMap() {
		return btsSpecialMap;
	}

	public Map<String, EutranCell> getCellMap() {
		return cellMap;
	}
	
	
}
