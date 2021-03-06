package com.sctt.net.bts.dao.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sctt.net.bts.bean.cdma.Bts;
import com.sctt.net.bts.bean.cdma.BtsSite;
import com.sctt.net.bts.bean.cdma.Cell;
import com.sctt.net.bts.bean.cdma.Country;
import com.sctt.net.bts.bean.cdma.TunelLib;
import com.sctt.net.bts.bean.cdma.TunelSite;
import com.sctt.net.bts.bean.cdma.WyBbu;
import com.sctt.net.bts.bean.cdma.WyBtsSpecial;
import com.sctt.net.bts.bean.cdma.WyWrongName;
import com.sctt.net.bts.dao.BtsDao;
import com.sctt.net.common.util.StringUtils;

public class BtsImpl implements BtsDao {

	private static Logger logger = Logger.getLogger("baseLog");

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 获取小区列表
	 */
	public List<Cell> getCells() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select a.int_id,a.name,a.bsc_name,a.btsid,a.bts_name,a.related_bts,B.base_long_b LONGITUDE,B.base_lat_b LATITUDE,a.cellid,a.ci,a.pn,a.lac,a.do_cell,c.vendor_btstype,a.city_id from c_cell a "
				+ "left join c_tzx_par_cell b on a.int_id=b.int_id LEFT JOIN c_bts c ON a.related_bts=c.int_id where a.bsc_name NOT LIKE '%华为%'");
		StringBuilder sb2 = new StringBuilder();
		sb2.append("select a.int_id,a.name,a.bsc_name,a.btsid,a.bts_name,a.related_bts,b.bslong LONGITUDE,b.bslat LATITUDE,a.cellid,a.ci,a.pn,a.lac,a.do_cell,c.vendor_btstype,a.city_id from c_cell a "
				+ "left join c_thw_par_cell_1x b on a.int_id=b.int_id LEFT JOIN c_bts c ON a.related_bts=c.int_id where a.bsc_name LIKE '%华为%'");
		List list = jdbcTemplate.queryForList(sb.toString());
		List list2= jdbcTemplate.queryForList(sb2.toString());
		list.addAll(list2);
		List<Cell> result = new ArrayList<Cell>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			Cell cell = new Cell();
			cell.setIntId(StringUtils.objToLong(map.get("int_id")));
			cell.setName(StringUtils.objToString(map.get("name")));
			cell.setBscName(StringUtils.objToString(map.get("bsc_name")));
			cell.setBtsId(StringUtils.objToLong(map.get("btsid")));
			cell.setBtsName(StringUtils.objToString(map.get("bts_name")));
			cell.setRelateBts(StringUtils.objToLong(map.get("related_bts")));
			cell.setLongitude(StringUtils.objToDouble(map.get("longitude")));
			cell.setLatitude(StringUtils.objToDouble(map.get("latitude")));
			cell.setVendor_btstype(StringUtils.objToString(map
					.get("vendor_btstype")));
			cell.setCellId(StringUtils.objToInt(map.get("cellId")));
			cell.setCi(StringUtils.objToInt(map.get("ci")));
			cell.setPn(StringUtils.objToInt(map.get("pn")));
			cell.setLac(StringUtils.objToInt(map.get("lac")));
			cell.setDo_cell(StringUtils.objToInt(map.get("do_cell")));
			cell.setSourceCityId(StringUtils.objToInt(map.get("city_id")));
			result.add(cell);
		}

		return result;
	}

	/**
	 * 插入物理基站信息
	 */
	public int insertBtsSite(BtsSite btsSite) throws Exception {
		String sql = "INSERT INTO WY_BTS(INT_ID,NAME,LONGITUDE,LATITUDE,IS_INDOOR,IS_RRU,BTS_NAME,BSC_NAME,BTS_ID,"
				+ "CIRCUITROOM_OWNERSHIP,TRANS_OWNERSHIP,VENDOR_BTSTYPE,CITY_ID,COUNTY_ID,RELATED_BTS,SERVICE_LEVEL,HIGHTRAIN_FLAG,REDLINE_FLAG)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[] { btsSite.getIntId(), btsSite.getName(),
				btsSite.getLongitude(), btsSite.getLatitude(),
				btsSite.getIsIndoor(), btsSite.getIs_rru(),
				btsSite.getBts_name(), btsSite.getBsc_name(),
				btsSite.getBts_id(), btsSite.getCircuitRoom_ownership(),
				btsSite.getTrans_ownership(), btsSite.getVendor_BtsType(),
				btsSite.getCityId(), btsSite.getCountryId(),
				btsSite.getRelated_bts(), btsSite.getServiceLevel(),
				btsSite.getHighTrainFlag(), btsSite.getRedLineFlag() };
		int[] types = new int[] { Types.BIGINT, Types.VARCHAR, Types.DOUBLE,
				Types.DOUBLE, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.BIGINT, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.BIGINT,
				Types.VARCHAR, Types.VARCHAR, Types.INTEGER };
		return jdbcTemplate.update(sql, params, types);
	}

	/**
	 * 加载物理站点
	 */
	public Map<String, BtsSite> getBtsSites() throws Exception {
		String sql = "select INT_ID,NAME,DELETE_FLAG from WY_BTS";
		List list = jdbcTemplate.queryForList(sql);
		Map<String, BtsSite> btsMap = new HashMap<String, BtsSite>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			long intId = StringUtils.objToLong(map.get("INT_ID"));
			String name = StringUtils.objToString(map.get("NAME"));
			int deleteFlag = StringUtils.objToInt(map.get("DELETE_FLAG"));
			BtsSite bts = new BtsSite();
			bts.setIntId(intId);
			bts.setName(name);
			bts.setDeleteFlag(deleteFlag);
			btsMap.put(intId + "", bts);
		}
		return btsMap;
	}

	/**
	 * 更新物理站点信息
	 */
	public int updateBtsSite(BtsSite btsSite) throws Exception {
		String sql = "UPDATE WY_BTS SET NAME=?,LONGITUDE=?,LATITUDE=?,IS_INDOOR=?,IS_RRU=?,BTS_NAME=?,BSC_NAME=?,BTS_ID=?,"
				+ "CIRCUITROOM_OWNERSHIP=?,TRANS_OWNERSHIP=?,VENDOR_BTSTYPE=?,CITY_ID=?,COUNTY_ID=?,RELATED_BTS=?,DELETE_FLAG=?,UPDATETIME=sysdate,SERVICE_LEVEL=?,HIGHTRAIN_FLAG=?,REDLINE_FLAG=? WHERE INT_ID=?";
		Object[] params = new Object[] { btsSite.getName(),
				btsSite.getLongitude(), btsSite.getLatitude(),
				btsSite.getIsIndoor(), btsSite.getIs_rru(),
				btsSite.getBts_name(), btsSite.getBsc_name(),
				btsSite.getBts_id(), btsSite.getCircuitRoom_ownership(),
				btsSite.getTrans_ownership(), btsSite.getVendor_BtsType(),
				btsSite.getCityId(), btsSite.getCountryId(),
				btsSite.getRelated_bts(), btsSite.getDeleteFlag(),
				btsSite.getServiceLevel(), btsSite.getHighTrainFlag(),
				btsSite.getRedLineFlag(), btsSite.getIntId() };
		int[] types = new int[] { Types.VARCHAR, Types.DOUBLE, Types.DOUBLE,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.INTEGER, Types.INTEGER, Types.BIGINT, Types.BIGINT,
				Types.VARCHAR, Types.VARCHAR, Types.BIGINT, Types.BIGINT };
		return jdbcTemplate.update(sql, params, types);
	}

	/**
	 * 将基站置为废弃
	 */
	public int updateBtsSiteByDeleteFlag(BtsSite btsSite) throws Exception {
		String sql = "UPDATE WY_BTS SET DELETE_FLAG=1,DELETETIME=sysdate,UPDATETIME=sysdate WHERE INT_ID=?";
		Object[] params = new Object[] { btsSite.getIntId() };
		int[] types = new int[] { Types.BIGINT };
		return jdbcTemplate.update(sql, params, types);
	}

	/**
	 * 插入错误小区名称
	 */
	public int insertWyWrongName(WyWrongName wwn) throws Exception {
		String sql = "INSERT INTO WY_WRONGNAME(INT_ID,CELL_NAME,BSC_NAME,BTS_ID,BTSNAME,TYPE,CITY_ID,NET_TYPE，WRONG_MSG)VALUES(?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[] { wwn.getInt_id(), wwn.getCellName(),
				wwn.getBscName(), wwn.getBtsId(), wwn.getBtsName(),
				wwn.getType(), wwn.getCityId(),wwn.getNetType(),wwn.getWrongMsg() };
		int[] types = new int[] { Types.BIGINT, Types.VARCHAR, Types.VARCHAR,
				Types.BIGINT, Types.VARCHAR, Types.INTEGER, Types.INTEGER,Types.INTEGER,Types.VARCHAR };
		return jdbcTemplate.update(sql, params, types);
	}

	/**
	 * 更新错误小区名称
	 */
	public int updateWyWrongName(WyWrongName wwn) throws Exception {
		String sql = "update WY_WRONGNAME set CELL_NAME=?,BSC_NAME=?,BTS_ID=?,BTSNAME=?,TYPE=?,UPDATETIME=sysdate,DELETE_FLAG=?,CITY_ID=?,NET_TYPE=?,WRONG_MSG=? where INT_ID=?";
		Object[] params = new Object[] { wwn.getCellName(), wwn.getBscName(),
				wwn.getBtsId(), wwn.getBtsName(), wwn.getType(),
				wwn.getDeleteFlag(), wwn.getCityId(), wwn.getNetType(),wwn.getWrongMsg(),wwn.getInt_id() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.BIGINT,
				Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER,Types.INTEGER,Types.VARCHAR,
				Types.BIGINT };
		return jdbcTemplate.update(sql, params, types);
	}

	/**
	 * 错误小区名称置为废弃
	 */
	public int updateWyWrongNameByDelFlag(WyWrongName wwn) throws Exception {
		String sql = "update WY_WRONGNAME set DELETE_FLAG=1,UPDATETIME=sysdate where INT_ID=?";
		Object[] params = new Object[] { wwn.getInt_id() };
		int[] types = new int[] { Types.BIGINT };
		return jdbcTemplate.update(sql, params, types);
	}

	/**
	 * 加载名称错误小区
	 */
	public Map<String, WyWrongName> getWwns(int netType) throws Exception {
		String sql = "select INT_ID,CELL_NAME,DELETE_FLAG from WY_WRONGNAME where NET_TYPE=?";
		Object[] params = new Object[] { netType };
		int[] types = new int[] { Types.INTEGER };
		List list = jdbcTemplate.queryForList(sql,params,types);
		Map<String, WyWrongName> wwnMap = new HashMap<String, WyWrongName>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			long intId = StringUtils.objToLong(map.get("INT_ID"));
			String cellName = StringUtils.objToString(map.get("CELL_NAME"));
			int deleteFlag = StringUtils.objToInt(map.get("DELETE_FLAG"));
			WyWrongName wwN = new WyWrongName();
			wwN.setInt_id(intId);
			wwN.setCellName(cellName);
			wwN.setDeleteFlag(deleteFlag);
			wwnMap.put(intId + "", wwN);
		}
		return wwnMap;
	}

	/**
	 * 获取区县列表
	 */
	public Map<String, Country> getCountrys() throws Exception {
		String sql = "SELECT a.id,a.parentid cityid,a.name countryname,b.name cityname FROM wy_city a "
				+ "LEFT JOIN wy_city b"
				+ " ON a.parentid=b.id WHERE a.parentid NOT IN(10001,-1) ORDER BY a.parentid";
		List list = jdbcTemplate.queryForList(sql);
		Map<String, Country> countryMap = new HashMap<String, Country>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			int id = StringUtils.objToInt(map.get("id"));
			int cityId = StringUtils.objToInt(map.get("cityid"));
			String countryName = StringUtils
					.objToString(map.get("countryname"));
			String cityName = StringUtils.objToString(map.get("cityname"));
			Country country = new Country();
			country.setId(id);
			country.setCityId(cityId);
			country.setContryName(countryName);
			country.setCityName(cityName);
			countryMap.put(countryName, country);
		}
		return countryMap;
	}

	/**
	 * 获取小区表wy_cell表数据
	 */
	public Map<String, Cell> getWyCell() throws Exception {
		String sql = "SELECT INT_ID,NAME,DELETE_FLAG FROM WY_CELL";
		List list = jdbcTemplate.queryForList(sql);
		Map<String, Cell> cellMap = new HashMap<String, Cell>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			Cell cell = new Cell();
			long initId = StringUtils.objToLong(map.get("INT_ID"));
			String name = StringUtils.objToString(map.get("NAME"));
			int deleteFlag = StringUtils.objToInt(map.get("DELETE_FLAG"));
			cell.setIntId(initId);
			cell.setName(name);
			cell.setDeleteFlag(deleteFlag);
			cellMap.put(initId + "", cell);
		}
		return cellMap;
	}

	/**
	 * 插入小区数据
	 */
	public int insertCell(Cell cell) throws Exception {
		String sql = "INSERT INTO WY_CELL(INT_ID,NAME,CITY_ID,COUNTRY_ID,IS_INDOOR,IS_RRU,IS_GF,IS_OR,IS_RR,IS_SR,WY_BTS_INT_ID,BTSID,CELLID,BSC_NAME,CI,PN,LAC,DO_CELL,RELATED_BTS,HIGHTRAIN_FLAG,REDLINE_FLAG)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[] { cell.getIntId(), cell.getName(),
				cell.getCityId(), cell.getCountryId(), cell.getIsIndoor(),
				cell.getIsRru(), cell.getIsGf(), cell.getIsOr(),
				cell.getIsRR(), cell.getIsSR(), cell.getWyBtsIntId(),
				cell.getBtsId(), cell.getCellId(), cell.getBscName(),
				cell.getCi(), cell.getPn(), cell.getLac(), cell.getDo_cell(),
				cell.getRelateBts(), cell.getHighTrainFlag(),
				cell.getRedLineFlag() };
		int[] types = new int[] { Types.BIGINT, Types.VARCHAR, Types.INTEGER,
				Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.BIGINT,
				Types.BIGINT, Types.BIGINT, Types.VARCHAR, Types.BIGINT,
				Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.BIGINT,
				Types.VARCHAR, Types.INTEGER };
		int result = 0;
		try {
			result = jdbcTemplate.update(sql, params, types);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("++++插入小区异常,小区信息：" + cell.getIntId() + "异常信息_"
					+ e.getMessage());
		}
		return result;
	}

	/**
	 * 更新小区数据
	 */
	public int updateCell(Cell cell) {
		String sql = "UPDATE WY_CELL SET NAME=?,CITY_ID=?,COUNTRY_ID=?,IS_INDOOR=?,IS_RRU=?,IS_GF=?,IS_OR=?,IS_RR=?,IS_SR=?,WY_BTS_INT_ID=?,BTSID=?,CELLID=?,BSC_NAME=?,CI=?,PN=?,LAC=?,DO_CELL=?,RELATED_BTS=?,DELETE_FLAG=?,HIGHTRAIN_FLAG=?,REDLINE_FLAG=?,UPDATETIME=sysdate WHERE INT_ID=?";
		Object[] params = new Object[] { cell.getName(), cell.getCityId(),
				cell.getCountryId(), cell.getIsIndoor(), cell.getIsRru(),
				cell.getIsGf(), cell.getIsOr(), cell.getIsRR(), cell.getIsSR(),
				cell.getWyBtsIntId(), cell.getBtsId(), cell.getCellId(),
				cell.getBscName(), cell.getCi(), cell.getPn(), cell.getLac(),
				cell.getDo_cell(), cell.getRelateBts(), cell.getDeleteFlag(),
				cell.getHighTrainFlag(), cell.getRedLineFlag(),cell.getIntId() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER,
				Types.INTEGER, Types.INTEGER, Types.BIGINT, Types.BIGINT,
				Types.BIGINT, Types.VARCHAR, Types.BIGINT, Types.BIGINT,
				Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.BIGINT,
				Types.VARCHAR, Types.INTEGER,Types.BIGINT };
		int j = 0;
		try {
			j = jdbcTemplate.update(sql, params, types);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return j;
	}

	@Override
	public int updateCellByDelFlag(Cell cell) {
		String sql = "update wy_cell set delete_flag=1,DELETETIME=sysdate,UPDATETIME=sysdate where int_id=?";
		Object[] params = new Object[] { cell.getIntId() };
		int[] types = new int[] { Types.BIGINT };
		int j = 0;
		try {
			j = jdbcTemplate.update(sql, params, types);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return j;
	}

	/**
	 * 获取BBU数据
	 */
	public List<Bts> getBbus() throws Exception {
		String sql = "SELECT int_id,name,btsid,related_bsc,bsc_name,city_id,longitude,latitude,vendor_btstype FROM c_bts WHERE NAME LIKE '%BBU%'";
		List list = jdbcTemplate.queryForList(sql);
		List<Bts> btsList = new ArrayList<Bts>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			Bts bts = new Bts();
			bts.setIntId(StringUtils.objToLong(map.get("int_id")));
			bts.setName(StringUtils.objToString(map.get("name")));
			bts.setBtsId(StringUtils.objToLong(map.get("btsid")));
			bts.setRelatedBsc(StringUtils.objToLong(map.get("related_bsc")));
			bts.setBscName(StringUtils.objToString(map.get("bsc_name")));
			bts.setLongitude(StringUtils.objToDouble(map.get("longitude")));
			bts.setLatitude(StringUtils.objToDouble(map.get("latitude")));
			bts.setVendorBtsType(StringUtils.objToString(map
					.get("vendor_btstype")));
			bts.setSourceCityId(StringUtils.objToInt(map.get("city_id")));
			btsList.add(bts);
		}
		return btsList;
	}

	@Override
	public Map<String, WyBbu> getWyBbu() throws Exception {
		String sql = "select INT_ID,DELETE_FLAG from wy_bbu";
		List list = jdbcTemplate.queryForList(sql);
		Map<String, WyBbu> bbuMap = new HashMap<String, WyBbu>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			WyBbu wyBbu = new WyBbu();
			wyBbu.setIntId(StringUtils.objToLong(map.get("INT_ID")));
			wyBbu.setDeleteFlag(StringUtils.objToInt(map.get("DELETE_FLAG")));
			bbuMap.put(wyBbu.getIntId() + "", wyBbu);
		}
		return bbuMap;
	}

	@Override
	public int insertWyBbu(WyBbu wybbu) throws Exception {
		String sql = "insert into wy_bbu(INT_ID,NAME,BBU_NO,LONGITUDE,LATITUDE,BTS_NAME,BSC_NAME,"
				+ "BTS_ID,CIRCUITROOM_OWNERSHIP,TRANS_OWNERSHIP,VENDOR_BTSTYPE,CITY_ID,COUNTY_ID,"
				+ "RELATED_BTS,IS_SHARE,BBU_TYPE,RELATE_WYBTS,HIGHTRAIN_FLAG,REDLINE_FLAG)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[] { wybbu.getIntId(), wybbu.getName(),
				wybbu.getBbuNo(), wybbu.getLongitude(), wybbu.getLatitude(),
				wybbu.getBtsName(), wybbu.getBscName(), wybbu.getBtsId(),
				wybbu.getCircuitRoomOwnership(), wybbu.getTransOwnership(),
				wybbu.getVendorBtsType(), wybbu.getCityId(),
				wybbu.getCountryId(), wybbu.getRelatedBts(),
				wybbu.getIsShare(), wybbu.getBbuType(), wybbu.getRelateWyBts(),
				wybbu.getHighTrainFlag(), wybbu.getRedLineFlag() };
		int[] types = new int[] { Types.BIGINT, Types.VARCHAR, Types.VARCHAR,
				Types.DOUBLE, Types.DOUBLE, Types.VARCHAR, Types.VARCHAR,
				Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.INTEGER, Types.INTEGER, Types.BIGINT, Types.INTEGER,
				Types.INTEGER, Types.BIGINT, Types.VARCHAR, Types.INTEGER };
		return jdbcTemplate.update(sql, params, types);
	}

	@Override
	public int updateWyBbu(WyBbu wybbu) throws Exception {
		String sql = "update wy_bbu set NAME=?,BBU_NO=?,LONGITUDE=?,LATITUDE=?,BTS_NAME=?,BSC_NAME=?,BTS_ID=?,"
				+ "CIRCUITROOM_OWNERSHIP=?,TRANS_OWNERSHIP=?,VENDOR_BTSTYPE=?,CITY_ID=?,COUNTY_ID=?,"
				+ "RELATED_BTS=?,IS_SHARE=?,BBU_TYPE=?,RELATE_WYBTS=?,DELETE_FLAG=?,UPDATETIME=sysdate,HIGHTRAIN_FLAG=?,REDLINE_FLAG=? where INT_ID=?";
		Object[] params = new Object[] { wybbu.getName(), wybbu.getBbuNo(),
				wybbu.getLongitude(), wybbu.getLatitude(), wybbu.getBtsName(),
				wybbu.getBscName(), wybbu.getBtsId(),
				wybbu.getCircuitRoomOwnership(), wybbu.getTransOwnership(),
				wybbu.getVendorBtsType(), wybbu.getCityId(),
				wybbu.getCountryId(), wybbu.getRelatedBts(),
				wybbu.getIsShare(), wybbu.getBbuType(), wybbu.getRelateWyBts(),
				wybbu.getDeleteFlag(), wybbu.getHighTrainFlag(),
				wybbu.getRedLineFlag(), wybbu.getIntId() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.DOUBLE,
				Types.DOUBLE, Types.VARCHAR, Types.VARCHAR, Types.BIGINT,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER,
				Types.INTEGER, Types.BIGINT, Types.INTEGER, Types.INTEGER,
				Types.BIGINT, Types.BIGINT, Types.VARCHAR, Types.INTEGER,
				Types.BIGINT };
		return jdbcTemplate.update(sql, params, types);
	}

	@Override
	public int updateWyBbuByDelFlag(WyBbu wybbu) throws Exception {
		String sql = "update wy_bbu set delete_flag=1,DELETETIME=sysdate,updatetime=sysdate where int_id=?";
		Object[] params = new Object[] { wybbu.getIntId() };
		int[] types = new int[] { Types.BIGINT };
		return jdbcTemplate.update(sql, params, types);
	}

	@Override
	public Map<String, TunelSite> getTunels() throws Exception {
		String sql = "select INT_ID,NAME,DELETE_FLAG from WY_TUNEL";
		List list = jdbcTemplate.queryForList(sql);
		Map<String, TunelSite> tunelMap = new HashMap<String, TunelSite>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			long intId = StringUtils.objToLong(map.get("INT_ID"));
			String name = StringUtils.objToString(map.get("NAME"));
			int deleteFlag = StringUtils.objToInt(map.get("DELETE_FLAG"));
			TunelSite bts = new TunelSite();
			bts.setIntId(intId);
			bts.setName(name);
			bts.setDeleteFlag(deleteFlag);
			tunelMap.put(intId + "", bts);
		}
		return tunelMap;
	}

	@Override
	public int insertWyTunel(TunelSite tunelSite) throws Exception {
		String sql = "INSERT INTO WY_TUNEL(INT_ID,NAME,LONGITUDE,LATITUDE,IS_RRU,BTS_NAME,BSC_NAME,BTS_ID,"
				+ "CIRCUITROOM_OWNERSHIP,TRANS_OWNERSHIP,VENDOR_BTSTYPE,CITY_ID,COUNTY_ID,RELATED_BTS,SERVICE_LEVEL,HIGHTRAIN_FLAG,REDLINE_FLAG)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[] { tunelSite.getIntId(),
				tunelSite.getName(), tunelSite.getLongitude(),
				tunelSite.getLatitude(), tunelSite.getIs_rru(),
				tunelSite.getBts_name(), tunelSite.getBsc_name(),
				tunelSite.getBts_id(), tunelSite.getCircuitRoom_ownership(),
				tunelSite.getTrans_ownership(), tunelSite.getVendor_BtsType(),
				tunelSite.getCityId(), tunelSite.getCountryId(),
				tunelSite.getRelated_bts(), tunelSite.getServiceLevel(),
				tunelSite.getHighTrainFlag(), tunelSite.getRedLineFlag() };
		int[] types = new int[] { Types.BIGINT, Types.VARCHAR, Types.DOUBLE,
				Types.DOUBLE, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.INTEGER, Types.INTEGER, Types.BIGINT, Types.VARCHAR,
				Types.VARCHAR, Types.INTEGER };
		return jdbcTemplate.update(sql, params, types);

	}

	@Override
	public int updateWyTunel(TunelSite tunelSite) throws Exception {
		String sql = "UPDATE WY_TUNEL SET NAME=?,LONGITUDE=?,LATITUDE=?,IS_RRU=?,BTS_NAME=?,BSC_NAME=?,BTS_ID=?,"
				+ "CIRCUITROOM_OWNERSHIP=?,TRANS_OWNERSHIP=?,VENDOR_BTSTYPE=?,CITY_ID=?,COUNTY_ID=?,RELATED_BTS=?,DELETE_FLAG=?,UPDATETIME=sysdate,SERVICE_LEVEL=?,HIGHTRAIN_FLAG=?,REDLINE_FLAG=? WHERE INT_ID=?";
		Object[] params = new Object[] { tunelSite.getName(),
				tunelSite.getLongitude(), tunelSite.getLatitude(),
				tunelSite.getIs_rru(), tunelSite.getBts_name(),
				tunelSite.getBsc_name(), tunelSite.getBts_id(),
				tunelSite.getCircuitRoom_ownership(),
				tunelSite.getTrans_ownership(), tunelSite.getVendor_BtsType(),
				tunelSite.getCityId(), tunelSite.getCountryId(),
				tunelSite.getRelated_bts(), tunelSite.getDeleteFlag(),
				tunelSite.getServiceLevel(), tunelSite.getHighTrainFlag(),
				tunelSite.getRedLineFlag(), tunelSite.getIntId() };
		int[] types = new int[] { Types.VARCHAR, Types.DOUBLE, Types.DOUBLE,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BIGINT,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER,
				Types.INTEGER, Types.BIGINT, Types.BIGINT, Types.VARCHAR,
				Types.VARCHAR, Types.BIGINT, Types.BIGINT };
		return jdbcTemplate.update(sql, params, types);
	}

	@Override
	public int updateWyTunelByDelFlag(TunelSite tunelSite) throws Exception {
		String sql = "UPDATE WY_TUNEL SET DELETE_FLAG=1,DELETETIME=sysdate,UPDATETIME=sysdate WHERE INT_ID=?";
		Object[] params = new Object[] { tunelSite.getIntId() };
		int[] types = new int[] { Types.BIGINT };
		return jdbcTemplate.update(sql, params, types);
	}

	@Override
	public Map<String, TunelLib> getTunelLib() throws Exception {
		String sql = "select ID,NAME,delete_flag from wy_lib_tunnel where auto_flag=1";
		List list = jdbcTemplate.queryForList(sql);
		Map<String, TunelLib> tunelLibMap = new HashMap<String, TunelLib>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			long id = StringUtils.objToLong(map.get("ID"));
			String name = StringUtils.objToString(map.get("NAME"));
			int deleteFlag = StringUtils.objToInt(map.get("delete_flag"));
			TunelLib tunelLib = new TunelLib();
			tunelLib.setId(id);
			tunelLib.setName(name);
			tunelLib.setDeleteFlag(deleteFlag);
			tunelLibMap.put(name, tunelLib);
		}
		return tunelLibMap;
	}

	@Override
	public int insertTunelLib(TunelLib tunelLib) throws Exception {
		String sql = "INSERT INTO wy_lib_tunnel(ID,NAME,CITY_ID,COUNTRY_ID,LONGITUDE,LATITUDE,DIRECTION,AUTO_FLAG)values(WY_LIB_TUNNEL_SEQ.NEXTVAL,?,?,?,?,?,?,1)";
		Object[] params = new Object[] { tunelLib.getName(),
				tunelLib.getCityId(), tunelLib.getCountryId(),
				tunelLib.getLongitude(), tunelLib.getLatitude(),
				tunelLib.getDirection() };
		int[] types = new int[] { Types.VARCHAR, Types.INTEGER, Types.INTEGER,
				Types.DOUBLE, Types.DOUBLE, Types.VARCHAR };
		return jdbcTemplate.update(sql, params, types);
	}

	@Override
	public int updateTunelLib(TunelLib tunelLib) throws Exception {
		String sql = "UPDATE wy_lib_tunnel set CITY_ID=?,COUNTRY_ID=?,LONGITUDE=?,LATITUDE=?,DIRECTION=?,UPDATETIME=sysdate where ID=?";
		Object[] params = new Object[] { tunelLib.getCityId(),
				tunelLib.getCountryId(), tunelLib.getLongitude(),
				tunelLib.getLatitude(), tunelLib.getDirection(),
				tunelLib.getId() };
		int[] types = new int[] { Types.INTEGER, Types.INTEGER, Types.DOUBLE,
				Types.DOUBLE, Types.VARCHAR, Types.INTEGER };
		return jdbcTemplate.update(sql, params, types);
	}

	@Override
	public int updateTunelLibByDelFlag(TunelLib tunelLib) throws Exception {
		String sql = "UPDATE wy_lib_tunnel SET DELETE_FLAG=1,UPDATETIME=sysdate WHERE ID=?";
		Object[] params = new Object[] { tunelLib.getId() };
		int[] types = new int[] { Types.BIGINT };
		return jdbcTemplate.update(sql, params, types);
	}

	@Override
	public int insertWyBtsSpecial(WyBtsSpecial wyBtsSpecial) throws Exception {
		String sql = "INSERT INTO WY_BTS_SPECIAL(INT_ID,NAME,CITY_ID,BSC_NAME,BTS_ID,BTSNAME,INTIME,UPDATETIME,TYPE,NET_TYPE,STATE,DELETE_FLAG)VALUES(?,?,?,?,?,?,sysdate,sysdate,?,?,?,0)";
		Object[] params = new Object[] { wyBtsSpecial.getIntId(), wyBtsSpecial.getName(),wyBtsSpecial.getCityId(),
				wyBtsSpecial.getBscName(), wyBtsSpecial.getBtsId(), wyBtsSpecial.getBtsName(),wyBtsSpecial.getType(),wyBtsSpecial.getNetType(),
				wyBtsSpecial.getState()};
		int[] types = new int[] { Types.BIGINT, Types.VARCHAR, Types.INTEGER,Types.VARCHAR,
				Types.BIGINT, Types.VARCHAR, Types.INTEGER, Types.INTEGER,Types.INTEGER  };
		return jdbcTemplate.update(sql, params, types);
	}

	@Override
	public int updateWyBtsSpecial(WyBtsSpecial wyBtsSpecial) throws Exception {
		String sql = "UPDATE WY_BTS_SPECIAL SET NAME=?,CITY_ID=?，BSC_NAME=?,BTS_ID=?,BTSNAME=?,UPDATETIME=SYSDATE,TYPE=?,NET_TYPE=?,STATE=? where INT_ID=?";
		Object[] params = new Object[] {wyBtsSpecial.getName(),wyBtsSpecial.getCityId(),
				wyBtsSpecial.getBscName(), wyBtsSpecial.getBtsId(), wyBtsSpecial.getBtsName(),wyBtsSpecial.getType(),wyBtsSpecial.getNetType(),
				wyBtsSpecial.getState(),wyBtsSpecial.getIntId()};
		int[] types = new int[] { Types.VARCHAR, Types.INTEGER,Types.VARCHAR,
				Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER,Types.INTEGER,Types.BIGINT};
		return jdbcTemplate.update(sql, params, types);
	}

	@Override
	public int updateWyBtsSpecialByDelFlag(WyBtsSpecial wyBtsSpecial)
			throws Exception {
		String sql = "UPDATE WY_BTS_SPECIAL SET DELETE_FLAG=1,UPDATETIME=sysdate WHERE INT_ID=?";
		Object[] params = new Object[] { wyBtsSpecial.getIntId() };
		int[] types = new int[] { Types.BIGINT };
		return jdbcTemplate.update(sql, params, types);
	}

	@Override
	public Map<String, WyBtsSpecial> getBtsSpecial(int netType) throws Exception {
		String sql = "select INT_ID,NAME,DELETE_FLAG from WY_BTS_SPECIAL where NET_TYPE=?";
		Object[] params = new Object[] { netType };
		int[] types = new int[] { Types.INTEGER };
		List list = jdbcTemplate.queryForList(sql, params, types);
		Map<String, WyBtsSpecial> specialMap = new HashMap<String, WyBtsSpecial>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			long intId = StringUtils.objToLong(map.get("INT_ID"));
			String name = StringUtils.objToString(map.get("NAME"));
			int deleteFlag = StringUtils.objToInt(map.get("DELETE_FLAG"));
			WyBtsSpecial wyBtsSpecial = new WyBtsSpecial();
			wyBtsSpecial.setIntId(intId);
			wyBtsSpecial.setName(name);
			wyBtsSpecial.setDeleteFlag(deleteFlag);
			specialMap.put(intId+"", wyBtsSpecial);
		}
		return specialMap;
	}
	
	
	
	
	
	

}
