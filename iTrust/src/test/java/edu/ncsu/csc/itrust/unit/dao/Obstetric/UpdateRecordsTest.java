package edu.ncsu.csc.itrust.unit.dao.Obstetric;

import edu.ncsu.csc.itrust.model.old.beans.ObstetricInfoBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricInfoDAO;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UpdateRecordsTest extends TestCase {
    private ObstetricInfoDAO obstetricInfoDAO = TestDAOFactory.getTestInstance().getObstetricInfoDAO();

    @Override
    protected void setUp() throws Exception {
        TestDataGenerator gen = new TestDataGenerator();
        gen.clearAllTables();
    }

    public void testUpdateRecord() throws Exception {
        List<ObstetricInfoBean> list = obstetricInfoDAO.getObstetricInfoForMID(1);
        ObstetricInfoBean info = list.get(0);
        info.setNumberOfHoursInLabor(8);
        info.setYearsOfConception(8);
        obstetricInfoDAO.updateObstetricInfo(info);
        int size = list.size();
        long mid = info.getMID();


        list = obstetricInfoDAO.getObstetricInfoForMID(mid);
        info = list.get(0);
        assertEquals(info.getInitDate().toString(), (new java.sql.Date(new Date().getTime())).toString());
        assertEquals(info.getMID(), 1);
        assertEquals(info.getLMP().toString(), (new java.sql.Date(new Date().getTime())).toString());
        assertEquals(info.getDeliveryType().getName(), DeliveryType.NS.getName());
        Calendar c = Calendar.getInstance();
        c.setTime(info.getLMP());
        c.add(Calendar.DAY_OF_YEAR, 280);
        assertEquals(info.getEDD(), c.getTime());
        assertEquals(info.getNumberOfHoursInLabor(), 8);
        assertEquals(info.getWeightGainDuringPregnancy(), 5);
        assertEquals(info.getNumBirths(), 6);
        assertEquals(info.getYearsOfConception(), 8);

    }
}

