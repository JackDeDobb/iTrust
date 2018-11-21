<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean"%>
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
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Date"%>
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
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/addObstetricOfficeVisit.jsp");
		return;
	}
	
	long patientMID = Long.parseLong(pidString);
	
	ViewPersonnelAction hcpAction = new ViewPersonnelAction(prodDAO, loggedInMID.longValue());
	PersonnelBean hcp = hcpAction.getPersonnel(String.valueOf(loggedInMID.longValue()));
	boolean isOBGYN = hcp.getSpecialty().equals("OB/GYN");
	
	EditPatientAction paction = new EditPatientAction(prodDAO,loggedInMID.longValue(), pidString);
	PatientBean pb = paction.getPatient();
	boolean isEligible = pb.getObstetricEligibility();
	
	AddObstetricOfficeVisitAction addOOVisitAction = new AddObstetricOfficeVisitAction(prodDAO, loggedInMID);
	boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");

	if (formIsFilled) {
		ObstetricOfficeVisitBean newVisit = new ObstetricOfficeVisitBean();
		newVisit.setPatientMID(patientMID);
		newVisit.setHcpMID(hcp.getMID());
		newVisit.setObstetricRecordID(Long.valueOf("obstetricRecordID"));
		newVisit.setWeight(Float.valueOf(request.getParameter("weight")));
		newVisit.setBloodPressure(Float.valueOf(request.getParameter("bloodPressure")));
		newVisit.setFetalHeartRate(Float.valueOf(request.getParameter("fetalHeartRate")));
		newVisit.setLowLyingPlacentaObserved(Integer.valueOf(request.getParameter("lowLyingPlacentaObserved")));
		newVisit.setNumberOfBabies(Integer.valueOf(request.getParameter("numberOfBabies")));
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = (Date) format.parse(request.getParameter("visitDate"));
		Timestamp visitTimestamp = new Timestamp(date.getTime());
		newVisit.setVisitDate(visitTimestamp);
		
		try{
			addOOVisitAction.addObstetricOfficeVisit(newVisit);
%>

	<div align=center>
		<span class="iTrustMessage">New Obstetric Office Visit added!</span>
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
		<td><%= StringEscapeUtils.escapeHtml("" + patientMID) %></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Visit Date:</td>
		<td><input type="date" name="visitDate"></td>   
	</tr>
	<tr>
		<td class="subHeaderVertical">Obstetric Record ID:</td>
		<td><input type="text" name="obstetricRecordID"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Weight:</td>
		<td><input type="number" name="weight"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Blood Pressure:</td>
		<td><input type="number" name="bloodPressure"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Fetal Heart Rate:</td>
		<td><input type="number" name="fetalHeartRate"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Low Lying Placenta Observed:</td>
		<td><input type="number" step="1" name="lowLyingPlacentaObserved"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Number of Babies:</td>
		<td><input type="number" step="1" name="numberOfBabies"></td>
	</tr>

</table>
<br />
<input type="submit" style="font-size: 16pt; font-weight: bold;" value="Add Visit">
<br />
</form>
</div>
<%
	} else if (!isOBGYN) {
%>
<div align=center>
	<p style="width: 50%; text-align:left;">Please use the <a href="/iTrust/auth/hcp-uap/viewOfficeVisit.xhtml">Document Office Visit</a> page.</p>
</div>
<%
	} else if (!isEligible) {
%>
<div align=center>
<h1>Patient is not eligible!</h1>
</div>
<%
	}
%>

<%@include file="/footer.jsp" %>
