package edu.ncsu.csc.itrust.model.old.dao.mysql;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ChildBirthVisitLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * this class is used to interact with the datbaase to add, get, edit, or remove
 * child birth visits
 */
public class ChildBirthVisitDAO {
    private DAOFactory factory;
    private ChildBirthVisitLoader loader;

    /**
     * makes a new ChildBirthVisitDAO
     *
     * @param factory the DAOfactory used to make a ChildBirthVisitDAO
     */
    public ChildBirthVisitDAO(DAOFactory factory) {
        this.factory = factory;
        this.loader = new ChildBirthVisitLoader();
    }

    /**
     * Get all child birth visits for a patient.
     *
     * @param mid patient's MID
     * @return a list of child birth visits.
     * @throws DBException
     */
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

    /**
     * add a new child birth visit to database
     *
     * @param info the visit being added to the database
     * @throws DBException
     */
    public void addChildBirthVisit(ChildBirthVisitBean info) throws DBException {
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = loader.loadParameters(conn.prepareStatement("INSERT INTO childBirthVisit "
                     + "(MID, id,visitId,obstetricInitId,previouslyScheduled,preferredDeliveryType,delivered, "
                     + "pitocinDosage,nitrousOxideDosage,epiduralAnaesthesiaDosage,magnesiumSulfateDosage,rhImmuneGlobulinDosage) "
                     + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)"), info)) {
            System.out.println(ps.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * update a child birth visit in the database
     *
     * @param info the visit being updated
     * @throws DBException
     */
    public void updateChildBirthVisit(ChildBirthVisitBean info) throws DBException {
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = loader.loadParameters(conn.prepareStatement("UPDATE childBirthVisit SET "
                     + "MID=?, id=?, visitId=?, obstetricInitId=?, previouslyScheduled=?, preferredDeliveryType=?, delivered=?, "
                     + "pitocinDosage=?, nitrousOxideDosage=?, epiduralAnaesthesiaDosage=?, magnesiumSulfateDosage=?, rhImmuneGlobulinDosage=? "
                     + "WHERE id=?"), info)) {
            ps.setLong(13, info.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * returns the child birth visit with a specific recordId
     *
     * @param recordId the id of the visit
     * @return the visit with the id
     * @throws DBException
     */
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
