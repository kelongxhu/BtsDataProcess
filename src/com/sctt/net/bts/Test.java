package com.sctt.net.bts;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.sctt.net.bts.dao.BtsDao;

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
		 
//		 System.out.println("你好吗".hashCode());
//		 System.out.println("你好".hashCode());
//		 
//		 BtsAnalyse btsAnalyse=new BtsAnalyse(btsDao);
//		 btsAnalyse.test2();
//		 btsAnalyse.test();
		 
		 
//		 String s="aaaa_新建";
//		 String[] arr=s.split("_");
//		 for(String a:arr){
//			 System.out.println("---"+a);
//		 }
//		 
//		 String name="aaa隧道去bb";
//		 System.out.print(name.substring(0,name.indexOf("隧道去")));
//		 System.out.print(name.substring(name.indexOf("隧道去")+3));
		 
//		 List<Country> list=btsDao.getCountrys();
//		 System.out.println("+++++++++"+list.size());
		 
		 
//		 System.out.println("+++"+"安龙龙堡".contains("兴义"));
//		 
//		 System.out.println("++++"+DateHelper.getSecondByDay());
//		 
//		 
//		 System.out.println("毕节电视台".substring(0, 2));

	}
	
	
}
