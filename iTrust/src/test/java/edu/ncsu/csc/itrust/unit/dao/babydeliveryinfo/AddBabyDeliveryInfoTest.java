package edu.ncsu.csc.itrust.unit.dao.babydeliveryinfo;

import com.sun.jna.platform.win32.Sspi;
import cucumber.api.java.it.Ma;
import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.BabyDeliveryInfoDAO;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.Gender;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.sql.Timestamp;
import java.util.List;

public class AddBabyDeliveryInfoTest extends TestCase {
    BabyDeliveryInfoDAO babyDeliveryInfoDAO = TestDAOFactory.getTestInstance().getBabyDeliveryInfoDAO();

    public void testAddRecord() throws Exception {
        BabyDeliveryInfoBean d = new BabyDeliveryInfoBean();
        d.setMID(10000);
        d.setDeliveryType(DeliveryType.Miscarriage.getName());
        d.setChildBirthVisitId(888);
        d.setGender(Gender.Female);
        Timestamp curr = new Timestamp(System.currentTimeMillis());

        d.setBirthTime(curr);
        d.setEstimated(true);
        babyDeliveryInfoDAO.addBabyDeliveryInfo(d);

        List<BabyDeliveryInfoBean> get = babyDeliveryInfoDAO.getBabyDeliveryInfosForMID(10000);
        d = get.get(0);
        assertEquals(DeliveryType.Miscarriage.getName(), d.getDeliveryType().toString());
        assertEquals(888, d.getChildBirthVisitId());
        //ignore millisecond
        assertEquals(Math.rint(curr.getTime()/1000.0), Math.rint(d.getBirthTime().getTime()/1000.0));
        assertEquals(Gender.Female.getName(), d.getGender().toString());
        assertTrue(d.isEstimated());
    }
}
