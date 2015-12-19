package com.sctt.net.bts.service;

import java.util.Map;

import com.sctt.net.bts.bean.cdma.WyBtsSpecial;
import com.sctt.net.bts.bean.cdma.WyWrongName;

public interface BizService {
	/**
	 * 错误命名增量更新
	 * @param wrongMap
	 * @param yesWwnMap
	 */
	void wwnUpdate(Map<String, WyWrongName> wrongMap,
			Map<String, WyWrongName> yesWwnMap);
	/**
	 * 特殊站點增量更新
	 * @param specialMap
	 * @param yesSpecialMap
	 */
	void specialBtsUpdate(Map<String, WyBtsSpecial> specialMap,
			Map<String, WyBtsSpecial> yesSpecialMap);
}
