package com.sctt.net.common.util;

public enum WrongMsg{
	MISS("������������ȱʧ���ߴ���"),
	OWNER_RIGHT("��վ��Ȩ��ʶ��������"),
	TRANS_RIGHT("�����Ȩ��ʶ��������"),
	RANK("ά���ȼ���������"),
	GOFEN("������Ϣ��ʶ��������"),
	ZF("ֱ��վ��ʶ��������"),
	LY("����Զ��վ����վ���Ʊ�������ѳ�����վ������һ��"),
	CITY("վ��������������������"),
	BBU_BTS_NOTEXIST("BBU1������վ��վ���в����ҵ���ͬ����վ��"),
	BBU_BHGZ("BBU2���ϱ��������վ��ʶ"),
	BBU_EXIST_GZ("BBU2�Ժ������ڹ�վ"),
	BBU_FLAG_ERROR("BBUλ��������"),
	ERROR("δ֪����");
	private String wrongMsg;
	private WrongMsg(String wrongMsg){
		this.wrongMsg=wrongMsg;
	}
	
	public String getWrongMsg(){
	    return this.wrongMsg;
	}
	 
	
}
