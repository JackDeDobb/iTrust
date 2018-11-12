package edu.ncsu.csc.itrust.action;

import java.util.List;

import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.exception.DBException;

public class ViewObstetricOfficeVisitAction {
	
    private ObstetricOfficeVisitDAO obstetricOfficeVisitDAO;
    private long loggedInMID;
    private long patientMID;
    
	public ViewObstetricOfficeVisitAction(DAOFactory factory, long loggedInMID, long patientMID)
			throws ITrustException {
		this.obstetricOfficeVisitDAO = factory.getObstetricsOfficeVisitDAO();
		this.loggedInMID = loggedInMID;
		this.patientMID = patientMID;
	}
	
	public boolean isObstetricsEligible() {
		return true;
	}
	
	public boolean isCurrentObstetricsPatient() {
		return true;
	}
	
	public List<ObstetricOfficeVisitBean> getObstetricOfficeVisitRecords() {
		try {
			List<ObstetricOfficeVisitBean> visitList = 
					obstetricOfficeVisitDAO.getObstetricOfficeVisitsByPatientMID(patientMID);
			return visitList;
		} catch (DBException e) {
			return null;
		}
	}

	public List<UltrasoundRecordBean> getUltrasoundRecords() {
		return null;
	}
}
