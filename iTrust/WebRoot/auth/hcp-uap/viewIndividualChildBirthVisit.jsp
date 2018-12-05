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
<%@page import="edu.ncsu.csc.itrust.action.ViewChildBirthVisit"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Ethnicity"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.DeliveryType"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ChildBirthVisitDAO"%>
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
	
	
	ViewChildBirthVisit viewChildBirthVisitAction = new ViewChildBirthVisit(prodDAO, loggedInMID.longValue(), pidString);
	ChildBirthVisitBean r = viewChildBirthVisitAction.getRecordById(recordId);
	
	if(request.getParameter("editRecordAction") != null) {
		try {
			ChildBirthVisitBean info = new ChildBirthVisitBean();
	        info.setMID(r.getMID());
	        info.setId(r.getId());
	        boolean invalid = false;
				
			
			try {
	        	info.setPitocinDosage(Float.valueOf(request.getParameter("Pitocin")));
	        } catch(Exception e) {
	        	if (!request.getParameter("Pitocin").equals("0.0")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid Pitocin <br>");
	        		invalid = true;
	        	}
	        }
	        
	        try {
	        	info.setNitrousOxideDosage(Float.valueOf(request.getParameter("NitrousOxide")));
	        } catch(Exception e) {
	        	if (!request.getParameter("NitrousOxide").equals("0.0")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid Nitrous Oxide <br>");
	        		invalid = true;
	        	}
	        }
	        
	        try {
	        	info.setEpiduralAnaesthesiaDosage(Long.valueOf(request.getParameter("EpiduralAnaesthesia")));
	        } catch(Exception e) {
	        	if (!request.getParameter("EpiduralAnaesthesia").equals("0.0")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid Epidural Anaesthesia <br>");
	        		invalid = true;
	        	}
	        }
	        
	        try {
	        	info.setMagnesiumSulfateDosage(Long.valueOf(request.getParameter("MagnesiumSulfate")));
	        } catch(Exception e) {
	        	if (!request.getParameter("MagnesiumSulfate").equals("0.0")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid Magnesium Sulfate <br>");
	        		invalid = true;
	        	}
	        }
	        
	        try {
	        	info.setRhImmuneGlobulinDosage(Long.valueOf(request.getParameter("RhImmuneGlobulin")));
	        } catch(Exception e) {
	        	if (!request.getParameter("RhImmuneGlobulin").equals("0.0")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid RHImmune Globulin <br>");
	        		invalid = true;
	        	}
	        }
	        
	        info.setPreferredDeliveryType(request.getParameter("deliveryTypeStr"));
	        if(!invalid) {
	        	viewChildBirthVisitAction.updateRecord(info);
				r = viewChildBirthVisitAction.getRecordById(recordId);
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
			
				<td class="subHeaderVertical">Previously Scheduled:</td>
				<td><input name="firstName" value="<%= StringEscapeUtils.escapeHtml("" + (r.isPreviouslyScheduled())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Delivered:</td>
				<td><input name="lastName" value="<%= StringEscapeUtils.escapeHtml("" + (r.isDelivered())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Pitocin Dosage:</td>
				<td><input name="Pitocin" value="<%= StringEscapeUtils.escapeHtml("" + (r.getPitocinDosage())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Nitrous Oxide Dosage:</td>
				<td><input name="NitrousOxide" value="<%= StringEscapeUtils.escapeHtml("" + (r.getNitrousOxideDosage())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Epidural Anaesthesia Dosage:</td>
				<td><input name="EpiduralAnaesthesia" value="<%= StringEscapeUtils.escapeHtml("" + (r.getEpiduralAnaesthesiaDosage())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Magnesium Sulfate Dosage:</td>
				<td><input name="MagnesiumSulfate" value="<%= StringEscapeUtils.escapeHtml("" + (r.getMagnesiumSulfateDosage())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">RhImmune Globulin Dosage:</td>
				<td><input name="RhImmuneGlobulin" value="<%= StringEscapeUtils.escapeHtml("" + (r.getRhImmuneGlobulinDosage())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Preferred Delivery Type:</td>
				<td><select name="deliveryTypeStr">
					<%
						String selected = "";
						for (DeliveryType dt : DeliveryType.values()) {
							selected = (dt.equals(r.getPreferredDeliveryType())) ? "selected=selected"
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
		<input type="submit" name="editRecordAction" style="font-size: 16pt; font-weight: bold;" value="Edit Child Birth Visit Record">
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