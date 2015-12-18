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
 * LTE ��վС������
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
		logger.info("LTEվ�������ʼ.....");
		try {
			LteBtsStat stat=new LteBtsStat();
			List<EutranCell> cells=lteDao.selectEutranCell();
			Map<String,Country> countryMap = btsDao.getCountrys();
			List<LteBts> dbLteBts=lteDao.selectLteBts();
			logger.info("��tco_pro_eutrancell_m�ɼ���С������:"+cells.size());
			logger.info("��wy_lte_bts�ɼ�������:"+dbLteBts.size());
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
			insertLteBts(lteBtsMap,dbLteBts);
			logger.info("������LTEվ����:"+lteBtsMap.size());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * ��������LTE��վ
	 * @param lteBtsMap
	 * @param dbLteBts
	 */
	public void insertLteBts(Map<String,LteBts> lteBtsMap,List<LteBts> dbLteBts){
		try {
			List<LteBts> lteBtsList = new ArrayList();
			lteBtsList.addAll(lteBtsMap.values());
			List<LteBts> temp = lteBtsList;
			//������
			lteBtsList.removeAll(dbLteBts);
			//���µ�
			temp.containsAll(dbLteBts);
			//ɾ����
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
			logger.info("wy_lte_bts�Ļ�վ��������:"+insertCount+",��������:"+updateCount+",ɾ������:"+deleteCount);
		} catch (Exception e) {
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
			// cqFlag:��վ��Ȩ��ʶ
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
			logger.info(cell.getUserLabel() + ":" + e.getMessage(), e);
			cell.setJudgeMsg(WrongMsg.MISS.getWrongMsg());
			return cell;// �����쳣��������С��
		}

		return cell;
	}


	

}


