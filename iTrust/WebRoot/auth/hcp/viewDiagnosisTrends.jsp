<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewDiagnosisStatisticsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>\
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewDiagnosisStatisticsAction"%>
<%@page import="edu.ncsu.csc.itrust.charts.DiagnosisTrendData" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.ParseException" %>

<% 
	//log the page view
	loggingAction.logEvent(TransactionType.DIAGNOSIS_TRENDS_VIEW, loggedInMID.longValue(), 0, "");
	//ViewDiagnosisStatisticsAction diagnoses = new ViewDiagnosisStatisticsAction(prodDAO);
	DiagnosisStatisticsBean dsBean = null;
	//get form data
	String startDate = request.getParameter("startDate");
	String zipCode = request.getParameter("zipCode");
	if (zipCode == null)
		zipCode = "     ";
	
	String icdCode = request.getParameter("icdCode");
	ArrayList<DiagnosisStatisticsBean> weeks = new ArrayList<>();
	int weekOfYr = 0;
	if(startDate!=null) {
		Date curDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/04/1998");
	    try {
			curDate = new SimpleDateFormat("MM/dd/yyyy").parse(startDate);
		}catch(ParseException e){
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(curDate);
		int year = curDate.getYear()+1900;
		weekOfYr = cal.get(Calendar.WEEK_OF_YEAR);
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.WEEK_OF_YEAR, weekOfYr);
		calendar.set(Calendar.YEAR, year);
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		//try to get the statistics. If there's an error, print it. If null is returned, it's the first page load
		//try {
			//weeks.add(diagnoses.getDiagnosisStatistics(df.format(date), startDate, icdCode, zipCode));
			for (int g = 0; g < 7; g++) {
				calendar.add(Calendar.DATE, -1);
			    String end = df.format(calendar.getTime());
				calendar.add(Calendar.DATE, -6);
				date = calendar.getTime();
				//weeks.add(diagnoses.getDiagnosisStatistics( df.format(date), end, icdCode, zipCode));
			}
// 		} catch (FormValidationException e) {
// 			e.printHTML(pageContext.getOut());
// 		}
	}
	if (startDate == null)
		startDate = "";
	if (icdCode == null)
		icdCode = "";
	long[] region = new long[8];
	long[] state = new long[8];
	long[] all = new long[8];
	if(!weeks.isEmpty()){
		for(int i=0;i<8;i++){
			state[i]=weeks.get(7-i).getStateStats();
			region[i] = weeks.get(7-i).getRegionStats();
 			all[i] = weeks.get(7-i).getAllStats();
		}
	}
	
%>
<br />
<form action="viewDiagnosisStatistics.jsp" method="post" id="formMain">
<input type="hidden" name="viewSelect" value="trends" />
<table class="fTable" align="center" id="diagnosisStatisticsSelectionTable">
	<tr>
		<th colspan="4">Diagnosis Statistics</th>
	</tr>
	<tr class="subHeader">
		<td>Diagnosis:</td>
		<td>
			<select name="icdCode" style="font-size:10" >
			<option value="">-- None Selected --</option>
<%-- 			<%for(DiagnosisBean diag : diagnoses.getDiagnosisCodes()) { %> --%>
<%-- 				<%if (diag.getICDCode().equals(icdCode)) { %> --%>
<%-- 					<option selected="selected" value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %> --%>
<%-- 					- <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option> --%>
<%-- 				<% } else { %> --%>
<%-- 					<option value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %> --%>
<%-- 					- <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option> --%>
<%-- 				<% } %> --%>
<%-- 			<%}%> --%>
			</select>
		</td>
		<td>Zip Code:</td>
		<td ><input name="zipCode" value="<%= StringEscapeUtils.escapeHtml(zipCode) %>" /></td>
	</tr>
	<tr class="subHeader">
		<td>Start Date:</td>
		<td>
			<input name="startDate" value="<%= StringEscapeUtils.escapeHtml("" + (startDate)) %>" size="10">
			<input type=button value="Select Date" onclick="displayDatePicker('startDate');">
		</td>
		<td> </td>
		<td> </td>
	</tr>
	<tr>
		<td colspan="4" style="text-align: center;"><input type="submit" id="select_diagnosis" value="View Statistics"></td>
	</tr>
</table>	

</form>

<br />

<% if (! weeks.isEmpty()) { %>

<br />

<head>
	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
	<script type="text/javascript">
        google.charts.load('current', {'packages':['bar']});
        google.charts.setOnLoadCallback(drawChart);
        function drawChart() {
            var tempArray = [['Week Number', 'Diagnoses in '+"<%=zipCode.substring(0,3)%>"+"XX", 'Diagnoses in '+"<%=zipCode.substring(0,2)%>"+"XXX", 'All diagnoses']];
            var WeekCode = <%=weekOfYr%>;
            var currentWeek = WeekCode-7;
            var state = <%=state[0]%>;
            var region = <%= region[0]%>;
            var all = <%= all[0]%>;
            tempArray.push(['week-'+currentWeek, region,state, all ]);
            currentWeek = WeekCode-6;
            state = <%=state[1]%>;
            region = <%= region[1]%>;
            all = <%= all[1]%>;
            tempArray.push(['week-'+currentWeek, region,state, all ]);
            currentWeek = WeekCode-5;
            state = <%=state[2]%>;
            region = <%= region[2]%>;
            all = <%= all[2]%>;
            tempArray.push(['week-'+currentWeek, region,state, all ]);
            currentWeek = WeekCode-4;
            state = <%=state[3]%>;
            region = <%= region[3]%>;
            all = <%= all[3]%>;
            tempArray.push(['week-'+currentWeek, region,state, all ]);
            currentWeek = WeekCode-3;
            state = <%=state[4]%>;
            region = <%= region[4]%>;
            all = <%= all[4]%>;
            tempArray.push(['week-'+currentWeek, region,state, all ]);
            currentWeek = WeekCode-2;
            state = <%=state[5]%>;
            region = <%= region[5]%>;
            all = <%= all[5]%>;
            tempArray.push(['week-'+currentWeek, region,state, all ]);
            currentWeek = WeekCode-1;
            state = <%=state[6]%>;
            region = <%= region[6]%>;
            all = <%= all[6]%>;
            tempArray.push(['week-'+currentWeek, region,state, all ]);
            currentWeek = WeekCode;
            state = <%=state[7]%>;
            region = <%= region[7]%>;
            all = <%= all[7]%>;
            tempArray.push(['week-'+currentWeek, region,state, all ]);
            var data = google.visualization.arrayToDataTable(tempArray);
            var options = {
                width: 900,
                chart: {
                    title: 'Trend for ' + "<%=zipCode%>"
                }
            };
            var chart = new google.charts.Bar(document.getElementById('columnchart_material'));
            chart.draw(data, google.charts.Bar.convertOptions(options));
        }
	</script>
</head>


<p style="display:block; margin-left:auto; margin-right:auto; width:600px;">

<div id="columnchart_material" style="width: 800px; height: 500px;"></div>

</p>

<% } %>
<br />
<br />