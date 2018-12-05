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

	public ChildBirthVisitBean getMostRecentChildBirthVisitForMID(long mid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM childBirthVisit WHERE MID = ? ORDER BY" +
						"id DESC LIMIT 1")) {

			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			ChildBirthVisitBean record = loader.loadSingle(rs);
			rs.close();

			return record;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public List<ChildBirthVisitBean> getChildBirthVisitsForMID(long mid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM childBirthVisit WHERE MID = ? ORDER BY " +
						"id DESC")) {
			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			List<ChildBirthVisitBean> records = rs.next() ? loader.loadList(rs) : null;
			rs.close();
			return records;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	public void addChildBirthVisit(ChildBirthVisitBean info) throws DBException{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = loader.loadParameters(conn.prepareStatement("INSERT INTO childBirthVisit "
						+"(MID, id,visitId,obstetricInitId,previouslyScheduled,preferredDeliveryType,delivered, "
						+"pitocinDosage,nitrousOxideDosage,epiduralAnaesthesiaDosage,magnesiumSulfateDosage,rhImmuneGlobulinDosage) "
						+"VALUES(?,?,?,?,?,?,?,?,?,?,?,?)"), info)) {
			System.out.println(ps.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	 
	public void updateChildBirthVisit(ChildBirthVisitBean info) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = loader.loadParameters(conn.prepareStatement("UPDATE childBirthVisit SET "
						+"MID=?, id=?, visitId=?, obstetricInitId=?, previouslyScheduled=?, preferredDeliveryType=?, delivered=?, "
						+"pitocinDosage=?, nitrousOxideDosage=?, epiduralAnaesthesiaDosage=?, magnesiumSulfateDosage=?, rhImmuneGlobulinDosage=? "
						+"WHERE id=?"), info)) {
			ps.setLong(13, info.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	public ChildBirthVisitBean getRecordById(long recordId) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM childBirthVisit WHERE id = ?")) {
			ps.setLong(1, recordId);
			ResultSet rs = ps.executeQuery();
			ChildBirthVisitBean record = rs.next() ? loader.loadSingle(rs) : null;
			rs.close();
			return record;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
