package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;

public class BabyDeliveryInfoLoader implements BeanLoader<BabyDeliveryInfoBean> {

	@Override
	public List<BabyDeliveryInfoBean> loadList(ResultSet rs) throws SQLException {
		List<BabyDeliveryInfoBean> list = new ArrayList<BabyDeliveryInfoBean>();
		list.add(loadSingle(rs));
		while (rs.next())
			list.add(loadSingle(rs));
		return list;
	}

	private void loadCommon(ResultSet rs, BabyDeliveryInfoBean b) throws SQLException{
		b.setId(rs.getLong("id"));
		b.setChildBirthVisitId(rs.getLong("childBirthVisitId"));
		b.setGender(rs.getString("gender"));
		b.setDeliveryType(rs.getString("deliveryType"));
		b.setEstimated(rs.getBoolean("isEstimated"));
		b.setBirthTime(rs.getTimestamp("birthTime"));
	}
	
	@Override
	public BabyDeliveryInfoBean loadSingle(ResultSet rs) throws SQLException {
		BabyDeliveryInfoBean b = new BabyDeliveryInfoBean();
		loadCommon(rs, b);
		return b;
	}
	
	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, BabyDeliveryInfoBean b) throws SQLException {
		int i = 1;
		ps.setLong(i++, b.getId());
		ps.setLong(i++, b.getChildBirthVisitId());
		ps.setString(i++, b.getGender().getName());
		ps.setString(i++, b.getDeliveryType().getName());
		ps.setBoolean(i++, b.isEstimated());
		ps.setTimestamp(i++, b.getBirthTime());
		return ps;
	}
	
}