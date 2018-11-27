package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;
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
	public BabyDeliveryInfoBean getBabyDeliveryInfoByID(long id) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM babyDeliveryInfo WHERE id = ? ")) {

			ps.setLong(1, id);
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
									+"(id,childBirthVisitId,gender,birthTime,deliveryType,isEstimated) "
									+"VALUES(?,?,?,?,?,?)"), bdInfoBean))
		{
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
	public void editBabyDeliveryInfo(BabyDeliveryInfoBean bdInfoBean) throws DBException {
		
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = loader.loadParameters(
						conn.prepareStatement("UPDATE babyDeliveryInfo SET "
							+"id=?, childBirthVisitId=?, gender=?, birthTime=?, deliveryType=?, isEstimated=?, "
							+"WHERE id=?"), bdInfoBean))
		{
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
	public List<BabyDeliveryInfoBean> getBabyDeliveryInfoList(long childBirthVisitId) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM babyDeliveryInfo WHERE childBirthVisitId = ?")) {
			ps.setLong(1, childBirthVisitId);
			ResultSet rs = ps.executeQuery();
			List<BabyDeliveryInfoBean> records = rs.next() ? loader.loadList(rs) : null;
			rs.close();
			return records;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
