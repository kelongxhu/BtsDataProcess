package com.sctt.net.common.util;

public enum WrongMsg{
	MISS("������������ȱʧ���ߴ���"),
	OWNER_RIGHT("��վ��Ȩ��ʶ��������"),
	TRANS_RIGHT("�����Ȩ��ʶ��������"),
	RANK("ά���ȼ���������"),
	GOFEN("������Ϣ��ʶ��������"),
	ZF("ֱ��վ��ʶ��������"),
	LY("����Զ��վ����վ���Ʊ�������ѳ�����վ������һ��"),
	CITY("վ��������������������");
	private String wrongMsg;
	private WrongMsg(String wrongMsg){
		this.wrongMsg=wrongMsg;
	}
	
	public String getWrongMsg(){
	    return this.wrongMsg;
	}
	 
	
}
