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
	// �洢����վ��Map,�ҷֺͷ��ҷ֣�keyΪint_id
	Map<String, BtsSite> btssiteMap = new HashMap<String, BtsSite>();
	//�洢����վ�㣬key��Ϊ����վ������
	Map<String,BtsSite> noIndoorMap=new HashMap<String,BtsSite>();
	// ��������Map
	Map<String, WyWrongName> wrongMap = new HashMap<String, WyWrongName>();
	// ����С��Map
	Map<String, Cell> cellMap = new HashMap<String, Cell>();

	/**
	 * ����С������
	 * 
	 * @param cell
	 *            С����Ϣ
	 * @return������վ���Ψһ��ʶ
	 */
	public void btsStat(Cell cell, Map<String,Country> countryMap) {
		String cellName = cell.getName();
		String[] splitName = cellName.split("_");
		String name = splitName[3];// ����վ������
		if (cellName.contains("�ҷ�")) {
			cell.setIsIndoor("��");
		} else {
			cell.setIsIndoor("��");
		}

		if (cellName.contains("��Զ")) {
			cell.setIsRru("��");
		} else {
			cell.setIsRru("��");
		}

		// ͨ��С�����Ƶ�ǰ�����ַ��ж�С����������
		Country country = BtsAnalyse.getCountry(countryMap, name);
		if (country != null) {
			cell.setCityId(country.getCityId());
			cell.setCountryId(country.getId());
		}else{
			//δ�ҵ���Ӧ����������Ϊ����С������
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

			btsSite.setIsIndoor(cell.getIsIndoor());// �Ƿ��ҷ�
			btsSite.setIs_rru(cell.getIsRru());// �Ƿ���Զ
			btsSite.setBts_name(cell.getBtsName());
			btsSite.setBsc_name(cell.getBscName());
			btsSite.setBts_id(cell.getBtsId());
			btsSite.setCircuitRoom_ownership(cell.getCircuitRoomOwnership());// ��վ��Ȩ
			btsSite.setTrans_ownership(cell.getTransOwnership());// �����Ȩ
			btsSite.setServiceLevel(cell.getServiceLevel());// ά���ȼ�
			btsSite.setCityId(cell.getCityId());
			btsSite.setCountryId(cell.getCountryId());
			btsSite.setRelated_bts(cell.getRelateBts());
			btsSite.setDeleteFlag(0);// ��ʼ���������õ�
			btssiteMap.put(btsKeyCode + "", btsSite);
		}
		
		//�洢���ҷ�����վ��Map
		
		String isIndoor = cell.getIsIndoor();
		if ("��".equals(isIndoor)) {
			BtsSite site = noIndoorMap.get(name);
			if (site == null) {
				site = new BtsSite();
				site.setIntId(btsKeyCode);
				site.setName(name);
				noIndoorMap.put(name, site);
			}
		}
		cell.setWyBtsIntId(btsSite.getIntId());
		cell.setDeleteFlag(0);// ����С��
		cellMap.put(cell.getIntId() + "", cell);
	}

	/**
	 * ���Ӵ�������С������
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
