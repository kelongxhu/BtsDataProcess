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
		Map<String,Country> countryMap = null;
		// 分析后Map
		Map<String, BtsSite> btssiteMap = null;	
		// 分析BBU
		List<Bts> bbus = null;
		BbuStat bbuStat = new BbuStat();
		Map<String, WyBbu> yesBbuMap = null;
		Map<String, BtsSite> noIndoorMap = null;// 非室分Map,bbu查找共站物理站点用
		
		
		try {
			logger.info("++++分析物理站点开始:");
			cells = btsDao.getCells();
			yesMap = btsDao.getBtsSites();
			yesWwnMap = btsDao.getWwns();
			countryMap = btsDao.getCountrys();
			yesCellMap = btsDao.getWyCell();
			logger.info("从网管c_cell采集到小区数据：" + cells.size());
			logger.info("wy_bts采集到物理站点数据:" + yesMap.size());
			logger.info("wy_wrongname采集到原有错误小区数据：" + yesWwnMap.size());
			logger.info("wy_cell采集到小区数：" + yesCellMap.size());
			for (Cell cell : cells) {
				if (ignoreCell(cell.getName())) {
					continue;// 忽略小区
				}
				Cell ruleCell = ruleCell(cell);
				if (ruleCell != null) {
					// 小区名称符合规则，进行物理基站统计
					stat.btsStat(ruleCell, countryMap);
				} else {
					// 小区名称不符合规则,入库c_cell_wrongname表
					stat.addWrongCell(cell);
				}
			}
			// 测试

			btssiteMap = stat.getBtssiteMap();
			Map<String, Cell> cellMap = stat.getCellMap();

			logger.info("解析到物理站点个数：" + btssiteMap.size());
			logger.info("解析到小区个数：" + cellMap.size());

			// 实现物理站点增量更新
			btsUpdate(btssiteMap, yesMap);
			// 实现小区表wy_cell增量更新
			cellUpdate(cellMap, yesCellMap);
			
			logger.info("++++分析物理站点结束.");		
			logger.info("++++分析BBU开始...");
			noIndoorMap=stat.getNoIndoorMap();
			bbus = btsDao.getBbus();
			yesBbuMap = btsDao.getWyBbu();
			logger.info("从c_bts表采集到BBU数据：" + bbus.size());
			logger.info("从wy_bbu表采集到BBU数据:" + yesBbuMap.size());
			for (Bts bbu : bbus) {
				Bts ruleBbu = ruleBbu(bbu, noIndoorMap,countryMap);
				if (ruleBbu != null) {
					bbuStat.bbuStat(ruleBbu);
				} else {
					// 错误命名BBU
					stat.addWrongBbu(bbu);
				}
			}
			bbuStat.finishBbuStat();
			Map<String, WyBbu> bbuMap = bbuStat.getBbuMap();
			Map<String, WyWrongName> wrongMap = stat.getWrongMap();
			logger.info("解析到BBU数量:" + bbuMap.size());
			logger.info("解析到错误命名小区、BBU数量：" + wrongMap.size());
			// 增量更新
			bbuUpdate(bbuMap, yesBbuMap);
			wwnUpdate(wrongMap, yesWwnMap);
			logger.info("++++分析BBU结束...");

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("++++++分析异常...."+e.getMessage());
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
				//测试....
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
					btsDao.updateCell(updateCell);
					i++;
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
			String btsName = cell.getBtsName();
			String[] splitName = cellName.split("_");
			int cellLength = splitName.length;
			// 必含字段：系统号_BTS序号_小区序号_市/县/区+小区名_基站产权标识_传输产权标识_维护等级
			// 可含字段：室分_拉远_直放站标识_小区功分信息标识。
			if (cellLength < 7) {
				return null;
			}
			int cqFlag = getCqFlag(splitName);// 产权开始标记

			// cqFlag:基站产权标识
			if (!isRight(splitName[cqFlag])) {
				return null;
			} else {
				cell.setCircuitRoomOwnership(splitName[cqFlag]);
			}

			// cqFlag+1:传输产权标识
			if (!isRight(splitName[cqFlag + 1])) {
				return null;
			} else {
				cell.setTransOwnership(splitName[cqFlag + 1]);
			}

			// cqFlag+2:维护等级
			if (!isRank(splitName[cqFlag + 2])) {
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
					if (!isZF(zfValue)) {
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
				if(btsName!=null&&!"".equals(btsName)){
					String[] btsName_Temp=btsName.split("_");
					btsName=btsName_Temp[0];
				}
				if(!btsName.equals(splitName[3])){
					return null;//错误命名
				}
			} 

		} catch (Exception e) {
			logger.info(cell.getName() + ":" + e.getMessage());
			return null;// 解析异常，不规则小区
		}

		return cell;
	}

	/**
	 * 是否符合产权归属
	 * 
	 * @param str
	 * @return:true:符合规则
	 */
	public boolean isRight(String str) {
		String[] rang = { "电", "联", "移", "电联", "电移" };
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
	 * 是否维护等级
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
	 * 是否满足直放站的标记
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
	 * 计算基站产权标识的标记位
	 * 
	 * @param splitName
	 * @return
	 */
	public int getCqFlag(String[] splitName) {
		int cqFlag = 0;
		if ("室分".equals(splitName[4])||"隧道".equals(splitName[4])) {
			if ("拉远".equals(splitName[5])) {
				cqFlag = 6;
			} else {
				cqFlag = 5;
			}
		} else if ("拉远".equals(splitName[4])) {
			cqFlag = 5;
		} else{
			cqFlag = 4;
		}
		return cqFlag;
	}

	/**
	 * 验证规则BBU
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
			
			//名称的前三字符找不到所属地市的依旧为错误命名
			Country country=BtsAnalyse.getCountry(countryMap, name);
			if(country!=null){			
				bts.setCityId(country.getCityId());
				bts.setCountryId(country.getId());
			}else{
				//不符合规则命名
				return null;
			}
			
			
			String bzKey =  name +"_"+bts.getBscName()+ "_" + bts.getBtsId();//基站标识
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
					if (!isRight(array[2]))
						return null;
					if (!isRight(array[3]))
						return null;
				}
			} else {
				// 如果BBU2以后一定得有共站
				if (!btsName.contains("共站")) {
					return null;
				}
			}
			
			
		} catch (Exception e) {
			logger.error("" + e.getMessage());
			return null;// 解析异常
		}
		return bts;
	}

	/**
	 * 通过小区名称获取小区所在区县
	 * 
	 * @param countryList
	 *            ：区县列表
	 * @param cell
	 *            ：小区对象
	 * @return
	 */
	public static Country getCountry(Map<String,Country> countryMap, String name) {
		String countryName = "";//前两个字
		String countryName2="";//前三个字
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
	 * 判断小区名称是否有忽略小区
	 * 
	 * @param cellName
	 * @return
	 */
	public boolean ignoreCell(String cellName) {
		String[] igArray = { "应急通信", "应急通讯", "通信应急", "通讯应急" };
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
		cell.setName("0_665_0_印江黔溪_电_电_C_3功分");
		cell.setBtsName("印江黔溪");
		Cell ruleCell = ruleCell(cell);
		System.out.println("OR:" + ruleCell);
	}

}
