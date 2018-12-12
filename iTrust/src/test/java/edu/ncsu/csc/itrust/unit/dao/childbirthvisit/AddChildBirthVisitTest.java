package edu.ncsu.csc.itrust.unit.dao.childbirthvisit;

import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ChildBirthVisitDAO;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.util.List;

public class AddChildBirthVisitTest extends TestCase {
    private ChildBirthVisitDAO childBirthVisitDAO = TestDAOFactory.getTestInstance().getChildBirthVisitDAO();

    @Override
    protected void setUp() throws Exception {
        TestDataGenerator gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.appointmentType();
    }

    public void testAddRecord() throws Exception {
        ChildBirthVisitBean visit = new ChildBirthVisitBean();
        visit.setMID(10000);
        visit.setPreviouslyScheduled(true);
        visit.setPitocinDosage((float) 0.0);
        visit.setNitrousOxideDosage((float) 2.5);
        visit.setPreferredDeliveryType(DeliveryType.Vaginal.getName());
        visit.setEpiduralAnaesthesiaDosage((float) 3.14159);
        visit.setMagnesiumSulfateDosage(7);
        visit.setRhImmuneGlobulinDosage(500);
        childBirthVisitDAO.addChildBirthVisit(visit);

        List<ChildBirthVisitBean> get = childBirthVisitDAO.getChildBirthVisitsForMID(10000);
        visit = get.get(0);
        assertEquals(visit.getPreferredDeliveryType().toString(), DeliveryType.Vaginal.getName());
        assertEquals(visit.getEpiduralAnaesthesiaDosage(), (float) 3.14159);
        assertEquals(visit.getMagnesiumSulfateDosage(), (float) 7.0);
        assertEquals(visit.getNitrousOxideDosage(), (float) 2.5);
        assertEquals(visit.getPitocinDosage(), (float) 0.0);
        assertEquals(visit.getRhImmuneGlobulinDosage(), (float) 500.0);
    }
}
