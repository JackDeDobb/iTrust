package edu.ncsu.csc.itrust.unit.dao.obstetricofficevisit;

import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.sql.Timestamp;
import java.util.List;

public class EditVisitTest extends TestCase {
    ObstetricOfficeVisitDAO obstetricOfficeVisitDAO = TestDAOFactory.getTestInstance().getObstetricsOfficeVisitDAO();

    @Override
    protected void setUp() throws Exception {
        TestDataGenerator gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.appointmentType();
    }

    public void testAdd() throws Exception {
        ObstetricOfficeVisitBean bean = new ObstetricOfficeVisitBean();
        bean.setPatientMID(21111111);
        bean.setHcpMID(12222);
        bean.setObstetricRecordID(222222);
        bean.setWeight((float) 100.0);
        bean.setFetalHeartRate(90);
        bean.setLowLyingPlacentaObserved(33);
        bean.setNumberOfBabies(8);
        Timestamp curr = new Timestamp(System.currentTimeMillis());
        bean.setVisitDate(curr);
        obstetricOfficeVisitDAO.addObstetricOfficeVisit(bean);

        List<ObstetricOfficeVisitBean> list = obstetricOfficeVisitDAO.getObstetricOfficeVisitsByPatientMID(11111111);
        bean = list.get(list.size() - 1);
        long id = bean.getVisitId();
        bean.setNumberOfBabies(1);
        bean.setWeight((float) 111.11);
        obstetricOfficeVisitDAO.editObstetricOfficeVisit(bean);

        bean = obstetricOfficeVisitDAO.getObstetricOfficeVisitByID(id);
        assertEquals((float) 111.11, bean.getWeight());
        assertEquals(1, bean.getNumberOfBabies());
        assertEquals(33, bean.getLowLyingPlacentaObserved());
    }
}
