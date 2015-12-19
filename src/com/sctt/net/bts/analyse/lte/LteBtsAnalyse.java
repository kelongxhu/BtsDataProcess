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
 * LTE ��վС������
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
		logger.info("LTEվ�������ʼ.....");
		try {
			LteBtsStat stat=new LteBtsStat();
			List<EutranCell> cells=lteDao.selectEutranCell();
			List<Enodeb> bbus=lteDao.selectEnodeb();//bbu�б�
			Map<String,Country> countryMap = btsDao.getCountrys();
			Map<String,LteBts> yesLteBtsMap=lteDao.selectLteBts();
			Map<String,EutranCell> yesLteCellMap=lteDao.selectLteCell();
			Map<String, LteBts> noIndoorMap = null;// ���ҷ�Map,bbu���ҹ�վ����վ����
			Map<String,LteBbu> yesBbuMap=lteDao.selectLteBbu();//wy_lte_bbu����
			Map<String,WyWrongName> yesWwnMap = btsDao.getWwns(Constants.LTE);
			Map<String, WyBtsSpecial> btsSpecialMap=btsDao.getBtsSpecial(Constants.LTE);
			logger.info("��tco_pro_eutrancell_m�ɼ���С������:"+cells.size());
			logger.info("��tco_pro_enodeb_m�ɼ���bbu��:"+bbus.size());
			logger.info("��wy_lte_bts�ɼ�������:"+yesLteBtsMap.size());
			logger.info("��wy_lte_cell�ɼ�������:"+yesLteCellMap.size());
			logger.info("��wy_lte_bbu�ɼ���������:"+yesBbuMap.size());
			logger.info("��wy_wrongName�ɼ���LTE��������:"+yesWwnMap.size());
			logger.info("��wy_speacail�ɼ���LTE����վ������:"+btsSpecialMap.size());
			//����
			for(EutranCell cell:cells){
				if (AnalyseUtil.ignoreCell(cell.getUserLabel())) {
					continue;// ����С��
				}
				EutranCell judgeCell=judgeCell(cell,countryMap);
				String judgeMsg=judgeCell.getJudgeMsg();
				if(StringUtils.isEmpty(judgeMsg)){
					stat.btsStat(judgeCell);
					//��ӵ�����վ��ͳ��
					boolean isSpeacial=judgeCell.isSpecial();
					if(isSpeacial){
						stat.addSpecialCell(judgeCell);
					}
				}else{
					//��������С��
					stat.addWrongCell(cell);
				}
			}
			//�������
			Map<String,LteBts> lteBtsMap=stat.getLteBtsMap();
			Map<String,EutranCell> cellMap=stat.getCellMap();
			insertLteBts(lteBtsMap,yesLteBtsMap);
			incrUpdateCell(cellMap,yesLteCellMap);
			logger.info("������LTEվ����:"+lteBtsMap.size());
			logger.info("������LTEС����:"+cellMap.size());
			logger.info("����LTE��BBU��ʼ...");
			LteBbuStat bbuStat=new LteBbuStat();
			noIndoorMap=stat.getNoIndoorMap();
			for (Enodeb bbu : bbus) {
				Enodeb judgeBbu = ruleLteBbu(bbu, noIndoorMap, countryMap);
				String bbuJudgeMsg=judgeBbu.getJudgeMsg();
				if (StringUtils.isEmpty(bbuJudgeMsg)) {
					bbuStat.bbuStat(judgeBbu);
					//ͳ������վ��
					boolean specialFlag=judgeBbu.isSpecial();
					if(specialFlag){
						stat.addSpecialBts(judgeBbu);
					}
				} else {
					// ��������BBU
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
			logger.info("����LTE��BBU��:"+bbuMap.size());
			logger.info("����LTE��BBU����...");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * ��������LTE��վ
	 * @param lteBtsMap
	 * @param dbLteBts
	 */
	public void insertLteBts(Map<String,LteBts> lteBtsMap,Map<String,LteBts> yesBtsMap){
		int i = 0;
		int j = 0;
		int q = 0;
		try {
			logger.info("++++С������������...");
			for (String keyObj : lteBtsMap.keySet()) {
				LteBts bts = yesBtsMap.get(keyObj);
				LteBts updateBts = lteBtsMap.get(keyObj);
				if (bts != null) {
					// ����
					i+= lteDao.updateLteBts(updateBts);
				} else {
					// ����
					j+=lteDao.insertLteBts(updateBts);
				}
			}

			for (String keyObj : yesBtsMap.keySet()) {
				LteBts bts = lteBtsMap.get(keyObj);
				LteBts updateBts = yesBtsMap.get(keyObj);
				if (bts == null) {
					// ����
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
		logger.info("wy_lte_bts��վ������������£�" + i + ";������" + j + ";������" + q);
	}
	
	public void incrUpdateCell(Map<String, EutranCell> cellMap,
			Map<String, EutranCell> yesCellMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		try {
			logger.info("++++С������������...");
			for (String keyObj : cellMap.keySet()) {
				EutranCell cell = yesCellMap.get(keyObj);
				EutranCell updateCell = cellMap.get(keyObj);
				if (cell != null) {
					// ����
					i+=lteDao.updateLteCell(updateCell);
				} else {
					// ����
					j+=lteDao.insertLteCell(updateCell);
				}
			}

			for (String keyObj : yesCellMap.keySet()) {
				EutranCell cell = cellMap.get(keyObj);
				EutranCell updateCell = yesCellMap.get(keyObj);
				if (cell == null) {
					// ����
					int deleteFlag = updateCell.getDeleteFlag();
					if (deleteFlag == 0) {
						q+=lteDao.updateLteCellByDeleteFlag(updateCell);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("wy_lte_cellС��������������£�" + i + ";������" + j + ";������" + q);
	}
	
	public void incrUpdateBbu(Map<String, LteBbu> bbuMap,
			Map<String, LteBbu> yesBbuMap) {
		int i = 0;
		int j = 0;
		int q = 0;
		try {
			logger.info("++++Lte BBU����������...");
			for (String keyObj : bbuMap.keySet()) {
				LteBbu bbu = yesBbuMap.get(keyObj);
				LteBbu updateBbu = bbuMap.get(keyObj);
				if (bbu != null) {
					// ����
					i+=lteDao.updateLteBbu(updateBbu);
				} else {
					// ����
					j+=lteDao.insertLteBbu(updateBbu);
				}
			}

			for (String keyObj : yesBbuMap.keySet()) {
				LteBbu bbu = bbuMap.get(keyObj);
				LteBbu updateBbu = yesBbuMap.get(keyObj);
				if (bbu == null) {
					// ����
					int deleteFlag = updateBbu.getDeleteFlag();
					if (deleteFlag == 0) {
						q+=lteDao.updateLteBbuByDeleteFlag(updateBbu);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("wy_lte_bbu��������������£�" + i + ";������" + j + ";������" + q);
	}
	/**
	 * С�����������ж�
	 * 
	 * @param cell
	 *            ��С������
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
			//ϵͳ��_BTS���_С�����_��/��/��+С����_������Ϣ��ʶ_�ҷ�/���_��Զ_��վ��Ȩ��ʶ_�����Ȩ��ʶ_ά���ȼ�_������վ��ʶ_ֱ��վ��ʶ_С��������Ϣ��ʶ��
			// �غ��ֶΣ�ϵͳ��_BTS���_С�����_��/��/��+С����_��վ��Ȩ��ʶ_�����Ȩ��ʶ_ά���ȼ�
			// �ɺ��ֶΣ�������Ϣ��ʶ_�ҷ�_��Զ_������վ��ʶ_ֱ��վ��ʶ_С��������Ϣ��ʶ��
			if (cellLength < 8) {
				//ȱʧ�ֶ�
				cell.setJudgeMsg(WrongMsg.MISS.getWrongMsg());
				return cell;
			}	
			String name = splitName[3];//վ������
			//�����Ƿ������ñ���
			Country country = AnalyseUtil.getCountry(countryMap, name);
			if (country != null) {
				cell.setCityId(country.getCityId());
				cell.setCountryId(country.getId());
			} else {
				// δ�ҵ���Ӧ����������Ϊ����С������
				cell.setJudgeMsg(WrongMsg.CITY.getWrongMsg());
				return cell;
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
			if(tunelStr.equals("�ҷ�")){
				cell.setIsIndoor("��");
			}else if(tunelStr.equals("���")){
				cell.setIsIndoor("��");
			}else{
				cell.setIsIndoor("��");
			}
			// cqFlag:��վ��Ȩ��ʶ, �����Ȩ��ά���ȼ���ѡ��
			if(cqFlag+2<cellLength){
				//ȱʧ�ֶ�
				cell.setJudgeMsg(WrongMsg.MISS.getWrongMsg());
				return cell;
			}
			
			if (!AnalyseUtil.isRight(splitName[cqFlag])) {
				cell.setJudgeMsg(WrongMsg.OWNER_RIGHT.getWrongMsg());
				return cell;
			} else {
				cell.setCircuitRoomOwnership(splitName[cqFlag]);
			}

			// cqFlag+1:�����Ȩ��ʶ
			if (!AnalyseUtil.isRight(splitName[cqFlag + 1])) {
				cell.setJudgeMsg(WrongMsg.TRANS_RIGHT.getWrongMsg());
				return cell;
			} else {
				cell.setTransOwnership(splitName[cqFlag + 1]);
			}

			// cqFlag+2:ά���ȼ�
			if (!AnalyseUtil.isRank(splitName[cqFlag + 2])) {
				cell.setJudgeMsg(WrongMsg.RANK.getWrongMsg());
				return cell;
			} else {
				cell.setServiceLevel(splitName[cqFlag + 2]);
			}
			//������վ��ʶ
			int togetherFlag=cqFlag+3;
			int zfStart=togetherFlag;// ֱ��վ��ʼ��ʶ
			if(cellLength>togetherFlag){
				String togetherStr=splitName[togetherFlag];
				if(AnalyseUtil.isTogether(togetherStr)){
					cell.setSiteTogether(togetherStr);
					zfStart=togetherFlag+1;//ֱ��վ��ʼ��ʶ
				}			
			}
			int zfEnd = 0;// ֱ��վ������ʶ
			// �Ƿ�������֣��ǣ��Ƿ����һ���ֶ�
			if (cellName.contains("����")) {
				String gfValue = splitName[cellLength - 1];
				if (!gfValue.contains("����")) {
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
			// ֱ��վ��֤
			if (zfEnd > zfStart) {
				for (int i = 0; i < zfEnd - zfStart; i++) {
					String zfValue = splitName[zfStart + i];
					if (!AnalyseUtil.isZF(zfValue)) {
						cell.setJudgeMsg(WrongMsg.ZF.getWrongMsg());
						return cell;
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
					cell.setJudgeMsg(WrongMsg.LY.getWrongMsg());
					return cell;// ��������
				}
			}			
			//�����ֶ�
			if (cellName.contains("��Զ")) {
				cell.setIsRru("��");
			} else {
				cell.setIsRru("��");
			}

		} catch (Exception e) {
			cell.setJudgeMsg(WrongMsg.ERROR.getWrongMsg());
			logger.error(e.getMessage(),e);
			return cell;// �����쳣��������С��
		}

		return cell;
	}

	
	/**
	 * ��֤����BBU
	 * 
	 * @param bts
	 * @return
	 */
	public Enodeb ruleLteBbu(Enodeb bts, Map<String, LteBts> noIndoorMap,
			Map<String, Country> countryMap) {
		try {
			// ���Ӹ�����ʶ,�غ�˼��������_BBU1_GGH_��_��
			//��BBU,�ڵ���������_BBU1_��_��
			//��վbbu���ڵ���������_BBU3_��վ
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
			// ���Ƶ�ǰ���ַ��Ҳ����������е�����Ϊ��������
			Country country = AnalyseUtil.getCountry(countryMap, name);
			if (country != null) {
				bts.setCityId(country.getCityId());
				bts.setCountryId(country.getId());
			} else {
				// �����Ϲ�������
				bts.setJudgeMsg(WrongMsg.CITY.getWrongMsg());
				return bts;
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
			//�жϵڶ�λ��array[1]����BBU+NUM
			String bbuFlag=array[1];
			if(!bbuFlag.contains("BBU")||bbuFlag.length()<4){
				bts.setJudgeMsg(WrongMsg.BBU_FLAG_ERROR.getWrongMsg());
				return bts;
			}
			if ("BBU1".equals(bbuFlag)) {
				if (btsName.contains("��վ")) {
					// BBU1������վ������վ�������ҵ���ͬ����վ��
					LteBts btsSite = noIndoorMap.get(name);
					if (btsSite == null) {
						bts.setJudgeMsg(WrongMsg.BBU_BTS_NOTEXIST.getWrongMsg());
						return bts;
					} else {
						bts.setRelatedWyLteBtsId(btsSite.getIntId());
					}
				} else {
					// BBU1������վ������������Ȩ�ʹ����Ȩ
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
				// ���BBU2�Ժ�һ�����й�վ
				if (!array[2].equals("��վ")) {
					bts.setJudgeMsg(WrongMsg.BBU_BHGZ.getWrongMsg());
					return bts;
				}
			}
		} catch (Exception e) {
			bts.setJudgeMsg(WrongMsg.ERROR.getWrongMsg());
			logger.error("" + e.getMessage(), e);
			return bts;// �����쳣
		}
		return bts;
	}

	

}


