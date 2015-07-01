package com.sctt.net.bts.test;

import java.util.ArrayList;
import java.util.List;

public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List a=new ArrayList();
		a.add(1);
		List b=new ArrayList();
		b.add(2);
		a.addAll(b);
		for(int i=0;i<a.size();i++){
			System.out.println(a.get(i));
		}
		

	}

}
