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