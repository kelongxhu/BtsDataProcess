package com.sctt.net.bts.main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.sctt.net.bts.analyse.BtsAnalyse;
import com.sctt.net.bts.analyse.InitInstance;
import com.sctt.net.bts.dao.BtsDao;
import com.sctt.net.common.util.DateHelper;

public class Main {

	private final static Log log = LogFactory.getLog("baseLog");

	private static BtsDao btsDao = null;

	public static void init() {
		try {
			log.info("++++��ʼ��ʼ������");
			Resource resource = new FileSystemResource("config/appcontext.xml");
			BeanFactory factory = new XmlBeanFactory(resource);
			btsDao = (BtsDao) factory.getBean("btsDao");
			InitInstance.getInstance().initCityMap();
			log.info("++++��ɳ�ʼ������");
		} catch (BeansException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		init() ;
		
		ScheduledExecutorService execService = Executors
				.newScheduledThreadPool(10);

		BtsAnalyse btsAnalyse = new BtsAnalyse(btsDao);

		// ÿ������12��ִ��һ��

		long seconds = DateHelper.getSecondByDay();// ��ǰʱ�䵽0�������
		
		//long seconds=0;

		log.info("++++����ִ�н�������վ��,�״��ӳ�ʱ������:" + seconds);

		execService
				.scheduleAtFixedRate(btsAnalyse, seconds, 86400, TimeUnit.SECONDS);

		// Thread thread=new Thread(btsAnalyse);
		// thread.start();

	}

}
