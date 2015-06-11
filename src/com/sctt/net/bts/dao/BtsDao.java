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

	int updateBtsSite(BtsSite btsSite) throws Exception;// 更新物理站点信息

	int updateBtsSiteByDeleteFlag(BtsSite btsSite) throws Exception;// 置为废弃标识

	int insertWyWrongName(WyWrongName wwn) throws Exception;// 插入小区命名错误表

	int updateWyWrongName(WyWrongName wwn) throws Exception;// 更新小区命名错误表

	int updateWyWrongNameByDelFlag(WyWrongName wwn) throws Exception;// 将小区命名错误置为废弃

	Map<String, WyWrongName> getWwns() throws Exception; // 获取错误小区命名小区

	Map<String,Country> getCountrys() throws Exception;// 获取区县

	Map<String, Cell> getWyCell() throws Exception;// 获取wy_cell小区数据

	int insertCell(Cell cell) throws Exception;// 插入wy_cell小区数据

	int updateCell(Cell cell) throws Exception;// 更新wy_cell字段数据

	int updateCellByDelFlag(Cell cell) throws Exception;// 将小区数据置为废弃

	List<Bts> getBbus() throws Exception;// 查询BBU数据

	Map<String, WyBbu> getWyBbu() throws Exception;// 获取wy_bbu数据

	int insertWyBbu(WyBbu wybbu) throws Exception;// 插入wy_bbu数据

	int updateWyBbu(WyBbu wybbu) throws Exception;// 更新wy_bbu字段

	int updateWyBbuByDelFlag(WyBbu wybbu) throws Exception;// 将bbu数据置为废弃

}
