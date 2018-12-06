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
<%@page import="edu.ncsu.csc.itrust.model.old.enums.BooleanType"%>
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
	    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
	    		Date date = (Date) format.parse(request.getParameter("birthTime"));
	    		Timestamp visitTimestamp = new Timestamp(date.getTime());
	    		info.setBirthTime(visitTimestamp);
	        } catch(Exception e) {
	        	
	        	out.println("<span class=\"font_failure\">" + "Invalid Birth Time <br>");
	        	invalid = true;
	        }
	        
			
			info.setEstimated(request.getParameter("estimated").equals("Yes"));
	        
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
	        	
	        	try{
	        		babyDeliveryInfoAction.addBabyDeliveryInfo(info);
        			%>
    				<div align="center">
    					<span class="iTrustMessage">
    						Baby delivery info added!
    					</span>
    				</div>
    			<% 
	    	
	        	}
	        	catch(ITrustException e){
	        		%>
	        		<div align="center">
	        			<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
	        		</div>
	        		<% 
	        	}
	        	
	        	AddPatientAction addPatientAction = new AddPatientAction(prodDAO, loggedInMID.longValue());
	        	
	        	Boolean miscarriage = request.getParameter("deliveryTypeStr").equals("Miscarriage");
	        	
	        	if (request.getParameter("gender") != null && !miscarriage) {
	        		PatientBean baby = new PatientBean();
	        		baby.setMotherMID(pidString);
	        		baby.setGenderStr(request.getParameter("gender"));
	        		try{
	        			long newMID = addPatientAction.addBaby(baby, loggedInMID.longValue());
	        			%>
	        				<div align="center">
	        					<span class="iTrustMessage">
	        						Baby successfully added with MID: <%=newMID %>
	        					</span>
	        				</div>
	        			<% 
	        		}
	        		catch(FormValidationException | ITrustException e){
		        		%>
		        		<div align="center">
		        			<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
		        		</div>
		        		<% 
	        		}
	        		
	        	}
	        }
		} catch(Exception e) {
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
				<td><input name="birthTime" type="datetime-local"></td>
			</tr>	
			
			
			<tr>
				<td class="subHeaderVertical">Estimated?:</td>
				<td><select name="estimated">
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
				<td class="subHeaderVertical">Gender:</td>
				<td><select name="gender">
					<%
						selected = "";
						for (Gender g : Gender.values()) {
							selected = (g.equals(Gender.NotSpecified)) ? "selected=selected" : "";
					%>
					<option value="<%=g.getName()%>" <%= StringEscapeUtils.escapeHtml("" + (selected)) %>><%= StringEscapeUtils.escapeHtml("" + (g.getName())) %></option>
					<%
						}
					%>
				</select></td>

			<tr>
				<td class="subHeaderVertical">Delivery Type:</td>
				<td><select name="deliveryTypeStr">
					<%
						selected = "";
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
		<input type="submit" name="editRecordAction" style="font-size: 16pt; font-weight: bold;" value="Add New Baby Record">
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