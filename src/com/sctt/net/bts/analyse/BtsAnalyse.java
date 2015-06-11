package com.sctt.net.bts.analyse;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.sctt.net.bts.bean.Bts;
import com.sctt.net.bts.bean.BtsSite;
import com.sctt.net.bts.bean.Cell;
import com.sctt.net.bts.bean.Country;
import com.sctt.net.bts.bean.WyBbu;
import com.sctt.net.bts.bean.WyWrongName;
import com.sctt.net.bts.dao.BtsDao;
import com.sctt.net.common.util.StringUtils;

/**
 * ��վ��������ÿ�����һ��
 * 
 * @author Administrator
 * 
 */

public class BtsAnalyse extends TimerTask {

	private static Logger logger = Logger.getLogger("baseLog");
	private BtsDao btsDao;

	public BtsAnalyse(BtsDao btsDao) {
		this.btsDao = btsDao;
	}

	public void run() {
		logger.info("++++++������ʼ��");
		BtsStat stat = new BtsStat();
		List<Cell> cells = null;
		Map<String, BtsSite> yesMap = null;// wy_bts�������
		Map<String, WyWrongName> yesWwnMap = null;// wy_wrongname����
		Map<String, Cell> yesCellMap = null;// wy_cell�������
		Map<String,Country> countryMap = null;
		// ������Map
		Map<String, BtsSite> btssiteMap = null;	
		// ����BBU
		List<Bts> bbus = null;
		BbuStat bbuStat = new BbuStat();
		Map<String, WyBbu> yesBbuMap = null;
		Map<String, BtsSite> noIndoorMap = null;// ���ҷ�Map,bbu���ҹ�վ����վ����
		
		
		try {
			logger.info("++++��������վ�㿪ʼ:");
			cells = btsDao.getCells();
			yesMap = btsDao.getBtsSites();
			yesWwnMap = btsDao.getWwns();
			countryMap = btsDao.getCountrys();
			yesCellMap = btsDao.getWyCell();
			logger.info("������c_cell�ɼ���С�����ݣ�" + cells.size());
			logger.info("wy_bts�ɼ�������վ������:" + yesMap.size());
			logger.info("wy_wrongname�ɼ���ԭ�д���С�����ݣ�" + yesWwnMap.size());
			logger.info("wy_cell�ɼ���С������" + yesCellMap.size());
			for (Cell cell : cells) {
				if (ignoreCell(cell.getName())) {
					continue;// ����С��
				}
				Cell ruleCell = ruleCell(cell);
				if (ruleCell != null) {
					// С�����Ʒ��Ϲ��򣬽��������վͳ��
					stat.btsStat(ruleCell, countryMap);
				} else {
					// С�����Ʋ����Ϲ���,���c_cell_wrongname��
					stat.addWrongCell(cell);
				}
			}
			// ����

			btssiteMap = stat.getBtssiteMap();
			Map<String, Cell> cellMap = stat.getCellMap();

			logger.info("����������վ�������" + btssiteMap.size());
			logger.info("������С��������" + cellMap.size());

			// ʵ������վ����������
			btsUpdate(btssiteMap, yesMap);
			// ʵ��С����wy_cell��������
			cellUpdate(cellMap, yesCellMap);
			
			logger.info("++++��������վ�����.");		
			logger.info("++++����BBU��ʼ...");
			noIndoorMap=stat.getNoIndoorMap();
			bbus = btsDao.getBbus();
			yesBbuMap = btsDao.getWyBbu();
			logger.info("��c_bts��ɼ���BBU���ݣ�" + bbus.size());
			logger.info("��wy_bbu��ɼ���BBU����:" + yesBbuMap.size());
			for (Bts bbu : bbus) {
				Bts ruleBbu = ruleBbu(bbu, noIndoorMap,countryMap);
				if (ruleBbu != null) {
					bbuStat.bbuStat(ruleBbu);
				} else {
					// ��������BBU
					stat.addWrongBbu(bbu);
				}
			}
			bbuStat.finishBbuStat();
			Map<String, WyBbu> bbuMap = bbuStat.getBbuMap();
			Map<String, WyWrongName> wrongMap = stat.getWrongMap();
			logger.info("������BBU����:" + bbuMap.size());
			logger.info("��������������С����BBU������" + wrongMap.size());
			// ��������
			bbuUpdate(bbuMap, yesBbuMap);
			wwnUpdate(wrongMap, yesWwnMap);
			logger.info("++++����BBU����...");

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("++++++�����쳣...."+e.getMessage());
		}
		logger.info("++++++��������");
	}

