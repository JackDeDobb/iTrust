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
<%@page import="edu.ncsu.csc.itrust.action.ChildBirthVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Ethnicity"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.BooleanType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.DeliveryType"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricInfoBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricInfoDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ChildBirthVisitDAO"%>
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
	
	ChildBirthVisitAction childBirthVisitAction = new ChildBirthVisitAction(prodDAO, loggedInMID.longValue(), pidString);
	//ObstetricInfoBean r = obstetricInfoAction.getRecordById(recordId);
	
	if(request.getParameter("editRecordAction") != null) {
		try {
			ChildBirthVisitBean info = new ChildBirthVisitBean();
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
	        	//setPitocinDosage(float pitocinDosage)
	        	info.setPitocinDosage(Float.valueOf(request.getParameter("Pitocin")));
	        } catch(Exception e) {
	        	if (!request.getParameter("Pitocin").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid Pitocin <br>");
	        		invalid = true;
	        	}
	        }
	        
	        try {
	        	info.setNitrousOxideDosage(Float.valueOf(request.getParameter("NitrousOxide")));
	        } catch(Exception e) {
	        	if (!request.getParameter("NitrousOxide").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid Nitrous Oxide <br>");
	        		invalid = true;
	        	}
	        }
	        
	        try {
	        	info.setEpiduralAnaesthesiaDosage(Float.valueOf(request.getParameter("EpiduralAnaesthesia")));
	        } catch(Exception e) {
	        	if (!request.getParameter("EpiduralAnaesthesia").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid Epidural Anaesthesia <br>");
	        		invalid = true;
	        	}
	        }
	        
	        try {
	        	info.setMagnesiumSulfateDosage(Float.valueOf(request.getParameter("MagnesiumSulfate")));
	        } catch(Exception e) {
	        	if (!request.getParameter("MagnesiumSulfate").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid Magnesium Sulfate <br>");
	        		invalid = true;
	        	}
	        }
	        
	        try {
	        	info.setRhImmuneGlobulinDosage(Float.valueOf(request.getParameter("RhImmuneGlobulin")));
	        } catch(Exception e) {
	        	if (!request.getParameter("RhImmuneGlobulin").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid RhImmune Globulin <br>");
	        		invalid = true;
	        	}
	        }
	        
	   		
	        boolean delivered = request.getParameter("delivered").equals("Yes");
	        info.setDelivered(delivered);
	        info.setPreviouslyScheduled(request.getParameter("previouslyScheduled").equals("Yes"));
	        info.setPreferredDeliveryType(request.getParameter("deliveryTypeStr"));
	        if(!invalid) {
	        	childBirthVisitAction.addChildBirthVisitRecord(info);
	        	if (delivered) {
	        		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/addNewBabyDeliveryInfo.jsp");
	        	} else {
					response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/obstetricCare.jsp");
	        	}
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
				<td><select name="previouslyScheduled">
					<%
						String selected = "";
						for (BooleanType g : BooleanType.values()) {
							selected = (g.equals(BooleanType.No)) ? "selected=selected" : "";
					%>
					<option value="<%=g.getName()%>" <%= StringEscapeUtils.escapeHtml("" + (selected)) %>><%= StringEscapeUtils.escapeHtml("" + (g.getName())) %></option>
					<%
						}
					%>
				</select></td>
			</tr>
			
			
			<tr>
				<td class="subHeaderVertical">Delivered:</td>
				<td><select name="delivered">
					<%
						selected = "";
						for (BooleanType g : BooleanType.values()) {
							selected = (g.equals(BooleanType.No)) ? "selected=selected" : "";
					%>
					<option value="<%=g.getName()%>" <%= StringEscapeUtils.escapeHtml("" + (selected)) %>><%= StringEscapeUtils.escapeHtml("" + (g.getName())) %></option>
					<%
						}
					%>
				</select></td>
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
						selected = "";
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