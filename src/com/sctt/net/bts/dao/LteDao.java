package com.sctt.net.bts.dao;

import java.util.List;

import com.sctt.net.bts.bean.lte.Enodeb;
import com.sctt.net.bts.bean.lte.EutranCell;
import com.sctt.net.bts.bean.lte.LteBts;

public interface LteDao {
	List<EutranCell> selectEutranCell();//��ѯ����LTEС������
	
	List<Enodeb> selectEnodeb();//��������LTE��վ����
	
	List<LteBts> selectLteBts();//��ԃ����LTE����վ�c
	
	int insertLteBts(List<LteBts> list);//����wy_lte_bts
	
	int updateLteBts(List<LteBts> list);//����wy_lte_bts
	
	int deleteLteBts(List<LteBts> list);//ɾ��wy_lte_bts
}
