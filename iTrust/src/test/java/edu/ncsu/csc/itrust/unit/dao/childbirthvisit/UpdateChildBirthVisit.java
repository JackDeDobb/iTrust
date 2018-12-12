package edu.ncsu.csc.itrust.unit.dao.childbirthvisit;

import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ChildBirthVisitDAO;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.util.List;

public class UpdateChildBirthVisit extends TestCase {
    ChildBirthVisitDAO childBirthVisitDAO = TestDAOFactory.getTestInstance().getChildBirthVisitDAO();

    @Override
    protected void setUp() throws Exception {
        TestDataGenerator gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.appointmentType();
    }

    public void testUpdate() throws Exception {
        ChildBirthVisitBean visit = new ChildBirthVisitBean();
        visit.setMID(100001);
        visit.setPreviouslyScheduled(true);
        visit.setPitocinDosage((float) 444444.0);
        visit.setNitrousOxideDosage((float) 2.5);
        visit.setPreferredDeliveryType(DeliveryType.Vaginal.getName());
        visit.setEpiduralAnaesthesiaDosage((float) 3.14159);
        visit.setMagnesiumSulfateDosage(7);
        visit.setRhImmuneGlobulinDosage(500);
        childBirthVisitDAO.addChildBirthVisit(visit);

        List<ChildBirthVisitBean> get = childBirthVisitDAO.getChildBirthVisitsForMID(100001);
        visit = get.get(0);
        visit.setNitrousOxideDosage((float) 0.0);
        visit.setMagnesiumSulfateDosage((float) 0.0);
        visit.setRhImmuneGlobulinDosage((float) 0.0);
        visit.setEpiduralAnaesthesiaDosage((float) 0.0);
        visit.setPreferredDeliveryType(DeliveryType.Miscarriage.getName());
        long id = visit.getId();
        childBirthVisitDAO.updateChildBirthVisit(visit);

        visit = childBirthVisitDAO.getRecordById(id);
        assertEquals(visit.getPreferredDeliveryType().toString(), DeliveryType.Miscarriage.getName());
        assertEquals(visit.getEpiduralAnaesthesiaDosage(), (float) 0.0);
        assertEquals(visit.getMagnesiumSulfateDosage(), (float) 0.0);
        assertEquals(visit.getNitrousOxideDosage(), (float) 0.0);
        assertEquals(visit.getPitocinDosage(), (float) 444444.0);
        assertEquals(visit.getRhImmuneGlobulinDosage(), (float) 0.0);
    }
}
