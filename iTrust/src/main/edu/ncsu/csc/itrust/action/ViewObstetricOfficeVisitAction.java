package edu.ncsu.csc.itrust.action;

import java.util.List;

import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;

public class ViewObstetricOfficeVisitAction {
	
    private ObstetricOfficeVisitDAO obstetricOfficeVisitDAO;
    private long loggedInMID;
    private String patientMID;
    
	public ViewObstetricOfficeVisitAction(DAOFactory factory, long loggedInMID, String patientMID)
			throws ITrustException {
		this.obstetricOfficeVisitDAO = factory.getObstetricsOfficeVisitDAO();
		this.loggedInMID = loggedInMID;
		this.patientMID = patientMID;
	}
	
	public long isObstetricsEligible() {
		return 0;
	}
	
	public boolean isCurrentObstetricsPatient() {
		return false;
	}
	
	public List<ObstetricOfficeVisitBean> getObstetricOfficeVisitRecords() {
		return null;
	}

	public List<UltrasoundRecordBean> getUltrasoundRecords() {
		return null;
	}
}
