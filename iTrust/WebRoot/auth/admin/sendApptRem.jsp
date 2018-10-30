<%--
  Created by IntelliJ IDEA.
  User: Yucheng Wang
  Date: 10/30/2018
  Time: 6:47 AM
  To change this template use File | Settings | File Templates.
--%>
<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@page import="java.net.URLEncoder" %>
<%@page import="java.util.List"%>
<%@include file="/global.jsp" %>
<%
    pageTitle = "iTrust - Send Appointment Reminders";
%>

<%@include file="/header.jsp" %>

<%
    if(request.getParameter("days") == null) {
        String para = request.getParameter("days");
        try {
            int days = Integer.parseInt(para);
            if(days < 0) {
%> <h1 align="center">Please input a positive number</h1> <%
            } else {
                DAOFactory.getProductionInstance().getFakeEmailDAO()
            }
        } catch (NumberFormatException e) {
%> <h1 align="center">Please input a number</h1> <%
        }
    }

%>

<div align="center">
    <br />

    <form name="mainForm" action="sendApptRem.jsp.jsp" id="mainForm" method="post">
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
