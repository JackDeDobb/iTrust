package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ApptRequestDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;
import edu.ncsu.csc.itrust.model.old.validate.ObstetricOfficeVisitValidator;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.GregorianCalendar;

public class AddObstetricOfficeVisitAction {
    private ObstetricOfficeVisitDAO obstetricOfficeVisitDAO;
    private ObstetricOfficeVisitValidator validator;
    private ApptRequestDAO apptRequestDAO;
    private AuthDAO authDAO;
    private long loggedInMID;
    private Optional<Long> visitID;

    private AddObstetricOfficeVisitAction(ObstetricOfficeVisitDAO obstetricOfficeVisitDAO, AuthDAO authDAO,
                                          ApptRequestDAO apptRequestDAO, long loggedInMID) {
        this.obstetricOfficeVisitDAO = obstetricOfficeVisitDAO;
        this.apptRequestDAO = apptRequestDAO;
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
                factory.getApptRequestDAO(),
                newLoggedInMID);
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

        // Given number of weeks pregnant,
        // find next appt date.

        // Make a call to ApptDAO.scheduleNextAvailableAppt.


    }
}
