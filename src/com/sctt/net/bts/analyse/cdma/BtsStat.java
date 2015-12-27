package com.sctt.net.bts.analyse.cdma;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.sctt.net.bts.bean.cdma.Bts;
import com.sctt.net.bts.bean.cdma.BtsSite;
import com.sctt.net.bts.bean.cdma.Cell;
import com.sctt.net.bts.bean.cdma.Country;
import com.sctt.net.bts.bean.cdma.TunelLib;
import com.sctt.net.bts.bean.cdma.TunelSite;
import com.sctt.net.bts.bean.cdma.WyBtsSpecial;
import com.sctt.net.bts.bean.cdma.WyWrongName;
import com.sctt.net.common.util.AnalyseUtil;
import com.sctt.net.common.util.Constants;

public class BtsStat {
	private static Logger logger = Logger.getLogger("baseLog");
	// 存储物理站点Map,室分和非室分，key为int_id
	Map<String, BtsSite> btssiteMap = new HashMap<String, BtsSite>();
	// 存储隧道站点Map，key为int_id
	Map<String, TunelSite> tunelMap = new HashMap<String, TunelSite>();
	// 存储物理站点，key，为物理站点名称
	Map<String, BtsSite> noIndoorMap = new HashMap<String, BtsSite>();
	// 错误名称Map
	Map<String, WyWrongName> wrongMap = new HashMap<String, WyWrongName>();
	// 特殊状态基站小区
	Map<String, WyBtsSpecial> btsSpecialMap = new HashMap<String, WyBtsSpecial>();
	// 分析小区Map
	Map<String, Cell> cellMap = new HashMap<String, Cell>();
	// 隧道库，key:name,value:TunelLib
	Map<String, TunelLib> tunelLibMap = new HashMap<String, TunelLib>();

	/**
	 * 分析小区名称
	 * 
	 * @param cell
	 *            小区信息
	 * @return：物理站点的唯一标识
	 */
	public void btsStat(Cell cell) {
		String cellName = cell.getName();
		String[] splitName = cellName.split("_");
		String name = splitName[3];// 物理站点名称
		if (cellName.contains("室分")) {
			cell.setIsIndoor("是");
		} else {
			cell.setIsIndoor("否");
		}

		if (cellName.contains("拉远")) {
			cell.setIsRru("是");
		} else {
			cell.setIsRru("否");
		}
		String btsKey = name + "_" + cell.getBscName() + "_" + cell.getBtsId();
		long btsKeyCode = btsKey.hashCode();
		BtsSite btsSite = btssiteMap.get(btsKeyCode + "");
		if (btsSite == null) {
			btsSite = new BtsSite();
			btsSite.setIntId(btsKeyCode);
			btsSite.setName(name);
			btsSite.setLongitude(cell.getLongitude());
			btsSite.setLatitude(cell.getLatitude());
			btsSite.setVendor_BtsType(cell.getVendor_btstype());

			btsSite.setIsIndoor(cell.getIsIndoor());// 是否室分
			btsSite.setIs_rru(cell.getIsRru());// 是否拉远
			btsSite.setBts_name(cell.getBtsName());
			btsSite.setBsc_name(cell.getBscName());
			btsSite.setBts_id(cell.getBtsId());
			btsSite.setCircuitRoom_ownership(cell.getCircuitRoomOwnership());// 基站产权
			btsSite.setTrans_ownership(cell.getTransOwnership());// 传输产权
			btsSite.setServiceLevel(cell.getServiceLevel());// 维护等级
			btsSite.setCityId(cell.getCityId());
			btsSite.setCountryId(cell.getCountryId());
			btsSite.setRelated_bts(cell.getRelateBts());
			btsSite.setDeleteFlag(0);// 初始化都是在用的
			btsSite.setHighTrainFlag(cell.getHighTrainFlag());
			btsSite.setRedLineFlag(cell.getRedLineFlag());
			btssiteMap.put(btsKeyCode + "", btsSite);
		}

		// 存储非室分物理站点Map

		String isIndoor = cell.getIsIndoor();
		if ("否".equals(isIndoor)) {
			BtsSite site = noIndoorMap.get(name);
			if (site == null) {
				site = new BtsSite();
				site.setIntId(btsKeyCode);
				site.setName(name);
				noIndoorMap.put(name, site);
			}
		}
		cell.setWyBtsIntId(btsSite.getIntId());
		cell.setDeleteFlag(0);// 再用小区
		cellMap.put(cell.getIntId() + "", cell);
	}

