<%@page import="com.mysql.jdbc.StringUtils"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="java.util.*"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.TransactionBean"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Role"%>
<%@page import="java.util.Optional"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.TransactionDAO.DateRange"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.google.gson.JsonObject"%>

<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - View Transactions";
%>

<%@include file="/header.jsp" %>

<%
// Load data for graph 1
Gson gsonObj = new Gson();
Map<Object,Object> map = null;
List<Map<Object,Object>> points = new ArrayList<Map<Object,Object>>();

for(Role role: Role.values()){
	map = new HashMap<Object,Object>();
	map.put("label", role.getUserRolesString());
	Optional<Role> optRole = Optional.ofNullable(role);
	List<TransactionBean> transPerRole = DAOFactory.getProductionInstance().getTransactionDAO().getTransactionsWithFilter(
			optRole, Optional.empty(), Optional.empty(), Optional.empty()
    );
	System.out.println(transPerRole.size());
	map.put("y", transPerRole.size());
	points.add(map);
}
String dataPoints1 = gsonObj.toJson(points);

// Load data for graph 2
gsonObj = new Gson();
map = null;
points = new ArrayList<Map<Object,Object>>();

for(Role role: Role.values()){
	map = new HashMap<Object,Object>();
	map.put("label", role.getUserRolesString());
	Optional<Role> optRole = Optional.ofNullable(role);
	List<TransactionBean> transPerRole = DAOFactory.getProductionInstance().getTransactionDAO().getTransactionsWithFilter(
			Optional.empty(), optRole, Optional.empty(), Optional.empty()
    );
	System.out.println(transPerRole.size());
	map.put("y", transPerRole.size());
	points.add(map);
}
String dataPoints2 = gsonObj.toJson(points);

//Load data for graph 3
gsonObj = new Gson();
map = null;
points = new ArrayList<Map<Object,Object>>();

for(TransactionType type: TransactionType.values()){
	map = new HashMap<Object,Object>();
	map.put("label", type.getCode());
	Optional<TransactionType> optType = Optional.ofNullable(type);
	List<TransactionBean> transPerType = DAOFactory.getProductionInstance().getTransactionDAO().getTransactionsWithFilter(
			Optional.empty(), Optional.empty(), optType, Optional.empty()
 );
	System.out.println(transPerType.size());
	map.put("y", transPerType.size());
	points.add(map);
}
String dataPoints3 = gsonObj.toJson(points);

// Load date for graph 4.
gsonObj = new Gson();
map = null;
points = new ArrayList<Map<Object,Object>>();
Map<String, Integer> aggData =DAOFactory.getProductionInstance().getTransactionDAO().aggregateTransactionsByMonth();
for (Map.Entry<String, Integer> entry: aggData.entrySet()) {
    map = new HashMap<Object, Object>();
    map.put("label", entry.getKey());
    map.put("y", entry.getValue());
    points.add(map);
}
String dataPoints4 = gsonObj.toJson(points);

%>

<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
<script type="text/javascript">
	var renderGraph1 = function() { 
		var chart = new CanvasJS.Chart("chartContainer1", {
			animationEnabled: true,
			exportEnabled: true,
			title: {
				text: "# Transactions per Role (for logged-in user)"
			},
			data: [{
				type: "column",
				//indexLabel: "{y}", //Shows y value on all Data Points
				indexLabelFontColor: "#5A5757",
				indexLabelPlacement: "outside",
				dataPoints: <%out.print(dataPoints1);%>
			}]
		});
		chart.render();
	}
	
	var renderGraph2 = function() { 
		var chart = new CanvasJS.Chart("chartContainer2", {
			animationEnabled: true,
			exportEnabled: true,
			title: {
				text: "# Transactions per Role (for secondary user)"
			},
			data: [{
				type: "column",
				//indexLabel: "{y}", //Shows y value on all Data Points
				indexLabelFontColor: "#5A5757",
				indexLabelPlacement: "outside",
				dataPoints: <%out.print(dataPoints2);%>
			}]
		});
		chart.render();
	}
	
	var renderGraph3 = function() {
        var chart = new CanvasJS.Chart("chartContainer3", {
            animationEnabled: true,
            exportEnabled: true,
            title: {
                text: "Transactions per Transaction Type"
            },
            data: [{
                type: "column",
                //indexLabel: "{y}", //Shows y value on all Data Points
                indexLabelFontColor: "#5A5757",
                indexLabelPlacement: "outside",
                dataPoints: <%out.print(dataPoints3);%>
            }]
        });
        chart.render();
    }

    var renderGraph4 = function() {
        var chart = new CanvasJS.Chart("chartContainer4", {
            animationEnabled: true,
            exportEnabled: true,
            title: {
                text: "Transactions per Month and Year"
            },
            data: [{
                type: "column",
                //indexLabel: "{y}", //Shows y value on all Data Points
                indexLabelFontColor: "#5A5757",
                indexLabelPlacement: "outside",
                dataPoints: <%out.print(dataPoints4);%>
            }]
        });
        chart.render();
    }
