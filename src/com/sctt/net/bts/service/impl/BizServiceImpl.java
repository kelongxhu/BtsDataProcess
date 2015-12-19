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
	 * 错误小区名称表实现增量更新
	 * 
	 * @param wrongMap
	 *            :先分析出的错误小区
	 * @param yesWwnMap
	 *            ：数据库中存储的错误小区
	 */
	public void wwnUpdate(Map<String, WyWrongName> wrongMap,
			Map<String, WyWrongName> yesWwnMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		try {
			logger.info("++++错误小区增量分析中...");
			for (String keyObj : wrongMap.keySet()) {
				WyWrongName wwN = yesWwnMap.get(keyObj);
				WyWrongName updateWwn = wrongMap.get(keyObj);
				if (wwN != null) {
					// 更新
					btsDao.updateWyWrongName(updateWwn);
					i++;
				} else {
					// 新增
					btsDao.insertWyWrongName(updateWwn);
					j++;
				}
			}
			// 或许小区命名已经更改
			for (String keyObj : yesWwnMap.keySet()) {
				WyWrongName wwN = wrongMap.get(keyObj);
				WyWrongName updateWwn = yesWwnMap.get(keyObj);
				if (wwN == null) {
					// 已更改正确
					int deleteFlag = updateWwn.getDeleteFlag();
					if (deleteFlag == 0) {
						btsDao.updateWyWrongNameByDelFlag(updateWwn);
						q++;
					}
				}
			}
			logger.info("错误小区增量结果：更新：" + i + ";新增：" + j + ";废弃：" + q);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * wy_special增量更新
	 * 
	 * @param bbuMap
	 * @param yesBbuMap
	 */
	public void specialBtsUpdate(Map<String, WyBtsSpecial> specialMap,
			Map<String, WyBtsSpecial> yesSpecialMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		logger.info("++++特殊站点增量更新中...");
		try {
			for (String keyObj : specialMap.keySet()) {
				WyBtsSpecial special = yesSpecialMap.get(keyObj);
				WyBtsSpecial specialUpdate = specialMap.get(keyObj);
				if (special != null) {
					// 更新字段
					btsDao.updateWyBtsSpecial(specialUpdate);
					i++;
				} else {
					// 新增
					btsDao.insertWyBtsSpecial(specialUpdate);
					j++;
				}
			}
			// 废弃
			for (String keyObj : yesSpecialMap.keySet()) {
				WyBtsSpecial special = specialMap.get(keyObj);
				WyBtsSpecial specialUpdate = yesSpecialMap.get(keyObj);
				if (special == null) {
					// 废弃
					int deleteFlag = specialUpdate.getDeleteFlag();
					if (deleteFlag == 0) {
						btsDao.updateWyBtsSpecialByDelFlag(specialUpdate);
						q++;
					}
				}
			}
			logger.info("wy_bts_specail增量结果：更新：" + i + ";新增：" + j + ";废弃：" + q);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}


}
