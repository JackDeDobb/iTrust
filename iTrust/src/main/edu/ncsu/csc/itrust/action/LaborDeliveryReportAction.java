package edu.ncsu.csc.itrust.action;

import java.util.Date;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricInfoBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricInfoDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;

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
	private long pid;
	
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
		this.pid = Long.valueOf(pidString);
	}
	
	
	public String getBloodType() throws DBException {
		PatientBean p = this.patientDAO.getPatient(this.pid);
		return p.getBloodType().getName();
	}
	
	public String getEstimatedDeliveryDate() throws DBException {
		ObstetricInfoBean obs = this.obstetricInfoDAO.getMostRecentObstetricInfoForMID(this.pid);
		Date edd = obs.getEDD();
		return edd.toString();
	}
	
	
	
	
}