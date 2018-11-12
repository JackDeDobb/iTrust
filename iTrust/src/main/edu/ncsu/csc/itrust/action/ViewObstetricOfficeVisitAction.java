package edu.ncsu.csc.itrust.action;

import java.util.List;

import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.UltrasoundRecordDAO;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.exception.DBException;

public class ViewObstetricOfficeVisitAction {
	
    private ObstetricOfficeVisitDAO obstetricOfficeVisitDAO;
    private UltrasoundRecordDAO ultrasoundRecordDAO;
    private long loggedInMID;
    private long patientMID;
    
    private long currentVisitId;
    
	public ViewObstetricOfficeVisitAction(DAOFactory factory, long loggedInMID, long patientMID)
			throws ITrustException {
		this.obstetricOfficeVisitDAO = factory.getObstetricsOfficeVisitDAO();
		this.ultrasoundRecordDAO = factory.getUltrasoundRecordDAO();
		this.loggedInMID = loggedInMID;
		this.patientMID = patientMID;
	}
	
	public boolean isObstetricsEligible() {
		//TODO: using self.loggedInMID
		return true;
	}
	
	public boolean isCurrentObstetricsPatient() {
		//TODO using self.loggedInMID
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
	
	public ObstetricOfficeVisitBean getObstetricOfficeVisitRecord(String index) {
		int idx = Integer.parseInt(index);
		List<ObstetricOfficeVisitBean> visitList = getObstetricOfficeVisitRecords();
		if(visitList.size() > idx) {
			ObstetricOfficeVisitBean visit = visitList.get(idx);
			currentVisitId = visit.getVisitId();
		}
		return null;
	}

	public List<UltrasoundRecordBean> getUltrasoundRecords() {
		try {
			List<UltrasoundRecordBean> recordList = 
					ultrasoundRecordDAO.getUltrasoundRecordsByVisitID(currentVisitId);
			return recordList;
		} catch (DBException e) {
			return null;
		}
	}
}
