package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ApptDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;
import edu.ncsu.csc.itrust.model.old.validate.ObstetricOfficeVisitValidator;


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.util.Optional;
import java.util.GregorianCalendar;

public class AddObstetricOfficeVisitAction {
    private ObstetricOfficeVisitDAO obstetricOfficeVisitDAO;
    private ObstetricOfficeVisitValidator validator;
    private ApptDAO apptDAO;
    private AuthDAO authDAO;
    private long loggedInMID;
    private Optional<Long> visitID;

    private AddObstetricOfficeVisitAction(ObstetricOfficeVisitDAO obstetricOfficeVisitDAO, AuthDAO authDAO,
                                          ApptDAO apptDAO, long loggedInMID) {
        this.obstetricOfficeVisitDAO = obstetricOfficeVisitDAO;
        this.apptDAO = apptDAO;
        this.authDAO = authDAO;
        this.loggedInMID = loggedInMID;
        this.visitID = Optional.empty();
        this.validator = new ObstetricOfficeVisitValidator();
    }
    
    

    public static AddObstetricOfficeVisitAction createObstetricOfficeVisitAction(DAOFactory factory,
                                                                                 long newLoggedInMID) {
        return new AddObstetricOfficeVisitAction(
                factory.getObstetricsOfficeVisitDAO(),
                factory.getAuthDAO(),
                factory.getApptDAO(),
                newLoggedInMID);
    }
    
    private final static Map<Integer, Integer> DAY_OFFSET = new HashMap<>();
    static {
        DAY_OFFSET.put(Calendar.SUNDAY, 1);
        DAY_OFFSET.put(Calendar.THURSDAY, 4);
        DAY_OFFSET.put(Calendar.FRIDAY, 3);
    }

    public void addObstetricOfficeVisit(ObstetricOfficeVisitBean obsOfficeVisit) throws DBException,
            FormValidationException {

        // Validate office visit bean.
        this.validator.validate(obsOfficeVisit);

        // Add obstetric office visit to DB.
        this.visitID = Optional.ofNullable(this.obstetricOfficeVisitDAO.addObstetricOfficeVisit(obsOfficeVisit));

        // Schedule next visit.
        scheduleNextOfficeVisit();
    }

    /**
     * XCXC: Stubbed, need to implement.
     */
    private void scheduleNextOfficeVisit() {
        // TODO: Replace
        int numberOfWeeksPregnant = 5; // Stubbed value for UC-93 feature.
        long hcpMID = 0L; // Stubbed doctor.
        Timestamp date = new Timestamp(); //Stubbed date

        // Given number of weeks pregnant,
        // find next appt date.
        ApptBean nextAppt = new ApptBean();
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        nextAppt.setHcp(hcpMID);
        if (numberOfWeeksPregnant <= 13) {
            cal.add(Calendar.WEEK, 4);
            apptDate = new Timestamp(cal.getTime().getTime());
            nextAppt.setDate(apptDate);
        	apptDAO.scheduleNextAvailableAppt(apptDate);
        }
        
        else if (numberOfWeeksPregnant <= 28) {
            cal.add(Calendar.WEEK, 2);
            apptDate = new Timestamp(cal.getTime().getTime());
            nextAppt.setDate(apptDate);
        	apptDAO.scheduleNextAvailableAppt(apptDate);
        }
        
        else if (numberOfWeeksPregnant <= 40) {
            cal.add(Calendar.WEEK, 1);
            apptDate = new Timestamp(cal.getTime().getTime());
            nextAppt.setDate(apptDate);
        	apptDAO.scheduleNextAvailableAppt(apptDate);
        }
        
        else {
        	int amountOfDaysToAdd = DAY_OFFSET.getOrDefault(cal.get(Calendar.DAY_OF_WEEK), 2);
            cal.add(Calendar.DAY, 2);
            apptDate = new Timestamp(cal.getTime().getTime());
            nextAppt.setDate(apptDate);
        	apptDAO.scheduleNextAvailableAppt(apptDate);
        }

        // Make a call to ApptDAO.scheduleNextAvailableAppt.


    }
}
