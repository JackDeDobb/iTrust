package edu.ncsu.csc.itrust.model.old.dao.mysql;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetricOfficeVisitLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * Used for managing all static information related to a ObstetricOfficeVisit.
 * <p>
 * DAO stands for Database Access Object. All DAOs are intended to be
 * reflections of the database, that is, one DAO per table in the database (most
 * of the time). For more complex sets of queries, extra DAOs are added. DAOs
 * can assume that all data has been validated and is correct.
 * <p>
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
     * @param factory The {@link DAOFactory} associated with this DAO, which is used
     *                for obtaining SQL connections, etc.
     */
    public ObstetricOfficeVisitDAO(DAOFactory factory) {
        this.factory = factory;
        this.ObsVisitLoader = new ObstetricOfficeVisitLoader();
    }


    /**
     * Adds a visit into the obstetricOfficeVisit table
     *
     * @param ObsVisitBean The ObstetricOfficeVisit bean representing the new information to be added
     * @throws DBException
     */
    public long addObstetricOfficeVisit(ObstetricOfficeVisitBean ObsVisitBean) throws DBException {

        try (Connection conn = factory.getConnection();
             PreparedStatement ps = ObsVisitLoader.loadInsertParameters(
                     conn.prepareStatement(
                             "INSERT INTO obstetricOfficeVisit "
                                     + "(obstetricRecordID, patientMID,hcpMID,weight,systolicBP,diastolicBP, "
                                     + "fetalHeartRate,lowLyingPlacentaObserved,numberOfBabies,visitDate) "
                                     + "VALUES(?,?,?,?,?,?,?,?,?,?)"), ObsVisitBean)) {
            ps.executeUpdate();
            return DBUtil.getLastInsert(conn);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }


    /**
     * Updates a visit's information for the given visitId
     *
     * @param ObsVisitBean The ObstetricOfficeVisit bean representing the new information for the
     *                     visit.
     * @throws DBException
     */
    public void editObstetricOfficeVisit(ObstetricOfficeVisitBean ObsVisitBean) throws DBException {

        try (Connection conn = factory.getConnection();
             PreparedStatement ps = ObsVisitLoader.loadUpdateParameters(
                     conn.prepareStatement("UPDATE obstetricOfficeVisit SET "
                             + "obstetricRecordID=?, patientMID=?, hcpMID=?, weight=?, systolicBP=?, diastolicBP=?, "
                             + "fetalHeartRate=?, lowLyingPlacentaObserved=?, numberOfBabies=?, visitDate=? "
                             + "WHERE visitId=?"), ObsVisitBean)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Returns the visit's information for a given visitID
     *
     * @param visitID The visitId of the ObstetricOfficeVisit to retrieve
     * @return A ObstetricOfficeVisitBean representing the Obstetric office visit.
     * @throws DBException
     */
    public ObstetricOfficeVisitBean getObstetricOfficeVisitByID(long visitID) throws DBException {

        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM obstetricOfficeVisit WHERE visitId = ?")) {
            ps.setLong(1, visitID);
            ResultSet rs = ps.executeQuery();
            ObstetricOfficeVisitBean obsVisitBean = rs.next() ? ObsVisitLoader.loadSingle(rs) : null;
            rs.close();
            return obsVisitBean;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Returns all visits for a given patient
     *
     * @param patientMID The patientMID of the ObstetricOfficeVisits to retrieve
     * @return A list of ObstetricOfficeVisitBeans
     * @throws DBException
     */
    public List<ObstetricOfficeVisitBean> getObstetricOfficeVisitsByPatientMID(long patientMID) throws DBException {

        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM obstetricOfficeVisit WHERE patientMID = ?")) {
            ps.setLong(1, patientMID);
            ResultSet rs = ps.executeQuery();
            List<ObstetricOfficeVisitBean> loadlist = ObsVisitLoader.loadList(rs);
            rs.close();
            return loadlist;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Returns the most recent visit for a given patient by date
     *
     * @param patientMID The patientMID of the ObstetricOfficeVisits to retrieve
     * @return A list of ObstetricOfficeVisitBeans
     * @throws DBException
     */
    public ObstetricOfficeVisitBean getMostRecentObstetricOfficeVisitsByPatientMID(long patientMID) throws DBException {

        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM obstetricOfficeVisit WHERE patientMID = ? "
                             + "ORDER BY visitDate DESC LIMIT 1")) {
            ps.setLong(1, patientMID);
            ResultSet rs = ps.executeQuery();
            ObstetricOfficeVisitBean obsVisitBean = rs.next() ? ObsVisitLoader.loadSingle(rs) : null;

            rs.close();
            return obsVisitBean;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}