	/**
	 * 隧道小区解析出隧道站点
	 * 
	 * @param cell
	 *            ：小区、区县
	 * @param countryMap
	 */
	public void tunelStat(Cell cell) {
		String cellName = cell.getName();
		String[] splitName = cellName.split("_");
		String name = splitName[3];// 物理站点名称

		if (cellName.contains("拉远")) {
			cell.setIsRru("是");
		} else {
			cell.setIsRru("否");
		}
		String btsKey = name + "_" + cell.getBscName() + "_" + cell.getBtsId();
		long btsKeyCode = btsKey.hashCode();

		TunelSite tunelSite = tunelMap.get(btsKeyCode + "");
		if (tunelSite == null) {
			tunelSite = new TunelSite();
			tunelSite.setIntId(btsKeyCode);
			tunelSite.setName(name);
			tunelSite.setLongitude(cell.getLongitude());
			tunelSite.setLatitude(cell.getLatitude());
			tunelSite.setVendor_BtsType(cell.getVendor_btstype());

			tunelSite.setIs_rru(cell.getIsRru());// 是否拉远
			tunelSite.setBts_name(cell.getBtsName());
			tunelSite.setBsc_name(cell.getBscName());
			tunelSite.setBts_id(cell.getBtsId());
			tunelSite.setCircuitRoom_ownership(cell.getCircuitRoomOwnership());// 基站产权
			tunelSite.setTrans_ownership(cell.getTransOwnership());// 传输产权
			tunelSite.setServiceLevel(cell.getServiceLevel());// 维护等级
			tunelSite.setCityId(cell.getCityId());
			tunelSite.setCountryId(cell.getCountryId());
			tunelSite.setRelated_bts(cell.getRelateBts());
			tunelSite.setDeleteFlag(0);// 初始化都是在用的
			tunelMap.put(btsKeyCode + "", tunelSite);
		}
		cell.setIsIndoor("隧");
		cell.setWyBtsIntId(tunelSite.getIntId());
		cell.setDeleteFlag(0);// 再用小区
		cellMap.put(cell.getIntId() + "", cell);
	}

	/**
	 * 根据隧道站点分析隧道库
	 */
	public void statTunelLib(Map<String, TunelSite> tunelSiteMap) {
		for (Entry<String, TunelSite> tunel : tunelSiteMap.entrySet()) {
			try {
				TunelSite tunelSite = tunel.getValue();
				String name = tunelSite.getName();
				int index = name.indexOf("隧道去");
				if (index == -1) {
					continue;
				}
				String libName = name.substring(0, index);
				String direction = name.substring(index + 3);
				TunelLib lib = tunelLibMap.get(libName);
				if (lib == null) {
					TunelLib tunelLib = new TunelLib();
					tunelLib.setName(libName);
					tunelLib.setCityId(tunelSite.getCityId());
					tunelLib.setCountryId(tunelSite.getCountryId());
					tunelLib.setLatitude(tunelSite.getLatitude());
					tunelLib.setLongitude(tunelSite.getLongitude());
					tunelLib.setDirection(direction);
					tunelLibMap.put(libName, tunelLib);
				} else {
					lib.setDirection(lib.getDirection() + "," + direction);
				}
			} catch (Exception e) {
				logger.error("分析隧道异常," + e.getMessage(), e);
				continue;
			}
		}
	}

