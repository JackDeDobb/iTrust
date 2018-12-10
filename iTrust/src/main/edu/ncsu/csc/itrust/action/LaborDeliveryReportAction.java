package edu.ncsu.csc.itrust.action;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricInfoBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
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
		TransactionLogger.getInstance().logTransaction(TransactionType.GENERATE_LABOR_DELIVERY_REPORT, loggedInMID, 0L, "");
	}
	
	/*
	 * Gets the blood type  
	 */
	public String getBloodType() throws DBException {
		PatientBean p = this.patientDAO.getPatient(this.pid);
		return p.getBloodType().getName();
	}
	
	/*
	 * Gets the estimated delivery date
	 */
	public String getEstimatedDeliveryDate() throws DBException {
		ObstetricInfoBean obs = this.obstetricInfoDAO.getMostRecentObstetricInfoForMID(this.pid);
		Date edd = obs.getEDD();
		return edd.toString();
	}
	
	/*
	 * Gets all the prior pregnancies based on a pid in descending order
	 */
	public List<ObstetricInfoBean> getPriorPregnancies() throws DBException{
		return this.obstetricInfoDAO.getObstetricInfoForMID(this.pid);
	}
	
	/*
	 * Gets all the prior office visits based on a pid in descending order
	 */
	public List<ObstetricOfficeVisitBean> getAllObstetricsOfficeVisits() throws DBException {
		return this.obstetricOfficeVisitDAO.getObstetricOfficeVisitsByPatientMID(this.pid);	
	}
	
	/*
	 * If have RH but not vaccinated - means that has RH Flag
	 */
	public boolean hasRHFlag() throws DBException {
		PatientBean p = this.patientDAO.getPatient(this.pid);
		return p.isRH() && !p.isRHImmunization();
	}
	
	/*
	 * checks if high blood pressure
	 */
	public boolean hasHighBloodPressure() throws DBException{
		ObstetricOfficeVisitBean obsVisitBean = this.obstetricOfficeVisitDAO.getMostRecentObstetricOfficeVisitsByPatientMID(this.pid);
		return false;
	}
	
	
	/*
	 * If the patient is older than 35 at the estimated date of delivery return true
	 */
	public boolean hasAdvancedMaternalAge() throws DBException {
		PatientBean patient = this.patientDAO.getPatient(this.pid);
		Date birthdate = patient.getDateOfBirth();
		LocalDate localBirthdate = birthdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		
		ObstetricInfoBean obs = this.obstetricInfoDAO.getMostRecentObstetricInfoForMID(this.pid);
		LocalDate localDate = obs.getEDD().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		int ageAtDelivery = Period.between(localBirthdate, localDate).getYears();
		
		return ageAtDelivery >= 35;
	}
	
	/*
	 * Checks if low lying placenta has been observed
	 */
	public boolean hasLowLyingPlacenta() throws DBException {
		ObstetricOfficeVisitBean obsVisitBean = this.obstetricOfficeVisitDAO.getMostRecentObstetricOfficeVisitsByPatientMID(this.pid);
		return obsVisitBean.getLowLyingPlacentaObserved() > 0;
	}
	
	/*
	 * Returns true if heart rate is not in range [120,160]
	 */
	public boolean hasAbnormalFetalHeartRate() throws DBException {
		ObstetricOfficeVisitBean obsVisitBean = this.obstetricOfficeVisitDAO.getMostRecentObstetricOfficeVisitsByPatientMID(this.pid);
		boolean abnormalFetalHeartRate = obsVisitBean.getFetalHeartRate() > 160 || obsVisitBean.getFetalHeartRate() < 120;
		return abnormalFetalHeartRate;
	}
	
	/*
	 * Returns true if number of children > 1
	 */
	public boolean hasMultiples() throws DBException {
		ObstetricOfficeVisitBean obsVisitBean = this.obstetricOfficeVisitDAO.getMostRecentObstetricOfficeVisitsByPatientMID(this.pid);
		return obsVisitBean.getNumberOfBabies() > 1;
	}
	
	/**
	 * If weight change is not in range [15.0, 35.0]
	 * @return
	 * @throws DBException
	 */
	public boolean hasAtypicalWeightChange() throws DBException {
		ObstetricOfficeVisitBean obsVisitBean = this.obstetricOfficeVisitDAO.getMostRecentObstetricOfficeVisitsByPatientMID(this.pid);
		boolean atypicalWeightChange = obsVisitBean.getWeight() < 15.0 || obsVisitBean.getWeight() > 35.0;
		return atypicalWeightChange;
	}
	
	/*
	 * Returns a list of allergy types
	 */
	public List<String> getAllergies() throws DBException {
		List<AllergyBean> allergyBeans = this.allergyDAO.getAllergies(this.pid);
		List<String> allergies = new ArrayList<String>();
		for (AllergyBean allergy : allergyBeans) {
			allergies.add(allergy.getDescription());
		}
		return allergies;
	}
	
	
}