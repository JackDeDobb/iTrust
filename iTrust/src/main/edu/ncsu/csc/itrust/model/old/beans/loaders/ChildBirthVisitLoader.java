package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;

public class ChildBirthVisitLoader implements BeanLoader<ChildBirthVisitBean> {

	@Override
	public List<ChildBirthVisitBean> loadList(ResultSet rs) throws SQLException {
		List<ChildBirthVisitBean> list = new ArrayList<ChildBirthVisitBean>();
		list.add(loadSingle(rs));
		while (rs.next())
			list.add(loadSingle(rs));
		return list;
	}

	private void loadCommon(ResultSet rs, ChildBirthVisitBean c) throws SQLException{
		c.setMID(rs.getLong("MID"));
		c.setId(rs.getLong("id"));
		c.setVisitId(rs.getLong("visitId"));
		c.setObstetricInitId(rs.getLong("obstetricInitId"));
		c.setPreviouslyScheduled(rs.getBoolean("previouslyScheduled"));
		c.setPreferredDeliveryType(rs.getString("preferredDeliveryType"));
		c.setDelivered(rs.getBoolean("delivered"));
		c.setPitocinDosage(rs.getFloat("pitocinDosage"));
		c.setNitrousOxideDosage(rs.getFloat("nitrousOxideDosage"));
		c.setEpiduralAnaesthesiaDosage(rs.getFloat("epiduralAnaesthesiaDosage"));
		c.setMagnesiumSulfateDosage(rs.getFloat("magnesiumSulfateDosage"));
		c.setRhImmuneGlobulinDosage(rs.getFloat("rhImmuneGlobulinDosage"));
	}
	
	@Override
	public ChildBirthVisitBean loadSingle(ResultSet rs) throws SQLException {
		ChildBirthVisitBean c = new ChildBirthVisitBean();
		loadCommon(rs, c);
		return c;
	}
	
	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, ChildBirthVisitBean c) throws SQLException {
		int i = 1;
		ps.setLong(i++, c.getMID());
		ps.setLong(i++, c.getId());
		ps.setLong(i++, c.getVisitId());
		ps.setLong(i++, c.getObstetricInitId());
		ps.setBoolean(i++, c.isPreviouslyScheduled());
		ps.setString(i++, c.getPreferredDeliveryType().getName());
		ps.setBoolean(i++, c.isDelivered());
		ps.setFloat(i++, c.getPitocinDosage());
		ps.setFloat(i++,  c.getNitrousOxideDosage());
		ps.setFloat(i++, c.getEpiduralAnaesthesiaDosage());
		ps.setFloat(i++, c.getMagnesiumSulfateDosage());
		ps.setFloat(i++, c.getRhImmuneGlobulinDosage());
		return ps;
	}
	
}