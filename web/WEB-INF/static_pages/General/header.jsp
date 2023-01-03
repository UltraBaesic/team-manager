<%-- 
    Document   : header
    Created on : 01-Oct-2017, 04:14:59
    Author     : Stephen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<input type="hidden" name="" value="<%= session.getAttribute("Id")%>" id="userid"/>
<input type="hidden" name="" value="<%= session.getAttribute("userType")%>" id="usertype"/>
<input type="hidden" name="" value="<%= session.getAttribute("userName")%>" id="username"/>
<div id="header">
    <div class="left">
        <div class="parts largetext">
            <div class="bigLogo normaltext mini-radius parts">TM</div>
            <div class="logotxt parts normaltext">
                <div class="smalltext">Team</div>
                <div class="neg-mini-margintop">Manager</div>
            </div>
        </div>
    </div>
    <div class="right hide mobile-show half-margintop mini-padding mobileHeaderLink">
        <i class="fa fa-fw fa-bars largetext"></i>
    </div>
    <div class="right mini-margintop hide-on-mobile mobileHeader">
        <a href="${pageContext.request.contextPath}/link?location=Home"><div class="header-link" id="header-dashboard">Dashboard</div></a>
        <a href="${pageContext.request.contextPath}/link?location=Team"><div class="header-link" id="header-team">Teams</div></a>
        <a href="${pageContext.request.contextPath}/link?location=Tasks"><div class="header-link" id="header-tasks">Tasks</div></a>
        <a href="${pageContext.request.contextPath}/link?location=Records"><div class="header-link" id="header-records">Records</div></a>
        <div class="parts mediumtext double-marginleft header-user">Hi <span class="redtext"><%= session.getAttribute("userName")%></span></div>
        <a href="${pageContext.request.contextPath}/link?location=Logout"><div class="header-link orangebtn radius double-marginleft bold header-btn">Sign Out</div></a>
    </div>
    <div style="clear: both"></div>
</div>
<div class="loader hide">
    <div class="lds-ellipsis"><div></div><div></div><div></div><div></div></div>
</div>
<%@include file="../../jspf/General/search.jspf" %>
<%@include file="../../jspf/Tasks/task_fullView.jspf" %>