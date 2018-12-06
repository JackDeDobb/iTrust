package edu.ncsu.csc.itrust.unit.bean;

import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import gherkin.lexer.De;
import junit.framework.TestCase;

import static org.junit.Assert.*;

public class ChildBirthVisitBeanTest extends TestCase {
    public void testBean() throws Exception{
        ChildBirthVisitBean c = new ChildBirthVisitBean();
        c.setPreferredDeliveryType(DeliveryType.Miscarriage);
        c.setEpiduralAnaesthesiaDosage((float)1.0);
        c.setRhImmuneGlobulinDosage((float)2.0);
        c.setMID(4);
        c.setDelivered(false);
        c.setId(5);
        c.setPreviouslyScheduled(true);
        c.setPitocinDosage((float)6.0);
        c.setNitrousOxideDosage((float)7.0);
        c.setVisitId(8);
        c.setObstetricInitId(9);
        c.setMagnesiumSulfateDosage((float)10.0);

        assertEquals(DeliveryType.Miscarriage, c.getPreferredDeliveryType());
        assertEquals(5, c.getId());
        assertEquals((float)7.0, c.getNitrousOxideDosage());
        assertEquals((float)10.0, c.getMagnesiumSulfateDosage());
        assertFalse(c.isDelivered());
    }
}