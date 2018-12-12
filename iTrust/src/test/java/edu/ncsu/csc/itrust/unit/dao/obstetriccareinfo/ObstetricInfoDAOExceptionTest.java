package edu.ncsu.csc.itrust.unit.dao.obstetriccareinfo;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricInfoBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricInfoDAO;
import edu.ncsu.csc.itrust.unit.testutils.EvilDAOFactory;
import junit.framework.TestCase;

public class ObstetricInfoDAOExceptionTest extends TestCase {
    private ObstetricInfoDAO evilDAO = EvilDAOFactory.getEvilInstance().getObstetricInfoDAO();

    @Override
    protected void setUp() throws Exception {
    }

    public void testAddEmptyRecordException() throws Exception {
        try {
            ObstetricInfoBean info = new ObstetricInfoBean();
            evilDAO.addObstetricInfo(info);
            fail("DBException should have been thrown");
        } catch (DBException e) {
            assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
        }
    }

    public void testUpdateEmptyRecordException() throws Exception {
        try {
            ObstetricInfoBean info = evilDAO.getObstetricInfoForMID(1).get(0);
            info.setMID(-1);
            evilDAO.updateObstetricInfo(info);
            fail("DBException should have been thrown");
        } catch (DBException e) {
            assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
        }
    }

    public void testUpdateNonExistPRecordException() throws Exception {
        try {
            ObstetricInfoBean info = evilDAO.getObstetricInfoForMID(1).get(0);
            info.setRecordId(50000000000L);
            evilDAO.updateObstetricInfo(info);
            fail("DBException should have been thrown");
        } catch (DBException e) {
            assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
        }
    }
}
