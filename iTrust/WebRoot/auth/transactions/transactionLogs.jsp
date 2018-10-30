<%@page import="com.mysql.jdbc.StringUtils"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.TransactionBean"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.List"%>

<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - View Transactions";
%>

<%@include file="/header.jsp" %>

<form method="post" action="transactionLogs.jsp">
	<label for="select-role">Role: </label>
	<select name="select-role" id="select-role">
		<option value="all">*</option>
		<option value="patient">Patient</option>
	  	<option value="er">ER</option>
	  	<option value="hcp">HCP</option>
	  	<option value="uap">UAP</option>
	  	<option value="lt">LT</option>
	  	<option value="admin">Admin</option>
	  	<option value="pha">PHA</option>
	  	<option value="tester">Tester</option>
	</select>
	&nbsp;&nbsp;
	<label for="select-second-role">Secondary Role: </label>
	<select name="select-second-role" id="select-second-role">
		<option value="all">*</option>
		<option value="patient">Patient</option>
	  	<option value="er">ER</option>
	  	<option value="hcp">HCP</option>
	  	<option value="uap">UAP</option>
	  	<option value="lt">LT</option>
	  	<option value="admin">Admin</option>
	  	<option value="pha">PHA</option>
	  	<option value="tester">Tester</option>
	</select>
	&nbsp;&nbsp;
	<label for="select-tx-type">Transaction Type:</label> 
	<select name="select-tx-type" id="select-tx-type">
		<option value="all">*</option>
		<%
			for (TransactionType type : TransactionType.values()) {
				int code = type.getCode();
		%>
			<option value="<%= code %>"><%= code %></option>
		<%
			}
		%>
	</select>
	&nbsp;&nbsp;
	<label for="start-date">Start Date: </label>
	<input id="start-date" type="date" value="*">
	&nbsp;&nbsp;
	<label for="end-date">End Date: </label>
	<input id="end-date" type="date" value="*">
	&nbsp;&nbsp;
	<input type="submit" value="Filter" />
</form>
<br/>
<form>
	<input type="submit" value="Summarize" />
</form>
<br/>

<%
    List<TransactionBean> list = DAOFactory.getProductionInstance().getTransactionDAO().getAllTransactions();

    if(true){
%>
<b>No transactions to display.</b>
<%
} else {
%>
<table class="fTable" align="center">
	<tr>
        <th>ID</th>
        <th>Time Logged</th>
        <th>Type</th>
        <th>Code</th>
        <th>Description</th>
        <th>Logged in User MID</th>
        <th>Secondary MID</th>
        <th>Extra Info</th>
    </tr>
    <%
        for (TransactionBean t : list) {
    %>
    <tr>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionID())) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getTimeLogged())) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionType().name())) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionType().getCode())) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionType().getDescription())) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getLoggedInMID())) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getSecondaryMID())) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getAddedInfo())) %></td>
    </tr>
    <%
        }
    %>
</table>
<%
    }
%>
<%@include file="/footer.jsp" %>