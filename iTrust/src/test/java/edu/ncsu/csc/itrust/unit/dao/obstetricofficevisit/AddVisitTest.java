package edu.ncsu.csc.itrust.unit.dao.obstetricofficevisit;

import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.sql.Timestamp;
import java.util.List;

public class AddVisitTest extends TestCase {
    ObstetricOfficeVisitDAO obstetricOfficeVisitDAO = TestDAOFactory.getTestInstance().getObstetricsOfficeVisitDAO();

    public void testAdd() throws Exception{
        ObstetricOfficeVisitBean bean = new ObstetricOfficeVisitBean();
        bean.setPatientMID(11111111);
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
        assertEquals((float)100.0, bean.getWeight());
        assertEquals(8, bean.getNumberOfBabies());
        assertEquals(33, bean.getLowLyingPlacentaObserved());

        ObstetricOfficeVisitBean recent = obstetricOfficeVisitDAO.getMostRecentObstetricOfficeVisitsByPatientMID(11111111);
        assertEquals(recent.getVisitId(), bean.getVisitId());
    }
}
