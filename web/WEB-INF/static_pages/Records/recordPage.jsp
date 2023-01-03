<%-- 
    Document   : recordPage
    Created on : 12-Mar-2018, 09:44:20
    Author     : Stephen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="rightContent" id="recordsroom">
    <div class="tabs mediumtext">
        <div class="parts padding-horizontal half-padding-vertical cursor pageoptionLink optionSelected pageoption1Link left nomargin btn-time-report">Time Reports</div>
        <div class="parts padding-horizontal half-padding-vertical cursor pageoptionLink pageoption2Link border-horizontal left lightborder nomargin hide">Timeline</div>
        <div class="parts padding-horizontal half-padding-vertical cursor pageoptionLink pageoption3Link borderleft lightborder left nomargin btn-task-report">Task Reports</div>
        <div style="clear: both"></div>
    </div>
    <%@include file="../../jspf/Records/reportsPage.jspf" %>
    <%@include file="../../jspf/Records/timesheetPage.jspf" %>
    <%@include file="../../jspf/Records/taskReportsPage.jspf" %>
</div>
