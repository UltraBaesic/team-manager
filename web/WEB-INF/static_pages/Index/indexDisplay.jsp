<%-- 
    Document   : indexDisplay
    Created on : 30-Sep-2017, 10:51:06
    Author     : Stephen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="wide">
    <div class="wide fullpage">
        <div class="loginPanel panBox parts">
            <div class="appName wide double-marginbottom">
                <div class="bigLogo parts">TM</div>
                <div class="logotxt parts">
                    <div class="smalltext">Team</div>
                    <div class="neg-mini-margintop">Manager</div>
                </div>
            </div>
            <div class="slightly-largetext marginbottom">
                <i class="fa fa-fw fa-user-circle largetext parts"></i>
                <div class="parts">Sign In to your Account</div>
            </div>
            <input type="text" name="email" value="" class="txt orangeborder" id="emailad" placeholder="Email"/>
            <input type="password" name="pass" value="" class="txt orangeborder" id="loginPass" placeholder="Password"/>
            <div class="big-btn orangebtn textcenter double-marginbottom" id="userLoginbutton">Sign In</div>
            <div class="padding border lightborder radius">
                <div class="que parts marginright">Don't yet have an Account?</div>
                <div class="parts btn blackbtn padding-horizontal radius btn-signup">Create Account</div>
            </div>
        </div>
        <div class="registerPanel panBox hide parts hide-on-load">
            <div class="appName wide double-marginbottom">
                <div class="bigLogo parts">TM</div>
                <div class="logotxt parts">
                    <div class="smalltext">Team</div>
                    <div class="neg-mini-margintop">Manager</div>
                </div>
            </div>
            <div class="slightly-largetext marginbottom">
                <i class="fa fa-fw fa-user-circle largetext parts"></i>
                <div class="parts">Create your Account</div>
            </div>
            <select name="" class="txt redborder wide drop-teams" id="team">
                <option>Select Team</option>
            </select>
            <input type="text" name="fnam" value="" class="txt half-txt redborder half-marginright" id="fnam" placeholder="First Name"/>
            <input type="text" name="lnam" value="" class="txt half-txt redborder" id="lnam" placeholder="Last Name"/>
            <input type="text" name="email" value="" class="txt redborder" id="email" placeholder="Email"/>
            <input type="text" name="phonenumber" value="" class="txt redborder" id="phonenumber" placeholder="Phone Number"/>
            <input type="password" name="pass" value="" class="txt half-txt redborder half-marginright" id="password" placeholder="Password"/>
            <input type="password" name="cpass" value="" class="txt half-txt redborder" id="confirmpassword" placeholder="Confirm Password"/>
            <div class="big-btn redbtn bold textcenter double-marginbottom" id="memberRegistrationbutton">Get Started</div>
            <div class="padding border lightborder radius">
                <div class="que parts marginright">Don't yet have an Account?</div>
                <div class="parts btn blackbtn padding-horizontal radius btn-signin">Sign In</div>
            </div>
        </div>
        <div class="parts msgBox">
            <div class="msg-mini">
                <b>"Team Manager"</b> is a sample project owned by <b>
                    UltraBaesic Technologies.</b> This project was developed using Java, MySql and 
                    web technologies (CSS, HTML, Ajax, JQuery, Javascript)
            </div>
            <div class="msg-mini">
                This Application is a sample team and task management project. As individuals and teams carry out
                daily tasks on a project, it is important for those tasks to be managed centrally. This improves
                efficiency, teamwork and creates an enabling environment for proper project management.
                <br /><br />
                Create your account or use the <b>admin login</b> to explore Team Manager
            </div>
            <div class="msg-mini">
                <b>Admin Login</b><br />
                This account supervises all teams on the application. Signing into this account gives you a bird's eye
                view on all the sample data collected from all teams and individuals on Team Manager
                <br /><br />
                Email: <b>chief@yahoo.com</b><br />
                Password: <b>inspector</b>
            </div>
        </div>        
    </div>
</div>