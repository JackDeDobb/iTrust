<%@page import="com.mysql.jdbc.StringUtils"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.TransactionBean"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Role"%>
<%@page import="java.util.Optional"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.TransactionDAO.DateRange"%>

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
	<input id="start-date" name="start-date" type="date" value="*">
	&nbsp;&nbsp;
	<label for="end-date">End Date: </label>
	<input id="end-date" name="end-date" type="date" value="*">
	&nbsp;&nbsp;
	<input type="submit" value="Filter" />
</form>
<br/>
<form>
	<input type="submit" value="Summarize" />
</form>
<br/>

<%
    // Parse role input string.
    String roleString = request.getParameter("select-role");
    Optional<String> roleStringOpt = Optional.ofNullable(roleString);
    Optional<Role> role;
    if ((roleStringOpt.isPresent() && roleStringOpt.get().equals("all")) || !roleStringOpt.isPresent())
        role = Optional.empty();
    else
        role = Optional.ofNullable(Role.parse(roleString));

    // Parse second role input string.
    String secondRoleString = request.getParameter("select-second-role");
    Optional<String> secondRoleStringOpt = Optional.ofNullable(secondRoleString);
    Optional<Role> secondRole;
    if ((secondRoleStringOpt.isPresent() && secondRoleStringOpt.get().equals("all")) ||
            !secondRoleStringOpt.isPresent())
        secondRole = Optional.empty();
    else
        secondRole = Optional.ofNullable(Role.parse(secondRoleString));

    // Parse transaction type input string.
    String transactionTypeString = request.getParameter("select-tx-type");
    Optional<TransactionType> transactionType;
    try {
        transactionType = Optional.ofNullable(TransactionType.parse(Integer.parseInt(transactionTypeString)));
    } catch (NumberFormatException e) {
        transactionType = Optional.empty();
    }

    // Parse date range strings.
    String startDate = request.getParameter("start-date");
    String endDate = request.getParameter("end-date");
    Optional<DateRange> dateRange = new DateRange(startDate, endDate).getOptional();

    List<TransactionBean> list = DAOFactory.getProductionInstance().getTransactionDAO().getTransactionsWithFilter(
            role, secondRole, transactionType, dateRange
    );

    if(list.size() == 0){
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