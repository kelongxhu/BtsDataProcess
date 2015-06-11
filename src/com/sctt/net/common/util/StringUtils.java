package com.sctt.net.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	// 判断字符串格式是否是 数字（包括小数）
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[\\d]+[.]?[\\d]+");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	/**
	 * 对象转换为字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String objToString(Object obj) {
		try {
			return String.valueOf(obj);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 字符串转换为整型
	 * 
	 * @param obj
	 * @return
	 */
	public static int objToInt(Object obj) {
		try {
			return Integer.parseInt(String.valueOf(obj));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 字符串转换为Long型
	 * 
	 * @param obj
	 * @return
	 */
	public static long objToLong(Object obj) {
		try {
			return Long.parseLong(String.valueOf(obj));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 对象转换为Double型
	 * 
	 * @param obj
	 * @return
	 */
	public static double objToDouble(Object obj) {
		try {
			return Double.parseDouble(String.valueOf(obj));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

}
