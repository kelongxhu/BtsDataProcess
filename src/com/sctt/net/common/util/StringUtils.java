package com.sctt.net.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	// �ж��ַ�����ʽ�Ƿ��� ���֣�����С����
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[\\d]+[.]?[\\d]+");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	/**
	 * ����ת��Ϊ�ַ���
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
	 * �ַ���ת��Ϊ����
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
	 * �ַ���ת��ΪLong��
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
	 * ����ת��ΪDouble��
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
