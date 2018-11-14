<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditObstetricOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ApptDAO"%>
<%@page import="edu.ncsu.csc.itrust.action.AddObstetricOfficeVisitAction"%>
<%@page import="java.util.List"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
pageTitle = "iTrust - Add Obsetrics Office Visit";
%>

<%@include file="/header.jsp"%>

<%
	ObstetricOfficeVisitDAO oovDAO = prodDAO.getObstetricsOfficeVisitDAO();
	authDAO = prodDAO.getAuthDAO();
	ApptDAO apptDAO = prodDAO.getApptDAO();
	AddObstetricOfficeVisitAction addOOVisitAction = new AddObstetricOfficeVisitAction(
			oovDAO, authDAO, apptDAO, loggedInMID);
%>
<%
	boolean formIsFilled = request.getParameter("formIsFilled") != null
	&& request.getParameter("formIsFilled").equals("true");

	if (formIsFilled) {
		
		//This page is not actually a "page", it just adds a visit and forwards.
		ObstetricOfficeVisitBean v = new BeanBuilder<ObstetricOfficeVisitBean>().build(request.getParameterMap(), new ObstetricsOfficeVisitBean());
		try{
			addOOVisitAction.addObstetricOfficeVisit(p);
%>

	<div align=center>
		<span class="iTrustMessage">New obstetrics office visit successfully added!</span>
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
%>


<div align=center>
<p style="width: 50%; text-align:left;">Please enter in the following fields to enter a new obstetrics office visit.</p>

<form action="addObstetricsOfficeVisit.jsp" method="post">
<input type="hidden" name="formIsFilled" value="true"><br />
<table class="fTable">
	<tr>
		<th colspan=2>Visit Information</th>
	</tr>
	<tr>
		<td class="subHeaderVertical">Visit ID:</td>
		<td><input type="text" name="visitId"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Patient MID:</td>
		<td><input type="text" name="patientMID">
	</tr>
	<tr>
		<td class="subHeaderVertical">HCP MID:</td>
		<td><input type="text" name="hcpMID"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Obstetric Record ID:</td>
		<td><input type="text" name="obstetricRecordID"></td>
	</tr>
		<tr>
		<td class="subHeaderVertical">Weight:</td>
		<td><input type="text" name="weight"></td>
	</tr>
	</tr>
		<tr>
		<td class="subHeaderVertical">Blood Pressure:</td>
		<td><input type="text" name="bloodPressure"></td>
	</tr>
	</tr>
	<tr>
		<td class="subHeaderVertical">Fetal Heart Rate:</td>
		<td><input type="text" name="fetalHeartRate"></td>
	</tr>
	</tr>
		<td class="subHeaderVertical">Low Lying Placenta Observed:</td>
		<td><select name="lowLyingPlacentaObserved">
		<option value="true">Yes</option>
		<option value="false">No</option>
	</tr>
	<tr>
		<td class="subHeaderVertical">Number of Babies:</td>
		<td><input type="text" name="numberOfBabies"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Visit Date:</td>
		<td><input type="text" name="visitDate"></td>   
	</tr>

</table>
<br />
<input type="submit" style="font-size: 16pt; font-weight: bold;" value="Add Obstetric Office Visit">
<br />
</form>
</div>

<%@include file="/footer.jsp" %>
