package edu.ncsu.csc.itrust.unit.dao.babydeliveryinfo;

import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.BabyDeliveryInfoDAO;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.Gender;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.sql.Timestamp;
import java.util.List;

public class UpdateInfoTest extends TestCase {
    BabyDeliveryInfoDAO babyDeliveryInfoDAO = TestDAOFactory.getTestInstance().getBabyDeliveryInfoDAO();

    public void testUpdateRecord() throws Exception {
        BabyDeliveryInfoBean d = new BabyDeliveryInfoBean();
        d.setMID(100001);
        d.setDeliveryType(DeliveryType.Miscarriage.getName());
        d.setChildBirthVisitId(888);
        d.setGender(Gender.Female);
        Timestamp curr = new Timestamp(System.currentTimeMillis());

        d.setBirthTime(curr);
        d.setEstimated(true);
        babyDeliveryInfoDAO.addBabyDeliveryInfo(d);
        Thread.sleep(1000);
        List<BabyDeliveryInfoBean> get = babyDeliveryInfoDAO.getBabyDeliveryInfosForMID(100001);
        d = get.get(0);
        long id = d.getId();
        Timestamp update = new Timestamp(System.currentTimeMillis());
        d.setBirthTime(update);
        d.setGender(Gender.Male);
        babyDeliveryInfoDAO.updateBabyDeliveryInfo(d);

        d = babyDeliveryInfoDAO.getRecordById(id);
        assertNotSame(Math.rint(update.getTime()/1000.0), Math.rint(curr.getTime()/1000.0));
        //ignore millisecond
        assertEquals(Math.rint(update.getTime()/1000.0), Math.rint(d.getBirthTime().getTime()/1000.0));
        assertEquals(Gender.Male.getName(), d.getGender().toString());
        assertTrue(d.isEstimated());
    }
}
