package edu.ncsu.csc.itrust.action.base;

import cucumber.api.java.ca.Cal;
import edu.ncsu.csc.itrust.action.SendMessageAction;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.beans.Email;
import edu.ncsu.csc.itrust.model.old.beans.MessageBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ApptDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SendRemindersAction {
    private  DAOFactory factory;
    private ApptDAO adao;
    private SendMessageAction smAction;
    private PersonnelDAO pdao;

    public SendRemindersAction(DAOFactory factory) {
        this.adao = factory.getApptDAO();
        this.smAction = new SendMessageAction(factory, 9000000001L);
        this.pdao = factory.getPersonnelDAO();
        this.factory = factory;
    }

    public void sendReminders(int n) throws DBException, ITrustException, SQLException, FormValidationException {
        try {
            List<ApptBean> list = adao.getApptForReminders(n);
            for (ApptBean item: list){
                MessageBean email = new MessageBean();
                email.setFrom(9000000009L);
                email.setTo(item.getPatient());
                Date date = new Date(item.getDate().getTime());
                Date now = new Date();
                long remains = (date.getTime() - now.getTime()) / (1000 * 24 * 60 * 60) + 1;
                email.setSubject("Reminder: upcoming appointment in " + remains +  "day(s)");
                email.setRead(0);
                email.setBody("You have an appointment on " + date.toString() + " with Dr. " + pdao.getPersonnel(item.getHcp()).getFullName());
                smAction.sendMessage(email);
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
