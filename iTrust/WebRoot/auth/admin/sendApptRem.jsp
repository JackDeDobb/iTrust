<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.base.SendRemindersAction"%>
<%@page import="java.lang.NumberFormatException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@include file="/global.jsp" %>
<%
    pageTitle = "iTrust - Send Appointment Reminders";
%>
<%@include file="/header.jsp" %>

<%
    if(request.getParameter("days") != null) {
        String para = request.getParameter("days");
        SendRemindersAction action = new SendRemindersAction(prodDAO);
        try {
            int days = Integer.parseInt(para);
            if(days < 0) {
%> <h1 align="center">Please input a positive number</h1> <%
} else {
    action.sendReminders(days);
%><h1 align="center">Reminders Sent</h1><%
    }
} catch (NumberFormatException e) {
%> <h1 align="center">Please input a number</h1> <%
        }
    }
%>
<div align="center">
    <br />

    <form name="mainForm" action="sendApptRem.jsp" id="mainForm" method="post">
        <table class="fTable" align="center">
            <tr>
                <td colspan="3">Send Appointment Reminders By </td>
                <td><input type="text" name="days" id="days" size="3" maxlength="3" /></td>
                <td colspan="3"> Days</td>
            </tr>
        </table>
        <br />
        <br />
        <input type="submit" name="send" id="send" value="Send Appointment Reminders" />
    </form>
</div>
<br />
<%@include file="/footer.jsp" %>
