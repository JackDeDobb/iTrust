<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.action.LaborDeliveryReportAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.DBException"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
pageTitle = "iTrust - View Labor & Delivery Report";
%>

<%@include file="/header.jsp"%>

<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
		out.println("pidstring is null");
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/viewLaborDeliveryReport.jsp");
		return;
	}
	
	List<ObstetricOfficeVisitBean> visits = new ArrayList<ObstetricOfficeVisitBean>();
	boolean isEligible = false;
	long patientMID = Long.parseLong(pidString);
	try {
		ViewObstetricOfficeVisitAction action = new ViewObstetricOfficeVisitAction(prodDAO, loggedInMID, patientMID);
		visits = action.getObstetricOfficeVisitRecords();

		EditPatientAction paction = new EditPatientAction(prodDAO, loggedInMID.longValue(), pidString);
		PatientBean pb = paction.getPatient();
		isEligible = pb.getObstetricEligibility();
	} catch (DBException ex) {
	    System.err.println("Uncaught DBException i viewObstetricOfficeVisit.jsp");
	    ex.printStackTrace();
	}
	
	if(visits.isEmpty()) {
%>
	<div align=center>
		<h1>The selected patient does not have any obstetrics record.</h1>
	</div>
<%		
	
	} else if(!isEligible) {
%>
	<div align=center>
		<h1>The selected patient is not eligible for obstetric care.</h1>
	</div>
<%
	} else {
		LaborDeliveryReportAction reportAction = new LaborDeliveryReportAction(prodDAO, loggedInMID, pidString);
		List<String> allergies = reportAction.getAllergies();
		boolean rhFlag = reportAction.hasRHFlag();
%>
	<div align=center>
		<h2>Patient Information</h2>
		<table class="fTable">
			<tr>
				<th>Type</th>
				<th>Details</th>
			</tr>
			<tr>
				<td>RH Flag</td>
				<td><% if(rhFlag) { StringEscapeUtils.escapeHtml("Yes"); } else { StringEscapeUtils.escapeHtml("No"); }%></td>
			</tr>
			<tr>
				<td>Allergies</td>
				<td><%= StringEscapeUtils.escapeHtml("" + String.join(", ", allergies)) %></td>
			</tr>
		</table>
	</div>
<%		
	}
%>

<%@include file="/footer.jsp"%>