package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetricOfficeVisitLoader;
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
	
	
	/**
	 * Adds a visit into the obstetricOfficeVisit table
	 * 
	 * @param ObsVisitBean
	 *            The ObstetricOfficeVisit bean representing the new information to be added
	 * @throws DBException
	 */
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
	
	
	/**
	 * Updates a visit's information for the given visitId
	 * 
	 * @param ObsVisitBean
	 *            The ObstetricOfficeVisit bean representing the new information for the
	 *            visit.
	 * @throws DBException
	 */
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
	
	
	/**
	 * Returns a list of ObstetricOfficeVisit given by the obstetricRecordID
	 * 
	 * @param obstetricInitID
	 *            The obstetricInitID of the visits in question.
	 * @return A java.util.List of Personnel Beans.
	 * @throws DBException
	 */
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

	
	/**
	 * Returns the visit's information for a given visitID
	 * 
	 * @param visitID
	 *            The visitId of the ObstetricOfficeVisit to retrieve
	 * @return A ObstetricOfficeVisitBean representing the Obstetric office visit.
	 * @throws DBException
	 */
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
