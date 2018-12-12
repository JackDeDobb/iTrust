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
							+"MID=?, childBirthVisitId=?, gender=?, birthTime=?, deliveryType=?, isEstimated=?  "
							+"WHERE id=?"), bdInfoBean))
		{
			ps.setLong(7, bdInfoBean.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	/**
	 * Returns a list of BabyDeliveryInfo given by the childBirthVisitId
	 * @return A java.util.List of BabyDeliveryInfo Beans.
	 * @throws DBException
	 */
	public List<BabyDeliveryInfoBean> getBabyDeliveryInfosForMID(long mid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM babyDeliveryInfo WHERE MID = ? ORDER BY id DESC")) {
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
