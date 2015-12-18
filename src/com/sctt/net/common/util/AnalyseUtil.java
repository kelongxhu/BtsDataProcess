package com.sctt.net.common.util;

import java.util.Map;

import com.sctt.net.bts.bean.cdma.Country;


public class AnalyseUtil {
	/**
	 * 判断小区名称是否有忽略小区
	 * 
	 * @param cellName
	 * @return
	 */
	public static boolean ignoreCell(String cellName) {
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
	
	/**
	 * 是否符合产权归属
	 * 
	 * @param str
	 * @return:true:符合规则
	 */
	public static boolean isRight(String str) {
		String[] rang = { "电", "联", "移", "电联", "电移","铁" };
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
	 * 是否满足直放站的标记
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
	 * 是否包含高铁标识
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
	 * 判断是否特殊站点
	 * @return
	 */
	public static boolean isSpecical(String s){
		if("_新".equals(s)||"_升".equals(s)||"_调".equals(s)){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否多网共站标识
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
	 * 计算基站产权标识的标记位
	 * 
	 * @param splitName
	 * @return
	 */

	public static int getCqFlag(String[] splitName, int startFlag) {
		// 系统号_BTS序号_小区序号_市/县/区+小区名_高铁信息标识_室分/隧道_拉远_基站产权标识_传输产权标识_维护等级_共站標識_直放站标识_小区功分信息标识
		int cqFlag = 0;
		if ("室分".equals(splitName[startFlag])
				|| "隧道".equals(splitName[startFlag])) {
			if ("拉远".equals(splitName[startFlag + 1])) {
				cqFlag = startFlag + 2;
			} else {
				cqFlag = startFlag + 1;
			}
		} else if ("拉远".equals(splitName[startFlag])) {
			cqFlag = startFlag + 1;
		} else {
			cqFlag = startFlag;
		}
		return cqFlag;
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
	public static Country getCountry(Map<String, Country> countryMap,
			String name) {
		String countryName = "";// 前两个字
		String countryName2 = "";// 前三个字
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
	 * 获取特殊状态标识
	 * @param s
	 * @return
	 */
	public static int getSpecicalState(String s) {
		int state = 0;
		if ("_新".equals(s)) {
			state = 1;
		} else if ("_调".equals(s)) {
			state = 2;
		} else if ("_升".equals(s)) {
			state = 3;
		}
		return state;
	}


}
