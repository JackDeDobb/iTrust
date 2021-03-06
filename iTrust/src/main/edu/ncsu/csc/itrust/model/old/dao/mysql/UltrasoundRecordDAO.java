package edu.ncsu.csc.itrust.model.old.dao.mysql;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.UltrasoundRecordLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * Used for managing all static information related to a UltrasoundRecord.
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
public class UltrasoundRecordDAO {
    private DAOFactory factory;
    private UltrasoundRecordLoader ultrasoundLoader;

    /**
     * The typical constructor.
     *
     * @param factory The {@link DAOFactory} associated with this DAO, which is used
     *                for obtaining SQL connections, etc.
     */
    public UltrasoundRecordDAO(DAOFactory factory) {
        this.factory = factory;
        this.ultrasoundLoader = new UltrasoundRecordLoader();
    }


    /**
     * Adds a ultrasound record into the ultrasoundRecord table
     *
     * @param ultrasoundBean The ultrasoundBean bean representing the new information to be added
     * @throws DBException
     */
    public void addUltrasoundRecord(UltrasoundRecordBean ultrasoundBean) throws DBException {

        try (Connection conn = factory.getConnection();
             PreparedStatement ps = ultrasoundLoader.loadParameters(conn.prepareStatement("INSERT INTO ultrasoundRecord"
                     + "(id,visitId,crownRumpLength,biparietalDiameter,"
                     + "headCircumference,femurLength,occipitofrontalDiameter,abdominalCircumference,"
                     + "humerusLength,estimatedFetalWeight,imagePath)"
                     + "VALUES(?,?,?,?,?,?,?,?,?,?,?)"), ultrasoundBean)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Returns a list of UltrasoundRecords given by the visitID
     *
     * @param visitID The visitID of the ultrasoundRecords in question.
     * @return A java.util.List of Personnel Beans.
     * @throws DBException
     */
    public List<UltrasoundRecordBean> getUltrasoundRecordsByVisitID(long visitID) throws DBException {
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM ultrasoundRecord "
                     + "WHERE visitId=? ORDER BY id DESC")) {
            ps.setLong(1, visitID);
            ResultSet rs = ps.executeQuery();
            List<UltrasoundRecordBean> loadlist = ultrasoundLoader.loadList(rs);
            rs.close();
            return loadlist;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