	/**
	 * ʵ������վ�����������
	 * 
	 * @param btssiteMap
	 *            :������������վ��
	 * @param yesMap
	 *            �����п�������վ��
	 */
	public void btsUpdate(Map<String, BtsSite> btssiteMap,
			Map<String, BtsSite> yesMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		try {
			logger.info("++++����վ������������...");
			// ��վ�������²���
			for (String keyObj : btssiteMap.keySet()) {
				// ����������������վ�Ѵ������п���,���¿��ֶ�
				BtsSite yesBts = yesMap.get(keyObj);
				BtsSite updateBts = btssiteMap.get(keyObj);
				//����....
				if (yesBts != null) {
					// ����
					btsDao.updateBtsSite(updateBts);
					i++;
				} else {
					// ����
					btsDao.insertBtsSite(updateBts);
					j++;
				}
			}

			// �ҳ�������վ

			for (String keyObj : yesMap.keySet()) {
				BtsSite toBts = btssiteMap.get(keyObj);
				BtsSite delBts = yesMap.get(keyObj);
				if (toBts == null) {
					// �Ҳ����������ѷ���
					// ���ԭ�����Ѿ����������ø���
					int deleteFlag = delBts.getDeleteFlag();
					if (deleteFlag == 0) {
						btsDao.updateBtsSiteByDeleteFlag(delBts);
						q++;
					}
				}
			}
			logger.info("����վ��������������£�" + i + ";������" + j + ";������" + q);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("+++++++ʵ������վ���������³���");
		}
	}

