package edu.ncsu.csc.itrust.model.old.dao.mysql;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricInfoBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetricInfoLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ObstetricInfoDAO {
    private DAOFactory factory;
    private ObstetricInfoLoader loader;

    public ObstetricInfoDAO(DAOFactory factory) {
        this.factory = factory;
        this.loader = new ObstetricInfoLoader();
    }

    /**
     * Get the most recent obstetric record for a patient.
     *
     * @param mid Patirnt's MID
     * @return The obstetric record
     * @throws DBException
     */
    public ObstetricInfoBean getMostRecentObstetricInfoForMID(long mid) throws DBException {
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM obstetricsInfo WHERE MID = ? ORDER BY " +
                     " recordID DESC LIMIT 1")) {

            ps.setLong(1, mid);
            ResultSet rs = ps.executeQuery();
            ObstetricInfoBean record = rs.next() ? loader.loadSingle(rs) : null;
            rs.close();

            return record;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Get all obstetric record for a patient.
     *
     * @param mid The patient's MID
     * @return A list of all obstetric record for a patient
     * @throws DBException
     */
    public List<ObstetricInfoBean> getObstetricInfoForMID(long mid) throws DBException {
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM obstetricsInfo WHERE MID = ? ORDER BY " +
                     "recordID DESC")) {
            ps.setLong(1, mid);
            ResultSet rs = ps.executeQuery();
            List<ObstetricInfoBean> records = rs.next() ? loader.loadList(rs) : null;
            rs.close();
            return records;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Add an obstetric record into database
     *
     * @param info the obstetric record
     * @throws DBException
     */
    public void addObstetricInfo(ObstetricInfoBean info) throws DBException {
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = loader.loadParameters(conn.prepareStatement("INSERT INTO obstetricsInfo(MID, " +
                     "yearsOfConception, numberOfHoursInLabor, WeightGainDuringPregnancy, deliveryType, numBirths, " +
                     "LMP, EDD, initDate) VALUES(?, ?, ?, ?, ?, ?, ?, ?, NOW())"), info)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Update a obstetric record in the database.
     *
     * @param info the obstetric record
     * @throws DBException
     */
    public void updateObstetricInfo(ObstetricInfoBean info) throws DBException {
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = loader.loadParameters(conn.prepareStatement("UPDATE obstetricsInfo SET" +
                     " MID = ?, yearsOfConception = ?, numberOfHoursInLabor = ?, WeightGainDuringPregnancy = ?, " +
                     "deliveryType=  ?, numBirths = ?, LMP=?, EDD = ? WHERE recordId=?"), info)) {
            ps.setLong(9, info.getRecordId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Get the obstetric record by an id
     *
     * @param recordId the record's id
     * @return the obstetric record
     * @throws DBException
     */
    public ObstetricInfoBean getRecordById(long recordId) throws DBException {
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM obstetricsInfo WHERE recordId = ?")) {
            ps.setLong(1, recordId);
            ResultSet rs = ps.executeQuery();
            ObstetricInfoBean record = rs.next() ? loader.loadSingle(rs) : null;
            rs.close();
            return record;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
