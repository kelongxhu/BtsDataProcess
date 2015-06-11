package com.sctt.net.bts.analyse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sctt.net.bts.bean.Bts;
import com.sctt.net.bts.bean.BtsSite;
import com.sctt.net.bts.bean.Cell;
import com.sctt.net.bts.bean.Country;
import com.sctt.net.bts.bean.WyWrongName;

public class BtsStat {
	// 存储物理站点Map,室分和非室分，key为int_id
	Map<String, BtsSite> btssiteMap = new HashMap<String, BtsSite>();
	//存储物理站点，key，为物理站点名称
	Map<String,BtsSite> noIndoorMap=new HashMap<String,BtsSite>();
	// 错误名称Map
	Map<String, WyWrongName> wrongMap = new HashMap<String, WyWrongName>();
	// 分析小区Map
	Map<String, Cell> cellMap = new HashMap<String, Cell>();

	/**
	 * 分析小区名称
	 * 
	 * @param cell
	 *            小区信息
	 * @return：物理站点的唯一标识
	 */
	public void btsStat(Cell cell, Map<String,Country> countryMap) {
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

		// 通过小区名称的前三个字符判断小区所属区县
		Country country = BtsAnalyse.getCountry(countryMap, name);
		if (country != null) {
			cell.setCityId(country.getCityId());
			cell.setCountryId(country.getId());
		}else{
			//未找到对应的所属区县为错误小区命名
			addWrongCell(cell);
			return;
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
			btssiteMap.put(btsKeyCode + "", btsSite);
		}
		
		//存储非室分物理站点Map
		
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
		wwN.setType(1);
		wwN.setCityId(InitInstance.getInstance().getCityId(cell.getSourceCityId()+""));
		wrongMap.put(cell.getIntId() + "", wwN);
	}

	public void addWrongBbu(Bts bbu) {
		WyWrongName wwN = new WyWrongName();
		wwN.setInt_id(bbu.getIntId());
		wwN.setCellName(bbu.getName());
		wwN.setBtsId(bbu.getBtsId());
		wwN.setBscName(bbu.getBscName());
		wwN.setCityId(InitInstance.getInstance().getCityId(bbu.getSourceCityId()+""));
		// wwN.setBtsName();
		wwN.setType(2);
		wrongMap.put(bbu.getIntId() + "", wwN);
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
	
	

}
