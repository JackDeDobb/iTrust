<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditObstetricOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ApptDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.AddObstetricOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="java.util.List"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
pageTitle = "iTrust - Add Obsetric Office Visit";
%>

<%@include file="/header.jsp"%>

<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
		out.println("pidstring is null");
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/viewDetailedObstetricOfficeVisit.jsp");
		return;
	}
	
	long patientMID = Long.parseLong(pidString);
	
	ViewPersonnelAction hcpAction = new ViewPersonnelAction(prodDAO, loggedInMID.longValue());
	PersonnelBean hcp = hcpAction.getPersonnel(String.valueOf(loggedInMID.longValue()));
	boolean isOBGYN = hcp.getSpecialty().equals("OB/GYN");
	
	EditPatientAction paction = new EditPatientAction(prodDAO,loggedInMID.longValue(), pidString);
	PatientBean pb = paction.getPatient();
	boolean isEligible = pb.getObstetricEligibility();
	
	long visitId = 0;
	if (request.getParameter("id") != null) {
        String idParameter = request.getParameter("id");
        try {
            visitId = Long.parseLong(idParameter);
        } catch (NumberFormatException nfe) {
            response.sendRedirect("viewObstetricOfficeVisits.jsp");
        }
	}
	
	ViewObstetricOfficeVisitAction viewVisitAction = new ViewObstetricOfficeVisitAction(prodDAO, loggedInMID, patientMID);
	EditObstetricOfficeVisitAction editVisitAction = new EditObstetricOfficeVisitAction(prodDAO, loggedInMID);
	ObstetricOfficeVisitBean visit = viewVisitAction.getObstetricOfficeVisitByVisitId(visitId);
	
	boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");
	
	if (formIsFilled) {
		ObstetricOfficeVisitBean v = new BeanBuilder<ObstetricOfficeVisitBean>().build(request.getParameterMap(), new ObstetricOfficeVisitBean());
		try{
			editVisitAction.updateVisitInformation(v);
%>

	<div align=center>
		<span class="iTrustMessage">Obstetric Office Visit information saved!</span>
	</div>
<%
		} catch(FormValidationException e){
%>
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
	</div>
<%
		}
	}
	
	if(isOBGYN) {
%>


<div align=center>
<p style="width: 50%; text-align:left;">Enter the following information to add a new obstetric office visit.</p>

<form action="addObstetricOfficeVisit.jsp" method="post">
<input type="hidden" name="formIsFilled" value="true"><br />
<table class="fTable">
	<tr>
		<th colspan=2>Visit Information</th>
	</tr>
	<tr>
		<td class="subHeaderVertical">Patient MID:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + (visit.getPatientMID())) %></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Visit Date:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + (visit.getVisitDate().toString())) %></td>   
	</tr>
	<tr>
		<td class="subHeaderVertical">Obstetric Record ID:</td>
		<td><input type="text" value="<%= StringEscapeUtils.escapeHtml("" + (visit.getObstetricRecordID())) %>" name="weight"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Weight:</td>
		<td><input type="text" value="<%= StringEscapeUtils.escapeHtml("" + (visit.getWeight())) %>" name="weight"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Blood Pressure:</td>
		<td><input type="text" value="<%= StringEscapeUtils.escapeHtml("" + (visit.getBloodPressure())) %>" name="bloodPressure"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Fetal Heart Rate:</td>
		<td><input type="text" value="<%= StringEscapeUtils.escapeHtml("" + (visit.getFetalHeartRate())) %>" name="fetalHeartRate"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Low Lying Placenta Observed:</td>
		<td><input type="text" value="<%= StringEscapeUtils.escapeHtml("" + (visit.getLowLyingPlacentaObserved())) %>" name="lowLyingPlacentaObserved"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Number of Babies:</td>
		<td><input type="text" value="<%= StringEscapeUtils.escapeHtml("" + (visit.getNumberOfBabies())) %>" name="numberOfBabies"></td>
	</tr>
</table>
<br />
<br />
</form>
<div align="center">
	<input type="submit" name="editOfficeVisitAction" style="font-size: 16pt; font-weight: bold;" value="Save Obstetric Visit">
</div>
</div>
<%
	} 
%>

<%@include file="/footer.jsp" %>