	/**
	 * ����С�����Ʊ�ʵ����������
	 * 
	 * @param wrongMap
	 *            :�ȷ������Ĵ���С��
	 * @param yesWwnMap
	 *            �����ݿ��д洢�Ĵ���С��
	 */
	public void wwnUpdate(Map<String, WyWrongName> wrongMap,
			Map<String, WyWrongName> yesWwnMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		try {
			logger.info("++++����С������������...");
			for (String keyObj : wrongMap.keySet()) {
				WyWrongName wwN = yesWwnMap.get(keyObj);
				WyWrongName updateWwn = wrongMap.get(keyObj);
				if (wwN != null) {
					// ����
					btsDao.updateWyWrongName(updateWwn);
					i++;
				} else {
					// ����
					btsDao.insertWyWrongName(updateWwn);
					j++;
				}
			}
			// ����С�������Ѿ�����
			for (String keyObj : yesWwnMap.keySet()) {
				WyWrongName wwN = wrongMap.get(keyObj);
				WyWrongName updateWwn = yesWwnMap.get(keyObj);
				if (wwN == null) {
					// �Ѹ�����ȷ
					int deleteFlag = updateWwn.getDeleteFlag();
					if (deleteFlag == 0) {
						btsDao.updateWyWrongNameByDelFlag(updateWwn);
						q++;
					}
				}
			}
			logger.info("����С��������������£�" + i + ";������" + j + ";������" + q);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * wy_cell��������
	 * 
	 * @param cellMap
	 *            ���ַ�����С��
	 * @param yesCellMap
	 *            �����ݿ���е�С��
	 */
	public void cellUpdate(Map<String, Cell> cellMap,
			Map<String, Cell> yesCellMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		try {
			logger.info("++++С������������...");
			for (String keyObj : cellMap.keySet()) {
				Cell cell = yesCellMap.get(keyObj);
				Cell updateCell = cellMap.get(keyObj);
				if (cell != null) {
					// ����
					btsDao.updateCell(updateCell);
					i++;
				} else {
					// ����
					btsDao.insertCell(updateCell);
					j++;
				}
			}

			for (String keyObj : yesCellMap.keySet()) {
				Cell cell = cellMap.get(keyObj);
				Cell updateCell = yesCellMap.get(keyObj);
				if (cell == null) {
					// ����
					int deleteFlag = updateCell.getDeleteFlag();
					if (deleteFlag == 0) {
						btsDao.updateCellByDelFlag(updateCell);
						q++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("wy_cellС��������������£�" + i + ";������" + j + ";������" + q);
	}

	/**
	 * wy_bbu��������
	 * 
	 * @param bbuMap
	 * @param yesBbuMap
	 */
	public void bbuUpdate(Map<String, WyBbu> bbuMap,
			Map<String, WyBbu> yesBbuMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		logger.info("++++BBU����������...");
		try {
			for (String keyObj : bbuMap.keySet()) {
				WyBbu bbu = yesBbuMap.get(keyObj);
				WyBbu bbuUpdate = bbuMap.get(keyObj);
				if (bbu != null) {
					// �����ֶ�
					btsDao.updateWyBbu(bbuUpdate);
					i++;
				} else {
					// ����
					btsDao.insertWyBbu(bbuUpdate);
					j++;
				}
			}
			// ����
			for (String keyObj : yesBbuMap.keySet()) {
				WyBbu bbu = bbuMap.get(keyObj);
				WyBbu bbuUpdate = yesBbuMap.get(keyObj);
				if (bbu == null) {
					// ����
					int deleteFlag = bbuUpdate.getDeleteFlag();
					if (deleteFlag == 0) {
						btsDao.updateWyBbuByDelFlag(bbuUpdate);
						q++;
					}
				}
			}
			logger.info("wy_bbu������������£�" + i + ";������" + j + ";������" + q);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * С�����������ж�
	 * 
	 * @param cell
	 *            ��С������
	 * @return
	 */
	public Cell ruleCell(Cell cell) {
		try {
			String cellName = cell.getName();
			String btsName = cell.getBtsName();
			String[] splitName = cellName.split("_");
			int cellLength = splitName.length;
			// �غ��ֶΣ�ϵͳ��_BTS���_С�����_��/��/��+С����_��վ��Ȩ��ʶ_�����Ȩ��ʶ_ά���ȼ�
			// �ɺ��ֶΣ��ҷ�_��Զ_ֱ��վ��ʶ_С��������Ϣ��ʶ��
			if (cellLength < 7) {
				return null;
			}
			int cqFlag = getCqFlag(splitName);// ��Ȩ��ʼ���

			// cqFlag:��վ��Ȩ��ʶ
			if (!isRight(splitName[cqFlag])) {
				return null;
			} else {
				cell.setCircuitRoomOwnership(splitName[cqFlag]);
			}

			// cqFlag+1:�����Ȩ��ʶ
			if (!isRight(splitName[cqFlag + 1])) {
				return null;
			} else {
				cell.setTransOwnership(splitName[cqFlag + 1]);
			}

			// cqFlag+2:ά���ȼ�
			if (!isRank(splitName[cqFlag + 2])) {
				return null;
			} else {
				cell.setServiceLevel(splitName[cqFlag + 2]);
			}

			int zfStart = cqFlag + 3;// ֱ��վ��ʼ��ʶ
			int zfEnd = 0;// ֱ��վ������ʶ
			// �Ƿ�������֣��ǣ��Ƿ����һ���ֶ�
			if (cellName.contains("����")) {
				if (!splitName[cellLength - 1].contains("����")) {
					return null;
				}
				String gfValue = splitName[cellLength - 1];
				if (gfValue.length() > 2) {
					cell.setIsGf(gfValue.substring(0, gfValue.length() - 2));
				} else {
					cell.setIsGf("2");
				}
				zfEnd = cellLength - 1;
			} else {
				cell.setIsGf("0");
				zfEnd = cellLength;
			}

			// ֱ��վ��֤
			if (zfEnd > zfStart) {
				for (int i = 0; i < zfEnd - zfStart; i++) {
					String zfValue = splitName[zfStart + i];
					if (!isZF(zfValue)) {
						return null;
					} else {
						// ����ֱ��վ������������һ����ֱ��վ
						String zfType = zfValue.substring(0, 2);
						String s = zfValue.substring(2);
						int count = StringUtils.objToInt(s);
						if ("OR".equals(zfType)) {
							// ����ֱ��վ
							cell.setIsOr(count);
						} else if ("RR".equals(zfType)) {
							// ����ֱ��վ
							cell.setIsRR(count);
						} else if ("SR".equals(zfType)) {
							// ��Ƶֱ��վ
							cell.setIsSR(count);
						}
					}
				}
			}

			// ����Զ��վ���������վ��������ѳ���������վ�����Ʋ�һ��
			if (!cellName.contains("��Զ")) {
				if(btsName!=null&&!"".equals(btsName)){
					String[] btsName_Temp=btsName.split("_");
					btsName=btsName_Temp[0];
				}
				if(!btsName.equals(splitName[3])){
					return null;//��������
				}
			} 

		} catch (Exception e) {
			logger.info(cell.getName() + ":" + e.getMessage());
			return null;// �����쳣��������С��
		}

		return cell;
	}

	/**
	 * �Ƿ���ϲ�Ȩ����
	 * 
	 * @param str
	 * @return:true:���Ϲ���
	 */
	public boolean isRight(String str) {
		String[] rang = { "��", "��", "��", "����", "����" };
		boolean flag = false;
		for (String s : rang) {
			if (s.equals(str.trim())) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * �Ƿ�ά���ȼ�
	 * 
	 * @param str
	 * @return
	 */
	public boolean isRank(String str) {
		String[] rang = { "A", "B", "C", "D" };
		boolean flag = false;
		for (String s : rang) {
			if (s.equals(str.trim())) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * �Ƿ�����ֱ��վ�ı��
	 * 
	 * @param str
	 * @return
	 */
	public boolean isZF(String str) {
		String[] rang = { "OR", "RR", "SR" };
		boolean flag = false;
		for (String s : rang) {
			if (str.contains(s)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * �����վ��Ȩ��ʶ�ı��λ
	 * 
	 * @param splitName
	 * @return
	 */
	public int getCqFlag(String[] splitName) {
		int cqFlag = 0;
		if ("�ҷ�".equals(splitName[4])||"���".equals(splitName[4])) {
			if ("��Զ".equals(splitName[5])) {
				cqFlag = 6;
			} else {
				cqFlag = 5;
			}
		} else if ("��Զ".equals(splitName[4])) {
			cqFlag = 5;
		} else{
			cqFlag = 4;
		}
		return cqFlag;
	}

	/**
	 * ��֤����BBU
	 * 
	 * @param bts
	 * @return
	 */
	public Bts ruleBbu(Bts bts, Map<String, BtsSite> noIndoorMap,Map<String,Country> countryMap) {
		try {
			String btsName = bts.getName();
			String[] array = btsName.split("_");
			int length = array.length;
			if (!(length > 2 && length < 5)) {
				return null;
			}
			String name = array[0];
			
			//���Ƶ�ǰ���ַ��Ҳ����������е�����Ϊ��������
			Country country=BtsAnalyse.getCountry(countryMap, name);
			if(country!=null){			
				bts.setCityId(country.getCityId());
				bts.setCountryId(country.getId());
			}else{
				//�����Ϲ�������
				return null;
			}
			
			
			String bzKey =  name +"_"+bts.getBscName()+ "_" + bts.getBtsId();//��վ��ʶ
			if ("BBU1".equals(array[1])) {
				if (btsName.contains("��վ")) {
					// BBU1������վ������վ�������ҵ���ͬ����վ��
					BtsSite btsSite = noIndoorMap.get(name);
					if (btsSite == null) {
						return null;
					} else {
						bts.setRelatedWyBts(btsSite.getIntId());
					}

				} else {
					// BBU1������վ������������Ȩ�ʹ����Ȩ
					if (!isRight(array[2]))
						return null;
					if (!isRight(array[3]))
						return null;
				}
			} else {
				// ���BBU2�Ժ�һ�����й�վ
				if (!btsName.contains("��վ")) {
					return null;
				}
			}
			
			
		} catch (Exception e) {
			logger.error("" + e.getMessage());
			return null;// �����쳣
		}
		return bts;
	}

	/**
	 * ͨ��С�����ƻ�ȡС����������
	 * 
	 * @param countryList
	 *            �������б�
	 * @param cell
	 *            ��С������
	 * @return
	 */
	public static Country getCountry(Map<String,Country> countryMap, String name) {
		String countryName = "";//ǰ������
		String countryName2="";//ǰ������
		if (name != null && name.length() > 2) {
			countryName = name.substring(0, 2);
			countryName2=name.substring(0, 3);
		}
		Country returnCountry = countryMap.get(countryName);
		if(returnCountry==null){
			returnCountry=countryMap.get(countryName2);
		}
		return returnCountry;
	}

	/**
	 * �ж�С�������Ƿ��к���С��
	 * 
	 * @param cellName
	 * @return
	 */
	public boolean ignoreCell(String cellName) {
		String[] igArray = { "Ӧ��ͨ��", "Ӧ��ͨѶ", "ͨ��Ӧ��", "ͨѶӦ��" };
		boolean flag = false;
		for (String ig : igArray) {
			if (cellName.contains(ig)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public void test() {
		Cell cell = new Cell();
		cell.setName("0_665_0_ӡ��ǭϪ_��_��_C_3����");
		cell.setBtsName("ӡ��ǭϪ");
		Cell ruleCell = ruleCell(cell);
		System.out.println("OR:" + ruleCell);
	}

}
