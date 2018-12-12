package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.action.base.PatientBaseAction;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricInfoBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricInfoDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import java.util.List;

/**
 * ViewPatientAction is just a class to help the edit demographics page get all the users that should
 * be displayed on the page.
 */
public class ViewObstetricInfoAction extends PatientBaseAction {

    /**
     * patientDAO is the patientDAO that retrieves the users from the database
     */
    private ObstetricInfoDAO obstetricInfoDAO;
    /**
     * loggedInMID is the patient that is logged in.
     */
    private long loggedInMID;
    /**
     * Viewer is the patient bean for the person that is logged in
     */
    private List<ObstetricInfoBean> viewer;

    /**
     * ViewPateintAction is the constructor for this action class. It simply initializes the
     * instance variables.
     *
     * @param factory     The facory used to get the patientDAO.
     * @param loggedInMID The MID of the logged in user.
     * @param pidString   The user ID patient we are viewing.
     * @throws ITrustException When there is a bad user.
     */
    public ViewObstetricInfoAction(DAOFactory factory, long loggedInMID, String pidString)
            throws ITrustException {
        super(factory, pidString);
        this.obstetricInfoDAO = factory.getObstetricInfoDAO();
        this.loggedInMID = loggedInMID;
        this.viewer = obstetricInfoDAO.getObstetricInfoForMID(loggedInMID);
        TransactionLogger.getInstance().logTransaction(TransactionType.ACTIVITY_FEED_VIEW, loggedInMID, 0L, "");
    }

    /**
     * getViewablePateints returns a list of patient beans that should be viewed by this
     * patient.
     *
     * @return The list of this users dependents and this user.
     * @throws ITrustException When there is a bad user.
     */
    public List<ObstetricInfoBean> getAllRecords(String input) throws ITrustException {
        List<ObstetricInfoBean> result;
        try {
            result = obstetricInfoDAO.getObstetricInfoForMID(Long.valueOf(input));
        } catch (DBException e) {
            throw new ITrustException("Invalid Record");
        }
        return result;
    }


    /**
     * get obstetric record for a specific id
     *
     * @param input the record id
     * @return an obstetric record
     * @throws ITrustException
     */
    public ObstetricInfoBean getRecordById(long input) throws ITrustException {
        ObstetricInfoBean result;
        try {
            result = obstetricInfoDAO.getRecordById(input);
        } catch (DBException e) {
            throw new ITrustException("Invalid Record");
        }
        return result;
    }

    /**
     * update the obstetric record in the database
     *
     * @param info the record being updated
     * @throws ITrustException
     */
    public void updateRecord(ObstetricInfoBean info) throws ITrustException {
        try {
            obstetricInfoDAO.updateObstetricInfo(info);
        } catch (DBException e) {
            throw new ITrustException("Invalid Record");
        }
    }

    /**
     * add new obstetric record into database
     *
     * @param info the record being added
     * @throws ITrustException
     */
    public void addNewRecord(ObstetricInfoBean info) throws ITrustException {
        try {
            obstetricInfoDAO.addObstetricInfo(info);
        } catch (DBException e) {
            throw new ITrustException("Invalid Record");
        }
    }
}
