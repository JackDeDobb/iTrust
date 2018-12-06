package edu.ncsu.csc.itrust.unit.dao.childbirthvisit;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ChildBirthVisitDAO;
import edu.ncsu.csc.itrust.unit.testutils.EvilDAOFactory;
import junit.framework.TestCase;

public class ChildBirthVisitExceptionTest extends TestCase {
    private ChildBirthVisitDAO evilDAO = EvilDAOFactory.getEvilInstance().getChildBirthVisitDAO();

    @Override
    protected void setUp() throws Exception {
    }

    public void testAddEmptyRecordException() throws Exception {
        try {
            ChildBirthVisitBean info = new ChildBirthVisitBean();
            evilDAO.addChildBirthVisit(info);
            fail("DBException should have been thrown");
        } catch (DBException e) {
            assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
        }
    }

    public void testUpdateEmptyRecordException() throws Exception {
        try {
            ChildBirthVisitBean info = evilDAO.getChildBirthVisitsForMID(1).get(0);
            info.setMID(-1);
            evilDAO.updateChildBirthVisit(info);
            fail("DBException should have been thrown");
        } catch (DBException e) {
            assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
        }
    }

    public void testUpdateNonExistPRecordException() throws Exception {
        try {
            ChildBirthVisitBean info = evilDAO.getChildBirthVisitsForMID(1).get(0);
            info.setId(50000000000L);
            evilDAO.updateChildBirthVisit(info);
            fail("DBException should have been thrown");
        } catch (DBException e) {
            assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
        }
    }


}
