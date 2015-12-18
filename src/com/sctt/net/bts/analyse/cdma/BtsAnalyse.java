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
 * 基站分析任务，每天分析一次
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
		logger.info("++++++分析开始。");
		BtsStat stat = new BtsStat();
		List<Cell> cells = null;
		Map<String, BtsSite> yesMap = null;// wy_bts库表数据
		Map<String, WyWrongName> yesWwnMap = null;// wy_wrongname数据
		Map<String, Cell> yesCellMap = null;// wy_cell库表数据
		Map<String, Country> countryMap = null;
		Map<String, TunelSite> yesTunelMap = null;// wy_tunel的数据
		Map<String, TunelLib> yesTunelLibMap = null;// wy_lib_tunel的数据
		Map<String, WyBtsSpecial> yesSpecialMap=null;//WY_BTS_SPECIAL数据
		// 分析后覆盖站点Map
		Map<String, BtsSite> btssiteMap = null;
		// 分析后的隧道Map
		Map<String, TunelSite> tunelSiteMap = null;
		// 分析后的小区
		Map<String, Cell> cellMap = null;
		// 分析BBU
		List<Bts> bbus = null;
		BbuStat bbuStat = new BbuStat();
		Map<String, WyBbu> yesBbuMap = null;
		Map<String, BtsSite> noIndoorMap = null;// 非室分Map,bbu查找共站物理站点用
		Map<String, TunelLib> tunelLibMap = null;
		Map<String, WyBtsSpecial> btsSpecialMap=null;

		try {
			logger.info("++++分析物理站点开始:");
			cells = btsDao.getCells();
			yesMap = btsDao.getBtsSites();
			yesWwnMap = btsDao.getWwns();
			countryMap = btsDao.getCountrys();
			yesCellMap = btsDao.getWyCell();
			yesTunelMap = btsDao.getTunels();
			yesTunelLibMap = btsDao.getTunelLib();
			yesSpecialMap=btsDao.getBtsSpecial();
			logger.info("从网管c_cell采集到小区数据：" + cells.size());
			logger.info("wy_bts采集到物理站点数据:" + yesMap.size());
			logger.info("wy_wrongname采集到原有错误小区数据：" + yesWwnMap.size());
			logger.info("wy_cell采集到小区数：" + yesCellMap.size());
			logger.info("wy_tunel采集到隧道站点数据:" + yesTunelMap.size());
			logger.info("wy_tunel_lib采集到解析的隧道库数量:" + yesTunelLibMap.size());
			logger.info("wy_bts_special采集到解析的特殊状态站点数量:"+yesSpecialMap.size());
			for (Cell cell : cells) {
				if (AnalyseUtil.ignoreCell(cell.getName())) {
					continue;// 忽略小区
				}
				Cell ruleCell = ruleCell(cell);
				if (ruleCell != null) {
					// 小区名称符合规则，进行物理基站统计
					boolean tunelFlag = ruleCell.isTunel();
					if (tunelFlag) {
						// 统计隧道
						stat.tunelStat(ruleCell, countryMap);
					} else {
						// 统计室内、室外
						stat.btsStat(ruleCell, countryMap);
					}
					boolean specailFlag=ruleCell.isSpecial();
					if(specailFlag){
						stat.addSpecialCell(ruleCell);
					}
				} else {
					// 小区名称不符合规则,入库c_cell_wrongname表
					stat.addWrongCell(cell);
				}
			}
			// 测试

			btssiteMap = stat.getBtssiteMap();
			cellMap = stat.getCellMap();
			tunelSiteMap = stat.getTunelMap();
			// 根据隧道站点分析隧道库
			stat.statTunelLib(tunelSiteMap);
			tunelLibMap = stat.getTunelLibMap();

			logger.info("解析到室内室外站点个数：" + btssiteMap.size());
			logger.info("解析到隧道站点个数:" + tunelSiteMap.size());
			logger.info("解析到小区个数：" + cellMap.size());
			logger.info("解析到隧道库：" + tunelLibMap.size());

			// 实现物理站点增量更新
			btsUpdate(btssiteMap, yesMap);
			// 对隧道站点进行增量更新
			tunelUpdate(tunelSiteMap, yesTunelMap);
			// 实现小区表wy_cell增量更新
			cellUpdate(cellMap, yesCellMap);
			// 实现对隧道库的增量更新
			tunelLibUpdate(tunelLibMap, yesTunelLibMap);// 隧道库增量更新

			logger.info("++++分析物理站点结束.");
			logger.info("++++分析BBU开始...");
			noIndoorMap = stat.getNoIndoorMap();
			bbus = btsDao.getBbus();
			yesBbuMap = btsDao.getWyBbu();
			logger.info("从c_bts表采集到BBU数据：" + bbus.size());
			logger.info("从wy_bbu表采集到BBU数据:" + yesBbuMap.size());
			for (Bts bbu : bbus) {
				Bts ruleBbu = ruleBbu(bbu, noIndoorMap, countryMap);
				if (ruleBbu != null) {
					bbuStat.bbuStat(ruleBbu);
					//统计特殊站点
					boolean specialFlag=ruleBbu.isSpecial();
					if(specialFlag){
						stat.addSpecialBts(ruleBbu);
					}
				} else {
					// 错误命名BBU
					stat.addWrongBbu(bbu);
				}
				
			}
			bbuStat.finishBbuStat();
			Map<String, WyBbu> bbuMap = bbuStat.getBbuMap();
			Map<String, WyWrongName> wrongMap = stat.getWrongMap();
			Map<String,WyBtsSpecial> specialMap=stat.getBtsSpecialMap();
			logger.info("解析到BBU数量:" + bbuMap.size());
			logger.info("解析到错误命名小区、BBU数量：" + wrongMap.size());
			logger.info("解析到特殊状态站点数量:"+specialMap.size());
			// 增量更新
			bbuUpdate(bbuMap, yesBbuMap);
			wwnUpdate(wrongMap, yesWwnMap);
			specialBtsUpdate(specialMap,yesSpecialMap);
			logger.info("++++分析BBU结束...");

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("++++++分析异常...." + e.getMessage());
		}
		logger.info("++++++分析结束");
	}

	/**
	 * 实现物理站点的增量更新
	 * 
	 * @param btssiteMap
	 *            :分析出的物理站点
	 * @param yesMap
	 *            ：现有库中物理站点
	 */
	public void btsUpdate(Map<String, BtsSite> btssiteMap,
			Map<String, BtsSite> yesMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		try {
			logger.info("++++物理站点增量分析中...");
			// 基站增量更新步骤
			for (String keyObj : btssiteMap.keySet()) {
				// 今天分析出来物理基站已存在现有库中,更新库字段
				BtsSite yesBts = yesMap.get(keyObj);
				BtsSite updateBts = btssiteMap.get(keyObj);
				// 测试....
				if (yesBts != null) {
					// 更新
					btsDao.updateBtsSite(updateBts);
					i++;
				} else {
					// 新增
					btsDao.insertBtsSite(updateBts);
					j++;
				}
			}

			// 找出废弃基站

			for (String keyObj : yesMap.keySet()) {
				BtsSite toBts = btssiteMap.get(keyObj);
				BtsSite delBts = yesMap.get(keyObj);
				if (toBts == null) {
					// 找不到，代表已废弃
					// 如果原来是已经废弃，则不用更改
					int deleteFlag = delBts.getDeleteFlag();
					if (deleteFlag == 0) {
						btsDao.updateBtsSiteByDeleteFlag(delBts);
						q++;
					}
				}
			}
			logger.info("物理站点增量结果：更新：" + i + ";新增：" + j + ";废弃：" + q);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("+++++++实现物理站点增量更新出错。");
		}
	}

	/**
	 * 隧道站点增量跟新
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
			logger.info("++++隧道覆盖站点增量分析中...");
			// 基站增量更新步骤
			for (String keyObj : tunelsiteMap.keySet()) {
				// 今天分析出来物理基站已存在现有库中,更新库字段
				TunelSite yesTunel = yesMap.get(keyObj);
				TunelSite updateTunel = tunelsiteMap.get(keyObj);
				// 测试....
				if (yesTunel != null) {
					// 更新
					btsDao.updateWyTunel(updateTunel);
					i++;
				} else {
					// 新增
					btsDao.insertWyTunel(updateTunel);
					j++;
				}
			}

			// 找出废弃基站

			for (String keyObj : yesMap.keySet()) {
				TunelSite toBts = tunelsiteMap.get(keyObj);
				TunelSite delBts = yesMap.get(keyObj);
				if (toBts == null) {
					// 找不到，代表已废弃
					// 如果原来是已经废弃，则不用更改
					int deleteFlag = delBts.getDeleteFlag();
					if (deleteFlag == 0) {
						btsDao.updateWyTunelByDelFlag(delBts);
						q++;
					}
				}
			}
			logger.info("隧道站点增量结果：更新：" + i + ";新增：" + j + ";废弃：" + q);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("+++++++实现隧道站点增量更新出错。");
		}
	}

	/**
	 * 隧道站点增量跟新
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
			logger.info("++++隧道覆盖站点增量分析中...");
			// 基站增量更新步骤
			for (String keyObj : tunelLibMap.keySet()) {
				// 今天分析出来物理基站已存在现有库中,更新库字段
				TunelLib yesTunel = yesMap.get(keyObj);
				TunelLib updateTunel = tunelLibMap.get(keyObj);
				// 测试....
				if (yesTunel != null) {
					// 更新
					updateTunel.setId(yesTunel.getId());
					btsDao.updateTunelLib(updateTunel);
					i++;
				} else {
					// 新增
					btsDao.insertTunelLib(updateTunel);
					j++;
				}
			}

			// 找出废弃基站

			for (String keyObj : yesMap.keySet()) {
				TunelLib toTunelLib = tunelLibMap.get(keyObj);
				TunelLib delTunelLib = yesMap.get(keyObj);
				if (toTunelLib == null) {
					// 找不到，代表已废弃
					// 如果原来是已经废弃，则不用更改
					int deleteFlag = delTunelLib.getDeleteFlag();
					if (deleteFlag == 0) {
						btsDao.updateTunelLibByDelFlag(delTunelLib);
						q++;
					}
				}
			}
			logger.info("隧道库增量结果：更新：" + i + ";新增：" + j + ";废弃：" + q);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("+++++++实现隧道库增量更新出错。");
		}
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
	 * wy_cell增量更新
	 * 
	 * @param cellMap
	 *            ：现分析的小区
	 * @param yesCellMap
	 *            ：数据库表中的小区
	 */
	public void cellUpdate(Map<String, Cell> cellMap,
			Map<String, Cell> yesCellMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		try {
			logger.info("++++小区增量分析中...");
			for (String keyObj : cellMap.keySet()) {
				Cell cell = yesCellMap.get(keyObj);
				Cell updateCell = cellMap.get(keyObj);
				if (cell != null) {
					// 更新
					int result = btsDao.updateCell(updateCell);
					if (result > 0) {
						i++;
					}
				} else {
					// 新增
					btsDao.insertCell(updateCell);
					j++;
				}
			}

			for (String keyObj : yesCellMap.keySet()) {
				Cell cell = cellMap.get(keyObj);
				Cell updateCell = yesCellMap.get(keyObj);
				if (cell == null) {
					// 废弃
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
		logger.info("wy_cell小区增量结果：更新：" + i + ";新增：" + j + ";废弃：" + q);
	}

	/**
	 * wy_bbu增量更新
	 * 
	 * @param bbuMap
	 * @param yesBbuMap
	 */
	public void bbuUpdate(Map<String, WyBbu> bbuMap,
			Map<String, WyBbu> yesBbuMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		logger.info("++++BBU增量更新中...");
		try {
			for (String keyObj : bbuMap.keySet()) {
				WyBbu bbu = yesBbuMap.get(keyObj);
				WyBbu bbuUpdate = bbuMap.get(keyObj);
				if (bbu != null) {
					// 更新字段
					btsDao.updateWyBbu(bbuUpdate);
					i++;
				} else {
					// 新增
					btsDao.insertWyBbu(bbuUpdate);
					j++;
				}
			}
			// 废弃
			for (String keyObj : yesBbuMap.keySet()) {
				WyBbu bbu = bbuMap.get(keyObj);
				WyBbu bbuUpdate = yesBbuMap.get(keyObj);
				if (bbu == null) {
					// 废弃
					int deleteFlag = bbuUpdate.getDeleteFlag();
					if (deleteFlag == 0) {
						btsDao.updateWyBbuByDelFlag(bbuUpdate);
						q++;
					}
				}
			}
			logger.info("wy_bbu增量结果：更新：" + i + ";新增：" + j + ";废弃：" + q);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
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

	/**
	 * 小区命名规则判断
	 * 
	 * @param cell
	 *            ：小区对象
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
			// 必含字段：系统号_BTS序号_小区序号_市/县/区+小区名_基站产权标识_传输产权标识_维护等级
			// 可含字段：室分_拉远_直放站标识_小区功分信息标识。
			if (cellLength < 7) {
				return null;
			}
			
			// 计算是否高铁覆盖
			int cqFlag = 0;
			boolean isHighFlag = AnalyseUtil.isHighBts(splitName[4]);
			boolean isTunel = false;
			String tunelStr = "";
			if (isHighFlag) {
				cqFlag = AnalyseUtil.getCqFlag(splitName, 5);// 产权开始标记
				cell.setHighTrainFlag(splitName[4]);
				String redLine = splitName[4]
						.substring(splitName[4].length() - 1);
				if ("H".equals(redLine)) {
					cell.setRedLineFlag(1);// 红线内
				} else {
					cell.setRedLineFlag(2);// 红线外
				}
				tunelStr = splitName[5];
			} else {
				cqFlag = AnalyseUtil.getCqFlag(splitName, 4);
				tunelStr = splitName[4];
			}

			// 设置隧道标识
			if (tunelStr.contains("隧道")) {
				cell.setTunel(true);
			} else {
				cell.setTunel(false);
			}
			// cqFlag:基站产权标识
			if (!AnalyseUtil.isRight(splitName[cqFlag])) {
				return null;
			} else {
				cell.setCircuitRoomOwnership(splitName[cqFlag]);
			}

			// cqFlag+1:传输产权标识
			if (!AnalyseUtil.isRight(splitName[cqFlag + 1])) {
				return null;
			} else {
				cell.setTransOwnership(splitName[cqFlag + 1]);
			}

			// cqFlag+2:维护等级
			if (!AnalyseUtil.isRank(splitName[cqFlag + 2])) {
				return null;
			} else {
				cell.setServiceLevel(splitName[cqFlag + 2]);
			}

			int zfStart = cqFlag + 3;// 直放站开始标识
			int zfEnd = 0;// 直放站结束标识
			// 是否包含功分，是：是否最后一个字段
			if (cellName.contains("功分")) {
				if (!splitName[cellLength - 1].contains("功分")) {
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

			// 直放站验证
			if (zfEnd > zfStart) {
				for (int i = 0; i < zfEnd - zfStart; i++) {
					String zfValue = splitName[zfStart + i];
					if (!AnalyseUtil.isZF(zfValue)) {
						return null;
					} else {
						// 包含直放站、具体区分那一个种直放站
						String zfType = zfValue.substring(0, 2);
						String s = zfValue.substring(2);
						int count = StringUtils.objToInt(s);
						if ("OR".equals(zfType)) {
							// 光纤直放站
							cell.setIsOr(count);
						} else if ("RR".equals(zfType)) {
							// 无线直放站
							cell.setIsRR(count);
						} else if ("SR".equals(zfType)) {
							// 移频直放站
							cell.setIsSR(count);
						}
					}
				}
			}

			// 非拉远基站，不允许基站名称与分裂出来的物理站点名称不一样
			if (!cellName.contains("拉远")) {
				if (btsName != null && !"".equals(btsName)) {
					String[] btsName_Temp = btsName.split("_");
					btsName = btsName_Temp[0];
				}
				if (!btsName.equals(splitName[3])) {
					return null;// 错误命名
				}
			}

		} catch (Exception e) {
			logger.info(cell.getName() + ":" + e.getMessage(), e);
			return null;// 解析异常，不规则小区
		}

		return cell;
	}

	/**
	 * 验证规则BBU
	 * 
	 * @param bts
	 * @return
	 */
	public Bts ruleBbu(Bts bts, Map<String, BtsSite> noIndoorMap,
			Map<String, Country> countryMap) {
		try {
			// 增加高铁标识,沿河思渠接入网_BBU1_GGH_电_电
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

			// 名称的前三字符找不到所属地市的依旧为错误命名
			Country country = AnalyseUtil.getCountry(countryMap, name);
			if (country != null) {
				bts.setCityId(country.getCityId());
				bts.setCountryId(country.getId());
			} else {
				// 不符合规则命名
				return null;
			}

			// 是否高铁标识
			boolean highFlag = AnalyseUtil.isHighBts(array[2]);
			if (highFlag) {
				bts.setHighTrainFlag(array[2]);
				String redLine = array[2].substring(array[2].length() - 1);
				if ("H".equals(redLine)) {
					bts.setRedLineFlag(1);// 红线内
				} else {
					bts.setRedLineFlag(2);// 红线外
				}
			}
			String bzKey = name + "_" + bts.getBscName() + "_" + bts.getBtsId();// 基站标识
			if ("BBU1".equals(array[1])) {
				if (btsName.contains("共站")) {
					// BBU1包含共站，物理站点中能找到相同名称站点
					BtsSite btsSite = noIndoorMap.get(name);
					if (btsSite == null) {
						return null;
					} else {
						bts.setRelatedWyBts(btsSite.getIntId());
					}

				} else {
					// BBU1不含共站，包含机房产权和传输产权
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
				// 如果BBU2以后一定得有共站
				if (!btsName.contains("共站")) {
					return null;
				}
			}

		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			return null;// 解析异常
		}
		return bts;
	}

	public void test() {
		Cell cell = new Cell();
		cell.setName("0_337_2_修文民政局_电_电_A_新");
		cell.setBtsName("修文民政局s");
		Cell ruleCell = ruleCell(cell);
		System.out.println("OR:" + ruleCell);
	}

	public void test2() throws Exception {
		Map<String, TunelSite> tunelSiteMap = new HashMap<String, TunelSite>();
		TunelSite site = new TunelSite();
		site.setBsc_name("bsc1");
		site.setBts_id(1);
		site.setName("AAAAs隧道去aaa");
		site.setCityId(2);
		site.setCountryId(3);
		site.setLatitude(1.11);
		site.setLongitude(2.222);

		TunelSite site2 = new TunelSite();
		site2.setBsc_name("bsc1");
		site2.setBts_id(1);
		site2.setName("AAAAs隧道去bbb");
		site2.setCityId(2);
		site2.setCountryId(3);
		site2.setLatitude(1.11);
		site2.setLongitude(2.222);

		TunelSite site3 = new TunelSite();
		site3.setBsc_name("bsc1");
		site3.setBts_id(1);
		site3.setName("AAAAs隧道");
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

		Map<String, TunelLib> yesTunelLibMap = btsDao.getTunelLib();// wy_lib_tunel的数据

		tunelLibUpdate(libMap, yesTunelLibMap);

	}

}
