package com.sctt.net.common.util;

import java.util.Map;

import com.sctt.net.bts.bean.cdma.Country;


public class AnalyseUtil {
	/**
	 * �ж�С�������Ƿ��к���С��
	 * 
	 * @param cellName
	 * @return
	 */
	public static boolean ignoreCell(String cellName) {
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
	
	/**
	 * �Ƿ���ϲ�Ȩ����
	 * 
	 * @param str
	 * @return:true:���Ϲ���
	 */
	public static boolean isRight(String str) {
		String[] rang = { "��", "��", "��", "����", "����","��" };
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
	public static boolean isRank(String str) {
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
	public static boolean isZF(String str) {
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
	 * �Ƿ����������ʶ
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isHighBts(String str) {
		String[] rang = Constants.HIGH_BTS_FLAG;
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
	 * �ж��Ƿ�����վ��
	 * @return
	 */
	public static boolean isSpecical(String s){
		if("_��".equals(s)||"_��".equals(s)||"_��".equals(s)){
			return true;
		}
		return false;
	}
	
	/**
	 * �Ƿ������վ��ʶ
	 * @param str
	 * @return
	 */
	public static boolean isTogether(String str) {
		String[] togegerFlag = Constants.TOGETHER_BTS;
		boolean flag = false;
		for (String ig : togegerFlag) {
			if (str.equals(ig)) {
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

	public static int getCqFlag(String[] splitName, int startFlag) {
		// ϵͳ��_BTS���_С�����_��/��/��+С����_������Ϣ��ʶ_�ҷ�/���_��Զ_��վ��Ȩ��ʶ_�����Ȩ��ʶ_ά���ȼ�_��վ���R_ֱ��վ��ʶ_С��������Ϣ��ʶ
		int cqFlag = 0;
		if ("�ҷ�".equals(splitName[startFlag])
				|| "���".equals(splitName[startFlag])) {
			if ("��Զ".equals(splitName[startFlag + 1])) {
				cqFlag = startFlag + 2;
			} else {
				cqFlag = startFlag + 1;
			}
		} else if ("��Զ".equals(splitName[startFlag])) {
			cqFlag = startFlag + 1;
		} else {
			cqFlag = startFlag;
		}
		return cqFlag;
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
	public static Country getCountry(Map<String, Country> countryMap,
			String name) {
		String countryName = "";// ǰ������
		String countryName2 = "";// ǰ������
		if (name != null && name.length() > 2) {
			countryName = name.substring(0, 2);
			countryName2 = name.substring(0, 3);
		}
		Country returnCountry = countryMap.get(countryName);
		if (returnCountry == null) {
			returnCountry = countryMap.get(countryName2);
		}
		return returnCountry;
	}
	/**
	 * ��ȡ����״̬��ʶ
	 * @param s
	 * @return
	 */
	public static int getSpecicalState(String s) {
		int state = 0;
		if ("_��".equals(s)) {
			state = 1;
		} else if ("_��".equals(s)) {
			state = 2;
		} else if ("_��".equals(s)) {
			state = 3;
		}
		return state;
	}


}
