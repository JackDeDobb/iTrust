package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;

/**
 * A loader for ObstetricOfficeVisitBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class ObstetricOfficeVisitLoader implements BeanLoader<ObstetricOfficeVisitBean> {
	
	/**
	 * loadList
	 * @param rs rs
	 * @throws SQLException
	 */
	@Override
	public List<ObstetricOfficeVisitBean> loadList(ResultSet rs) throws SQLException {
		List<ObstetricOfficeVisitBean> list = new ArrayList<ObstetricOfficeVisitBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	private void loadCommon(ResultSet rs, ObstetricOfficeVisitBean oov) throws SQLException{
		oov.setVisitId(rs.getLong("visitId"));
		oov.setPatientMID(rs.getLong("patientMID"));
		oov.setHcpMID(rs.getLong("hcpMID"));
		oov.setObstetricRecordID(rs.getLong("obstetricRecordID"));
		oov.setWeight(rs.getFloat("weight"));
		oov.setSystolicBloodPressure(rs.getFloat("systolicBP"));
		oov.setDiastolicBloodPressure(rs.getFloat("diastolicBP"));
		oov.setFetalHeartRate(rs.getFloat("fetalHeartRate"));
		oov.setLowLyingPlacentaObserved(rs.getInt("lowLyingPlacentaObserved"));
		oov.setNumberOfBabies(rs.getInt("numberOfBabies"));
		oov.setVisitDate(rs.getTimestamp("visitDate"));
	}
	
	/**
	 * loadSingle
	 * @param rs rs
	 * @return oov
	 * @throws SQLException
	 */
	@Override
	public ObstetricOfficeVisitBean loadSingle(ResultSet rs) throws SQLException {
		ObstetricOfficeVisitBean oov = new ObstetricOfficeVisitBean();
		loadCommon(rs, oov);
		return oov;
	}

	/**
	 * Load parameters for an insert query into the obstetric office visit table.
	 * @throws SQLException
	 */
	public PreparedStatement loadInsertParameters(PreparedStatement ps, ObstetricOfficeVisitBean oov) throws SQLException {
		int i = 1;
		ps.setLong(i++, oov.getObstetricRecordID());
		ps.setLong(i++, oov.getPatientMID());
		ps.setLong(i++, oov.getHcpMID());
		ps.setFloat(i++, oov.getWeight());
		ps.setFloat(i++, oov.getSystolicBloodPressure());
		ps.setFloat(i++, oov.getDiastolicBloodPressure());
		ps.setFloat(i++, oov.getFetalHeartRate());
		ps.setInt(i++, oov.getLowLyingPlacentaObserved());
		ps.setInt(i++, oov.getNumberOfBabies());
		ps.setTimestamp(i++, oov.getVisitDate());
		return ps;
	}

	/**
	 * Load parameters for an update query into the obstetric office visit table.
	 * @throws SQLException
	 */
	public PreparedStatement loadUpdateParameters(PreparedStatement ps, ObstetricOfficeVisitBean oov) throws SQLException {
		int i = 1;
		ps.setLong(i++, oov.getObstetricRecordID());
		ps.setLong(i++, oov.getPatientMID());
		ps.setLong(i++, oov.getHcpMID());
		ps.setFloat(i++, oov.getWeight());
		ps.setFloat(i++, oov.getSystolicBloodPressure());
		ps.setFloat(i++, oov.getDiastolicBloodPressure());
		ps.setFloat(i++, oov.getFetalHeartRate());
		ps.setInt(i++, oov.getLowLyingPlacentaObserved());
		ps.setInt(i++, oov.getNumberOfBabies());
		ps.setTimestamp(i++, oov.getVisitDate());
		ps.setLong(i++, oov.getVisitId());
		return ps;
	}

	/**
	 * loadParameters
	 * @throws SQLException
	 */
	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, ObstetricOfficeVisitBean oov) throws SQLException {
		int i = 1;
		ps.setLong(i++, oov.getVisitId());
		ps.setLong(i++, oov.getPatientMID());
		ps.setLong(i++, oov.getHcpMID());
		ps.setLong(i++, oov.getObstetricRecordID());
		ps.setFloat(i++, oov.getWeight());
		ps.setFloat(i++, oov.getSystolicBloodPressure());
		ps.setFloat(i++, oov.getDiastolicBloodPressure());
		ps.setFloat(i++, oov.getFetalHeartRate());
		ps.setInt(i++, oov.getLowLyingPlacentaObserved());
		ps.setInt(i++, oov.getNumberOfBabies());
		ps.setTimestamp(i++, oov.getVisitDate());
		return ps;
	}

}