	/**
	 * 增加错误名称小区数据
	 * 
	 * @param cell
	 */
	public void addWrongCell(Cell cell) {
		WyWrongName wwN = new WyWrongName();
		wwN.setInt_id(cell.getIntId());
		wwN.setCellName(cell.getName());
		wwN.setBtsId(cell.getBtsId());
		wwN.setBscName(cell.getBscName());
		wwN.setBtsName(cell.getBtsName());
		wwN.setNetType(Constants.CDMA);// CDMA
		wwN.setType(Constants.CELL);
		wwN.setCityId(InitInstance.getInstance().getCityId(
				cell.getSourceCityId() + ""));
		wwN.setWrongMsg(cell.getJudgeMsg());
		wrongMap.put(cell.getIntId() + "", wwN);
	}

	public void addWrongBbu(Bts bbu) {
		WyWrongName wwN = new WyWrongName();
		wwN.setInt_id(bbu.getIntId());
		wwN.setCellName(bbu.getName());
		wwN.setBtsId(bbu.getBtsId());
		wwN.setBscName(bbu.getBscName());
		wwN.setNetType(Constants.CDMA);// CDMA
		wwN.setCityId(InitInstance.getInstance().getCityId(
				bbu.getSourceCityId() + ""));
		wwN.setType(Constants.BBU);
		wwN.setWrongMsg(bbu.getJudgeMsg());
		wrongMap.put(bbu.getIntId() + "", wwN);
	}

	/**
	 * 添加特殊站点小区
	 * @param cell
	 */
	public void addSpecialCell(Cell cell) {
		String name = cell.getName();
		String type = name.substring(name.length() - 2);
		WyBtsSpecial btsSpecical = new WyBtsSpecial();
		btsSpecical.setIntId(cell.getIntId());
		btsSpecical.setBscName(cell.getBscName());
		btsSpecical.setBtsName(cell.getBtsName());
		btsSpecical.setBtsId(cell.getBtsId());
		btsSpecical.setCityId(cell.getCityId());
		btsSpecical.setName(cell.getName());
		btsSpecical.setDeleteFlag(0);
		btsSpecical.setInTime(new Date());
		btsSpecical.setNetType(Constants.CDMA);// CDMA
		btsSpecical.setState(getSpecicalState(type));
		btsSpecical.setType(1);
		btsSpecialMap.put(cell.getIntId()+"", btsSpecical);
	}
	
	
	/**
	 * 添加特殊站点基站
	 * @param cell
	 */
	public void addSpecialBts(Bts bts) {
		String name = bts.getName();
		String type = name.substring(name.length() - 2);
		WyBtsSpecial btsSpecical = new WyBtsSpecial();
		btsSpecical.setIntId(bts.getIntId());
		btsSpecical.setBscName(bts.getBscName());
		btsSpecical.setBtsName(bts.getName());
		btsSpecical.setBtsId(bts.getBtsId());
		btsSpecical.setCityId(bts.getCityId());
		btsSpecical.setName(bts.getName());
		btsSpecical.setDeleteFlag(0);
		btsSpecical.setInTime(new Date());
		btsSpecical.setNetType(Constants.CDMA);// CDMA
		btsSpecical.setState(getSpecicalState(type));
		btsSpecical.setType(2);//站点
		btsSpecialMap.put(bts.getIntId()+"", btsSpecical);
	}


	private int getSpecicalState(String s) {
		int state = 0;
		if ("_新".equals(s)) {
			state = 1;
		} else if ("_调".equals(s)) {
			state = 2;
		} else if ("_升".equals(s)) {
			state = 3;
		}
		return state;
	}


	public Map<String, BtsSite> getBtssiteMap() {
		return btssiteMap;
	}

	public Map<String, WyWrongName> getWrongMap() {
		return wrongMap;
	}

	public Map<String, Cell> getCellMap() {
		return cellMap;
	}

	public Map<String, BtsSite> getNoIndoorMap() {
		return noIndoorMap;
	}

	public Map<String, TunelSite> getTunelMap() {
		return tunelMap;
	}

	public Map<String, TunelLib> getTunelLibMap() {
		return tunelLibMap;
	}

	public Map<String, WyBtsSpecial> getBtsSpecialMap() {
		return btsSpecialMap;
	}

}
