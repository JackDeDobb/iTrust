<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricInfoBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricInfoDAO" %>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.action.LaborDeliveryReportAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricInfoAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.DBException"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Date"%>
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
	ObstetricInfoBean oBean = null;
	
	try {
		ViewObstetricOfficeVisitAction action = new ViewObstetricOfficeVisitAction(prodDAO, loggedInMID, patientMID);
		visits = action.getObstetricOfficeVisitRecords();

		EditPatientAction paction = new EditPatientAction(prodDAO, loggedInMID.longValue(), pidString);
		PatientBean pb = paction.getPatient();
		isEligible = pb.getObstetricEligibility();
		
		ObstetricInfoDAO oDAO = prodDAO.getObstetricInfoDAO();
		oBean = oDAO.getMostRecentObstetricInfoForMID(patientMID);
	} catch (DBException ex) {
	    System.err.println("Uncaught DBException i viewObstetricOfficeVisit.jsp");
	    ex.printStackTrace();
	}
	
	if(visits.isEmpty() || oBean == null) {
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
		if(allergies.isEmpty()) { 
			allergies.add("None");
		}
		List<String> conditions = reportAction.getConditions();
		if(conditions.isEmpty()) { 
			conditions.add("None");
		}
		
		String bloodType = reportAction.getBloodType();
		boolean rhFlag = reportAction.hasRHFlag();
		boolean highBloodPressure = reportAction.hasHighBloodPressure();
		boolean advancedMaternalAge = reportAction.hasAdvancedMaternalAge();
		boolean abnormalFetalHeartRate = reportAction.hasAbnormalFetalHeartRate();
		boolean lowLyingPlacenta = reportAction.hasLowLyingPlacenta();
		boolean multiples = reportAction.hasMultiples();
		boolean atypicalWeightChange = reportAction.hasAtypicalWeightChange();
		
		List<ObstetricOfficeVisitBean> oVisits = reportAction.getAllObstetricsOfficeVisits();
		
		List<ObstetricInfoBean> pastPregnancies = reportAction.getPriorPregnancies();
%>
	<div align=center>
		<h1>Labor & Delivery Report</h1>
		<h3>Past Pregnancies</h3>
		<table class="fTable">
			<tr>
				<th>Pregnancy Term</th>
				<th>Delivery Type</th>
				<th>Conception Year</th>
				<th>Estimated Delivery Date</th>
			</tr>
<%

		for(ObstetricInfoBean pastPreg: pastPregnancies) {
			String deliveryType = pastPreg.getDeliveryType().name();
			long conceptionYear = pastPreg.getYearsOfConception();
			
			Date edd = pastPreg.getEDD();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String eddDateString  = dateFormat.format(edd);
			Date lmp = pastPreg.getLMP();
			String lmpDateString = dateFormat.format(lmp);

%>
			<tr>
				<td><%= StringEscapeUtils.escapeHtml(lmpDateString + " - " + eddDateString) %></td>
				<td><%= StringEscapeUtils.escapeHtml(deliveryType)%></td>
				<td><%= StringEscapeUtils.escapeHtml("" + conceptionYear)%></td>
				<td><%= StringEscapeUtils.escapeHtml(eddDateString)%></td>
			</tr>	
<%
		}
%>	
		</table>
		<h3>Obstetric Office Visits</h3>
		<table class="fTable">
			<tr>
				<th>Date</th>
				<th>Weeks Pregnant</th>
				<th>Weight</th>
				<th>Blood Pressure</th>
				<th>Fetal Heart Rate</th>
				<th>Number of Babies</th>
				<th>Low-Lying Placenta</th>
				<th>Complications</th>
			</tr>
<%
		for(ObstetricOfficeVisitBean visit : oVisits) {
			Date date = new Date();
			date.setTime(visit.getVisitDate().getTime());
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String dateString  = dateFormat.format(date);
			
			ViewObstetricInfoAction obstetricInfoAction = new ViewObstetricInfoAction(prodDAO, loggedInMID.longValue(), pidString);
			ObstetricInfoBean r = obstetricInfoAction.getRecordById(visit.getObstetricRecordID());
			String timePregnantAtVisit = reportAction.getTimePregnantAtVisit(visit, r);
%>
			<tr>
				<td><%= StringEscapeUtils.escapeHtml("" + dateString)%></td>
				<td><%= StringEscapeUtils.escapeHtml("" + timePregnantAtVisit)%></td>
				<td><%= StringEscapeUtils.escapeHtml("" + visit.getWeight())%></td>
				<td><%= StringEscapeUtils.escapeHtml("" + visit.getBloodPressure())%></td>
				<td><%= StringEscapeUtils.escapeHtml("" + visit.getFetalHeartRate())%></td>
				<td><%= StringEscapeUtils.escapeHtml("" + visit.getNumberOfBabies())%></td>
				<td><% if(visit.getLowLyingPlacentaObserved() > 0) { %> Yes <% } else { %> No <% } %></td>
				<td>None</td>
			
			</tr>
<%
		}

%>
			
		</table>
		<h3>Patient Information</h3>
		<table class="fTable">
			<tr>
				<th>Type</th>
				<th>Details</th>
			</tr>
			<tr>
				<td>Blood Type</td>
				<td><%= StringEscapeUtils.escapeHtml("" + bloodType)%></td>
			</tr>	
			<tr>
				<td>RH Flag</td>
				<td><% if(rhFlag) { %> Yes <% } else { %> No <% } %></td>
			</tr>
			<tr>
				<td>High Blood Pressure</td>
				<td><% if(highBloodPressure) { %> Yes <% } else { %> No <% } %></td>
			</tr>
			<tr>
				<td>Advanced Maternal Age</td>
				<td><% if(advancedMaternalAge) { %> Yes <% } else { %> No <% } %></td>
			</tr>
			<tr>
				<td>Low-Lying Placenta</td>
				<td><% if(lowLyingPlacenta) { %> Yes <% } else { %> No <% } %></td>
			</tr>
			<tr>
				<td>Abnormal Fetal Heart Rate</td>
				<td><% if(abnormalFetalHeartRate) { %> Yes <% } else { %> No <% } %></td>
			</tr>
			<tr>
				<td>Multiples</td>
				<td><% if(multiples) { %> Yes <% } else { %> No <% } %></td>
			</tr>
			<tr>
				<td>Atypical Weight Change</td>
				<td><% if(atypicalWeightChange) { %> Yes <% } else { %> No <% } %></td>
			</tr>
			<tr>
				<td>Allergies</td>
				<td><%= StringEscapeUtils.escapeHtml("" + String.join(", ", allergies)) %></td>
			</tr>
			<tr>
				<td>Conditions</td>
				<td><%= StringEscapeUtils.escapeHtml("" + String.join(", ", conditions)) %></td>
			</tr>
		</table>
	</div>
<%		
	}
%>

<%@include file="/footer.jsp"%>