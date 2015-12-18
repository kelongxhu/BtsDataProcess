package com.sctt.net.bts.analyse.cdma;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.sctt.net.bts.bean.cdma.Bts;
import com.sctt.net.bts.bean.cdma.BtsSite;
import com.sctt.net.bts.bean.cdma.Cell;
import com.sctt.net.bts.bean.cdma.Country;
import com.sctt.net.bts.bean.cdma.TunelLib;
import com.sctt.net.bts.bean.cdma.TunelSite;
import com.sctt.net.bts.bean.cdma.WyBbu;
import com.sctt.net.bts.bean.cdma.WyBtsSpecial;
import com.sctt.net.bts.bean.cdma.WyWrongName;
import com.sctt.net.bts.dao.BtsDao;
import com.sctt.net.common.util.AnalyseUtil;
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
		Map<String, Country> countryMap = null;
		Map<String, TunelSite> yesTunelMap = null;// wy_tunel������
		Map<String, TunelLib> yesTunelLibMap = null;// wy_lib_tunel������
		Map<String, WyBtsSpecial> yesSpecialMap=null;//WY_BTS_SPECIAL����
		// �����󸲸�վ��Map
		Map<String, BtsSite> btssiteMap = null;
		// ����������Map
		Map<String, TunelSite> tunelSiteMap = null;
		// �������С��
		Map<String, Cell> cellMap = null;
		// ����BBU
		List<Bts> bbus = null;
		BbuStat bbuStat = new BbuStat();
		Map<String, WyBbu> yesBbuMap = null;
		Map<String, BtsSite> noIndoorMap = null;// ���ҷ�Map,bbu���ҹ�վ����վ����
		Map<String, TunelLib> tunelLibMap = null;
		Map<String, WyBtsSpecial> btsSpecialMap=null;

		try {
			logger.info("++++��������վ�㿪ʼ:");
			cells = btsDao.getCells();
			yesMap = btsDao.getBtsSites();
			yesWwnMap = btsDao.getWwns();
			countryMap = btsDao.getCountrys();
			yesCellMap = btsDao.getWyCell();
			yesTunelMap = btsDao.getTunels();
			yesTunelLibMap = btsDao.getTunelLib();
			yesSpecialMap=btsDao.getBtsSpecial();
			logger.info("������c_cell�ɼ���С�����ݣ�" + cells.size());
			logger.info("wy_bts�ɼ�������վ������:" + yesMap.size());
			logger.info("wy_wrongname�ɼ���ԭ�д���С�����ݣ�" + yesWwnMap.size());
			logger.info("wy_cell�ɼ���С������" + yesCellMap.size());
			logger.info("wy_tunel�ɼ������վ������:" + yesTunelMap.size());
			logger.info("wy_tunel_lib�ɼ������������������:" + yesTunelLibMap.size());
			logger.info("wy_bts_special�ɼ�������������״̬վ������:"+yesSpecialMap.size());
			for (Cell cell : cells) {
				if (AnalyseUtil.ignoreCell(cell.getName())) {
					continue;// ����С��
				}
				Cell ruleCell = ruleCell(cell);
				if (ruleCell != null) {
					// С�����Ʒ��Ϲ��򣬽��������վͳ��
					boolean tunelFlag = ruleCell.isTunel();
					if (tunelFlag) {
						// ͳ�����
						stat.tunelStat(ruleCell, countryMap);
					} else {
						// ͳ�����ڡ�����
						stat.btsStat(ruleCell, countryMap);
					}
					boolean specailFlag=ruleCell.isSpecial();
					if(specailFlag){
						stat.addSpecialCell(ruleCell);
					}
				} else {
					// С�����Ʋ����Ϲ���,���c_cell_wrongname��
					stat.addWrongCell(cell);
				}
			}
			// ����

			btssiteMap = stat.getBtssiteMap();
			cellMap = stat.getCellMap();
			tunelSiteMap = stat.getTunelMap();
			// �������վ����������
			stat.statTunelLib(tunelSiteMap);
			tunelLibMap = stat.getTunelLibMap();

			logger.info("��������������վ�������" + btssiteMap.size());
			logger.info("���������վ�����:" + tunelSiteMap.size());
			logger.info("������С��������" + cellMap.size());
			logger.info("����������⣺" + tunelLibMap.size());

			// ʵ������վ����������
			btsUpdate(btssiteMap, yesMap);
			// �����վ�������������
			tunelUpdate(tunelSiteMap, yesTunelMap);
			// ʵ��С����wy_cell��������
			cellUpdate(cellMap, yesCellMap);
			// ʵ�ֶ���������������
			tunelLibUpdate(tunelLibMap, yesTunelLibMap);// �������������

			logger.info("++++��������վ�����.");
			logger.info("++++����BBU��ʼ...");
			noIndoorMap = stat.getNoIndoorMap();
			bbus = btsDao.getBbus();
			yesBbuMap = btsDao.getWyBbu();
			logger.info("��c_bts��ɼ���BBU���ݣ�" + bbus.size());
			logger.info("��wy_bbu��ɼ���BBU����:" + yesBbuMap.size());
			for (Bts bbu : bbus) {
				Bts ruleBbu = ruleBbu(bbu, noIndoorMap, countryMap);
				if (ruleBbu != null) {
					bbuStat.bbuStat(ruleBbu);
					//ͳ������վ��
					boolean specialFlag=ruleBbu.isSpecial();
					if(specialFlag){
						stat.addSpecialBts(ruleBbu);
					}
				} else {
					// ��������BBU
					stat.addWrongBbu(bbu);
				}
				
			}
			bbuStat.finishBbuStat();
			Map<String, WyBbu> bbuMap = bbuStat.getBbuMap();
			Map<String, WyWrongName> wrongMap = stat.getWrongMap();
			Map<String,WyBtsSpecial> specialMap=stat.getBtsSpecialMap();
			logger.info("������BBU����:" + bbuMap.size());
			logger.info("��������������С����BBU������" + wrongMap.size());
			logger.info("����������״̬վ������:"+specialMap.size());
			// ��������
			bbuUpdate(bbuMap, yesBbuMap);
			wwnUpdate(wrongMap, yesWwnMap);
			specialBtsUpdate(specialMap,yesSpecialMap);
			logger.info("++++����BBU����...");

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("++++++�����쳣...." + e.getMessage());
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
				// ����....
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
	 * ���վ����������
	 * 
	 * @param tunelsiteMap
	 * @param yesMap
	 */

	public void tunelUpdate(Map<String, TunelSite> tunelsiteMap,
			Map<String, TunelSite> yesMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		try {
			logger.info("++++�������վ������������...");
			// ��վ�������²���
			for (String keyObj : tunelsiteMap.keySet()) {
				// ����������������վ�Ѵ������п���,���¿��ֶ�
				TunelSite yesTunel = yesMap.get(keyObj);
				TunelSite updateTunel = tunelsiteMap.get(keyObj);
				// ����....
				if (yesTunel != null) {
					// ����
					btsDao.updateWyTunel(updateTunel);
					i++;
				} else {
					// ����
					btsDao.insertWyTunel(updateTunel);
					j++;
				}
			}

			// �ҳ�������վ

			for (String keyObj : yesMap.keySet()) {
				TunelSite toBts = tunelsiteMap.get(keyObj);
				TunelSite delBts = yesMap.get(keyObj);
				if (toBts == null) {
					// �Ҳ����������ѷ���
					// ���ԭ�����Ѿ����������ø���
					int deleteFlag = delBts.getDeleteFlag();
					if (deleteFlag == 0) {
						btsDao.updateWyTunelByDelFlag(delBts);
						q++;
					}
				}
			}
			logger.info("���վ��������������£�" + i + ";������" + j + ";������" + q);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("+++++++ʵ�����վ���������³���");
		}
	}

	/**
	 * ���վ����������
	 * 
	 * @param tunelsiteMap
	 * @param yesMap
	 */

	public void tunelLibUpdate(Map<String, TunelLib> tunelLibMap,
			Map<String, TunelLib> yesMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		try {
			logger.info("++++�������վ������������...");
			// ��վ�������²���
			for (String keyObj : tunelLibMap.keySet()) {
				// ����������������վ�Ѵ������п���,���¿��ֶ�
				TunelLib yesTunel = yesMap.get(keyObj);
				TunelLib updateTunel = tunelLibMap.get(keyObj);
				// ����....
				if (yesTunel != null) {
					// ����
					updateTunel.setId(yesTunel.getId());
					btsDao.updateTunelLib(updateTunel);
					i++;
				} else {
					// ����
					btsDao.insertTunelLib(updateTunel);
					j++;
				}
			}

			// �ҳ�������վ

			for (String keyObj : yesMap.keySet()) {
				TunelLib toTunelLib = tunelLibMap.get(keyObj);
				TunelLib delTunelLib = yesMap.get(keyObj);
				if (toTunelLib == null) {
					// �Ҳ����������ѷ���
					// ���ԭ�����Ѿ����������ø���
					int deleteFlag = delTunelLib.getDeleteFlag();
					if (deleteFlag == 0) {
						btsDao.updateTunelLibByDelFlag(delTunelLib);
						q++;
					}
				}
			}
			logger.info("�����������������£�" + i + ";������" + j + ";������" + q);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("+++++++ʵ��������������³���");
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
					int result = btsDao.updateCell(updateCell);
					if (result > 0) {
						i++;
					}
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
			logger.error(e.getMessage(), e);
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
			logger.error(e.getMessage(), e);
		}
	}
	
	
	/**
	 * wy_special��������
	 * 
	 * @param bbuMap
	 * @param yesBbuMap
	 */
	public void specialBtsUpdate(Map<String, WyBtsSpecial> specialMap,
			Map<String, WyBtsSpecial> yesSpecialMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		logger.info("++++����վ������������...");
		try {
			for (String keyObj : specialMap.keySet()) {
				WyBtsSpecial special = yesSpecialMap.get(keyObj);
				WyBtsSpecial specialUpdate = specialMap.get(keyObj);
				if (special != null) {
					// �����ֶ�
					btsDao.updateWyBtsSpecial(specialUpdate);
					i++;
				} else {
					// ����
					btsDao.insertWyBtsSpecial(specialUpdate);
					j++;
				}
			}
			// ����
			for (String keyObj : yesSpecialMap.keySet()) {
				WyBtsSpecial special = specialMap.get(keyObj);
				WyBtsSpecial specialUpdate = yesSpecialMap.get(keyObj);
				if (special == null) {
					// ����
					int deleteFlag = specialUpdate.getDeleteFlag();
					if (deleteFlag == 0) {
						btsDao.updateWyBtsSpecialByDelFlag(specialUpdate);
						q++;
					}
				}
			}
			logger.info("wy_bts_specail������������£�" + i + ";������" + j + ";������" + q);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
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
			String specialName=cellName.substring(cellName.length()-2);
			boolean specialFlag=AnalyseUtil.isSpecical(specialName);
			cell.setSpecial(specialFlag);
			if(specialFlag){
				cellName=cellName.substring(0, cellName.length()-2);
			}
			String btsName = cell.getBtsName();
			String[] splitName = cellName.split("_");
			int cellLength = splitName.length;
			// �غ��ֶΣ�ϵͳ��_BTS���_С�����_��/��/��+С����_��վ��Ȩ��ʶ_�����Ȩ��ʶ_ά���ȼ�
			// �ɺ��ֶΣ��ҷ�_��Զ_ֱ��վ��ʶ_С��������Ϣ��ʶ��
			if (cellLength < 7) {
				return null;
			}
			
			// �����Ƿ��������
			int cqFlag = 0;
			boolean isHighFlag = AnalyseUtil.isHighBts(splitName[4]);
			boolean isTunel = false;
			String tunelStr = "";
			if (isHighFlag) {
				cqFlag = AnalyseUtil.getCqFlag(splitName, 5);// ��Ȩ��ʼ���
				cell.setHighTrainFlag(splitName[4]);
				String redLine = splitName[4]
						.substring(splitName[4].length() - 1);
				if ("H".equals(redLine)) {
					cell.setRedLineFlag(1);// ������
				} else {
					cell.setRedLineFlag(2);// ������
				}
				tunelStr = splitName[5];
			} else {
				cqFlag = AnalyseUtil.getCqFlag(splitName, 4);
				tunelStr = splitName[4];
			}

			// ���������ʶ
			if (tunelStr.contains("���")) {
				cell.setTunel(true);
			} else {
				cell.setTunel(false);
			}
			// cqFlag:��վ��Ȩ��ʶ
			if (!AnalyseUtil.isRight(splitName[cqFlag])) {
				return null;
			} else {
				cell.setCircuitRoomOwnership(splitName[cqFlag]);
			}

			// cqFlag+1:�����Ȩ��ʶ
			if (!AnalyseUtil.isRight(splitName[cqFlag + 1])) {
				return null;
			} else {
				cell.setTransOwnership(splitName[cqFlag + 1]);
			}

			// cqFlag+2:ά���ȼ�
			if (!AnalyseUtil.isRank(splitName[cqFlag + 2])) {
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
					if (!AnalyseUtil.isZF(zfValue)) {
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
				if (btsName != null && !"".equals(btsName)) {
					String[] btsName_Temp = btsName.split("_");
					btsName = btsName_Temp[0];
				}
				if (!btsName.equals(splitName[3])) {
					return null;// ��������
				}
			}

		} catch (Exception e) {
			logger.info(cell.getName() + ":" + e.getMessage(), e);
			return null;// �����쳣��������С��
		}

		return cell;
	}

	/**
	 * ��֤����BBU
	 * 
	 * @param bts
	 * @return
	 */
	public Bts ruleBbu(Bts bts, Map<String, BtsSite> noIndoorMap,
			Map<String, Country> countryMap) {
		try {
			// ���Ӹ�����ʶ,�غ�˼��������_BBU1_GGH_��_��
			String btsName = bts.getName();
			String specialName=btsName.substring(btsName.length()-2);
			boolean specialFlag=AnalyseUtil.isSpecical(specialName);
			bts.setSpecial(specialFlag);
			if(specialFlag){
				btsName=btsName.substring(0, btsName.length()-2);
			}
			String[] array = btsName.split("_");
			int length = array.length;
			if (!(length > 2 && length < 6)) {
				return null;
			}
			String name = array[0];

			// ���Ƶ�ǰ���ַ��Ҳ����������е�����Ϊ��������
			Country country = AnalyseUtil.getCountry(countryMap, name);
			if (country != null) {
				bts.setCityId(country.getCityId());
				bts.setCountryId(country.getId());
			} else {
				// �����Ϲ�������
				return null;
			}

			// �Ƿ������ʶ
			boolean highFlag = AnalyseUtil.isHighBts(array[2]);
			if (highFlag) {
				bts.setHighTrainFlag(array[2]);
				String redLine = array[2].substring(array[2].length() - 1);
				if ("H".equals(redLine)) {
					bts.setRedLineFlag(1);// ������
				} else {
					bts.setRedLineFlag(2);// ������
				}
			}
			String bzKey = name + "_" + bts.getBscName() + "_" + bts.getBtsId();// ��վ��ʶ
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
					String circuitRoom_ownership = "";
					String Trans_ownership = "";
					if (highFlag) {
						circuitRoom_ownership = array[3];
						Trans_ownership = array[4];
					} else {
						circuitRoom_ownership = array[2];
						Trans_ownership = array[3];
					}
					if (!AnalyseUtil.isRight(circuitRoom_ownership))
						return null;
					if (!AnalyseUtil.isRight(Trans_ownership))
						return null;
				}
			} else {
				// ���BBU2�Ժ�һ�����й�վ
				if (!btsName.contains("��վ")) {
					return null;
				}
			}

		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			return null;// �����쳣
		}
		return bts;
	}

	public void test() {
		Cell cell = new Cell();
		cell.setName("0_337_2_����������_��_��_A_��");
		cell.setBtsName("����������s");
		Cell ruleCell = ruleCell(cell);
		System.out.println("OR:" + ruleCell);
	}

	public void test2() throws Exception {
		Map<String, TunelSite> tunelSiteMap = new HashMap<String, TunelSite>();
		TunelSite site = new TunelSite();
		site.setBsc_name("bsc1");
		site.setBts_id(1);
		site.setName("AAAAs���ȥaaa");
		site.setCityId(2);
		site.setCountryId(3);
		site.setLatitude(1.11);
		site.setLongitude(2.222);

		TunelSite site2 = new TunelSite();
		site2.setBsc_name("bsc1");
		site2.setBts_id(1);
		site2.setName("AAAAs���ȥbbb");
		site2.setCityId(2);
		site2.setCountryId(3);
		site2.setLatitude(1.11);
		site2.setLongitude(2.222);

		TunelSite site3 = new TunelSite();
		site3.setBsc_name("bsc1");
		site3.setBts_id(1);
		site3.setName("AAAAs���");
		site3.setCityId(2);
		site3.setCountryId(3);
		site3.setLatitude(1.11);
		site3.setLongitude(2.222);
		tunelSiteMap.put(site.getName(), site);
		tunelSiteMap.put(site2.getName(), site2);
		tunelSiteMap.put(site3.getName(), site3);

		BtsStat btsStat = new BtsStat();
		btsStat.statTunelLib(tunelSiteMap);

		Map<String, TunelLib> libMap = btsStat.getTunelLibMap();

		Map<String, TunelLib> yesTunelLibMap = btsDao.getTunelLib();// wy_lib_tunel������

		tunelLibUpdate(libMap, yesTunelLibMap);

	}

}
