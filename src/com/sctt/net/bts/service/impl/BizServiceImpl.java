package com.sctt.net.bts.service.impl;

import java.util.Map;

import org.apache.log4j.Logger;

import com.sctt.net.bts.bean.cdma.WyBtsSpecial;
import com.sctt.net.bts.bean.cdma.WyWrongName;
import com.sctt.net.bts.dao.BtsDao;
import com.sctt.net.bts.service.BizService;

public class BizServiceImpl implements BizService {
	private static Logger logger = Logger.getLogger("baseLog");
	private BtsDao btsDao;

	public void setBtsDao(BtsDao btsDao) {
		this.btsDao = btsDao;
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


}
