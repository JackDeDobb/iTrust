package edu.ncsu.csc.itrust.unit.dao.babydeliveryinfo;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.BabyDeliveryInfoDAO;
import edu.ncsu.csc.itrust.unit.testutils.EvilDAOFactory;
import junit.framework.TestCase;

public class BabyDeliveryInfoExceptionTest extends TestCase {
    private BabyDeliveryInfoDAO evilDAO = EvilDAOFactory.getEvilInstance().getBabyDeliveryInfoDAO();

    @Override
    protected void setUp() throws Exception {
    }

    public void testAddEmptyRecordException() throws Exception {
        try {
            BabyDeliveryInfoBean info = new BabyDeliveryInfoBean();
            evilDAO.addBabyDeliveryInfo(info);
            fail("DBException should have been thrown");
        } catch (DBException e) {
            assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
        }
    }

    public void testUpdateEmptyRecordException() throws Exception {
        try {
            BabyDeliveryInfoBean info = evilDAO.getBabyDeliveryInfosForMID(1).get(0);
            info.setMID(-1);
            evilDAO.updateBabyDeliveryInfo(info);
            fail("DBException should have been thrown");
        } catch (DBException e) {
            assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
        }
    }

    public void testUpdateNonExistPRecordException() throws Exception {
        try {
            BabyDeliveryInfoBean info = evilDAO.getBabyDeliveryInfosForMID(1).get(0);
            info.setId(50000000000L);
            evilDAO.updateBabyDeliveryInfo(info);
            fail("DBException should have been thrown");
        } catch (DBException e) {
            assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
        }
    }
}
