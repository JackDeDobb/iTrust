<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.MessageBean"%>
<%@page import="java.util.List"%>
<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
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
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
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
	


	
	
	ViewObstetricInfoAction obstetricInfoAction = new ViewObstetricInfoAction(prodDAO, loggedInMID.longValue(), pidString);
	//ObstetricInfoBean r = obstetricInfoAction.getRecordById(recordId);
	
	if(request.getParameter("editRecordAction") != null) {
		try {
			ObstetricInfoBean info = new ObstetricInfoBean();
	        info.setMID(p.getMID());
	        
	        boolean invalid = false;

	        try {
	        	String lmpdate = request.getParameter("lmp");
		        DateFormat formatter;
		        java.util.Date date;
		        formatter = new SimpleDateFormat("YYYY-MM-DD");
		        date = formatter.parse(lmpdate);
		        info.setLMP(date);
		        info.setEDD();
	        } catch(Exception e) {
	        	out.println("<span class=\"font_failure\">" + "Invalid LMP Date <br>");
	        	invalid = true;
	        }
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
	        
	   		
	        
	        info.setDeliveryType(request.getParameter("deliveryTypeStr"));
	        if(!invalid) {
	        	obstetricInfoAction.addNewRecord(info);
				response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/obstetricCare.jsp");
	        }
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
				<td class="subHeaderVertical">Last Menstrual Period:</td>
				<td><input name="lmp" type="date"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Year of Conception:</td>
				<td><input name="years"  type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Number of Hours in Labor:</td>
				<td><input name="hours"  type="text"></td>
			</tr>
			
			
			<tr>
				<td class="subHeaderVertical">Delivery Type:</td>
				<td><select name="deliveryTypeStr">
					<%
						String selected = "";
						for (DeliveryType dt : DeliveryType.values()) {
							selected = (dt.equals(DeliveryType.NS)) ? "selected=selected"
									: "";
					%>
					<option value="<%=dt.getName()%>" <%= StringEscapeUtils.escapeHtml("" + (selected)) %>><%= StringEscapeUtils.escapeHtml("" + (dt.getName())) %></option>
					<%
						}
					%>
				</select>
			</tr>
			
			
			
			<tr>
				<td class="subHeaderVertical">Weight Gained During Pregnancy:</td>
				<td><input name="weight" type="text"></td>
			</tr>

			<tr>
				<td class="subHeaderVertical">Number Infants Born:</td>
				<td><input name="number"  type="text"></td>
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
		<input type="submit" name="editRecordAction" style="font-size: 16pt; font-weight: bold;" value="Add Obstetrics Record">
	<%
	}
	%>
	<br /><br />
	
</div>
</form>
<br />
<br />
<itrust:patientNav thisTitle="Demographics" />

<%
	
	%>

<%@include file="/footer.jsp"%>