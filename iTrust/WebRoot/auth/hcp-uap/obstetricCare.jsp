<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricInfoAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewChildBirthVisit"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Ethnicity"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.DeliveryType"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricInfoBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricInfoDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ChildBirthVisitDAO"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Edit Patient";
%>

<%@include file="/header.jsp"%>
<itrust:patientNav thisTitle="Demographics" />
<%	
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
		out.println("pidstring is null");
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/obstetricCare.jsp");
		return;
	}
	
	/* If the patient id doesn't check out, then kick 'em out to the exception handler */
	EditPatientAction action = new EditPatientAction(prodDAO,
			loggedInMID.longValue(), pidString);
	ViewPersonnelAction hcpAction = new ViewPersonnelAction(prodDAO, loggedInMID.longValue());
	
	PersonnelBean hcp = hcpAction.getPersonnel(String.valueOf(loggedInMID.longValue()));
	boolean isOBGYN = hcp.getSpecialty().equals("OB/GYN");
	
	PatientBean p = action.getPatient();
	boolean obstEligibility = p.getObstetricEligibility();
	
	if(request.getParameter("eligibilityAction") != null){
		p.setObstetricEligibility(true);
		action.updateInformation(p);
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/obstetricCare.jsp");
	}
	
	if(request.getParameter("Try") != null){
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/obstetricCare.jsp");
	}
	
	
	
	//if patient is eligibile for care, display record
	
	if(!obstEligibility){
		%>
			<div align=center>
				<h1>THIS PATIENT IS NOT ELIGIBLE</h1>
				<form action = "obstetricCare.jsp" method="POST">
					<input type="submit" name="eligibilityAction" style="font-size: 10pt; font-weight: bold;" value="Change Eligibility">
				</form>
				
				<form action="obstetricCare.jsp" method="POST">
					<input type="submit" style="font-size: 10pt; font-weight: bold;" name="Try" value="Try Again"/>
				</form>
				
			</div>
		<%
	}
	//if not eligible, 
	else{
		
		
		ViewObstetricInfoAction obstetricInfoAction = new ViewObstetricInfoAction(prodDAO, loggedInMID.longValue(), pidString);
		List<ObstetricInfoBean> list = obstetricInfoAction.getAllRecords(pidString);
		
		
		
		if (list != null && list.size() > 0) { 
		%>
		<div align=center>
	    <br />
	    <table class="fancyTable">
	        <tr>
	            <th>Record ID</th>
	            <th>Date Created</th>
	            <th>Last Menstrual Period</th>
	            <th></th>
	        </tr>
	        <%		int index = 0; %>
	        <%		for(ObstetricInfoBean record : list) { %>
	        <tr <%=(index%2 == 1)?"class=\"alt\"":"" %>>
	            <td><%= StringEscapeUtils.escapeHtml("" + ( record.getRecordId())) %></td>
	            <td><%= StringEscapeUtils.escapeHtml("" + ( record.getInitDate())) %></td>
	            <td><%= StringEscapeUtils.escapeHtml("" + ( record.getLMP())) %></td>
	            <td><a href="viewIndividualObstetricsRecord.jsp?msg=<%= StringEscapeUtils.escapeHtml("" + ( record.getRecordId() )) %>">View</a></td>
	        </tr>
	        <%			index ++; %>
	        <%		} %>
	        <%		} %>
	    </table>
	     <%
	    ViewChildBirthVisit childBirthVisitAction = new ViewChildBirthVisit(prodDAO, loggedInMID.longValue(), pidString);
		List<ChildBirthVisitBean> cb = childBirthVisitAction.getAllRecords(pidString);
		
		if (cb != null && cb.size() > 0) { 
		%>
		<div align=center>
	    <br />
	    <table class="fancyTable">
	        <tr>
	            <th>Record ID</th>
	            <th></th>
	        </tr>
	        <%		int index = 0; %>
	        <%		for(ChildBirthVisitBean record : cb) { %>
	        <tr <%=(index%2 == 1)?"class=\"alt\"":"" %>>
	            <td><%= StringEscapeUtils.escapeHtml("" + ( record.getId())) %></td>
	            <td><a href="viewIndividualChildBirthVisit.jsp?msg=<%= StringEscapeUtils.escapeHtml("" + ( record.getId() )) %>">View</a></td>
	        </tr>
	        <%			index ++; %>
	        <%		} %>
	    </table>
	    <%	} else { %>
	    <div align=center>
	        <i>You have no obstetric records for this patient!</i>
	    </div>
	    <%	} %>
	    <br />
	    
	    
	    
	    
	    <%	if (isOBGYN) { %>
	    	<a href="addNewObstetricRecord.jsp" style="font-size: 16pt; font-weight: bold;">Add Obstetric Record</a>
	    	<br/>
	    	<a href="addNewChildBirthVisit.jsp" style="font-size: 16pt; font-weight: bold;">Add New Child Birth Record</a>
	    	<br/>
	    	<a href="addNewBabyDeliveryInfo.jsp" style="font-size: 16pt; font-weight: bold;">Add New Baby</a>
	    <%	} %>
	    
	    
	    
	    
	    
		</div>
		



<% }  %>


<%@include file="/footer.jsp"%>