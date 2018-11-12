package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;

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

}
