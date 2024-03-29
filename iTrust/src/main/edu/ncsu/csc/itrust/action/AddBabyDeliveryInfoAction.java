package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.action.base.PatientBaseAction;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.BabyDeliveryInfoDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import java.util.List;

/**
 * ViewPatientAction is just a class to help the edit demographics page get all the users that should
 * be displayed on the page.
 */
public class AddBabyDeliveryInfoAction extends PatientBaseAction {

    /**
     * patientDAO is the patientDAO that retrieves the users from the database
     */
    private BabyDeliveryInfoDAO babyDeliveryInfoDAO;
    /**
     * loggedInMID is the patient that is logged in.
     */
    private long loggedInMID;
    /**
     * Viewer is the patient bean for the person that is logged in
     */
    private List<BabyDeliveryInfoBean> viewer;

    /**
     * ViewPateintAction is the constructor for this action class. It simply initializes the
     * instance variables.
     *
     * @param factory     The factory used to get the patientDAO.
     * @param loggedInMID The MID of the logged in user.
     * @param pidString   The user ID patient we are viewing.
     * @throws ITrustException When there is a bad user.
     */
    public AddBabyDeliveryInfoAction(DAOFactory factory, long loggedInMID, String pidString)
            throws ITrustException {
        super(factory, pidString);
        this.babyDeliveryInfoDAO = factory.getBabyDeliveryInfoDAO();
        this.loggedInMID = loggedInMID;
        this.viewer = babyDeliveryInfoDAO.getBabyDeliveryInfosForMID(loggedInMID);
        TransactionLogger.getInstance().logTransaction(TransactionType.ACTIVITY_FEED_VIEW, loggedInMID, 0L, "");
    }

    /**
     * getViewablePateints returns a list of patient beans that should be viewed by this
     * patient.
     *
     * @return The list of this users dependents and this user.
     * @throws ITrustException When there is a bad user.
     */
    public List<BabyDeliveryInfoBean> getAllRecords(String input) throws ITrustException {
        List<BabyDeliveryInfoBean> result;
        try {
            result = babyDeliveryInfoDAO.getBabyDeliveryInfosForMID(Long.valueOf(input));
        } catch (DBException e) {
            throw new ITrustException("Invalid Record");
        }
        return result;
    }

    /**
     * Add a baby delivery info
     *
     * @param info
     * @throws ITrustException
     */
    public void addBabyDeliveryInfo(BabyDeliveryInfoBean info) throws ITrustException {
        try {
            babyDeliveryInfoDAO.addBabyDeliveryInfo(info);
        } catch (DBException e) {
            e.printStackTrace();
            throw new ITrustException("Invalid Record");
        }
    }
}
