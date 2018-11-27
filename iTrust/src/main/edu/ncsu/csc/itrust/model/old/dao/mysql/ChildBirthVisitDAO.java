package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ChildBirthVisitLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

public class ChildBirthVisitDAO {
	private DAOFactory factory;
	private ChildBirthVisitLoader loader;
	
	public ChildBirthVisitDAO(DAOFactory factory) {
		this.factory = factory;
		this.loader = new ChildBirthVisitLoader();
	}

	/**
	 * Returns the visit's information for a given id
	 * 
	 * @param id
	 *            The id of the ChildBirthVisit to retrieve
	 * @return A ChildBirthVisitBean representing the child birth visit.
	 * @throws DBException
	 */
	public ChildBirthVisitBean getChildBirthVisitByID(long id) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM childBirthVisit WHERE id = ? ")) {

			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			ChildBirthVisitBean record = loader.loadSingle(rs);
			rs.close();
			return record;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Adds a visit into the childBirthVisit table
	 * 
	 * @param cbVisitBean
	 *            The ChildBirthVisit bean representing the new information to be added
	 * @throws DBException
	 */
	public long addChildBirthVisit(ChildBirthVisitBean cbVisitBean) throws DBException {
		
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = ChildBirthVisitLoader.loadParameters(
						conn.prepareStatement(
								"INSERT INTO childBirthVisit "
									+"(id,visitID,obstetricInitId,previouslyScheduled,preferredDeliveryType,hasDelivered, "
									+"pitocinDosage,nitrousOxideDosage,epiduralAnaesthesiaDosage,magnesiumSulfateDosage,rhImmuneGlobulinDosage) "
									+"VALUES(?,?,?,?,?,?,?,?)"), cbVisitBean))
		{
			ps.executeUpdate();
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Updates a visit's information for the given bean
	 * 
	 * @param cbVisitBean
	 *            The ChildBirthVisit bean representing the new information for the
	 *            visit.
	 * @throws DBException
	 */
	public void editChildBirthVisit(ChildBirthVisitBean cbVisitBean) throws DBException {
		
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = ObsVisitLoader.loadParameters(
						conn.prepareStatement("UPDATE childBirthVisit SET "
							+"id=?, visitID=?, obstetricInitId=?, previouslyScheduled=?, preferredDeliveryType=?, hasDelivered=?, "
							+"pitocinDosage=?, nitrousOxideDosage=?, epiduralAnaesthesiaDosage=?, magnesiumSulfateDosage=?, rhImmuneGlobulinDosage=? "
							+"WHERE id=?"), cbVisitBean))
		{
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	/**
	 * Returns a list of ChildBirthVisit given by the obstetricInitId
	 * 
	 * @param obstetricInitId
	 *            The obstetricInitID of the visits in question.
	 * @return A java.util.List of ChildBirthVisit Beans.
	 * @throws DBException
	 */
	public List<ChildBirthVisitBean> getChildBirthVisitList(long obstetricInitId) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM childBirthVisit WHERE obstetricInitId = ?")) {
			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			List<ChildBirthVisitBean> records = rs.next() ? loader.loadList(rs) : null;
			rs.close();
			return records;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
