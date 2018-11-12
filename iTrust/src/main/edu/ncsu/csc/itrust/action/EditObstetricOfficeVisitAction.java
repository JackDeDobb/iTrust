package src.main.edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ApptRequestDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;
import edu.ncsu.csc.itrust.model.old.validate.ObstetricOfficeVisitValidator;
import java.util.Optional;

/**
 * Edits a patient Used by editPatient.jsp
 * 
 * 
 */
public class EditObstetricOfficeVisitAction  {
    private ObstetricOfficeVisitDAO obstetricOfficeVisitDAO;
    private ObstetricOfficeVisitValidator validator;
    private ApptRequestDAO apptRequestDAO;
    private AuthDAO authDAO;
    private long loggedInMID;
    private Optional<Long> visitID;

	/**
	 * The super class validates the patient id
	 * 
	 * @param factory The DAOFactory used to create the DAOs for this action.
	 * @param loggedInMID The MID of the user who is authorizing this action.
	 * @param pidString The MID of the patient being edited.
	 * @throws ITrustException
	 */
	public EditObstetricOfficeVisitAction(ObstetricOfficeVisitDAO obstetricOfficeVisitDAO, AuthDAO authDAO,
            ApptRequestDAO apptRequestDAO, long loggedInMID) {
        this.obstetricOfficeVisitDAO = obstetricOfficeVisitDAO;
        this.apptRequestDAO = apptRequestDAO;
        this.authDAO = authDAO;
        this.loggedInMID = loggedInMID;
        this.visitID = Optional.empty();
        this.validator = new ObstetricOfficeVisitValidator();
	}

	/**
	 * Takes the information out of the ObsetricOfficeVisitBean param and updates the visit's information
	 * 
	 * @param v
	 *            the new office visit information

	 */
	public void updateInformation(ObstetricOfficeVisitBean v) {
		v.setVisitId(this.visitID);
		validator.validate(v);
		obstetricOfficeVisitDAO.editObstetricOfficeVisit(v);
		scheduleNextOfficeVisit();
	}

	/**
	 * Returns an ObsetricOfficeVisitBean for the visit
	 * 
	 * @return the ObstetricOfficeVisitBean
	 * @throws DBException
	 */
	public ObstetricOfficeVisitBean getObstetricOfficeVisit() throws DBException {
		return obstetricOfficeVisitDAO.getObstetricOfficeVisit(this.visitID);
	}
	
    /**
     * XCXC: Stubbed, need to implement.
     */
    private void scheduleNextOfficeVisit() {

    }
	
}

	