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
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricInfoAction"%>
<%@page import="edu.ncsu.csc.itrust.action.BabyDeliveryInfoAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Ethnicity"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.DeliveryType"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricInfoBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricInfoDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.BabyDeliveryInfoDAO"%>
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
	
	BabyDeliveryInfoAction babyDeliveryInfoAction = new BabyDeliveryInfoAction(prodDAO, loggedInMID.longValue(), pidString);
	//ObstetricInfoBean r = obstetricInfoAction.getRecordById(recordId);
	
	if(request.getParameter("editRecordAction") != null) {
		try {
			BabyDeliveryInfoBean info = new BabyDeliveryInfoBean();
	        info.setMID(p.getMID());
	        
	        
	        
	        //Add visitId, obstetricInitId, delivered, previouslyScheduled to info
	        
	        boolean invalid = false;
			/*
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
			*/
			
	        try {
	        	String deliveryDate= request.getParameter("deliveryDate");
		        DateFormat formatter;
		        java.util.Timestamp birthTime;
		        formatter = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.");
		        birthTime = formatter.parse(deliveryDate);
		        info.setBirthTime(birthTime);
	        } catch(Exception e) {
	        	
	        	out.println("<span class=\"font_failure\">" + "Invalid Birth Time <br>");
	        	invalid = true;
	        }
	        
	        
	        try {
	        	info.setGender(request.getParameter("gender"));
	        } catch(Exception e) {
	        	if (!request.getParameter("gender").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid gender <br>");
	        		invalid = true;
	        	}
	        }
	        
	   		
	        
	        info.setDeliveryType(request.getParameter("deliveryTypeStr"));
	        if(!invalid) {
	        	babyDeliveryInfoAction.addBabyDeliveryInfo(info);
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
				<td class="subHeaderVertical">Previously Scheduled:</td>
				<td><input name="firstName" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Delivered:</td>
				<td><input name="lastName" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Pitocin Dosage:</td>
				<td><input name="Pitocin" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Nitrous Oxide Dosage:</td>
				<td><input name="NitrousOxide"  type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Epidural Anesthesia Dosage:</td>
				<td><input name="EpiduralAnaesthesia"  type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Magnesium Sulfate Dosage:</td>
				<td><input name="MagnesiumSulfate"  type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">RHImmune Globulin Dosage:</td>
				<td><input name="RhImmuneGlobulin"  type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Preferred Delivery Type:</td>
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
		<input type="submit" name="editRecordAction" style="font-size: 16pt; font-weight: bold;" value="Add Child Visit Record">
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