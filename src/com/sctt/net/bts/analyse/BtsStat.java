package com.sctt.net.bts.analyse;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.sctt.net.bts.bean.Bts;
import com.sctt.net.bts.bean.BtsSite;
import com.sctt.net.bts.bean.Cell;
import com.sctt.net.bts.bean.Country;
import com.sctt.net.bts.bean.TunelLib;
import com.sctt.net.bts.bean.TunelSite;
import com.sctt.net.bts.bean.WyBtsSpecial;
import com.sctt.net.bts.bean.WyWrongName;

public class BtsStat {
	private static Logger logger = Logger.getLogger("baseLog");
	// �洢����վ��Map,�ҷֺͷ��ҷ֣�keyΪint_id
	Map<String, BtsSite> btssiteMap = new HashMap<String, BtsSite>();
	// �洢���վ��Map��keyΪint_id
	Map<String, TunelSite> tunelMap = new HashMap<String, TunelSite>();
	// �洢����վ�㣬key��Ϊ����վ������
	Map<String, BtsSite> noIndoorMap = new HashMap<String, BtsSite>();
	// ��������Map
	Map<String, WyWrongName> wrongMap = new HashMap<String, WyWrongName>();
	// ����״̬��վС��
	Map<String, WyBtsSpecial> btsSpecialMap = new HashMap<String, WyBtsSpecial>();
	// ����С��Map
	Map<String, Cell> cellMap = new HashMap<String, Cell>();
	// ����⣬key:name,value:TunelLib
	Map<String, TunelLib> tunelLibMap = new HashMap<String, TunelLib>();

	/**
	 * ����С������
	 * 
	 * @param cell
	 *            С����Ϣ
	 * @return������վ���Ψһ��ʶ
	 */
	public void btsStat(Cell cell, Map<String, Country> countryMap) {
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
		} else {
			// δ�ҵ���Ӧ����������Ϊ����С������
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
			btsSite.setHighTrainFlag(cell.getHighTrainFlag());
			btsSite.setRedLineFlag(cell.getRedLineFlag());
			btssiteMap.put(btsKeyCode + "", btsSite);
		}

		// �洢���ҷ�����վ��Map

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
	 * ���С�����������վ��
	 * 
	 * @param cell
	 *            ��С��������
	 * @param countryMap
	 */
	public void tunelStat(Cell cell, Map<String, Country> countryMap) {
		String cellName = cell.getName();
		String[] splitName = cellName.split("_");
		String name = splitName[3];// ����վ������

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
		} else {
			// δ�ҵ���Ӧ����������Ϊ����С������
			addWrongCell(cell);
			return;
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

			tunelSite.setIs_rru(cell.getIsRru());// �Ƿ���Զ
			tunelSite.setBts_name(cell.getBtsName());
			tunelSite.setBsc_name(cell.getBscName());
			tunelSite.setBts_id(cell.getBtsId());
			tunelSite.setCircuitRoom_ownership(cell.getCircuitRoomOwnership());// ��վ��Ȩ
			tunelSite.setTrans_ownership(cell.getTransOwnership());// �����Ȩ
			tunelSite.setServiceLevel(cell.getServiceLevel());// ά���ȼ�
			tunelSite.setCityId(cell.getCityId());
			tunelSite.setCountryId(cell.getCountryId());
			tunelSite.setRelated_bts(cell.getRelateBts());
			tunelSite.setDeleteFlag(0);// ��ʼ���������õ�
			tunelMap.put(btsKeyCode + "", tunelSite);
		}
		cell.setIsIndoor("��");
		cell.setWyBtsIntId(tunelSite.getIntId());
		cell.setDeleteFlag(0);// ����С��
		cellMap.put(cell.getIntId() + "", cell);
	}

	/**
	 * �������վ����������
	 */
	public void statTunelLib(Map<String, TunelSite> tunelSiteMap) {
		for (Entry<String, TunelSite> tunel : tunelSiteMap.entrySet()) {
			try {
				TunelSite tunelSite = tunel.getValue();
				String name = tunelSite.getName();
				int index = name.indexOf("���ȥ");
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
				logger.error("��������쳣," + e.getMessage(), e);
				continue;
			}
		}
	}

	/**
	 * ������ϴ��������С��map,����������վ��С���ͻ�վ
	 */
	public void parseSpecicalBts() {
		List<String> removeList=new ArrayList();
		for (Entry<String, WyWrongName> entry : wrongMap.entrySet()) {
			String intId=entry.getKey();
			WyWrongName wrongName = entry.getValue();
			WyBtsSpecial btsSpecical=specicalBts(wrongName);
			if(btsSpecical!=null){
				//����״̬��վ
				btsSpecialMap.put(intId,btsSpecical);
				removeList.add(intId);
			}
		}
		//�Ӵ���������ȥ��
		for(String key:removeList){
			wrongMap.remove(key);
		}
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
		wwN.setCityId(InitInstance.getInstance().getCityId(
				cell.getSourceCityId() + ""));
		wrongMap.put(cell.getIntId() + "", wwN);
	}

	public void addWrongBbu(Bts bbu) {
		WyWrongName wwN = new WyWrongName();
		wwN.setInt_id(bbu.getIntId());
		wwN.setCellName(bbu.getName());
		wwN.setBtsId(bbu.getBtsId());
		wwN.setBscName(bbu.getBscName());
		wwN.setCityId(InitInstance.getInstance().getCityId(
				bbu.getSourceCityId() + ""));
		// wwN.setBtsName();
		wwN.setType(2);
		wrongMap.put(bbu.getIntId() + "", wwN);
	}

	/**
	 * �ж�
	 * @param wrongName
	 * @return
	 */
	public WyBtsSpecial specicalBts(WyWrongName wrongName) {
		WyBtsSpecial btsSpecical=null;
		try {
			String name = wrongName.getCellName();
			String type = name.substring(name.length() - 2);
			int state=0;
			if("_��".equals(type)){
				state=1;
			}else if("_��".equals(type)){
				state=2;
			}else if("_��".equals(type)){
				state=3;
			}
			if(state==0){
				return null;
			}
		    btsSpecical=new WyBtsSpecial();
			btsSpecical.setIntId(wrongName.getInt_id());
			btsSpecical.setBscName(wrongName.getBscName());
			btsSpecical.setBtsName(wrongName.getBtsName());
			btsSpecical.setBtsId(wrongName.getBtsId());
			btsSpecical.setCityId(wrongName.getCityId());
			btsSpecical.setName(wrongName.getCellName());
			btsSpecical.setDeleteFlag(0);
			btsSpecical.setInTime(new Date());
			btsSpecical.setNetType(1);//CDMA
			btsSpecical.setState(state);
			btsSpecical.setType(wrongName.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return btsSpecical;
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
