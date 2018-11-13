package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricInfoBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetricInfoLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

public class ObstetricInfoDAO {
	private DAOFactory factory;
	private ObstetricInfoLoader loader;
	
	public ObstetricInfoDAO(DAOFactory factory) {
		this.factory = factory;
		this.loader = new ObstetricInfoLoader();
	}
	
	public List<ObstetricInfoBean> getObstetricInfoForMID(long mid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM obstetricsInfo WHERE MID = ? ORDER BY initDate DESC")) {
			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			List<ObstetricInfoBean> records = rs.next() ? loader.loadList(rs) : null;
			rs.close();
			return records;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	public void addObstetricInfo(ObstetricInfoBean info) 
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = loader.loadParameters(conn.prepareStatement("INSERT INTO obstetricsInfo(MID, yearsOfConception, "
						+ "numberOfHoursInLabor, WeightGainDuringPregnancy, deliveryType, numBirths, LMP, EDD, "
						+ "initDate VALUES(?, ?, ?, ?, ?, ?, ?, ?, NOW())"), info)) {
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	 
	public void updateObstetricInfo(ObstetricInfoBean info) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = loader
						.loadParameters(conn.prepareStatement("UPDATE obstetricsInfo SET MID=?,yearsOfConception=?,numberOfHoursInLabor=?,"
								+ "WeightGainDuringPregnancy=?,deliveryType=?,numBirths=?,LMP=?,EDD=?,initDate=? WHERE recordId=?"), info)) {
			ps.setLong(10, info.getRecordId());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
