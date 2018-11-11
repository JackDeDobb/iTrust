package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetricOfficeVisitLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PatientLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PersonnelLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;


/**
 * Used for managing all static information related to a ObstetricOfficeVisit. 
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be
 * reflections of the database, that is, one DAO per table in the database (most
 * of the time). For more complex sets of queries, extra DAOs are added. DAOs
 * can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than
 * a factory. All DAOs should be accessed by DAOFactory (@see
 * {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 */
public class ObstetricOfficeVisitDAO {
	private DAOFactory factory;
	private ObstetricOfficeVisitLoader ObsVisitLoader;
	
	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public ObstetricOfficeVisitDAO(DAOFactory factory) {
		this.factory = factory;
		this.ObsVisitLoader = new ObstetricOfficeVisitLoader();
	}
	
	
	public long addObstetricOfficeVisit(ObstetricOfficeVisitBean ObsVisitBean) throws DBException {
		
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = ObsVisitLoader.loadParameters(conn.prepareStatement("INSERT INTO obstetricOfficeVisit"
																							+"(visitId,obstetricRecordID,weight,bloodPressure,"
																							+"fetalHeartRate,fetalHeartRate,lowLyingPlacentaObserved,numberOfBabies)"
																							+"VALUES(?,?,?,?,?,?,?,?)"), ObsVisitBean)) {
			ps.executeUpdate();
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	
	public void editObstetricOfficeVisit(ObstetricOfficeVisitBean ObsVisitBean) throws DBException {
		
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = ObsVisitLoader.loadParameters(conn.prepareStatement("UPDATE obstetricOfficeVisit SET "
																							+"visitId=?, obstetricRecordID=?, weight=?, bloodPressure=?,"
																							+"fetalHeartRate=?, fetalHeartRate=?, lowLyingPlacentaObserved=?, numberOfBabies=?"
																							+"WHERE visitId=?"), ObsVisitBean)) {
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	
	public List<ObstetricOfficeVisitBean> getObstetricOfficeVisitByInitRecord(long obstetricInitID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM obstetricOfficeVisit "
						+ "WHERE obstetricRecordID=?")) {
			ps.setLong(1, obstetricInitID);
			ResultSet rs = ps.executeQuery();
			List<ObstetricOfficeVisitBean> loadlist = ObsVisitLoader.loadList(rs);
			rs.close();
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	
	public ObstetricOfficeVisitBean getObstetricOfficeVisitByID(long visitID) throws DBException {
		
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM obstetricOfficeVisit WHERE visitId = ?")) {
			ps.setLong(1, visitID);
			ResultSet rs = ps.executeQuery();
			ObstetricOfficeVisitBean obsVisitBean = rs.next() ? ObsVisitLoader.loadSingle(rs) : null;
			rs.close();
			return obsVisitBean;
		} catch (SQLException e) {
			throw new DBException(e);
		}
		
	}
}
