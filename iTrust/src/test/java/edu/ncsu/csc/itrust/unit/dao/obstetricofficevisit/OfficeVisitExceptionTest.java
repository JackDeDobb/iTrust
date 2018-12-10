package edu.ncsu.csc.itrust.unit.dao.obstetricofficevisit;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ChildBirthVisitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;
import edu.ncsu.csc.itrust.unit.testutils.EvilDAOFactory;
import junit.framework.TestCase;

public class OfficeVisitExceptionTest extends TestCase {
    private ObstetricOfficeVisitDAO evilDAO = EvilDAOFactory.getEvilInstance().getObstetricsOfficeVisitDAO();

    @Override
    protected void setUp() throws Exception {
    }

    public void testUpdateEmptyRecordException() throws Exception {
        try {
            ObstetricOfficeVisitBean info = evilDAO.getObstetricOfficeVisitsByPatientMID(1).get(0);
            info.setPatientMID(-1);
            evilDAO.editObstetricOfficeVisit(info);
            fail("DBException should have been thrown");
        } catch (DBException e) {
            assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
        }
    }

    public void testUpdateNonExistPRecordException() throws Exception {
        try {
            ObstetricOfficeVisitBean info = evilDAO.getObstetricOfficeVisitsByPatientMID(1).get(0);
            info.setVisitId(50000000000L);
            evilDAO.editObstetricOfficeVisit(info);
            fail("DBException should have been thrown");
        } catch (DBException e) {
            assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
        }
    }
}
