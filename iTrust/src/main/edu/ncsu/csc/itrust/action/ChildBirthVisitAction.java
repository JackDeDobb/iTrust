package edu.ncsu.csc.itrust.action;

import java.util.List;

import edu.ncsu.csc.itrust.Messages;
import edu.ncsu.csc.itrust.action.base.PatientBaseAction;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ChildBirthVisitDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

/**
 * ViewPatientAction is just a class to help the edit demographics page get all the users that should
 * be displayed on the page.
 */
public class ChildBirthVisitAction extends PatientBaseAction {

	/**patientDAO is the patientDAO that retrieves the users from the database*/
	private ChildBirthVisitDAO childBirthVisitDAO;
	/**loggedInMID is the patient that is logged in.*/
	private long loggedInMID;
	/**Viewer is the patient bean for the person that is logged in*/ 
	private List<ChildBirthVisitBean> viewer;

	/**
	 * ViewPateintAction is the constructor for this action class. It simply initializes the
	 * instance variables.
	 * @param factory The facory used to get the patientDAO.
	 * @param loggedInMID The MID of the logged in user.
	 * @param pidString The user ID patient we are viewing.
	 * @throws ITrustException When there is a bad user.
	 */
	public ChildBirthVisitAction(DAOFactory factory, long loggedInMID, String pidString)
			throws ITrustException {
		super(factory, pidString);
		this.childBirthVisitDAO = factory.getChildBirthVisitDAO();
		this.loggedInMID = loggedInMID;
		this.viewer = childBirthVisitDAO.getChildBirthVisitsForMID(loggedInMID);
		TransactionLogger.getInstance().logTransaction(TransactionType.ACTIVITY_FEED_VIEW, loggedInMID, 0L , "");
	}
	
	/**
	 * getViewablePateints returns a list of patient beans that should be viewed by this
	 * patient.
	 * @return The list of this users dependents and this user.
	 * @throws ITrustException When there is a bad user.
	 */
	public List<ChildBirthVisitBean> getAllRecords(String input) throws ITrustException{
		List<ChildBirthVisitBean> result;
		try {
			result = childBirthVisitDAO.getChildBirthVisitsForMID(Long.valueOf(input));
		} catch (DBException e) {
			throw new ITrustException("Invalid Record");
		}
		return result;
	}
	
	
	
	
	public ChildBirthVisitBean getRecordById(long input) throws ITrustException{
		ChildBirthVisitBean result;
		try {
			result = childBirthVisitDAO.getRecordById(input);
		} catch (DBException e) {
			throw new ITrustException("Invalid Record");
		}
		return result;
	}
	
	
	public void updateRecord(ChildBirthVisitBean info) throws ITrustException{
		try {
			childBirthVisitDAO.updateChildBirthVisit(info);
		} catch (DBException e) {
			throw new ITrustException("Invalid Record");
		}
	}
	
	
	public void addChildBirthVisitRecord(ChildBirthVisitBean info) throws ITrustException{
		try {
			childBirthVisitDAO.addChildBirthVisit(info);
		} catch (DBException e) {
			throw new ITrustException("Invalid Record");
		}
	}
	
	

	public void logViewDemographics(Long mid, Long secondaryMID) {
		TransactionLogger.getInstance().logTransaction(TransactionType.DEMOGRAPHICS_VIEW, mid, secondaryMID, "");
	}
	
	public void logEditDemographics(Long mid, Long secondaryMID) {
		TransactionLogger.getInstance().logTransaction(TransactionType.DEMOGRAPHICS_EDIT, mid, secondaryMID, "");
	}
}
