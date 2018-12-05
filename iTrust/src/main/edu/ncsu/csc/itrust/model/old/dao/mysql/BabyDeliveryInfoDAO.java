package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.BabyDeliveryInfoLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

public class BabyDeliveryInfoDAO {
	private DAOFactory factory;
	private BabyDeliveryInfoLoader loader;
	
	public BabyDeliveryInfoDAO(DAOFactory factory) {
		this.factory = factory;
		this.loader = new BabyDeliveryInfoLoader();
	}

	/**
	 * Returns the Baby delivery information for a given id
	 * 
	 * @param id
	 *            The id of the BabyDeliveryInfo to retrieve
	 * @return A BabyDeliveryInfo representing the given id.
	 * @throws DBException
	 */
	public BabyDeliveryInfoBean getMostRecentChildBirthVisitForMID(long mid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM babyDeliveryInfo WHERE MID = ? ORDER BY" +
						"id DESC LIMIT 1")) {

			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			BabyDeliveryInfoBean record = loader.loadSingle(rs);
			rs.close();

			return record;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Adds a visit into the babyDeliveryInfo table
	 * 
	 * @param bdInfoBean
	 *            The BabyDeliveryInfo bean representing the new information to be added
	 * @throws DBException
	 */
	public long addBabyDeliveryInfo(BabyDeliveryInfoBean bdInfoBean) throws DBException {
		
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = loader.loadParameters(
						conn.prepareStatement(
								"INSERT INTO babyDeliveryInfo "
									+"(MID,childBirthVisitId,gender,birthTime,deliveryType,isEstimated) "
									+"VALUES(?,?,?,?,?,?)"), bdInfoBean))
		{
			
			System.out.println(ps.toString());
			ps.executeUpdate();
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Updates a baby's delivery information for the given bean
	 * 
	 * @param bdInfoBean
	 *            The BabyDeliveryInfo bean representing the new information for the
	 *            delivery.
	 * @throws DBException
	 */
	public void updateBabyDeliveryInfo(BabyDeliveryInfoBean bdInfoBean) throws DBException {
		
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = loader.loadParameters(
						conn.prepareStatement("UPDATE babyDeliveryInfo SET "
							+"MID=?, id=?, childBirthVisitId=?, gender=?, birthTime=?, deliveryType=?, isEstimated=?, "
							+"WHERE id=?"), bdInfoBean))
		{
			ps.setLong(8, bdInfoBean.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	/**
	 * Returns a list of BabyDeliveryInfo given by the childBirthVisitId
	 * 
	 * @param childBirthVisitId
	 *            The childBirthVisitId of the visit in question
	 * @return A java.util.List of BabyDeliveryInfo Beans.
	 * @throws DBException
	 */
	public List<BabyDeliveryInfoBean> getBabyDeliveryInfosForMID(long mid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM babyDeliveryInfo WHERE MID = ?")) {
			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			List<BabyDeliveryInfoBean> records = rs.next() ? loader.loadList(rs) : null;
			rs.close();
			return records;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	public BabyDeliveryInfoBean getRecordById(long id) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM babyDeliveryInfo WHERE id = ?")) {
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			BabyDeliveryInfoBean record = rs.next() ? loader.loadSingle(rs) : null;
			rs.close();
			return record;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
