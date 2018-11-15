package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.*;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.ObstetricOfficeVisitValidator;

/**
 * Edits an obstetric office visit.
 */
public class EditObstetricOfficeVisitAction  {
    private ObstetricOfficeVisitDAO obstetricOfficeVisitDAO;
    private UltrasoundRecordDAO ultrasoundRecordDAO;
    private TransactionDAO transactionDAO;
    private ObstetricOfficeVisitValidator validator;
    private long loggedInMID;

	/**
	 *
	 * @param daoFactory The DAOFactory used to create the DAOs for this action.
	 * @param loggedInMID The MID of the user who is authorizing this action.
	 */
	public EditObstetricOfficeVisitAction(DAOFactory daoFactory, long loggedInMID) {
        this.obstetricOfficeVisitDAO = daoFactory.getObstetricsOfficeVisitDAO();
        this.ultrasoundRecordDAO = daoFactory.getUltrasoundRecordDAO();
        this.transactionDAO = daoFactory.getTransactionDAO();
        this.loggedInMID = loggedInMID;
        this.validator = new ObstetricOfficeVisitValidator();
	}

	/**
	 * Takes the information out of the ObstetricOfficeVisitBean param and updates the visit's information.
	 * 
	 * @param obsOfficeVisit
	 *            Updated office visit information.
	 */
	public void updateVisitInformation(ObstetricOfficeVisitBean obsOfficeVisit) throws FormValidationException,
			DBException {
		validator.validate(obsOfficeVisit);
		obstetricOfficeVisitDAO.editObstetricOfficeVisit(obsOfficeVisit);
		transactionDAO.logTransaction(TransactionType.EDIT_OBSTETRIC_OFFICE_VISIT, loggedInMID, obsOfficeVisit.getPatientMID(),
				obsOfficeVisit.getVisitId() + "");
	}

	public void updateUltrasoundInformation(ObstetricOfficeVisitBean obsOfficeVisit,
											UltrasoundRecordBean ultrasoundRecord) throws DBException {
		this.ultrasoundRecordDAO.editUltrasoundRecord(ultrasoundRecord);
		transactionDAO.logTransaction(TransactionType.ULTRASOUND,
				loggedInMID,
				obsOfficeVisit.getPatientMID(),
				obsOfficeVisit.getVisitId() + "");
	}



	
}

	