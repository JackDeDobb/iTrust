<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitAction"%>
<%@page import="java.util.List"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
pageTitle = "iTrust - View Obsetric Office Visits";
%>

<%@include file="/header.jsp"%>

<h1>Obstetric Office Visits</h1>
<form method="post" action="viewObstetricOfficeVisit.jsp">
	<input type="hidden" name="midIsFilled" value="true"><br />
	<label for="patient-mid">Patient MID: </label>
	<input type="text" name="patient-mid" id="patient-mid">
	&nbsp;&nbsp;
	<input type="submit" value="Submit" />
</form>

<%
	boolean midIsFilled = request.getParameter("midIsFilled") != null && request.getParameter("midIsFilled").equals("true");
	if(midIsFilled) {
		long patientMID = Long.parseLong(request.getParameter("patient-mid"));
		ViewObstetricOfficeVisitAction action = new ViewObstetricOfficeVisitAction(prodDAO, loggedInMID, patientMID);
		List<ObstetricOfficeVisitBean> visits = action.getObstetricOfficeVisitRecords();
%>

<button onclick="window.location.href='/auth/hcp/addObstetricOfficeVisit.jsp'"></button>
<br/>

<%
	}
%>


<%@include file="/footer.jsp"%>