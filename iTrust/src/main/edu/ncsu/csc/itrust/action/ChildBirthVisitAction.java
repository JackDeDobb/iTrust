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
	/**obstetricInitID is the pregnancy in question */
	private long obstetricInitId;
	/**Viewer is the patient bean for the person that is logged in*/ 
	private List<ChildBirthVisitBean> viewer;

	/**
	 * ViewPateintAction is the constructor for this action class. It simply initializes the
	 * instance variables.
	 * @param factory The facory used to get the patientDAO.
	 * @param loggedInMID The MID of the logged in user.
	 * @param obstetricInitId The pregnancy we are viewing.
	 * @throws ITrustException When there is a bad user.
	 */
	public ViewChildBirthVisitAction(DAOFactory factory, long loggedInMID, long obstetricInitId)
			throws ITrustException {
		super(factory, pidString);
		this.childBirthVisitDAO = factory.getChildBirthVisitDAO();
		this.loggedInMID = loggedInMID;
		this.obstetricInitId = obstetricInitId;
		this.viewer = childBirthVisitDAO.getChildBirthVisitByID(loggedInMID);
		TransactionLogger.getInstance().logTransaction(TransactionType.ACTIVITY_FEED_VIEW, loggedInMID, 0L , "");
	}
	
	/**
	 * getAllRecords returns a list of child birth visits that should be viewed by this
	 * pregnancy.
	 * @return list of child birth visits.
	 * @throws ITrustException When there is a bad user.
	 */
	public List<ChildBirthVisitBean> getAllRecords() throws ITrustException{
		List<ChildBirthVisitBean> result;
		try {
			result = childBirthVisitDAO.getChildBirthVisitList(this.obstetricInitId);
		} catch (DBException e) {
			throw new ITrustException("Invalid Record");
		}
		return result;
	}
	
	
	
	public void editChildBirthVisitRecord(ChildBirthVisitBean visit) throws ITrustException{
		try {
			childBirthVisitDAO.editChildBirthVisit(visit);
		} catch (DBException e) {
			throw new ITrustException("Invalid Record");
		}
	}
	
	
	public void addChildBirthVisitRecord(ChildBirthVisitBean visit) throws ITrustException{
		try {
			childBirthVisitDAO.addChildBirthVisit(visit);
		} catch (DBException e) {
			throw new ITrustException("Invalid Record");
		}
	}
	
	
}
