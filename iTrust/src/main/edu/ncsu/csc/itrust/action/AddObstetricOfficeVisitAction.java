package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.beans.ApptTypeBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricInfoBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.*;
import edu.ncsu.csc.itrust.model.old.validate.ObstetricOfficeVisitValidator;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Calendar;

public class AddObstetricOfficeVisitAction {
    private ObstetricOfficeVisitDAO obstetricOfficeVisitDAO;
    private ApptDAO apptDAO;
    private AuthDAO authDAO;
    private ApptTypeDAO apptTypeDAO;
    private ObstetricInfoDAO obstetricInfoDAO;
    private ObstetricOfficeVisitValidator validator;
    private long loggedInMID;
    private Optional<Long> visitID;

    private AddObstetricOfficeVisitAction(DAOFactory factory, long loggedInMID) {
        this.obstetricOfficeVisitDAO = factory.getObstetricsOfficeVisitDAO();
        this.apptDAO = factory.getApptDAO();
        this.apptTypeDAO = factory.getApptTypeDAO();
        this.authDAO = factory.getAuthDAO();
        this.obstetricInfoDAO = factory.getObstetricInfoDAO();
        this.loggedInMID = loggedInMID;
        this.visitID = Optional.empty();
        this.validator = new ObstetricOfficeVisitValidator();
    }

    private final static Map<DayOfWeek, Integer> DAY_OFFSET = new HashMap<>();
    static {
        DAY_OFFSET.put(DayOfWeek.SUNDAY, 1);
        DAY_OFFSET.put(DayOfWeek.THURSDAY, 4);
        DAY_OFFSET.put(DayOfWeek.FRIDAY, 3);
    }

    public void addObstetricOfficeVisit(ObstetricOfficeVisitBean obsOfficeVisit) throws DBException,
            FormValidationException {

        // Fetch the obstetric info bean.
        ObstetricInfoBean patientInfo = obstetricInfoDAO.getMostRecentObstetricInfoForMID(obsOfficeVisit.getPatientMID());

        // Validate office visit bean.
        this.validator.validate(obsOfficeVisit);

        // Add obstetric office visit to DB.
        this.visitID = Optional.ofNullable(this.obstetricOfficeVisitDAO.addObstetricOfficeVisit(obsOfficeVisit));

        // Schedule next visit.
        scheduleNextOfficeVisit(obsOfficeVisit, patientInfo);
    }

    /**
     *
     */
    private int getNumberOfWeeksBetween(Date before, Date after) {
        if (before.after(after)) {
            return getNumberOfWeeksBetween(after, before);
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(before);

        int weeks = 0;
        while (cal.getTime().before(after)) {
            // add another week
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            weeks++;
        }
        return weeks;
    }

    /**
     * XCXC: Stubbed, need to implement.
     */
    private void scheduleNextOfficeVisit(ObstetricOfficeVisitBean obsOfficeVisit, ObstetricInfoBean patientInfo) throws DBException {


        // Get current date.
        Instant now = Instant.now();
        LocalDateTime today = LocalDateTime.ofInstant(now, ZoneId.systemDefault());

        // Get number of weeks pregnant.
        int numberOfWeeksPregnant = getNumberOfWeeksBetween(patientInfo.getLMP(), Date.from(now));

        // Find next appointment date.
        // TODO(avjykmr2): Determine if appt type is correct.

        ApptBean nextAppt = new ApptBean();
        ApptTypeBean apptType = apptTypeDAO.getApptType("Consultation");
        nextAppt.setPrice(apptType.getPrice());
        nextAppt.setHcp(obsOfficeVisit.getHcpMID());
        nextAppt.setApptType("Consultation");

        //  -> Given number of weeks pregnant:

        if (numberOfWeeksPregnant <= 13 && numberOfWeeksPregnant >= 0) {
            LocalDateTime apptTime = today.plus(4, ChronoUnit.WEEKS)
                    .withHour(9).withMinute(0).withSecond(0);
            nextAppt.setDate(Timestamp.valueOf(apptTime));
        } else if (numberOfWeeksPregnant <= 28) {
            LocalDateTime apptTime = today.plus(2, ChronoUnit.WEEKS)
                    .withHour(9).withMinute(0).withSecond(0);
            nextAppt.setDate(Timestamp.valueOf(apptTime));
        } else if (numberOfWeeksPregnant <= 40) {
            LocalDateTime apptTime = today.plus(1, ChronoUnit.WEEKS)
                    .withHour(9).withMinute(0).withSecond(0);
            nextAppt.setDate(Timestamp.valueOf(apptTime));
        } else if (numberOfWeeksPregnant <= 42) {
            int amountOfDaysToAdd = DAY_OFFSET.getOrDefault(today.getDayOfWeek(), 2);
            LocalDateTime apptTime = today.plus(amountOfDaysToAdd, ChronoUnit.DAYS)
                    .withHour(9).withMinute(0).withSecond(0);
            nextAppt.setDate(Timestamp.valueOf(apptTime));
        }

        try {
            apptDAO.scheduleNextAvailableAppt(nextAppt);
        } catch (SQLException e) {
            throw new DBException(e);
        }


    }
}
