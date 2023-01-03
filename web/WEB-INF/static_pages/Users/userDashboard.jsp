<%-- 
    Document   : userDashboard
    Created on : 01-Oct-2017, 04:41:50
    Author     : Stephen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="pageContent table">
    <div class="table-cell wide height-400 center textcenter">
        <div class="tile">
            <div class="tile-header">Welcome <%=session.getAttribute("userName")%></div>
            <div class="tile-body">Team manager helps you organise your work and put 
                your output in perspective.</div>
        </div>
        <div class="tile">
            <div class="tile-header"><i class="fa fa-fw fa-phone orangetext half-marginright"></i>Contact Team Members</div>
            <div class="tile-body">
                Stay in touch with team members, share and collaborate with Team Manager's Work Chat feature
            </div>
        </div>
        <div class="tile">
            <div class="tile-header"><i class="fa fa-fw fa-clock-o orangetext half-marginright"></i>Timesheet</div>
            <div class="tile-body">
                Team Manager generates  a timesheet for you on a daily basis. 
                So you can now keep up with your progress over a period of time. 
                Get statistics on work progress, time spent on a subject and many
                other delightful features.
            </div>
        </div>
        <div class="tile">
            <div class="tile-header"><i class="fa fa-fw fa-list-ul orangetext half-marginright"></i>Manage Deliverables</div>
            <div class="tile-body">
                Get your tasks for each day from th team leader, Add tasks personally.
            </div>
        </div>
        <div class="tile">
            <div class="tile-header">Take a tour | <span class="orangetext cursor"> Get Started </span></div>
        </div>
    </div>    
</div>
