package com.sctt.net.bts;

import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.sctt.net.bts.analyse.BtsAnalyse;
import com.sctt.net.bts.bean.Cell;
import com.sctt.net.bts.dao.BtsDao;
import com.sctt.net.common.util.DateHelper;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		 Resource resource = new FileSystemResource("config/appcontext.xml");
		 BeanFactory factory = new XmlBeanFactory(resource);
		 
		 BtsDao btsDao=(BtsDao)factory.getBean("btsDao");
		 
//		 List<Cell> cells=btsDao.getCells();
//		 System.out.println("++++++"+cells.size());
		 
//		 System.out.println("�����".hashCode());
//		 System.out.println("���".hashCode());
//		 
		 BtsAnalyse btsAnalyse=new BtsAnalyse(btsDao);
		 btsAnalyse.test();
		 
		 
		 String s="aaaa_�½�";
		 String[] arr=s.split("_");
		 for(String a:arr){
			 System.out.println("---"+a);
		 }
		 
//		 List<Country> list=btsDao.getCountrys();
//		 System.out.println("+++++++++"+list.size());
		 
		 
//		 System.out.println("+++"+"��������".contains("����"));
//		 
//		 System.out.println("++++"+DateHelper.getSecondByDay());
//		 
//		 
//		 System.out.println("�Ͻڵ���̨".substring(0, 2));

	}
	
	
}
