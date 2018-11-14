<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.MessageBean"%>
<%@page import="java.util.List"%>
<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.Calendar"%>

<%@page import="java.sql.Date"%>
<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricInfoAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Ethnicity"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.DeliveryType"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricInfoBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricInfoDAO"%>
<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - View Message";
%>

<%@include file="/header2.jsp" %>


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
	

	long recordId = 0;
	if (request.getParameter("msg") != null) {
        String msgParameter = request.getParameter("msg");
        try {
            recordId = Long.parseLong(msgParameter);
        } catch (NumberFormatException nfe) {
            response.sendRedirect("obstetricCare.jsp");
        }
	
	
	ViewObstetricInfoAction obstetricInfoAction = new ViewObstetricInfoAction(prodDAO, loggedInMID.longValue(), pidString);
	ObstetricInfoBean r = obstetricInfoAction.getRecordById(recordId);
	
	if(request.getParameter("editRecordAction") != null) {
		try {
			ObstetricInfoBean info = new ObstetricInfoBean();
	        info.setMID(r.getMID());
	        info.setRecordId(r.getRecordId());
	        boolean invalid = false;
				
			
			try {
	        	info.setYearsOfConception(Long.valueOf(request.getParameter("years")));
	        } catch(Exception e) {
	        	if (!request.getParameter("years").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid Years <br>");
	        		invalid = true;
	        	}
	        }
	        
	        try {
	        	info.setNumberOfHoursInLabor(Long.valueOf(request.getParameter("hours")));
	        } catch(Exception e) {
	        	if (!request.getParameter("hours").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid Hours <br>");
	        		invalid = true;
	        	}
	        }
	        
	        try {
	        	info.setWeightGainDuringPregnancy(Long.valueOf(request.getParameter("weight")));
	        } catch(Exception e) {
	        	if (!request.getParameter("weight").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid Weight <br>");
	        		invalid = true;
	        	}
	        }
	        
	        try {
	        	info.setNumBirths(Long.valueOf(request.getParameter("number")));
	        } catch(Exception e) {
	        	if (!request.getParameter("number").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid Number Births <br>");
	        		invalid = true;
	        	}
	        }
	        
	        info.setLMP(r.getLMP());
	        info.setEDD();
	        info.setInitDate(r.getInitDate());
	        
	        
	        info.setDeliveryType(request.getParameter("deliveryTypeStr"));
	        if(!invalid) {
		        obstetricInfoAction.updateRecord(info);
				r = obstetricInfoAction.getRecordById(recordId);
	        }
			
			
			
			

			//response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/obstetricCare.jsp");
		} catch(Exception  e) {
			e.printStackTrace();
		}
	}


%>


<form action="" method="post">
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
				<td class="subHeaderVertical">Initialization Date:</td>
				<td><input name="init" value="<%= StringEscapeUtils.escapeHtml("" + (r.getInitDate())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Last Menstrual Period:</td>
				<td><input name="lmp" value="<%= StringEscapeUtils.escapeHtml("" + (r.getLMP())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Estimated Delivery Date:</td>
				<td><input name="edd" value="<%= StringEscapeUtils.escapeHtml("" + (new java.sql.Date(r.getEDD().getTime()))) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Year of Conception:</td>
				<td><input name="years" value="<%= StringEscapeUtils.escapeHtml("" + (r.getYearsOfConception())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Number of Weeks, Days Pregnant:</td>
				<td><input name="weeks" value="<%= StringEscapeUtils.escapeHtml("" + (r.getTimePregnant())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Number of Hours in Labor:</td>
				<td><input name="hours" value="<%= StringEscapeUtils.escapeHtml("" + (r.getNumberOfHoursInLabor())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Weight Gained During Pregnancy:</td>
				<td><input name="weight" value="<%= StringEscapeUtils.escapeHtml("" + (r.getWeightGainDuringPregnancy())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Delivery Type:</td>
				<td><select name="deliveryTypeStr">
					<%
						String selected = "";
						for (DeliveryType dt : DeliveryType.values()) {
							selected = (dt.equals(r.getDeliveryType())) ? "selected=selected"
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
				<td><input name="number" value="<%= StringEscapeUtils.escapeHtml("" + (r.getNumBirths())) %>" type="text"></td>
			</tr>
		</table>
		<br />
		</td>
		<td width="15px">&nbsp;</td>
		
	</tr>
</table>
<br />

<div align=center>
	<%
		if(isOBGYN){
	%>
		<input type="submit" name="editRecordAction" style="font-size: 16pt; font-weight: bold;" value="Edit Obstetric Record">
	<%
	}
	%>
	<br /><br />
	
</div>
</form>
<br />
<br />
<itrust:patientNav thisTitle="Demographics" />


<% }  %>


<%@include file="/footer.jsp"%>