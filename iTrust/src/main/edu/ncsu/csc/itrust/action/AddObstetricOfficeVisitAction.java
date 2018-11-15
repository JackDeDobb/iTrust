package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.*;
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

/**
 * Class that defines the new obstetric office visit `document` action.
 */
public class AddObstetricOfficeVisitAction {
    private ObstetricOfficeVisitDAO obstetricOfficeVisitDAO;
    private ApptDAO apptDAO;
    private PatientDAO patientDAO;
    private ApptTypeDAO apptTypeDAO;
    private UltrasoundRecordDAO ultrasoundRecordDAO;
    private ObstetricInfoDAO obstetricInfoDAO;
    private ObstetricOfficeVisitValidator validator;
    private long loggedInMID;
    private Optional<Long> visitID;

    public AddObstetricOfficeVisitAction(DAOFactory factory, long loggedInMID) {
        this.obstetricOfficeVisitDAO = factory.getObstetricsOfficeVisitDAO();
        this.apptDAO = factory.getApptDAO();
        this.apptTypeDAO = factory.getApptTypeDAO();
        this.patientDAO = factory.getPatientDAO();
        this.ultrasoundRecordDAO = factory.getUltrasoundRecordDAO();
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

    /**
     * Add obstetric office visit,
     * and schedules next office visit.
     *
     * Returns true if the patient should be given an RH immunization shot, else false.
     */
    public boolean addObstetricOfficeVisit(ObstetricOfficeVisitBean obsOfficeVisit) throws DBException,
            FormValidationException {

        // Fetch the obstetric info bean.
        ObstetricInfoBean patientInfo = obstetricInfoDAO.getMostRecentObstetricInfoForMID(obsOfficeVisit.getPatientMID());

        // Validate office visit bean.
        this.validator.validate(obsOfficeVisit);

        // Add obstetric office visit to DB.
        this.visitID = Optional.ofNullable(this.obstetricOfficeVisitDAO.addObstetricOfficeVisit(obsOfficeVisit));

        // Schedule next visit.
        scheduleNextOfficeVisit(obsOfficeVisit, patientInfo);

        PatientBean patient = patientDAO.getPatient(obsOfficeVisit.getPatientMID());
        if (patient.isRH() && !patient.isRHImmunization() && getNumberOfWeeksPregnant(patientInfo) >= 28) {
            return true;
        }

        return false;
    }

    public void addUltrasoundRecord(UltrasoundRecordBean ultrasoundRecord) throws DBException {
        ultrasoundRecord.setVisitID(visitID.orElseThrow(() -> {
            throw new IllegalArgumentException("No visit");
        }));
        ultrasoundRecordDAO.addUltrasoundRecord(ultrasoundRecord);
        visitID = Optional.empty();
    }

    /**
     * Get number of weeks between two dates.
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

    private int getNumberOfWeeksPregnant(ObstetricInfoBean patientInfo) {
        return getNumberOfWeeksPregnant(patientInfo, Date.from(Instant.now()));
    }

    private int getNumberOfWeeksPregnant(ObstetricInfoBean patientInfo, Date now) {
        return getNumberOfWeeksBetween(patientInfo.getLMP(), now);
    }

    /**
     * Schedule next office visit.
     */
    private void scheduleNextOfficeVisit(ObstetricOfficeVisitBean obsOfficeVisit, ObstetricInfoBean patientInfo) throws DBException {


        // Get current date.
        Instant now = Instant.now();
        LocalDateTime today = LocalDateTime.ofInstant(now, ZoneId.systemDefault());

        // Get number of weeks pregnant.
        int numberOfWeeksPregnant = getNumberOfWeeksPregnant(patientInfo, Date.from(now));

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
            // TODO(avjykmr2): Dependent on UC-96, add childbirth visit.
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
