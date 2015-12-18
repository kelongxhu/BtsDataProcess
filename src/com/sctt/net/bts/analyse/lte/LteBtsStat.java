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
	// 存储物理站点Map,室分和非室分，key为int_id
	Map<String, LteBts> lteBtsMap = new HashMap<String, LteBts>();
	// 存储物理站点，key，为物理站点名称
	Map<String, LteBts> noIndoorMap = new HashMap<String, LteBts>();
	// 错误名称Map
	Map<String, WyWrongName> wrongMap = new HashMap<String, WyWrongName>();
	// 特殊状态基站小区
	Map<String, WyBtsSpecial> btsSpecialMap = new HashMap<String, WyBtsSpecial>();
	// 分析小区Map
	Map<String, EutranCell> cellMap = new HashMap<String, EutranCell>();
	/**
	 * 分析小区名称
	 * 
	 * @param cell
	 *            小区信息
	 * @return：物理站点的唯一标识
	 */
	public void btsStat(EutranCell cell) {
		String cellName = cell.getUserLabel();
		String[] splitName = cellName.split("_");                                              
		String name = splitName[3];// 物理站点名称
		String btsKey = name;
		long btsKeyCode = btsKey.hashCode();
		LteBts lteBts = lteBtsMap.get(String.valueOf(btsKeyCode));
		if (lteBts == null) {
			lteBts = new LteBts();
			lteBts.setIntId(btsKeyCode);
			lteBts.setName(name);
			lteBts.setVenderName(cell.getVendorName());
			lteBts.setIsIndoor(cell.getIsIndoor());// 是否室分
			lteBts.setIsRru(cell.getIsRru());// 是否拉远
			lteBts.setEnbName(cell.getRelateEnbUserLabel());
			lteBts.setCircuitRoomOwnerShip(cell.getCircuitRoomOwnership());
			lteBts.setTransOwnerShip(cell.getTransOwnership());// 传输产权
			lteBts.setServiceLevel(cell.getServiceLevel());// 维护等级
			lteBts.setCityId(cell.getCityId());
			lteBts.setCountryId(cell.getCountryId());
			lteBts.setRelateEnbIntId(cell.getRelatedEnbIntId());
			lteBts.setHightrainFlag(cell.getHighTrainFlag());
			lteBts.setRedlineFlag(cell.getRedLineFlag());
			lteBts.setSiteTogether(cell.getSiteTogether());
			lteBtsMap.put(btsKeyCode + "", lteBts);
		}
		// 存储非室分物理站点Map
		String isIndoor = cell.getIsIndoor();
		if ("否".equals(isIndoor)) {
			LteBts site = noIndoorMap.get(name);
			if (site == null) {
				site = new LteBts();
				site.setIntId(btsKeyCode);
				site.setName(name);
				noIndoorMap.put(name, site);
			}
		}
		cell.setWyBtsIntId(lteBts.getIntId());
		cell.setDeleteFlag(0);// 再用小区
		cellMap.put(cell.getIntId() + "", cell);
	}

	/**
	 * 增加错误名称小区数据
	 * 
	 * @param cell
	 */
	public void addWrongCell(EutranCell cell) {
		WyWrongName wwN = new WyWrongName();
		wwN.setInt_id(cell.getIntId());
		wwN.setCellName(cell.getUserLabel());
		wwN.setBtsId(cell.getRelatedEnbIntId());
		wwN.setBtsName(cell.getRelateEnbUserLabel());
		wwN.setType(Constants.CELL);//小区
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
	 * 添加特殊站点小区
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
	 * 添加特殊站点基站
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
		btsSpecical.setType(Constants.BBU);//站点
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
