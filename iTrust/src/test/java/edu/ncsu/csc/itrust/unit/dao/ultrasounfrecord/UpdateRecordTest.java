package edu.ncsu.csc.itrust.unit.dao.ultrasounfrecord;

import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.UltrasoundRecordDAO;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.util.List;

public class UpdateRecordTest extends TestCase {
    UltrasoundRecordDAO ultrasoundRecordDAO = TestDAOFactory.getTestInstance().getUltrasoundRecordDAO();

    public void testUpdate() throws Exception {
        UltrasoundRecordBean bean = new UltrasoundRecordBean();
        List<UltrasoundRecordBean> list = ultrasoundRecordDAO.getUltrasoundRecordsByVisitID(273829);
        bean = list.get(list.size() - 1);
        bean.setHumerusLength((float) 3.33);
        bean.setBiparietalDiameter((float)1.11);
        ultrasoundRecordDAO.editUltrasoundRecord(bean);
        list = ultrasoundRecordDAO.getUltrasoundRecordsByVisitID(273829);
        bean = list.get(list.size() - 1);
        assertEquals((float)0.618, bean.getEstimatedFetalWeight());
        assertEquals((float)1.11, bean.getBiparietalDiameter());
        assertEquals((float)3.33, bean.getHumerusLength());
    }
}
