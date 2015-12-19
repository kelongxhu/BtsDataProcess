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

	int updateBtsSite(BtsSite btsSite) throws Exception;// 更新物理站点信息

	int updateBtsSiteByDeleteFlag(BtsSite btsSite) throws Exception;// 置为废弃标识

	int insertWyWrongName(WyWrongName wwn) throws Exception;// 插入小区命名错误表

	int updateWyWrongName(WyWrongName wwn) throws Exception;// 更新小区命名错误表

	int updateWyWrongNameByDelFlag(WyWrongName wwn) throws Exception;// 将小区命名错误置为废弃

	Map<String, WyWrongName> getWwns(int netType) throws Exception; // 获取错误小区命名小区

	Map<String,Country> getCountrys() throws Exception;// 获取区县

	Map<String, Cell> getWyCell() throws Exception;// 获取wy_cell小区数据

	int insertCell(Cell cell) throws Exception;// 插入wy_cell小区数据

	int updateCell(Cell cell);// 更新wy_cell字段数据

	int updateCellByDelFlag(Cell cell) throws Exception;// 将小区数据置为废弃

	List<Bts> getBbus() throws Exception;// 查询BBU数据

	Map<String, WyBbu> getWyBbu() throws Exception;// 获取wy_bbu数据

	int insertWyBbu(WyBbu wybbu) throws Exception;// 插入wy_bbu数据

	int updateWyBbu(WyBbu wybbu) throws Exception;// 更新wy_bbu字段

	int updateWyBbuByDelFlag(WyBbu wybbu) throws Exception;// 将bbu数据置为废弃
	
	Map<String,TunelSite> getTunels()throws Exception;//获取wy_tunel数据
	
	int insertWyTunel(TunelSite tunelSite)throws Exception;//插入wy_tunel数据
	
	int updateWyTunel(TunelSite tunelSite)throws Exception;//跟新wy_tunel字段
	
	int updateWyTunelByDelFlag(TunelSite tunelSite)throws Exception;//讲tunel数据置为废弃
	
	Map<String,TunelLib> getTunelLib()throws Exception;//获取wy_tunel_lib
	
	int insertTunelLib(TunelLib tunelLib)throws Exception;//插入隧道库
	
	int updateTunelLib(TunelLib tunelLib)throws Exception;//隧道库
	
	int updateTunelLibByDelFlag(TunelLib tunelLib)throws Exception;//删除隧道库
	
	int insertWyBtsSpecial(WyBtsSpecial wyBtsSpecial)throws Exception;//增加特殊站点表
	
	int updateWyBtsSpecial(WyBtsSpecial wyBtsSpecial)throws Exception;//编辑特殊站点表
	
	int updateWyBtsSpecialByDelFlag(WyBtsSpecial wyBtsSpecial)throws Exception;//删除特殊站点表
	
	Map<String,WyBtsSpecial> getBtsSpecial(int netType)throws Exception;//获取特殊站点
	

}
