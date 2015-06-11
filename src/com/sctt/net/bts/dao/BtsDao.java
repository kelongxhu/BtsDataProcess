package com.sctt.net.bts.dao;

import java.util.List;
import java.util.Map;

import com.sctt.net.bts.bean.Bts;
import com.sctt.net.bts.bean.BtsSite;
import com.sctt.net.bts.bean.Cell;
import com.sctt.net.bts.bean.Country;
import com.sctt.net.bts.bean.WyBbu;
import com.sctt.net.bts.bean.WyWrongName;

public interface BtsDao {
	List<Cell> getCells() throws Exception;

	int insertBtsSite(BtsSite btsSite) throws Exception;

	Map<String, BtsSite> getBtsSites() throws Exception;

	int updateBtsSite(BtsSite btsSite) throws Exception;// ��������վ����Ϣ

	int updateBtsSiteByDeleteFlag(BtsSite btsSite) throws Exception;// ��Ϊ������ʶ

	int insertWyWrongName(WyWrongName wwn) throws Exception;// ����С�����������

	int updateWyWrongName(WyWrongName wwn) throws Exception;// ����С�����������

	int updateWyWrongNameByDelFlag(WyWrongName wwn) throws Exception;// ��С������������Ϊ����

	Map<String, WyWrongName> getWwns() throws Exception; // ��ȡ����С������С��

	Map<String,Country> getCountrys() throws Exception;// ��ȡ����

	Map<String, Cell> getWyCell() throws Exception;// ��ȡwy_cellС������

	int insertCell(Cell cell) throws Exception;// ����wy_cellС������

	int updateCell(Cell cell) throws Exception;// ����wy_cell�ֶ�����

	int updateCellByDelFlag(Cell cell) throws Exception;// ��С��������Ϊ����

	List<Bts> getBbus() throws Exception;// ��ѯBBU����

	Map<String, WyBbu> getWyBbu() throws Exception;// ��ȡwy_bbu����

	int insertWyBbu(WyBbu wybbu) throws Exception;// ����wy_bbu����

	int updateWyBbu(WyBbu wybbu) throws Exception;// ����wy_bbu�ֶ�

	int updateWyBbuByDelFlag(WyBbu wybbu) throws Exception;// ��bbu������Ϊ����

}
