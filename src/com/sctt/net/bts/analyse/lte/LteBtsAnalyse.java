package com.sctt.net.bts.analyse.lte;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sctt.net.bts.bean.cdma.Country;
import com.sctt.net.bts.bean.cdma.WyBtsSpecial;
import com.sctt.net.bts.bean.cdma.WyWrongName;
import com.sctt.net.bts.bean.lte.Enodeb;
import com.sctt.net.bts.bean.lte.EutranCell;
import com.sctt.net.bts.bean.lte.LteBbu;
import com.sctt.net.bts.bean.lte.LteBts;
import com.sctt.net.bts.dao.BtsDao;
import com.sctt.net.bts.dao.LteDao;
import com.sctt.net.bts.service.BizService;
import com.sctt.net.common.util.AnalyseUtil;
import com.sctt.net.common.util.Constants;
import com.sctt.net.common.util.StringUtils;
import com.sctt.net.common.util.WrongMsg;

/**
 * LTE 基站小区解析
 * @author _think
 *
 */
public class LteBtsAnalyse implements Runnable {

	private static Logger logger = Logger.getLogger("baseLog");
	private BtsDao btsDao;
	private LteDao lteDao;
	private BizService bizService;
	
	public LteBtsAnalyse(BtsDao btsDao,LteDao lteDao,BizService bizService){
		this.btsDao=btsDao;
		this.lteDao=lteDao;
		this.bizService=bizService;
	}
	public void run() {
		logger.info("LTE站点分析开始.....");
		try {
			LteBtsStat stat=new LteBtsStat();
			List<EutranCell> cells=lteDao.selectEutranCell();
			List<Enodeb> bbus=lteDao.selectEnodeb();//bbu列表
			Map<String,Country> countryMap = btsDao.getCountrys();
			Map<String,LteBts> yesLteBtsMap=lteDao.selectLteBts();
			Map<String,EutranCell> yesLteCellMap=lteDao.selectLteCell();
			Map<String, LteBts> noIndoorMap = null;// 非室分Map,bbu查找共站物理站点用
			Map<String,LteBbu> yesBbuMap=lteDao.selectLteBbu();//wy_lte_bbu数据
			Map<String,WyWrongName> yesWwnMap = btsDao.getWwns(Constants.LTE);
			Map<String, WyBtsSpecial> btsSpecialMap=btsDao.getBtsSpecial(Constants.LTE);
			logger.info("从tco_pro_eutrancell_m采集到小区数据:"+cells.size());
			logger.info("从tco_pro_enodeb_m采集到bbu数:"+bbus.size());
			logger.info("從wy_lte_bts采集到数据:"+yesLteBtsMap.size());
			logger.info("从wy_lte_cell采集到数据:"+yesLteCellMap.size());
			logger.info("从wy_lte_bbu采集到的数据:"+yesBbuMap.size());
			logger.info("从wy_wrongName采集到LTE错误命名:"+yesWwnMap.size());
			logger.info("从wy_speacail采集到LTE特殊站点命名:"+btsSpecialMap.size());
			//解析
			for(EutranCell cell:cells){
				if (AnalyseUtil.ignoreCell(cell.getUserLabel())) {
					continue;// 忽略小区
				}
				EutranCell judgeCell=judgeCell(cell,countryMap);
				String judgeMsg=judgeCell.getJudgeMsg();
				if(StringUtils.isEmpty(judgeMsg)){
					stat.btsStat(judgeCell);
					//添加到特殊站点统计
					boolean isSpeacial=judgeCell.isSpecial();
					if(isSpeacial){
						stat.addSpecialCell(judgeCell);
					}
				}else{
					//错误命名小区
					stat.addWrongCell(cell);
				}
			}
			//解析完成
			Map<String,LteBts> lteBtsMap=stat.getLteBtsMap();
			Map<String,EutranCell> cellMap=stat.getCellMap();
			insertLteBts(lteBtsMap,yesLteBtsMap);
			incrUpdateCell(cellMap,yesLteCellMap);
			logger.info("解析到LTE站点数:"+lteBtsMap.size());
			logger.info("解析到LTE小区数:"+cellMap.size());
			logger.info("解析LTE的BBU开始...");
			LteBbuStat bbuStat=new LteBbuStat();
			noIndoorMap=stat.getNoIndoorMap();
			for (Enodeb bbu : bbus) {
				Enodeb judgeBbu = ruleLteBbu(bbu, noIndoorMap, countryMap);
				String bbuJudgeMsg=judgeBbu.getJudgeMsg();
				if (StringUtils.isEmpty(bbuJudgeMsg)) {
					bbuStat.bbuStat(judgeBbu);
					//统计特殊站点
					boolean specialFlag=judgeBbu.isSpecial();
					if(specialFlag){
						stat.addSpecialBts(judgeBbu);
					}
				} else {
					// 错误命名BBU
					stat.addWrongBbu(bbu);
				}
				
			}
			bbuStat.finishBbuStat();
			Map<String,LteBbu> bbuMap=bbuStat.getBbuMap();
			Map<String,WyWrongName> wwNMap=stat.getWrongMap();
			Map<String,WyBtsSpecial> specialMap=stat.getBtsSpecialMap();
			incrUpdateBbu(bbuMap,yesBbuMap);
			bizService.wwnUpdate(wwNMap, yesWwnMap);
			bizService.specialBtsUpdate(specialMap,btsSpecialMap);
			logger.info("解析LTE的BBU数:"+bbuMap.size());
			logger.info("解析LTE的BBU结束...");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 增量更新LTE基站
	 * @param lteBtsMap
	 * @param dbLteBts
	 */
	public void insertLteBts(Map<String,LteBts> lteBtsMap,Map<String,LteBts> yesBtsMap){
		int i = 0;
		int j = 0;
		int q = 0;
		try {
			logger.info("++++小区增量分析中...");
			for (String keyObj : lteBtsMap.keySet()) {
				LteBts bts = yesBtsMap.get(keyObj);
				LteBts updateBts = lteBtsMap.get(keyObj);
				if (bts != null) {
					// 更新
					i+= lteDao.updateLteBts(updateBts);
				} else {
					// 新增
					j+=lteDao.insertLteBts(updateBts);
				}
			}

			for (String keyObj : yesBtsMap.keySet()) {
				LteBts bts = lteBtsMap.get(keyObj);
				LteBts updateBts = yesBtsMap.get(keyObj);
				if (bts == null) {
					// 废弃
					int deleteFlag = updateBts.getDeleteFlag();
					if (deleteFlag == 0) {
						q+=lteDao.updateLteBtsByDeleteFlag(updateBts);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		logger.info("wy_lte_bts基站增量结果：更新：" + i + ";新增：" + j + ";废弃：" + q);
	}
	
	public void incrUpdateCell(Map<String, EutranCell> cellMap,
			Map<String, EutranCell> yesCellMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		try {
			logger.info("++++小区增量分析中...");
			for (String keyObj : cellMap.keySet()) {
				EutranCell cell = yesCellMap.get(keyObj);
				EutranCell updateCell = cellMap.get(keyObj);
				if (cell != null) {
					// 更新
					i+=lteDao.updateLteCell(updateCell);
				} else {
					// 新增
					j+=lteDao.insertLteCell(updateCell);
				}
			}

			for (String keyObj : yesCellMap.keySet()) {
				EutranCell cell = cellMap.get(keyObj);
				EutranCell updateCell = yesCellMap.get(keyObj);
				if (cell == null) {
					// 废弃
					int deleteFlag = updateCell.getDeleteFlag();
					if (deleteFlag == 0) {
						q+=lteDao.updateLteCellByDeleteFlag(updateCell);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("wy_lte_cell小区增量结果：更新：" + i + ";新增：" + j + ";废弃：" + q);
	}
	
	public void incrUpdateBbu(Map<String, LteBbu> bbuMap,
			Map<String, LteBbu> yesBbuMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		try {
			logger.info("++++Lte BBU增量分析中...");
			for (String keyObj : bbuMap.keySet()) {
				LteBbu bbu = yesBbuMap.get(keyObj);
				LteBbu updateBbu = bbuMap.get(keyObj);
				if (bbu != null) {
					// 更新
					i+=lteDao.updateLteBbu(updateBbu);
				} else {
					// 新增
					j+=lteDao.insertLteBbu(updateBbu);
				}
			}

			for (String keyObj : yesBbuMap.keySet()) {
				LteBbu bbu = bbuMap.get(keyObj);
				LteBbu updateBbu = yesBbuMap.get(keyObj);
				if (bbu == null) {
					// 废弃
					int deleteFlag = updateBbu.getDeleteFlag();
					if (deleteFlag == 0) {
						q+=lteDao.updateLteBbuByDeleteFlag(updateBbu);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("wy_lte_bbu表增量结果：更新：" + i + ";新增：" + j + ";废弃：" + q);
	}
	/**
	 * 小区命名规则判断
	 * 
	 * @param cell
	 *            ：小区对象
	 * @return
	 */
	public EutranCell judgeCell(EutranCell cell,Map<String, Country> countryMap) {
		try {
			String cellName = cell.getUserLabel();
			String specialName=cellName.substring(cellName.length()-2);
			boolean specialFlag=AnalyseUtil.isSpecical(specialName);
			cell.setSpecial(specialFlag);
			if(specialFlag){
				cellName=cellName.substring(0, cellName.length()-2);
			}
			String btsName = cell.getRelateEnbUserLabel();
			String[] splitName = cellName.split("_");
			int cellLength = splitName.length;
			//系统号_BTS序号_小区序号_市/县/区+小区名_高铁信息标识_室分/隧道_拉远_基站产权标识_传输产权标识_维护等级_多网共站标识_直放站标识_小区功分信息标识。
			// 必含字段：系统号_BTS序号_小区序号_市/县/区+小区名_基站产权标识_传输产权标识_维护等级
			// 可含字段：高铁信息标识_室分_拉远_多网共站标识_直放站标识_小区功分信息标识。
			if (cellLength < 8) {
				//缺失字段
				cell.setJudgeMsg(WrongMsg.MISS.getWrongMsg());
				return cell;
			}	
			String name = splitName[3];//站点名称
			//区县是否在配置表中
			Country country = AnalyseUtil.getCountry(countryMap, name);
			if (country != null) {
				cell.setCityId(country.getCityId());
				cell.setCountryId(country.getId());
			} else {
				// 未找到对应的所属区县为错误小区命名
				cell.setJudgeMsg(WrongMsg.CITY.getWrongMsg());
				return cell;
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
			if(tunelStr.equals("室分")){
				cell.setIsIndoor("是");
			}else if(tunelStr.equals("隧道")){
				cell.setIsIndoor("隧");
			}else{
				cell.setIsIndoor("否");
			}
			// cqFlag:基站产权标识, 传输产权，维护等级必选项
			if(cqFlag+2<cellLength){
				//缺失字段
				cell.setJudgeMsg(WrongMsg.MISS.getWrongMsg());
				return cell;
			}
			
			if (!AnalyseUtil.isRight(splitName[cqFlag])) {
				cell.setJudgeMsg(WrongMsg.OWNER_RIGHT.getWrongMsg());
				return cell;
			} else {
				cell.setCircuitRoomOwnership(splitName[cqFlag]);
			}

			// cqFlag+1:传输产权标识
			if (!AnalyseUtil.isRight(splitName[cqFlag + 1])) {
				cell.setJudgeMsg(WrongMsg.TRANS_RIGHT.getWrongMsg());
				return cell;
			} else {
				cell.setTransOwnership(splitName[cqFlag + 1]);
			}

			// cqFlag+2:维护等级
			if (!AnalyseUtil.isRank(splitName[cqFlag + 2])) {
				cell.setJudgeMsg(WrongMsg.RANK.getWrongMsg());
				return cell;
			} else {
				cell.setServiceLevel(splitName[cqFlag + 2]);
			}
			//多网共站标识
			int togetherFlag=cqFlag+3;
			int zfStart=togetherFlag;// 直放站开始标识
			if(cellLength>togetherFlag){
				String togetherStr=splitName[togetherFlag];
				if(AnalyseUtil.isTogether(togetherStr)){
					cell.setSiteTogether(togetherStr);
					zfStart=togetherFlag+1;//直放站开始标识
				}			
			}
			int zfEnd = 0;// 直放站结束标识
			// 是否包含功分，是：是否最后一个字段
			if (cellName.contains("功分")) {
				String gfValue = splitName[cellLength - 1];
				if (!gfValue.contains("功分")) {
					cell.setJudgeMsg(WrongMsg.GOFEN.getWrongMsg());
					return cell;
				}
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
						cell.setJudgeMsg(WrongMsg.ZF.getWrongMsg());
						return cell;
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
					cell.setJudgeMsg(WrongMsg.LY.getWrongMsg());
					return cell;// 错误命名
				}
			}			
			//冗余字段
			if (cellName.contains("拉远")) {
				cell.setIsRru("是");
			} else {
				cell.setIsRru("否");
			}

		} catch (Exception e) {
			cell.setJudgeMsg(WrongMsg.ERROR.getWrongMsg());
			logger.error(e.getMessage(),e);
			return cell;// 解析异常，不规则小区
		}

		return cell;
	}

	
	/**
	 * 验证规则BBU
	 * 
	 * @param bts
	 * @return
	 */
	public Enodeb ruleLteBbu(Enodeb bts, Map<String, LteBts> noIndoorMap,
			Map<String, Country> countryMap) {
		try {
			// 增加高铁标识,沿河思渠接入网_BBU1_GGH_电_电
			//纯BBU,乌当行政中心_BBU1_电_电
			//共站bbu，乌当行政中心_BBU3_共站
			String btsName = bts.getUserLabel();
			String specialName=btsName.substring(btsName.length()-2);
			boolean specialFlag=AnalyseUtil.isSpecical(specialName);
			bts.setSpecial(specialFlag);
			if(specialFlag){
				btsName=btsName.substring(0, btsName.length()-2);
			}
			String[] array = btsName.split("_");
			int length = array.length;
			if (!(length > 2 && length < 6)) {
				bts.setJudgeMsg(WrongMsg.MISS.getWrongMsg());
				return bts;
			}
			String name = array[0];
			// 名称的前三字符找不到所属地市的依旧为错误命名
			Country country = AnalyseUtil.getCountry(countryMap, name);
			if (country != null) {
				bts.setCityId(country.getCityId());
				bts.setCountryId(country.getId());
			} else {
				// 不符合规则命名
				bts.setJudgeMsg(WrongMsg.CITY.getWrongMsg());
				return bts;
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
			//判断第二位，array[1]规则，BBU+NUM
			String bbuFlag=array[1];
			if(!bbuFlag.contains("BBU")||bbuFlag.length()<4){
				bts.setJudgeMsg(WrongMsg.BBU_FLAG_ERROR.getWrongMsg());
				return bts;
			}
			if ("BBU1".equals(bbuFlag)) {
				if (btsName.contains("共站")) {
					// BBU1包含共站，物理站点中能找到相同名称站点
					LteBts btsSite = noIndoorMap.get(name);
					if (btsSite == null) {
						bts.setJudgeMsg(WrongMsg.BBU_BTS_NOTEXIST.getWrongMsg());
						return bts;
					} else {
						bts.setRelatedWyLteBtsId(btsSite.getIntId());
					}
				} else {
					// BBU1不含共站，包含机房产权和传输产权
					String circuitRoomOwnership = "";
					String transOwnership = "";
					if (highFlag) {
						circuitRoomOwnership = array[3];
						transOwnership = array[4];
					} else {
						circuitRoomOwnership = array[2];
						transOwnership = array[3];
					}
					if (!AnalyseUtil.isRight(circuitRoomOwnership)){
						bts.setJudgeMsg(WrongMsg.OWNER_RIGHT.getWrongMsg());
						return bts;
					}
					if (!AnalyseUtil.isRight(transOwnership)){
						bts.setJudgeMsg(WrongMsg.TRANS_RIGHT.getWrongMsg());
						return bts;
					}
					bts.setCircuitRoomOwnership(circuitRoomOwnership);
					bts.setTransOwnership(transOwnership);
				}
			} else {
				// 如果BBU2以后一定得有共站
				if (!array[2].equals("共站")) {
					bts.setJudgeMsg(WrongMsg.BBU_BHGZ.getWrongMsg());
					return bts;
				}
			}
		} catch (Exception e) {
			bts.setJudgeMsg(WrongMsg.ERROR.getWrongMsg());
			logger.error("" + e.getMessage(), e);
			return bts;// 解析异常
		}
		return bts;
	}

	

}


