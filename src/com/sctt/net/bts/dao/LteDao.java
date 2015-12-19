package com.sctt.net.bts.dao;

import java.util.List;
import java.util.Map;

import com.sctt.net.bts.bean.lte.Enodeb;
import com.sctt.net.bts.bean.lte.EutranCell;
import com.sctt.net.bts.bean.lte.LteBbu;
import com.sctt.net.bts.bean.lte.LteBts;

public interface LteDao {
	List<EutranCell> selectEutranCell();//��ѯ����LTEС������
	
	List<Enodeb> selectEnodeb();//��������LTE��վ����
	
    Map<String, LteBts> selectLteBts();//��ԃ����LTE����վ�c
	
    int insertLteBts(LteBts btsSite);//����wy_lte_bts
	
	int updateLteBts(LteBts btsSite);//����wy_lte_bts
	
	int updateLteBtsByDeleteFlag(LteBts btsSite);//ɾ��wy_lte_bts
	
	Map<String, EutranCell> selectLteCell();//�@ȡ�F��wy_lte_cell����
	
	int insertLteCell(EutranCell cell);//����wy_lte_cell
	
	int updateLteCell(EutranCell cell);//�༭wy_lte_cell
	
	int updateLteCellByDeleteFlag(EutranCell cell);//ɾ��wy_lte_cell
	
	Map<String,LteBbu> selectLteBbu();//��ȡwy_lte_bbu����
	
	int insertLteBbu(LteBbu bbu);//����wy_lte_bbu
	
	int updateLteBbu(LteBbu bbu);//����wy_lte_bbu
	
	int updateLteBbuByDeleteFlag(LteBbu bbu);//ɾ��wy_lte_bbu
	
	
}
