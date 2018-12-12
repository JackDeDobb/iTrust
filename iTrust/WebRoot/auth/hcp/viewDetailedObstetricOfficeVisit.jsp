<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditObstetricOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.UltrasoundRecordDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ApptDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.AddObstetricOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.server.ImageStore"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
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
	UltrasoundRecordDAO ultrasoundDAO = prodDAO.getUltrasoundRecordDAO();
	List<UltrasoundRecordBean> ultrasoundRecords = ultrasoundDAO.getUltrasoundRecordsByVisitID(visitId);
	UltrasoundRecordBean ultrasoundRec = null;
	if (!ultrasoundRecords.isEmpty()) {
		ultrasoundRec = ultrasoundRecords.get(0);
	}

	
	// get visit date
	Date visitDate = new Date(visit.getVisitDate().getTime());
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
	String dateString = format.format(visitDate);
	
	boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");
	
	if (formIsFilled) {
		ObstetricOfficeVisitBean newVisit = new ObstetricOfficeVisitBean();
		newVisit.setPatientMID(visit.getPatientMID());
		newVisit.setHcpMID(visit.getHcpMID());
		newVisit.setObstetricRecordID(0);
		newVisit.setWeight(Float.valueOf(request.getParameter("weight")));
		newVisit.setSystolicBloodPressure(Float.valueOf(request.getParameter("systolicBP")));
		newVisit.setDiastolicBloodPressure(Float.valueOf(request.getParameter("diastolicBP")));
		newVisit.setFetalHeartRate(Float.valueOf(request.getParameter("fetalHeartRate")));
		newVisit.setLowLyingPlacentaObserved(Integer.valueOf(request.getParameter("lowLyingPlacentaObserved")));
		newVisit.setNumberOfBabies(Integer.valueOf(request.getParameter("numberOfBabies")));
		pb.setRHImmunization(Boolean.parseBoolean(request.getParameter("RHImmunization")));
		paction.updateInformation(pb);

		Date date = (Date) format.parse(request.getParameter("visitDate"));
		Timestamp visitTimestamp = new Timestamp(date.getTime());
		newVisit.setVisitDate(visitTimestamp);
		
		try{
			editVisitAction.updateVisitInformation(newVisit);
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
		<td><input type="datetime-local" value="<%= StringEscapeUtils.escapeHtml("" + dateString) %>" name="visitDate"></td>   
	</tr>
	<tr>
		<td class="subHeaderVertical">Weight:</td>
		<td><input type="number" value="<%= StringEscapeUtils.escapeHtml("" + (visit.getWeight())) %>" name="weight"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Systolic Blood Pressure:</td>
		<td><input type="number" value="<%= StringEscapeUtils.escapeHtml("" + (visit.getSystolicBloodPressure())) %>"
				   name="systolicBP"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Diastolic Blood Pressure:</td>
		<td><input type="number" value="<%= StringEscapeUtils.escapeHtml("" + (visit.getDiastolicBloodPressure())) %>"
				   name="diastolicBP"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Fetal Heart Rate:</td>
		<td><input type="number" value="<%= StringEscapeUtils.escapeHtml("" + (visit.getFetalHeartRate())) %>" name="fetalHeartRate"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Low Lying Placenta Observed:</td>
		<td><input type="number" step="1" value="<%= StringEscapeUtils.escapeHtml("" + (visit.getLowLyingPlacentaObserved())) %>" name="lowLyingPlacentaObserved"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Number of Babies:</td>
		<td><input type="number" step="1" value="<%= StringEscapeUtils.escapeHtml("" + (visit.getNumberOfBabies())) %>" name="numberOfBabies"></td>
	</tr>
	<% if (ultrasoundRec != null) { %>
	<tr>
		<th colspan=2>Ultrasound Information</th>
	</tr>
	<tr class="ultra" >
		<td class="subHeaderVertical">Crown Rump Length:</td>
		<td><input type="number" name="crownRumpLength" value="<%= StringEscapeUtils.escapeHtml("" + ultrasoundRec.getCrownRumpLength())%>"></td>
	</tr>
	<tr class="ultra" >
		<td class="subHeaderVertical">Head circumference:</td>
		<td><input type="number" name="headCircumference" value="<%= StringEscapeUtils.escapeHtml("" + ultrasoundRec.getHeadCircumference())%>"></td>
	</tr>
	<tr class="ultra" >
		<td class="subHeaderVertical">Biparietal Diameter:</td>
		<td><input type="number" name="biparietalDiameter" value="<%= StringEscapeUtils.escapeHtml("" + ultrasoundRec.getBiparietalDiameter())%>"></td>
	</tr>
	<tr class="ultra" >
		<td class="subHeaderVertical">Femur Length:</td>
		<td><input type="number" name="femurLength" value="<%= StringEscapeUtils.escapeHtml("" + ultrasoundRec.getFemurLength())%>"></td>
	</tr>
	<tr class="ultra" >
		<td class="subHeaderVertical">Occipitofrontal Diameter:</td>
		<td><input type="number" name="occipitofrontalDiameter" value="<%= StringEscapeUtils.escapeHtml("" + ultrasoundRec.getOccipitofrontalDiameter())%>"></td>
	</tr>
	<tr class="ultra" >
		<td class="subHeaderVertical">Abdominal Circumference:</td>
		<td><input type="number" name="abdominalCircumference" value="<%= StringEscapeUtils.escapeHtml("" + ultrasoundRec.getAbdominalCircumference())%>"></td>
	</tr>
	<tr class="ultra" >
		<td class="subHeaderVertical">Humerus Length:</td>
		<td><input type="number" name="humerusLength" value="<%= StringEscapeUtils.escapeHtml("" + ultrasoundRec.getHumerusLength())%>"></td>
	</tr>
	<tr class="ultra" >
		<td class="subHeaderVertical">Estimated Fetal Weight:</td>
		<td><input type="number" name="estimatedFetalWeight" value="<%= StringEscapeUtils.escapeHtml("" + ultrasoundRec.getEstimatedFetalWeight())%>"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Is patient RH- immunized?:</td>
		<td><select name="RHImmunization">
			<option value="true" <%= StringEscapeUtils.escapeHtml(pb.isRH() ? "selected=selected" : "")%>>Yes</option>
			<option value="false" <%= StringEscapeUtils.escapeHtml(!pb.isRH() ? "selected=selected" : "")%>>No
			</option>
		</select>
	</tr>
	<% } %>
</table>
<br />
<% if (ultrasoundRec != null) { %>
	<%--<img src="<%="/iTrust" + ImageStore.baseFilePath + File.separator + ultrasoundRec.getImagePath()%>" />--%>
	<a href="<%="/iTrust" + ImageStore.baseFilePath + File.separator + ultrasoundRec.getImagePath()%>">View
		Ultrasound Image</a>
	<br />
<% } %>
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
