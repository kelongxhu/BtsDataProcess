package com.sctt.net.bts.dao.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sctt.net.bts.bean.lte.Enodeb;
import com.sctt.net.bts.bean.lte.EutranCell;
import com.sctt.net.bts.bean.lte.LteBbu;
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
		List<EutranCell> cells = new ArrayList<EutranCell>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			EutranCell cell = new EutranCell();
			cell.setIntId(StringUtils.objToLong(map.get("int_id")));
			cell.setRelatedEnbIntId(StringUtils.objToLong(map
					.get("related_enb_int_id")));
			cell.setRelateEnbUserLabel(StringUtils.objToString(map
					.get("related_enb_userlabel")));
			cell.setUserLabel(StringUtils.objToString(map.get("userlabel")));
			cell.setVendorName(StringUtils.objToString(map.get("vendor_name")));
			cells.add(cell);
		}
		return cells;
	}

	@Override
	public List<Enodeb> selectEnodeb() {
		StringBuilder sb = new StringBuilder();
		sb.append("select int_id,userlabel,vendor_name,enb_id from tco_pro_enodeb_m where userlabel like '%BBU%'");
		List list = jdbcTemplate.queryForList(sb.toString());
		List<Enodeb> enodebs = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			Enodeb enodeb = new Enodeb();
			enodeb.setIntId(StringUtils.objToLong(map.get("int_id")));
			enodeb.setUserLabel(StringUtils.objToString(map.get("userlabel")));
			enodeb.setVenderName(StringUtils.objToString(map.get("vendor_name")));
			enodeb.setEnbId(StringUtils.objToInt(map.get("enb_id")));
			enodebs.add(enodeb);
		}
		return enodebs;
	}

	@Override
	public Map<String, LteBts> selectLteBts() {
		StringBuilder sb = new StringBuilder();
		sb.append("select int_id,delete_flag from wy_lte_bts");
		List list = jdbcTemplate.queryForList(sb.toString());
		Map<String, LteBts> btsMap = new HashMap<String, LteBts>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			LteBts lteBts = new LteBts();
			Long intId = StringUtils.objToLong(map.get("int_id"));
			lteBts.setIntId(intId);
			lteBts.setDeleteFlag(StringUtils.objToInt(map.get("delete_flag")));
			btsMap.put(intId + "", lteBts);
		}
		return btsMap;
	}

	@Override
	public int insertLteBts(LteBts btsSite) {
		StringBuilder sql = new StringBuilder(
				"INSERT INTO WY_LTE_BTS(INT_ID,NAME,IS_INDOOR,IS_RRU,RELATE_ENB_INTID,ENB_NAME,");
		sql.append("CIRCUITROOM_OWNERSHIP,TRANS_OWNERSHIP,VENDER_NAME,SERVICE_LEVEL,SITE_TOGETHER,HIGHTRAIN_FLAG,REDLINE_FLAG,CITY_ID,COUNTRY_ID)");
		sql.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		Object[] params = new Object[] {btsSite.getIntId(), btsSite.getName(),
				btsSite.getIsIndoor(), btsSite.getIsRru(),
				btsSite.getRelateEnbIntId(), btsSite.getEnbName(),
				btsSite.getCircuitRoomOwnerShip(),btsSite.getTransOwnerShip(), btsSite.getVenderName(),
				btsSite.getServiceLevel(),btsSite.getSiteTogether(),btsSite.getHightrainFlag(),btsSite.getRedlineFlag(),
				btsSite.getCityId(), btsSite.getCountryId()};
		int[] types = new int[] { Types.BIGINT,Types.VARCHAR,Types.VARCHAR, Types.VARCHAR,
				Types.BIGINT, Types.VARCHAR,Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR,Types.VARCHAR, Types.INTEGER,
				 Types.INTEGER, Types.INTEGER};
		int result=0;
		try {
			result = jdbcTemplate.update(sql.toString(), params, types);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	@Override
	public int updateLteBts(LteBts btsSite) {
		StringBuilder sql = new StringBuilder(
				"update WY_LTE_BTS set NAME=?,IS_INDOOR=?,IS_RRU=?,RELATE_ENB_INTID=?,ENB_NAME=?,");
		sql.append("CIRCUITROOM_OWNERSHIP=?,TRANS_OWNERSHIP=?,VENDER_NAME=?,SERVICE_LEVEL=?,SITE_TOGETHER=?,HIGHTRAIN_FLAG=?,REDLINE_FLAG=?,CITY_ID=?,COUNTRY_ID=?,DELETE_FLAG=0,UPDATETIME=sysdate");
		sql.append(" where INT_ID=?");
		Object[] params = new Object[] {btsSite.getName(),
				btsSite.getIsIndoor(), btsSite.getIsRru(),
				btsSite.getRelateEnbIntId(), btsSite.getEnbName(),
				btsSite.getCircuitRoomOwnerShip(),btsSite.getTransOwnerShip(), btsSite.getVenderName(),
				btsSite.getServiceLevel(),btsSite.getSiteTogether(),btsSite.getHightrainFlag(),btsSite.getRedlineFlag(),
				btsSite.getCityId(), btsSite.getCountryId(),btsSite.getIntId()};
		int[] types = new int[] {Types.VARCHAR,Types.VARCHAR, Types.VARCHAR,
				Types.BIGINT, Types.VARCHAR,Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR,Types.VARCHAR, Types.INTEGER,
				 Types.INTEGER, Types.INTEGER,Types.BIGINT};
		int result=0;
		try {
			result = jdbcTemplate.update(sql.toString(), params, types);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	@Override
	public int updateLteBtsByDeleteFlag(LteBts btsSite) {
		StringBuilder sql = new StringBuilder(
				"update WY_LTE_BTS set delete_flag=1,DELETETIME=sysdate where INT_ID=?");
		Object[] params = new Object[] { btsSite.getIntId() };
		int[] types = new int[] { Types.BIGINT };
		int j = 0;
		try {
			j = jdbcTemplate.update(sql.toString(), params, types);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return j;
	}

	/**
	 * 获取小区表wy_cell表数据
	 */
	public Map<String, EutranCell> selectLteCell() {
		String sql = "SELECT INT_ID,NAME,DELETE_FLAG FROM WY_LTE_CELL";
		List list = jdbcTemplate.queryForList(sql);
		Map<String, EutranCell> cellMap = new HashMap<String, EutranCell>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			EutranCell cell = new EutranCell();
			long initId = StringUtils.objToLong(map.get("INT_ID"));
			String name = StringUtils.objToString(map.get("NAME"));
			int deleteFlag = StringUtils.objToInt(map.get("DELETE_FLAG"));
			cell.setIntId(initId);
			cell.getUserLabel();
			cell.setDeleteFlag(deleteFlag);
			cellMap.put(initId + "", cell);
		}
		return cellMap;
	}

	@Override
	public int insertLteCell(EutranCell cell) {
		String sql = "INSERT INTO WY_LTE_CELL(INT_ID,NAME,CITY_ID,COUNTRY_ID,IS_INDOOR,IS_RRU,IS_GF,IS_OR,IS_RR,IS_SR,SITE_TOGETHER,VENDOR_NAME,LET_INT_ID,ENB_INT_ID,HIGHTRAIN_FLAG,REDLINE_FLAG)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[] { cell.getIntId(), cell.getUserLabel(),
				cell.getCityId(), cell.getCountryId(), cell.getIsIndoor(),
				cell.getIsRru(), cell.getIsGf(), cell.getIsOr(),
				cell.getIsRR(), cell.getIsSR(), cell.getSiteTogether(),cell.getVendorName(),
				cell.getWyLteIntId(),cell.getRelatedEnbIntId(), cell.getHighTrainFlag(),
				cell.getRedLineFlag()};
		int[] types = new int[] { Types.BIGINT, Types.VARCHAR, Types.INTEGER,
				Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.BIGINT, Types.BIGINT, Types.VARCHAR,
				Types.INTEGER };
		int result = 0;
		try {
			result = jdbcTemplate.update(sql, params, types);
		} catch (Exception e) {
			logger.info("错误小区:"+cell.toString());
			logger.error("++++插入小区异常,小区信息：" + cell.getIntId() + "异常信息_"
					+ e.getMessage(),e);
		}
		return result;
	}

	@Override
	public int updateLteCell(EutranCell cell) {
		String sql = "UPDATE WY_LTE_CELL SET NAME=?,CITY_ID=?,COUNTRY_ID=?,IS_INDOOR=?,IS_RRU=?,IS_GF=?,IS_OR=?,IS_RR=?,IS_SR=?,SITE_TOGETHER=?,VENDOR_NAME=?,LET_INT_ID=?,ENB_INT_ID=?,HIGHTRAIN_FLAG=?,REDLINE_FLAG=?,DELETE_FLAG=0,UPDATETIME=sysdate WHERE INT_ID=?";
		Object[] params = new Object[] { cell.getUserLabel(), cell.getCityId(),
				cell.getCountryId(), cell.getIsIndoor(), cell.getIsRru(),
				cell.getIsGf(), cell.getIsOr(), cell.getIsRR(), cell.getIsSR(),
				cell.getSiteTogether(), cell.getVendorName(),
				cell.getWyLteIntId(), cell.getRelatedEnbIntId(),
				cell.getHighTrainFlag(), cell.getRedLineFlag(), cell.getIntId() };
		int[] types = new int[] { Types.VARCHAR, Types.INTEGER, Types.INTEGER,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.BIGINT, Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.BIGINT };
		int j = 0;
		try {
			j = jdbcTemplate.update(sql, params, types);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return j;
	}

	@Override
	public int updateLteCellByDeleteFlag(EutranCell cell) {
		StringBuilder sql = new StringBuilder(
				"update WY_LTE_CELL set delete_flag=1,DELETETIME=sysdate where INT_ID=?");
		Object[] params = new Object[] { cell.getIntId() };
		int[] types = new int[] { Types.BIGINT };
		int j = 0;
		try {
			j = jdbcTemplate.update(sql.toString(), params, types);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return j;
	}

	@Override
	public Map<String, LteBbu> selectLteBbu() {
		String sql = "select INT_ID,DELETE_FLAG from wy_bbu";
		List list = jdbcTemplate.queryForList(sql);
		Map<String, LteBbu> bbuMap = new HashMap<String, LteBbu>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			LteBbu lteBbu = new LteBbu();
			lteBbu.setIntId(StringUtils.objToLong(map.get("INT_ID")));
			lteBbu.setDeleteFlag(StringUtils.objToInt(map.get("DELETE_FLAG")));
			bbuMap.put(lteBbu.getIntId() + "", lteBbu);
		}
		return bbuMap;
	}

	@Override
	public int insertLteBbu(LteBbu bbu) {
		String sql = "insert into wy_lte_bbu(INT_ID,NAME,BBU_NO,ENB_ID,ENB_BTS_NAME,ENB_BTS_ID,CIRCUITROOM_OWNERSHIP,TRANS_OWNERSHIP,VENDOR_NAME,CITY_ID,COUNTY_ID,"
				+ "IS_SHARE,BBU_TYPE,WY_LTE_BTSID,HIGHTRAIN_FLAG,REDLINE_FLAG)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[] { bbu.getIntId(), bbu.getName(),
				bbu.getBbuNo(),bbu.getEnbId(),bbu.getEnbBtsName(), bbu.getRelateEnbBtsId(),
				bbu.getCircuitRoomOwnership(), bbu.getTransOwnership(),
				bbu.getVendorName(), bbu.getCityId(),
				bbu.getCountryId(), 
				bbu.getIsShare(), bbu.getBuuType(), bbu.getWyLteBtsId(),
				bbu.getHightranFlag(), bbu.getRedlineFlag()};
		int[] types = new int[] { Types.BIGINT, Types.VARCHAR, Types.VARCHAR,Types.INTEGER,
				Types.VARCHAR,Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.INTEGER, Types.INTEGER,Types.INTEGER,
				Types.INTEGER, Types.BIGINT, Types.VARCHAR, Types.INTEGER };
		return jdbcTemplate.update(sql, params, types);
	}

	@Override
	public int updateLteBbu(LteBbu bbu) {
		String sql = "update wy_lte_bbu set NAME=?,BBU_NO=?,ENB_ID=?,ENB_BTS_NAME=?,ENB_BTS_ID=?,CIRCUITROOM_OWNERSHIP=?,TRANS_OWNERSHIP=?,VENDOR_NAME=?,CITY_ID=?,COUNTY_ID=?,"
				+ "IS_SHARE=?,BBU_TYPE=?,WY_LTE_BTSID=?,HIGHTRAIN_FLAG=?,REDLINE_FLAG=? where INT_ID=?";
		Object[] params = new Object[] {bbu.getName(),
				bbu.getBbuNo(),bbu.getEnbId(), bbu.getEnbBtsName(), bbu.getRelateEnbBtsId(),
				bbu.getCircuitRoomOwnership(), bbu.getTransOwnership(),
				bbu.getVendorName(), bbu.getCityId(),
				bbu.getCountryId(), bbu.getIsShare(), bbu.getBuuType(), bbu.getWyLteBtsId(),
				bbu.getHightranFlag(), bbu.getRedlineFlag(),bbu.getIntId()};
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR,Types.INTEGER,
				Types.VARCHAR,Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.INTEGER, Types.INTEGER,Types.INTEGER,
				Types.INTEGER, Types.BIGINT, Types.VARCHAR, Types.INTEGER,Types.BIGINT };
		return jdbcTemplate.update(sql, params, types);
	}

	@Override
	public int updateLteBbuByDeleteFlag(LteBbu bbu) {
		String sql="update wy_lte_bbu set DELETE_FLAG=1,DELETETIME=sysdate where int_id=?";
		Object[] params = new Object[] { bbu.getIntId() };
		int[] types = new int[] { Types.BIGINT };
		int j = 0;
		try {
			j = jdbcTemplate.update(sql.toString(), params, types);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return j;
	}
	
	
	

}