</script>
<form method="post" action="transactionLogs.jsp">
	<label for="select-role">Role: </label>
	<select name="select-role" id="select-role">
		<option value="all">*</option>
		<option value="patient">Patient</option>
	  	<option value="er">ER</option>
	  	<option value="hcp">HCP</option>
	  	<option value="uap">UAP</option>
	  	<option value="lt">LT</option>
	  	<option value="admin">Admin</option>
	  	<option value="pha">PHA</option>
	  	<option value="tester">Tester</option>
	</select>
	&nbsp;&nbsp;
	<label for="select-second-role">Secondary Role: </label>
	<select name="select-second-role" id="select-second-role">
		<option value="all">*</option>
		<option value="patient">Patient</option>
	  	<option value="er">ER</option>
	  	<option value="hcp">HCP</option>
	  	<option value="uap">UAP</option>
	  	<option value="lt">LT</option>
	  	<option value="admin">Admin</option>
	  	<option value="pha">PHA</option>
	  	<option value="tester">Tester</option>
	</select>
	&nbsp;&nbsp;
	<label for="select-tx-type">Transaction Type:</label> 
	<select name="select-tx-type" id="select-tx-type">
		<option value="all">*</option>
		<%
			for (TransactionType type : TransactionType.values()) {
				int code = type.getCode();
		%>
			<option value="<%= code %>"><%= code %></option>
		<%
			}
		%>
	</select>
	&nbsp;&nbsp;
	<label for="start-date">Start Date: </label>
	<input id="start-date" name="start-date" type="date" value="*">
	&nbsp;&nbsp;
	<label for="end-date">End Date: </label>
	<input id="end-date" name="end-date" type="date" value="*">
	&nbsp;&nbsp;
	<input type="submit" value="Filter" />
</form>
<br/>
<input type="submit" id="summarize" value="Summarize" />
<br/><br/>

<%
    // Parse role input string.
    String roleString = request.getParameter("select-role");
    Optional<String> roleStringOpt = Optional.ofNullable(roleString);
    Optional<Role> role;
    if ((roleStringOpt.isPresent() && roleStringOpt.get().equals("all")) || !roleStringOpt.isPresent())
        role = Optional.empty();
    else
        role = Optional.ofNullable(Role.parse(roleString));

    // Parse second role input string.
    String secondRoleString = request.getParameter("select-second-role");
    Optional<String> secondRoleStringOpt = Optional.ofNullable(secondRoleString);
    Optional<Role> secondRole;
    if ((secondRoleStringOpt.isPresent() && secondRoleStringOpt.get().equals("all")) ||
            !secondRoleStringOpt.isPresent())
        secondRole = Optional.empty();
    else
        secondRole = Optional.ofNullable(Role.parse(secondRoleString));

    // Parse transaction type input string.
    String transactionTypeString = request.getParameter("select-tx-type");
    Optional<TransactionType> transactionType;
    try {
        transactionType = Optional.ofNullable(TransactionType.parse(Integer.parseInt(transactionTypeString)));
    } catch (NumberFormatException e) {
        transactionType = Optional.empty();
    }

    // Parse date range strings.
    String startDate = request.getParameter("start-date");
    String endDate = request.getParameter("end-date");
    Optional<DateRange> dateRange = new DateRange(startDate, endDate).getOptional();

    List<TransactionBean> list = DAOFactory.getProductionInstance().getTransactionDAO().getTransactionsWithFilter(
            role, secondRole, transactionType, dateRange
    );

    if(list.size() == 0){
%>
<b>No transactions to display.</b>
<%
} else {
%>
<table class="fTable" align="center">
	<tr>
        <th>ID</th>
        <th>Time Logged</th>
        <th>Type</th>
        <th>Code</th>
        <th>Description</th>
        <th>Logged in User MID</th>
        <th>Secondary MID</th>
        <th>Extra Info</th>
    </tr>
    <%
        for (TransactionBean t : list) {
    %>
    <tr>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionID())) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getTimeLogged())) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionType().name())) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionType().getCode())) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionType().getDescription())) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getLoggedInMID())) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getSecondaryMID())) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (t.getAddedInfo())) %></td>
    </tr>
    <%
        }
    %>
</table>
<%
    }
%>
<div id="graphModal" class="modal">

  <!-- Modal content -->
  <div class="modal-content">
    <span id="closeModal" class="close">&times;</span>
    <br/><br/>
    <div id="chartContainer1" style="height:370px; width:100%;"></div>
    <br/><br/>
    <div id="chartContainer2" style="height:370px; width:100%"></div>
    <br/><br/>
    <div id="chartContainer3" style="height:370px; width:100%"></div>
    <br/><br/>
    <div id="chartContainer4" style="height:370px; width:100%"></div>
  </div>

</div>
<script type="text/javascript">
var summarize = document.getElementById('summarize');
var modal = document.getElementById('graphModal');
var close = document.getElementById('closeModal');

summarize.onclick = function() {
	renderGraph1();
	renderGraph2();
	renderGraph3();
	renderGraph4();
	modal.style.display = "block";
}

close.onclick = function() {
	modal.style.display = "none";
}
</script>
<%@include file="/footer.jsp" %>