package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import edu.ncsu.csc.itrust.model.old.beans.ObstetricInfoBean;

public class ObstetricInfoLoader implements BeanLoader<ObstetricInfoBean> {
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	
	@Override
	public List<ObstetricInfoBean> loadList(ResultSet rs) throws SQLException {
		List<ObstetricInfoBean> list = new ArrayList<ObstetricInfoBean>();
		list.add(loadSingle(rs));
		while (rs.next())
			list.add(loadSingle(rs));
		return list;
	}

	private void loadCommon(ResultSet rs, ObstetricInfoBean p) throws SQLException{
		p.setMID(rs.getInt("MID"));
		p.setRecordId(rs.getInt("recordId"));
		p.setYearsOfConception(rs.getInt("yearsOfConception"));
		p.setNumberOfHoursInLabor(rs.getInt("numberOfHoursInLabor"));
		p.setWeightGainDuringPregnancy(rs.getInt("weightGainDuringPregnancy"));
		p.setDeliveryType(rs.getString("deliveryType"));
		p.setNumBirths(rs.getInt("numBirths"));
		Date LMP = rs.getDate("LMP");
		if(LMP != null) {
			p.setLMP(LMP);
			p.setEDD();
		}
		Date initDate = rs.getDate("initDate");
		if(initDate != null) {
			p.setInitDate(initDate);
		}
		
	}
	
	@Override
	public ObstetricInfoBean loadSingle(ResultSet rs) throws SQLException {
		ObstetricInfoBean p = new ObstetricInfoBean();
		loadCommon(rs, p);
		return p;
	}
	
	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, ObstetricInfoBean p) throws SQLException {
		int i = 1;
		ps.setLong(i++, p.getMID());
		ps.setLong(i++, p.getYearsOfConception());
		ps.setLong(i++, p.getNumberOfHoursInLabor());
		ps.setLong(i++, p.getWeightGainDuringPregnancy());
		ps.setString(i++, p.getDeliveryType().getName());
		ps.setLong(i++, p.getNumBirths());
		Date LMP = null;
		try {
			LMP = new java.sql.Date(p.getLMP().getTime());
		} catch (NullPointerException e) {
			LMP = new java.sql.Date((new java.util.Date()).getTime());
		}
		ps.setDate(i++, LMP);
		
		Date EDD = null;
		try {
			EDD = new java.sql.Date(p.getEDD().getTime());
		} catch (NullPointerException e) {
			LMP = new java.sql.Date((new java.util.Date()).getTime());
		}
		ps.setDate(i++, EDD);
		return ps;
	}
	
	
	
	
}