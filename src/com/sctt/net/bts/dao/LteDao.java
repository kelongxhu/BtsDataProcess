package com.sctt.net.bts.dao;

import java.util.List;

import com.sctt.net.bts.bean.lte.Enodeb;
import com.sctt.net.bts.bean.lte.EutranCell;
import com.sctt.net.bts.bean.lte.LteBts;

public interface LteDao {
	List<EutranCell> selectEutranCell();//查询所有LTE小区数据
	
	List<Enodeb> selectEnodeb();//查找所有LTE基站数据
	
	List<LteBts> selectLteBts();//查詢所有LTE物理站點
	
	int insertLteBts(List<LteBts> list);//插入wy_lte_bts
	
	int updateLteBts(List<LteBts> list);//更新wy_lte_bts
	
	int deleteLteBts(List<LteBts> list);//删除wy_lte_bts
}
