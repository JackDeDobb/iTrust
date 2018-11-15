package edu.ncsu.csc.itrust.action;

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
 * Edits an obstetric office visit.
 */
public class EditObstetricOfficeVisitAction  {
    private ObstetricOfficeVisitDAO obstetricOfficeVisitDAO;
    private ObstetricOfficeVisitValidator validator;
    private AuthDAO authDAO;
    private long loggedInMID;
    private Optional<Long> visitID;

	/**
	 * 
	 * @param factory The DAOFactory used to create the DAOs for this action.
	 * @param loggedInMID The MID of the user who is authorizing this action.
	 */
	public EditObstetricOfficeVisitAction(DAOFactory daoFactory, long loggedInMID) {
        this.obstetricOfficeVisitDAO = daoFactory.getObstetricsOfficeVisitDAO();
        this.authDAO = daoFactory.getAuthDAO();
        this.loggedInMID = loggedInMID;
        this.validator = new ObstetricOfficeVisitValidator();
	}

	/**
	 * Takes the information out of the ObstetricOfficeVisitBean param and updates the visit's information
	 * 
	 * @param obsOfficeVisit
	 *            Updated office visit information.
	 */
	public void updateInformation(ObstetricOfficeVisitBean obsOfficeVisit) throws FormValidationException, DBException {
		this.visitID = Optional.ofNullable(obsOfficeVisit.getVisitId());
		validator.validate(obsOfficeVisit);
		obstetricOfficeVisitDAO.editObstetricOfficeVisit(obsOfficeVisit);
	}

	/**
	 * Returns an ObstetricOfficeVisitBean for the visit
	 * 
	 * @return the ObstetricOfficeVisitBean
	 * @throws DBException
	 */
	public ObstetricOfficeVisitBean getObstetricOfficeVisit() throws DBException {
		return obstetricOfficeVisitDAO.getObstetricOfficeVisitByID(this.visitID.get());
	}

	
}

	