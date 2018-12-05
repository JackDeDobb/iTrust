<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.MessageBean"%>
<%@page import="java.util.List"%>
<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.List"%>


<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.AddPatientAction"%>
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
<%@page import="edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricInfoDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.BabyDeliveryInfoDAO"%>
<%@page import="edu.ncsu.csc.itrust.action.AddBabyDeliveryInfoAction"%>
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
	
	AddBabyDeliveryInfoAction babyDeliveryInfoAction = new AddBabyDeliveryInfoAction(prodDAO, loggedInMID.longValue(), pidString);
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
	        	
	        	String deliveryDate= request.getParameter("birthTime");
		        /*
	        	DateFormat formatter;
		        Timestamp birthTime;
		        formatter = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.").format(Date())));
		        birthTime = formatter.parse(deliveryDate);
		        info.setBirthTime(birthTime);
		        */
	        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
	            Date parsedDate = dateFormat.parse(deliveryDate);
	            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
	            info.setBirthTime(timestamp);
	        } catch(Exception e) {
	        	
	        	out.println("<span class=\"font_failure\">" + "Invalid Birth Time <br>");
	        	invalid = true;
	        }
	        
	        
	        try {
	        	info.setGender(request.getParameter("g1"));
	        } catch(Exception e) {
	        	if (!request.getParameter("g1").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid gender <br>");
	        		invalid = true;
	        	}
	        }
	        try {
	        	info.setGender(request.getParameter("g2"));
	        } catch(Exception e) {
	        	if (!request.getParameter("g2").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid gender <br>");
	        		invalid = true;
	        	}
	        }
	        try {
	        	info.setGender(request.getParameter("g3"));
	        } catch(Exception e) {
	        	if (!request.getParameter("g3").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid gender <br>");
	        		invalid = true;
	        	}
	        }
	        try {
	        	info.setGender(request.getParameter("g3"));
	        } catch(Exception e) {
	        	if (!request.getParameter("g3").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid gender <br>");
	        		invalid = true;
	        	}
	        }
	        try {
	        	info.setGender(request.getParameter("g4"));
	        } catch(Exception e) {
	        	if (!request.getParameter("g4").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid gender <br>");
	        		invalid = true;
	        	}
	        }
	        try {
	        	info.setGender(request.getParameter("g5"));
	        } catch(Exception e) {
	        	if (!request.getParameter("g5").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid gender <br>");
	        		invalid = true;
	        	}
	        }
	        try {
	        	info.setGender(request.getParameter("g6"));
	        } catch(Exception e) {
	        	if (!request.getParameter("g6").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid gender <br>");
	        		invalid = true;
	        	}
	        }
	        try {
	        	info.setGender(request.getParameter("g7"));
	        } catch(Exception e) {
	        	if (!request.getParameter("g7").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid gender <br>");
	        		invalid = true;
	        	}
	        }
	        try {
	        	info.setGender(request.getParameter("g8"));
	        } catch(Exception e) {
	        	if (!request.getParameter("g8").equals("")) {
	        		out.println("<span class=\"font_failure\">" + "Invalid gender <br>");
	        		invalid = true;
	        	}
	        }
	        
	        info.setDeliveryType(request.getParameter("deliveryTypeStr"));
	        if(!invalid) {
	        	babyDeliveryInfoAction.addBabyDeliveryInfo(info);
	        	AddPatientAction addPatientAction = new AddPatientAction(prodDAO, loggedInMID.longValue());
	        	
	        	if (request.getParameter("g1") != null) {
	        		PatientBean baby1 = new PatientBean();
	        		long newMID = addPatientAction.addPatient(baby1, loggedInMID.longValue());
	        		EditPatientAction editPatientAction = new EditPatientAction(prodDAO, loggedInMID.longValue(), Long.toString(newMID));
	        		baby1.setMotherMID(pidString);
	        		baby1.setGenderStr(request.getParameter("g1"));
	        		editPatientAction.updateInformation(baby1);	
	        	}
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
				<td class="subHeaderVertical">Birth Time:</td>
				<td><input name="birthTime" type="text"></td>
			</tr>	
			<tr>		
				<td class="subHeaderVertical">Estimated?</td>
				<td><input name="estimated" type="text"></td>
			</tr>
			
			<tr>
				<td class="subHeaderVertical">Gender Baby 1:</td>
				<td><input name="g1" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Gender Baby 2:</td>
				<td><input name="g2"  type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Gender Baby 3:</td>
				<td><input name="g3"  type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Gender Baby 4:</td>
				<td><input name="g4"  type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Gender Baby 5:</td>
				<td><input name="g5"  type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Gender Baby 6:</td>
				<td><input name="g6"  type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Gender Baby 7:</td>
				<td><input name="g7"  type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Gender Baby 8:</td>
				<td><input name="g8"  type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Delivery Type:</td>
				<td><select name="deliveryTypeStr">
					<%
						String selected = "";
						for (DeliveryType dt : DeliveryType.values()) {
							selected = (dt.equals(DeliveryType.Vaginal)) ? "selected=selected"
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