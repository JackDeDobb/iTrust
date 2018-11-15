<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitAction"%>
<%@page import="java.util.List"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
pageTitle = "iTrust - View Obsetric Office Visits";
%>

<%@include file="/header.jsp"%>

<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
		out.println("pidstring is null");
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/viewObstetricOfficeVisit.jsp");
		return;
	}

	long patientMID = Long.parseLong(pidString);
	ViewObstetricOfficeVisitAction action = new ViewObstetricOfficeVisitAction(prodDAO, loggedInMID, patientMID);
	List<ObstetricOfficeVisitBean> visits = action.getObstetricOfficeVisitRecords();
	
	ViewPersonnelAction hcpAction = new ViewPersonnelAction(prodDAO, loggedInMID.longValue());
	PersonnelBean hcp = hcpAction.getPersonnel(String.valueOf(loggedInMID.longValue()));
	boolean isOBGYN = hcp.getSpecialty().equals("OB/GYN");
	
	EditPatientAction paction = new EditPatientAction(prodDAO,loggedInMID.longValue(), pidString);
	PatientBean pb = paction.getPatient();
	boolean isEligible = pb.getObstetricEligibility();
	
	if(!isOBGYN) {
%>
<div align=center>
<h1>Please use the <a href="/iTrust/auth/hcp-uap/viewOfficeVisit.xhtml">Document Office Visit</a> page.</h1>
</div>

<%
	} else if(!isEligible) {
%>
<div align=center>
<h1>Patient is not eligible!</h1>
</div>
<%
	} else {
		int index = 0;
%>
<a href="addNewObstetricOfficeVisit.jsp" style="font-size: 16pt; font-weight: bold;">Add Obstetric Record</a>
<br/>

<table class="fancyTable">
	<tr>
		<th>Visit Date</th>
	    <th></th>
	    <th></th>
	</tr>
<%
		for(ObstetricOfficeVisitBean visit : visits) {
%>
	        <tr <%=(index%2 == 1)?"class=\"alt\"":"" %>>
	            <td><%= StringEscapeUtils.escapeHtml("" + ( visit.getVisitDate().toString())) %></td>
	            <td><a href="editDetailedObstetricOfficeVisit.jsp?msg=<%= StringEscapeUtils.escapeHtml("" + index) %>">View</a></td>
	        </tr>
	        <% index ++; %>
<%
		}
%>
</table>
<%
	}
%>

<%@include file="/footer.jsp"%>