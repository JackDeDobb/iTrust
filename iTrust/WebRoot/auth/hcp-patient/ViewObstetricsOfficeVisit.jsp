<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitAction"%>
<%@page import="java.util.List"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
pageTitle = "iTrust - View Obsetrics Office Visit";
%>

<%@include file="/header.jsp"%>

<%
	ViewObstetricsOfficeVisitAction viewOOVisitAction = new ViewObstetricsOfficeVisitAction(
			loggedInMID, prodDAO);
	List<ObstetricOfficeVisitBean> visits = viewOOVisitAction.getObstetricOfficeVisitRecords();
	PatientDAO pDAO = prodDAO.getPatientDAO();
%>
<h1>My Obstetric Office Visits</h1>
<%
	String msg = "";
	if (request.getParameter("visitId") != null) {
		boolean myVisit = false;
		ObstetricOfficeVisitBean theVisit = null;
		for (ApptRequestBean visit : visits) {
			if (visit.getVisitID() == request.getParameter("visitId")) {
				myVisit = true;
				theVisit = visit;
			}
		}
	
%>

<%@include file="/footer.jsp"%>