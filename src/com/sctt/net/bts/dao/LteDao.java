package com.sctt.net.bts.dao;

import java.util.List;
import java.util.Map;

import com.sctt.net.bts.bean.lte.Enodeb;
import com.sctt.net.bts.bean.lte.EutranCell;
import com.sctt.net.bts.bean.lte.LteBbu;
import com.sctt.net.bts.bean.lte.LteBts;

public interface LteDao {
	List<EutranCell> selectEutranCell();//查询所有LTE小区数据
	
	List<Enodeb> selectEnodeb();//查找所有LTE基站数据
	
    Map<String, LteBts> selectLteBts();//查詢所有LTE物理站點
	
    int insertLteBts(LteBts btsSite);//插入wy_lte_bts
	
	int updateLteBts(LteBts btsSite);//更新wy_lte_bts
	
	int updateLteBtsByDeleteFlag(LteBts btsSite);//删除wy_lte_bts
	
	Map<String, EutranCell> selectLteCell();//獲取現有wy_lte_cell数据
	
	int insertLteCell(EutranCell cell);//插入wy_lte_cell
	
	int updateLteCell(EutranCell cell);//编辑wy_lte_cell
	
	int updateLteCellByDeleteFlag(EutranCell cell);//删除wy_lte_cell
	
	Map<String,LteBbu> selectLteBbu();//获取wy_lte_bbu数据
	
	int insertLteBbu(LteBbu bbu);//插入wy_lte_bbu
	
	int updateLteBbu(LteBbu bbu);//更新wy_lte_bbu
	
	int updateLteBbuByDeleteFlag(LteBbu bbu);//删除wy_lte_bbu
	
	
}
