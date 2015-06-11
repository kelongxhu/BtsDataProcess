package com.sctt.net.common.util;

import java.util.Calendar;
import java.util.Date;

public class DateHelper {
	/**
	 * 获取第二天9点时间
	 * 
	 * @return：秒数
	 */
	public static long getSecondByDay() {
		Calendar calendar = Calendar.getInstance();
		Date start = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date end = calendar.getTime();
		System.out.println("++++"+end);
		long s = (end.getTime() - start.getTime()) / 1000;
		return s;
	}
}
