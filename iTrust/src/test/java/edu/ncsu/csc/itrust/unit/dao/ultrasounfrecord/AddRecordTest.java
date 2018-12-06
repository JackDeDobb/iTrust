package edu.ncsu.csc.itrust.unit.dao.ultrasounfrecord;

import cucumber.api.java.gl.E;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.UltrasoundRecordDAO;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.util.List;

public class AddRecordTest extends TestCase {
    UltrasoundRecordDAO ultrasoundRecordDAO = TestDAOFactory.getTestInstance().getUltrasoundRecordDAO();

    public void testAdd() throws Exception {
        UltrasoundRecordBean bean = new UltrasoundRecordBean();
        bean.setCrownRumpLength((float)1.0);
        bean.setBiparietalDiameter((float)2.0);
        bean.setHeadCircumference((float)3.0);
        bean.setFemurLength((float)4.0);
        bean.setOccipitofrontalDiameter((float)5.0);
        bean.setAbdominalCircumference((float)6.0);
        bean.setHumerusLength((float)7.0);
        bean.setEstimatedFetalWeight((float)0.618);
        bean.setVisitID(273829);

        ultrasoundRecordDAO.addUltrasoundRecord(bean);
        List<UltrasoundRecordBean> list = ultrasoundRecordDAO.getUltrasoundRecordsByVisitID(273829);
        bean = list.get(list.size() - 1);
        assertEquals((float)0.618, bean.getEstimatedFetalWeight());
        assertEquals((float)2.0, bean.getBiparietalDiameter());
        assertEquals((float)7.0, bean.getHumerusLength());
    }
}
