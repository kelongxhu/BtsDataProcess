package com.sctt.net.bts.main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.sctt.net.bts.analyse.cdma.BtsAnalyse;
import com.sctt.net.bts.analyse.cdma.InitInstance;
import com.sctt.net.bts.analyse.lte.LteBtsAnalyse;
import com.sctt.net.bts.dao.BtsDao;
import com.sctt.net.bts.dao.LteDao;
import com.sctt.net.bts.service.BizService;
import com.sctt.net.common.util.BaseConstants;
import com.sctt.net.common.util.DateHelper;

public class Main {

	private final static Log log = LogFactory.getLog("baseLog");

	private static BtsDao btsDao = null;
	
	private static LteDao lteDao=null;
	
	private static BizService bizSerivce=null;

	private static BaseConstants baseConstants;

	public static void init() {
		try {
			log.info("++++开始初始化程序");
			Resource resource = new FileSystemResource("config/appcontext.xml");
			BeanFactory factory = new XmlBeanFactory(resource);
			btsDao = (BtsDao) factory.getBean("btsDao");
			lteDao=(LteDao)factory.getBean("lteDao");
			bizSerivce=(BizService)factory.getBean("bizService");
			baseConstants = (BaseConstants) factory.getBean("baseConstants");
			InitInstance.getInstance().initCityMap();
			InitInstance.getInstance().initLteCityMap();
			log.info("++++完成初始化程序");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		init();

		ScheduledExecutorService execService = Executors
				.newScheduledThreadPool(10);

		BtsAnalyse btsAnalyse = new BtsAnalyse(btsDao,bizSerivce);

		LteBtsAnalyse lteAnalyse=new LteBtsAnalyse(btsDao,lteDao,bizSerivce);
		// 每天晚上12点执行一次
		int flag = baseConstants.getFlag();

		long seconds = 0;

		if (flag == 1) {
			seconds = DateHelper.getSecondByDay();// 当前时间到0点的秒数
		}

		log.info("++++按天执行解析物理站点,首次延迟时间秒数:" + seconds);

		execService.scheduleAtFixedRate(btsAnalyse, seconds, 86400,
				TimeUnit.SECONDS);

        execService.scheduleAtFixedRate(lteAnalyse, seconds, 86400, TimeUnit.SECONDS);

	}

}
