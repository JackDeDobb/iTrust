<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditObstetricOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ApptDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.AddObstetricOfficeVisitAction"%>
<%@page import="java.util.List"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
pageTitle = "iTrust - Add Obsetric Office Visit";
%>

<%@include file="/header.jsp"%>

<%
	AddObstetricOfficeVisitAction addOOVisitAction = new AddObstetricOfficeVisitAction(
			prodDAO, loggedInMID);
%>
<%
	boolean formIsFilled = request.getParameter("formIsFilled") != null
	&& request.getParameter("formIsFilled").equals("true");

	if (formIsFilled) {
		
		//This page is not actually a "page", it just adds a visit and forwards.
		ObstetricOfficeVisitBean v = new BeanBuilder<ObstetricOfficeVisitBean>().build(request.getParameterMap(), new ObstetricOfficeVisitBean());
		try{
			addOOVisitAction.addObstetricOfficeVisit(v);
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
	
	PersonnelDAO pd = prodDAO.getPersonnelDAO();
	if(pd.isOBGYN(loggedInMID)) {
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
<input type="submit" style="font-size: 16pt; font-weight: bold;" value="Save">
<br />
</form>

<button onclick="window.location='/iTrust/auth/patient/viewObstetricOfficeVisit.jsp'">Finish</button>
</div>


<%
	} else {
%>
<div align=center>
	<p style="width: 50%; text-align:left;">Please use the <a href="/iTrust/auth/hcp-uap/viewOfficeVisit.xhtml">Document Office Visit</a> page.</p>
</div>
<%
	}
%>

<%@include file="/footer.jsp" %>
