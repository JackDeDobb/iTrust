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
		while (rs.next())
			list.add(loadSingle(rs));
		return list;
	}
	@Override
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
		}
		Date EDD = rs.getDate("EDD");
		if(EDD != null) {
			p.setEDD(EDD);
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
		ps.setString(i++, p.getMID());
		ps.setString(i++, p.getRecordId());
		ps.setString(i++, p.getYearsOfConception());
		ps.setString(i++, p.getNumberOfHoursInLabor());
		ps.setString(i++, p.getWeightGainDuringPregnancy());
		ps.setString(i++, p.getDeliveryType());
		ps.setString(i++, p.getNumBirths());
		Date date = null;
		try {
			date = new java.sql.Date(DATE_FORMAT.parse(p.getLMP())
					.getTime());
		} catch (ParseException e) {
			if ("".equals(p.getLMP())){
				date = null;
			}
		}
		ps.setDate(i++, date);
		
		date = null;
		try {
			date = new java.sql.Date(DATE_FORMAT.parse(p.getEDD())
					.getTime());
		} catch (ParseException e) {
			if ("".equals(p.getEDD())){
				date = null;
			}
		}
		ps.setDate(i++, date);
		
		date = null;
		try {
			date = new java.sql.Date(DATE_FORMAT.parse(p.getInitDate())
					.getTime());
		} catch (ParseException e) {
			if ("".equals(p.getInitDate())){
				date = null;
			}
		}catch (NullPointerException e) {
			if ("".equals(p.getInitDate())){
				date = null;
			}else{
				
			}
		}
		ps.setDate(i++, date);
		return ps;
	}
	
	
	
	
}