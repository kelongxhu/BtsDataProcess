package com.sctt.net.bts.dao;

import java.util.List;
import java.util.Map;

import com.sctt.net.bts.bean.cdma.Bts;
import com.sctt.net.bts.bean.cdma.BtsSite;
import com.sctt.net.bts.bean.cdma.Cell;
import com.sctt.net.bts.bean.cdma.Country;
import com.sctt.net.bts.bean.cdma.TunelLib;
import com.sctt.net.bts.bean.cdma.TunelSite;
import com.sctt.net.bts.bean.cdma.WyBbu;
import com.sctt.net.bts.bean.cdma.WyBtsSpecial;
import com.sctt.net.bts.bean.cdma.WyWrongName;

public interface BtsDao {
	List<Cell> getCells() throws Exception;

	int insertBtsSite(BtsSite btsSite) throws Exception;

	Map<String, BtsSite> getBtsSites() throws Exception;

	int updateBtsSite(BtsSite btsSite) throws Exception;// ��������վ����Ϣ

	int updateBtsSiteByDeleteFlag(BtsSite btsSite) throws Exception;// ��Ϊ������ʶ

	int insertWyWrongName(WyWrongName wwn) throws Exception;// ����С�����������

	int updateWyWrongName(WyWrongName wwn) throws Exception;// ����С�����������

	int updateWyWrongNameByDelFlag(WyWrongName wwn) throws Exception;// ��С������������Ϊ����

	Map<String, WyWrongName> getWwns(int netType) throws Exception; // ��ȡ����С������С��

	Map<String,Country> getCountrys() throws Exception;// ��ȡ����

	Map<String, Cell> getWyCell() throws Exception;// ��ȡwy_cellС������

	int insertCell(Cell cell) throws Exception;// ����wy_cellС������

	int updateCell(Cell cell);// ����wy_cell�ֶ�����

	int updateCellByDelFlag(Cell cell) throws Exception;// ��С��������Ϊ����

	List<Bts> getBbus() throws Exception;// ��ѯBBU����

	Map<String, WyBbu> getWyBbu() throws Exception;// ��ȡwy_bbu����

	int insertWyBbu(WyBbu wybbu) throws Exception;// ����wy_bbu����

	int updateWyBbu(WyBbu wybbu) throws Exception;// ����wy_bbu�ֶ�

	int updateWyBbuByDelFlag(WyBbu wybbu) throws Exception;// ��bbu������Ϊ����
	
	Map<String,TunelSite> getTunels()throws Exception;//��ȡwy_tunel����
	
	int insertWyTunel(TunelSite tunelSite)throws Exception;//����wy_tunel����
	
	int updateWyTunel(TunelSite tunelSite)throws Exception;//����wy_tunel�ֶ�
	
	int updateWyTunelByDelFlag(TunelSite tunelSite)throws Exception;//��tunel������Ϊ����
	
	Map<String,TunelLib> getTunelLib()throws Exception;//��ȡwy_tunel_lib
	
	int insertTunelLib(TunelLib tunelLib)throws Exception;//���������
	
	int updateTunelLib(TunelLib tunelLib)throws Exception;//�����
	
	int updateTunelLibByDelFlag(TunelLib tunelLib)throws Exception;//ɾ�������
	
	int insertWyBtsSpecial(WyBtsSpecial wyBtsSpecial)throws Exception;//��������վ���
	
	int updateWyBtsSpecial(WyBtsSpecial wyBtsSpecial)throws Exception;//�༭����վ���
	
	int updateWyBtsSpecialByDelFlag(WyBtsSpecial wyBtsSpecial)throws Exception;//ɾ������վ���
	
	Map<String,WyBtsSpecial> getBtsSpecial(int netType)throws Exception;//��ȡ����վ��
	

}
