package edu.ncsu.csc.itrust.action;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricInfoBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.*;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

/**
 * View list of obstetric office visits.
 */
public class ViewObstetricOfficeVisitAction {
	
    private ObstetricOfficeVisitDAO obstetricOfficeVisitDAO;
    private ObstetricInfoDAO obstetricInfoDAO;
    private PatientDAO patientDAO;
    private UltrasoundRecordDAO ultrasoundRecordDAO;
    private TransactionDAO transactionDAO;
    private long loggedInMID;
    private long patientMID;
    
    private long currentVisitId;
    
	public ViewObstetricOfficeVisitAction(DAOFactory factory, long loggedInMID, long patientMID) {
		this.obstetricOfficeVisitDAO = factory.getObstetricsOfficeVisitDAO();
		this.obstetricInfoDAO = factory.getObstetricInfoDAO();
		this.patientDAO = factory.getPatientDAO();
		this.ultrasoundRecordDAO = factory.getUltrasoundRecordDAO();
		this.transactionDAO = factory.getTransactionDAO();
		this.loggedInMID = loggedInMID;
		this.patientMID = patientMID;
	}

	/**
	 * Returns true if given patient is listed as obstetric eligible on the database.
	 */
	public boolean isObstetricsEligible() throws DBException {
		return patientDAO.getPatient(patientMID).getObstetricEligibility();
	}

	/**
	 * Returns true if provided patient's most recent record has an LMP that is less
	 * than 49 weeks prior to the current date.
	 */
	public boolean isCurrentObstetricsPatient() throws DBException {
		ObstetricInfoBean obstetricInfo =
				obstetricInfoDAO.getMostRecentObstetricInfoForMID(patientMID);

		int numberOfWeeksSinceLMP = TimeUtilityFunctions.getNumberOfWeeksBetween(obstetricInfo.getLMP(),
				Date.from(Instant.now()));

		return numberOfWeeksSinceLMP < 49;
	}

	/**
	 * Get list of current obstetric office visit records.
	 */
	public List<ObstetricOfficeVisitBean> getObstetricOfficeVisitRecords() throws DBException {
//		transactionDAO.logTransaction(
//				TransactionType.VIEW_OBSTETRIC_OFFICE_VISIT,
//				loggedInMID,
//				obsOfficeVisit.getPatientMID(),
//				obsOfficeVisit.getVisitId() + "");
		return obstetricOfficeVisitDAO.getObstetricOfficeVisitsByPatientMID(patientMID);
	}
	
	public ObstetricOfficeVisitBean getObstetricOfficeVisitByVisitId(long visitID) throws DBException {
		return obstetricOfficeVisitDAO.getObstetricOfficeVisitByID(visitID);
	}

	/**
	 * Get list of ultrasound records.
	 */
	public List<UltrasoundRecordBean> getUltrasoundRecords() throws DBException  {
		return ultrasoundRecordDAO.getUltrasoundRecordsByVisitID(currentVisitId);
	}
}
