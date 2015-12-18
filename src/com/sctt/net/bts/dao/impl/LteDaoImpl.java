package com.sctt.net.bts.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sctt.net.bts.bean.lte.Enodeb;
import com.sctt.net.bts.bean.lte.EutranCell;
import com.sctt.net.bts.bean.lte.LteBts;
import com.sctt.net.bts.dao.LteDao;
import com.sctt.net.common.util.StringUtils;

public class LteDaoImpl implements LteDao {

	private static Logger logger = Logger.getLogger("baseLog");

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<EutranCell> selectEutranCell() {
		StringBuilder sb = new StringBuilder();
		sb.append("select int_id,userlabel,related_enb_userlabel,related_enb_int_id,vendor_name from tco_pro_eutrancell_m");
		List list = jdbcTemplate.queryForList(sb.toString());
		List<EutranCell> cells=new ArrayList<EutranCell>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			EutranCell cell = new EutranCell();
			cell.setIntId(StringUtils.objToLong(map.get("int_id")));
			cell.setRelatedEnbIntId(StringUtils.objToLong(map.get("related_enb_int_id")));
			cell.setRelateEnbUserLabel(StringUtils.objToString(map.get("related_enb_userlabel")));
			cell.setUserLabel(StringUtils.objToString(map.get("userlabel")));
			cell.setVendorName(StringUtils.objToString(map.get("vendor_name")));
			cells.add(cell);
		}
		return cells;
	}

	@Override
	public List<Enodeb> selectEnodeb() {
		StringBuilder sb=new StringBuilder();
		sb.append("select int_id,userlabel,vendor_name from tco_pro_enodeb_m");
		List list=jdbcTemplate.queryForList(sb.toString());
		List<Enodeb> enodebs=new ArrayList();
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			Enodeb enodeb=new Enodeb();
			enodeb.setIntId(StringUtils.objToLong(map.get("int_id")));
			enodeb.setUserLabel(StringUtils.objToString(map.get("userlabel")));
			enodeb.setVenderName(StringUtils.objToString(map.get("vendor_name")));
			enodebs.add(enodeb);
		}
		return enodebs;
	}

	@Override
	public List<LteBts> selectLteBts() {
		StringBuilder sb=new StringBuilder();
		sb.append("select int_id,delete_flag from wy_lte_bts");
		List list=jdbcTemplate.queryForList(sb.toString());
		List<LteBts> lteBtsList=new ArrayList();
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			LteBts lteBts=new LteBts();
			lteBts.setIntId(StringUtils.objToLong(map.get("int_id")));
			lteBts.setDeleteFlag(StringUtils.objToInt(map.get("delete_flag")));
			lteBtsList.add(lteBts);
		}
		return lteBtsList;
	}

	@Override
	public int insertLteBts(final List<LteBts> list) {
		StringBuilder sql=new StringBuilder("INSERT INTO WY_LTE_BTS(INT_ID,NAME,IS_INDOOR,IS_RRU,RELATE_ENB_INTID,ENB_NAME,");
		sql.append("CIRCUITROOM_OWNERSHIP,TRANS_OWNERSHIP,VENDER_NAME,SERVICE_LEVEL,SITE_TOGETHER,HIGHTRAIN_FLAG,REDLINE_FLAG,CITY_ID,COUNTRY_ID)");
		sql.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		 int[] result=jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter(){
	            public void setValues(PreparedStatement ps,int i)throws SQLException
	               {
	            	LteBts bts=list.get(i);
	                ps.setLong(1,bts.getIntId());
	                ps.setString(2, bts.getName());
	                ps.setString(3, bts.getIsIndoor());
	                ps.setString(4,bts.getIsRru());
	                ps.setLong(5, bts.getRelateEnbIntId());
	                ps.setString(6, bts.getEnbName());
	                ps.setString(7, bts.getCircuitRoomOwnerShip());
	                ps.setString(8, bts.getTransOwnerShip());
	                ps.setString(9, bts.getVenderName());
	                ps.setString(10,bts.getServiceLevel());
	                ps.setString(11, bts.getSiteTogether());
	                ps.setString(12, bts.getHightrainFlag());
	                ps.setObject(13, bts.getRedlineFlag());
	                ps.setInt(14, bts.getCityId());
	                ps.setInt(15, bts.getCountryId());
	               }
	               public int getBatchSize()
	               {
	                return list.size();
	               }
	        });
	       return result.length; 
	}

	@Override
	public int updateLteBts(final List<LteBts> list) {
		StringBuilder sql=new StringBuilder("update WY_LTE_BTS set NAME=?,IS_INDOOR=?,IS_RRU=?,RELATE_ENB_INTID,ENB_NAME=?,");
		sql.append("CIRCUITROOM_OWNERSHIP=?,TRANS_OWNERSHIP=?,VENDER_NAME=?,SERVICE_LEVEL=?,SITE_TOGETHER=?,HIGHTRAIN_FLAG=?,REDLINE_FLAG=?,CITY_ID=?,COUNTRY_ID=?,DELETE_FLAG=0,UPDATETIME=sysdate");
		sql.append("where INT_ID=?");
		 int[] result=jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter(){
	            public void setValues(PreparedStatement ps,int i)throws SQLException
	               {
	            	LteBts bts=list.get(i);
	                ps.setString(1, bts.getName());
	                ps.setString(2, bts.getIsIndoor());
	                ps.setString(3,bts.getIsRru());
	                ps.setLong(4, bts.getRelateEnbIntId());
	                ps.setString(5, bts.getEnbName());
	                ps.setString(6, bts.getCircuitRoomOwnerShip());
	                ps.setString(7, bts.getTransOwnerShip());
	                ps.setString(8, bts.getVenderName());
	                ps.setString(9,bts.getServiceLevel());
	                ps.setString(10, bts.getSiteTogether());
	                ps.setString(11, bts.getHightrainFlag());
	                ps.setInt(12, bts.getRedlineFlag());
	                ps.setInt(13, bts.getCityId());
	                ps.setInt(14, bts.getCountryId());
	                ps.setLong(15,bts.getIntId());
	               }
	               public int getBatchSize()
	               {
	                return list.size();
	               }
	        });
	       return result.length; 
	}

	@Override
	public int deleteLteBts(final List<LteBts> list) {
		StringBuilder sql=new StringBuilder("update WY_LTE_BTS set delete_flag=1 and DELETETIME=sysdate where INT_ID=?");
		 int[] result=jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter(){
	            public void setValues(PreparedStatement ps,int i)throws SQLException
	               {
	            	LteBts bts=list.get(i);
	                ps.setLong(1,bts.getIntId());
	               }
	               public int getBatchSize()
	               {
	                return list.size();
	               }
	        });
	       return result.length; 
	}
	
	
	
	
	
}
