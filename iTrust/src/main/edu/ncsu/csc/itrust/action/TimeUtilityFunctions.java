package edu.ncsu.csc.itrust.action;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtilityFunctions {
    /**
     * Get number of weeks between two dates.
     */
    public static int getNumberOfWeeksBetween(Date before, Date after) {
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
}
