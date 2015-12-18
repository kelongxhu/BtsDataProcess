package com.sctt.net.bts.analyse.lte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sctt.net.bts.bean.cdma.Country;
import com.sctt.net.bts.bean.lte.EutranCell;
import com.sctt.net.bts.bean.lte.LteBts;
import com.sctt.net.bts.dao.BtsDao;
import com.sctt.net.bts.dao.LteDao;
import com.sctt.net.common.util.AnalyseUtil;
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
	
	public LteBtsAnalyse(BtsDao btsDao,LteDao lteDao){
		this.btsDao=btsDao;
		this.lteDao=lteDao;
	}
	public void run() {
		logger.info("LTE站点分析开始.....");
		try {
			LteBtsStat stat=new LteBtsStat();
			List<EutranCell> cells=lteDao.selectEutranCell();
			Map<String,Country> countryMap = btsDao.getCountrys();
			List<LteBts> dbLteBts=lteDao.selectLteBts();
			logger.info("从tco_pro_eutrancell_m采集到小区数据:"+cells.size());
			logger.info("從wy_lte_bts采集到数据:"+dbLteBts.size());
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
			insertLteBts(lteBtsMap,dbLteBts);
			logger.info("解析到LTE站点数:"+lteBtsMap.size());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 增量更新LTE基站
	 * @param lteBtsMap
	 * @param dbLteBts
	 */
	public void insertLteBts(Map<String,LteBts> lteBtsMap,List<LteBts> dbLteBts){
		try {
			List<LteBts> lteBtsList = new ArrayList();
			lteBtsList.addAll(lteBtsMap.values());
			List<LteBts> temp = lteBtsList;
			//新增的
			lteBtsList.removeAll(dbLteBts);
			//更新的
			temp.containsAll(dbLteBts);
			//删除的
			dbLteBts.removeAll(lteBtsMap.values());
			List<LteBts> deleteList = new ArrayList();
			for (LteBts bts : dbLteBts) {
				if (bts.getDeleteFlag() == 0) {
					deleteList.add(bts);
				}
			}
			List<LteBts> lte=new ArrayList();
			lte.add(lteBtsList.get(1));
			int insertCount=lteDao.insertLteBts(lte);
			int updateCount=lteDao.updateLteBts(temp);
			int deleteCount=lteDao.deleteLteBts(deleteList);
			logger.info("wy_lte_bts的基站数据新增:"+insertCount+",更新数量:"+updateCount+",删除数量:"+deleteCount);
		} catch (Exception e) {
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
			// cqFlag:基站产权标识
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
			logger.info(cell.getUserLabel() + ":" + e.getMessage(), e);
			cell.setJudgeMsg(WrongMsg.MISS.getWrongMsg());
			return cell;// 解析异常，不规则小区
		}

		return cell;
	}


	

}


