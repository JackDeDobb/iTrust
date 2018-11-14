<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - View My Sent Messages";


%>

<%@include file="/header.jsp" %>

<div align=center>
    <h2>Sent Reminders</h2>
    <%
        //sorts messages by time
    	loggingAction.logEvent(TransactionType.OUTBOX_VIEW, loggedInMID.longValue(), loggedInMID.longValue(), "");
        ViewMyMessagesAction action = new ViewMyMessagesAction(prodDAO, 9000000009L);
        List<MessageBean> messages = null;
        if(request.getParameter("sortby") != null) {
            if(request.getParameter("sortby").equals("name")) {
                if(request.getParameter("sorthow").equals("asce")) {
                    messages = action.getAllMySentMessagesNameAscending();
                } else {
                    messages = action.getAllMySentMessagesNameDescending();
                }
            } else if(request.getParameter("sortby").equals("time")) {
                if(request.getParameter("sorthow").equals("asce")) {
                    messages = action.getAllMySentMessagesTimeAscending();
                } else {
                    messages = action.getAllMySentMessages();
                }
            }
        }
        else {
            messages = action.getAllMySentMessages();
        }
        //Organizes the messages(stored in the session attribute) into a table
        session.setAttribute("messages", messages);
        if (messages.size() > 0) { %>
    
    <br />
    <table class="fancyTable">
        <tr>
            <th>To</th>
            <th>Subject</th>
            <th>Received</th>
            <th></th>
        </tr>
        <%		int index = 0; %>
        <%		for(MessageBean message : messages) { %>
        <tr <%=(index%2 == 1)?"class=\"alt\"":"" %>>
            <td><%= StringEscapeUtils.escapeHtml("" + ( action.getName(message.getTo()) )) %></td>
            <td><%= StringEscapeUtils.escapeHtml("" + ( message.getSubject() )) %></td>
            <td><%= StringEscapeUtils.escapeHtml("" + ( message.getSentDate() )) %></td>
            <td><a href="viewMessageOutbox.jsp?msg=<%= StringEscapeUtils.escapeHtml("" + ( index )) %>">Read</a></td>
        </tr>
        <%			index ++; %>
        <%		} %>
    </table>
    <%	} else { %>
    <div>
        <i>You have no messages</i>
    </div>
    <%	} %>
    <br />
</div>

<%@include file="/footer.jsp" %>
