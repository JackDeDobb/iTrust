package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricInfoDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;

import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

/**
 * This class is responsible for generating all relevant information about a pregnancy
 * @author Aditya
 *
 */
public class LaborDeliveryReportAction {
	private PatientDAO patientDAO;
	private ObstetricInfoDAO obstetricInfoDAO;
	private ObstetricOfficeVisitDAO obstetricOfficeVisitDAO;
	private AllergyDAO allergyDAO;
	private long loggedInMID;
	
	/**
	 *  Initializes a labor delivery report action
	 * @param factory
	 * @param loggedInMID
	 * @param pidString
	 */
	public LaborDeliveryReportAction(DAOFactory factory, long loggedInMID, String pidString) {
		this.patientDAO = factory.getPatientDAO();
		this.obstetricInfoDAO = factory.getObstetricInfoDAO();
		this.obstetricOfficeVisitDAO = factory.getObstetricsOfficeVisitDAO();
		this.allergyDAO = factory.getAllergyDAO();
		this.loggedInMID = loggedInMID;
		TransactionLogger.getInstance().logTransaction(TransactionType.GENERATE_LABOR_DELIVERY_REPORT, loggedInMID, 0L, "");
	}
	
	
	
}