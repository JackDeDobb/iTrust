<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Ethnicity"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.DeliveryType"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>

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
			<div>
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
		
		
%>

<form id="editForm" action="editPatient.jsp" method="post"><input type="hidden"
	name="formIsFilled" value="true"> <br />
<table cellspacing=0 align=center cellpadding=0>
	<tr>
		<td valign=top>
		<table class="fTable" align=center style="width: 350px;">
			<tr>
				<th colspan=2>Patient Information</th>
			</tr>		
			<tr>
			
				<td class="subHeaderVertical">First Name:</td>
				<td><input name="firstName" value="<%= StringEscapeUtils.escapeHtml("" + (p.getFirstName())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Last Name:</td>
				<td><input name="lastName" value="<%= StringEscapeUtils.escapeHtml("" + (p.getLastName())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Year of Conception:</td>
				<td><input name="email" value="<%= StringEscapeUtils.escapeHtml("" + (p.getEmail())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Number of Weeks Pregnant:</td>
				<td><input name="email" value="<%= StringEscapeUtils.escapeHtml("" + (p.getEmail())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Number of Hours in Labor:</td>
				<td><input name="email" value="<%= StringEscapeUtils.escapeHtml("" + (p.getEmail())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Weight Gained During Pregnancy:</td>
				<td><input name="email" value="<%= StringEscapeUtils.escapeHtml("" + (p.getEmail())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Delivery Type:</td>
				<td><select name="deliveryTypeStr">
					<%
						String selected = "";
						for (DeliveryType dt : DeliveryType.values()) {
							selected = (dt.equals(/*p.getDeliveryType()*/"hi")) ? "selected=selected"
									: "";
					%>
					<option value="<%=dt.getName()%>" <%= StringEscapeUtils.escapeHtml("" + (selected)) %>><%= StringEscapeUtils.escapeHtml("" + (dt.getName())) %></option>
					<%
						}
					%>
				</select>
			</tr>
			<tr>
				<td class="subHeaderVertical">Number Infants Born:</td>
				<td><input name="email" value="<%= StringEscapeUtils.escapeHtml("" + (p.getEmail())) %>" type="text"></td>
			</tr>
		</table>
		<br />
		</td>
		<td width="15px">&nbsp;</td>
		
	</tr>
</table>
<br />
<div align=center>
	<% if(p.getDateOfDeactivationStr().equals("")){ %>
	<input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="Edit Patient Record">
	<% } else { %>
	<span style="font-size: 16pt; font-weight: bold;">Patient is deactivated.  Cannot edit.</span>
	<% } %>
	<br /><br />
	<span style="font-size: 14px;">
		Note: in order to set the password for this user, use the "Reset Password" link at the login page.
	</span>
</div>
</form>
<br />
<br />
<itrust:patientNav thisTitle="Demographics" />


<% }  %>


<%@include file="/footer.jsp"%>
