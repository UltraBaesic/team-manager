/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var userid, type, currId, currType, currSec = "timeReport", date = "today", date1 = "today", date2 = "today", loaded;
var extension = "", mason, sortvalue = "1";
var dates = [];
function performPageActions() {
    $(".loader").removeClass("hide");
    $(".loader").hide();
    var page = getCurrentPage();
    userid = $("#userid").val();
    type = $("#usertype").val();
    var name = $("#username").val();
    $(".memberName").text(name);
    $(".username").text(name);
    $(".usertype").text(capitaliseFirstLetter(type));
    if (page === "index.jsp") {
        getTeams();
    } else if (page === "team.jsp") {
        extension = "../";
        $("#header-team").addClass("header-link-selected");
        getMemberTeams(userid, "teamFunctions", "teamMemberFunctions");
    } else if (page === "tasks.jsp") {
        extension = "../";
        if (type === "member") {
            $("#leftPanel").hide();
            $(".mob-nm").addClass("mobile-hide");
            $("#taskroom").addClass("wide nomarginleft");
            getUserTasks(userid, "User", "todo");
            getUserCurrentTask(userid, "User");
            getUserTasks(userid, "User", "done");
            getTeamTasks("User", userid);
            checkAddTask("User", userid, name);
        } else {
            getMemberTeams(userid, "taskFunctions", "memberTaskFunctions");
        }
        $("#header-tasks").addClass("header-link-selected");
    } else if (page === "records.jsp") {
        $("#datepicker").datepicker({
            maxDate: "+1M +15D",
            changeMonth: true,
            changeYear: true,
            dateFormat: "yy-mm-dd",
            onSelect: function (dateText, inst) {
                date = $(this).val();
                date1 = $(this).val();
//                getUserTimesheet(currId, currType, "all", date);
                getUserReports(currId, currType, "all", date1, date2);
            }
        });
        $("#datepicker2").datepicker({
            maxDate: "+1M +15D",
            changeMonth: true,
            changeYear: true,
            dateFormat: "yy-mm-dd",
            onSelect: function (dateText, inst) {
                date2 = $(this).val();
//                getUserTimesheet(currId, currType, "all", date);
                getUserReports(currId, currType, "all", date1, date2);
            }
        });
        extension = "../";
        if (type === "member") {
            $("#leftPanel").hide();
            $("#recordsroom").addClass("wide nomarginleft");
            currId = userid;
            currType = "User";
//            getUserTimesheet(userid, "User", "all", date);
            getUserReports(userid, "User", "all", date1, date2);
        } else {
            getMemberTeams(userid, "reportsFunctions", "memberReportFunctions");
//            getMemberTeams(userid, "timesheetFunctions", "memberTimesheetFunctions");
        }
        $("#header-records").addClass("header-link-selected");
    } else if (page === "UserDashBoard.jsp") {
        extension = "../";
        $("#header-dashboard").addClass("header-link-selected");
    } else if (page === "AdminDashBoard.jsp") {
        extension = "../";
        $("#header-dashboard").addClass("header-link-selected");
    } else if (page === "ManagerDashBoard.jsp") {
        extension = "../";
        $("#header-dashboard").addClass("header-link-selected");
    }
    General();
    verifyUser();
    btnEvents();
}

function GetExtension() {
    return extension;
}

function btnEvents() {
    $(".btn-signin").click(function () {
        $(".loginPanel").removeClass("hide").show();
        $(".registerPanel").addClass("hide").hide();
    });
    $(".btn-signup").click(function () {
        $(".registerPanel").removeClass("hide").show();
        $(".loginPanel").addClass("hide").hide();
    });
    $("#cancelFileUpload").click(function () {
        $(".confirm").addClass("hide");
        $(".confirm").hide();
        $(".not-confirm").removeClass("hide");
        $(".not-confirm").show();
        $("#filename").text("");
    });
    $("#uploadFile").click(function () {
        $("#FileLoader").click();
        $(".not-confirm").addClass("hide");
        $(".not-confirm").hide();
        $(".confirm").removeClass("hide");
        $(".confirm").show();
    });
    $("#FileLoader").change(function () {
        previewCardFileName(this);
    });
    $(".dateTrigger").click(function () {
        $("#datepicker").datepicker("show");
    });
    $(".dateTrigger2").click(function () {
        $("#datepicker2").datepicker("show");
    });
    $(".searchBtn").click(function () {
        $(".searchBack").removeClass("hide").show();
        $(".searchTxt").focus();
    });
    $(".searchClose").click(function () {
        $(".searchBack").addClass("hide").hide();
//        $(".searchTxt").val("");
    });
    $(".searchTxt").keyup(function () {
        $(".task").hide();
        var searchtxt = $(this).val().trim();
        if (searchtxt === "") {
            $(".task").show();
        } else {
            $(".task:contains(" + searchtxt + ")").closest(".task").show();
        }
    });
    $(".t-v-close").click(function () {
        $(".taskBack").addClass("hide").hide();
    });
    $(".btn-task-report").click(function () {
        currSec = "taskReport";
        getTaskReports(currId, currType);
    });
    $(".btn-time-report").click(function () {
        currSec = "timeReport";
    });
    $(".c-t-hide").unbind("click").click(function (){
        $(".active-pane").addClass("hide").hide();
    });
}

function drag(draggable, droppable) {
    $(draggable).sortable({connectWith: droppable,
        handle: ".task-topic",
        cancel: ".tileclose"
    });
    $(".task")
            .addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
            .find(".task-topic").addClass("ui-widget-header ui-corner-all")
            .prepend("<span class='ui-icon ui-icon-minusthick portlet-toggle'></span>");
}

function previewCardFileName(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = (function (file) {
            $("#filename").text(file.value);
        })(input);
        reader.readAsDataURL(input.files[0]);
    }
}

function getTeams() {
    $.ajax({
        url: extension + "TeamServlet",
        type: 'GET',
        data: {
            type: "GetTeams"
        },
        success: function (teams) {
            var parent = $(".drop-teams");
            parent.empty();
            $("<option />", {text: "Select Team", value: 0}).appendTo(parent);
            $.each(teams, function (id, team) {
                $("<option />", {text: team, value: id}).appendTo(parent);
            });
        }
    });
}

function getMemberTeams(userid, action, memAction) {
    $.ajax({
        url: extension + "TeamServlet",
        type: 'GET',
        data: {
            type: "GetMemberTeams",
            id: userid
        },
        success: function (teams) {
            var parent = $(".teams-list");
            parent.empty();
            var got = 0;
            var first = true;
            var mob = getIsMobile();
            if (mob) {
                $("<div />", {class: "closepanel cursor mobile-show hide", text: "X", click: function () {
                        parent.hide();
                    }}).appendTo(parent);
            }
            $.each(teams, function (id, team) {
                team = team + " Team";
                var params = [id, team];
                var teamPar = $("<div />", {class: "accordion-parent"}).appendTo(parent);
                var pan = $("<div />", {class: "panel-list teamName", text: team, click: function () {
                        $(".panel-list").removeClass("selected-member");
                        $(this).addClass("selected-member");
                        linkToFunction(action, params);
                        if (mob)
                            parent.hide();
                    }}).appendTo(teamPar);
                $("<div />", {class: "smalltext greytext", text: "Team"}).prependTo(pan);
                $("<div />", {class: "team-id", text: id}).appendTo(pan).hide();
                $("<div />", {class: "orangetext right midi-margintop half-marginright cursor accordion-handler"}).prependTo(teamPar);
                $("<div />", {style: "clear: both"}).appendTo(pan);
                var mem = $("<div />", {class: "team-members accordion-content"}).appendTo(teamPar);
                getTeamMembers(id, mem, memAction, got, action, params);
                if (got === 0) {
                    mem.addClass("accordion-expanded");
//                    $(".panel-list").removeClass("selected-member");
//                    pan.addClass("selected-member");
//                    linkToFunction(action, params);
                }
                got++;
                if (first) {
                    linkToFunction("teamFunctions", params);
                }
                first = false;
            });
            Accordion();
            checkMobile();
        }
    });
}

function teamFunctions(teamid, teamname) {
    $("#teamid").val(teamid);
    $(".team_name").text(teamname);
    getTeamMembersDetails(teamid);
    getTeamResources(teamid);
    getMessages(teamid, "Team", $("#teamMessagesList"), 0);
    $("#teamroom").show();
    $("#chatroom").hide();
}

function teamMemberFunctions(data) {
    var id = data[0];
    var member = data[1];
    if (userid !== id) {
        $("#teamroom").hide();
        $("#chatroom").show();
        $(".memberName").text(member);
        getMessages(id, "User", $("#memberMessagesList"), 0);
    }
}

function taskFunctions(teamid, name) {
    getUserTasks(teamid, "Team", "todo");
    $(".active-pane-empty").show();
    getUserTasks(teamid, "Team", "done");
    $(".memberName").text(name);
    checkAddTask("Team", teamid, name);
    getTeamTasks("Team", teamid);
}

function memberTaskFunctions(data) {
    var id = data[0];
    var member = data[1];
    getUserTasks(id, "User", "todo");
    getUserCurrentTask(id, "User");
    getUserTasks(id, "User", "done");
    $(".memberName").text(member);
    checkAddTask("User", id, member);
    getTeamTasks("User", id);
}

function timesheetFunctions(teamid, name) {
    if (date === "")
        date = "today";
//    alert("going");
    currId = teamid;
    currType = "Team";
    getUserTimesheet(teamid, "Team", "all", date);
    $(".memberName").text(name);
}

function memberTimesheetFunctions(data) {
    var id = data[0];
    var member = data[1];
    if (date === "")
        date = "today";
//    alert("going again");
    currId = id;
    currType = "User";
    getUserTimesheet(id, "User", "all", date);
    $(".memberName").text(member);
}

function reportsFunctions(teamid, name) {
    if (date === "")
        date = "today";
    currId = teamid;
    currType = "Team";
    if (currSec === "timeReport") {
        getUserReports(teamid, "Team", "all", date1, date2);
    } else if (currSec === "taskReport") {
        getTaskReports(currId, currType);
    }
    $(".memberName").text(name);
}

function memberReportFunctions(data) {
//    alert("here");
    var id = data[0];
    var member = data[1];
    if (date === "")
        date = "today";
    currId = id;
    currType = "User";
    if (currSec === "timeReport") {
        getUserReports(id, "User", "all", date1, date2);
    } else if (currSec === "taskReport") {
        getTaskReports(currId, currType);
    }
    $(".memberName").text(member);
}

function getTeamMembers(teamid, parent, memberAction, got, action, params) {
    $.ajax({
        url: extension + "TeamServlet",
        type: 'GET',
        data: {
            type: "GetTeamMembers",
            id: teamid
        },
        success: function (data) {
            var team = data[0];
            var leadid = data[1];
            parent.empty();
            var mob = getIsMobile();
            $.each(team, function (id, member) {
                var params = [id, member];
                var m = $("<div />", {class: "panel-list teamMember user-" + id, click: function () {
                        $(".panel-list").removeClass("selected-member");
                        $(this).addClass("selected-member");
//                        alert("going with " + member + " : " + id + " to " + memberAction);
                        linkToFunction(memberAction, params);
                        if (mob)
                            $(".teams-list").hide();
                    }}).appendTo(parent);
                $("<div />", {class: "profile-pic-mini"}).appendTo(m);
                var panel = $("<div />", {class: "parts mediumtext"}).appendTo(m);
                $("<div />", {class: "member-name", text: member}).prependTo(panel);
                $("<div />", {class: "smalltext greytext desc", text: "Team Member"}).prependTo(panel);
                $("<div />", {class: "user-id", text: id}).appendTo(panel).hide();
                if (id === userid && loaded !== 1) {
                    loaded = 1;
                    m.prependTo(".teams-list");
                    $(".panel-list").removeClass("selected-member");
                    m.addClass("selected-member");
//                    alert("going with " + member + " : " + id + " to " + memberAction);
                    linkToFunction(memberAction, params);
                }
            });
            $(".user-" + leadid).addClass("team-leader").find(".desc").text("Team Leader");
        }
    });
}

function getTeamMembersDetails(teamid) {
    $.ajax({
        url: extension + "TeamServlet",
        type: 'GET',
        data: {
            type: "GetTeamMembersDetails",
            id: teamid
        },
        success: function (team) {
            var parent = $("#memberTable");
            parent.empty();
            if (team === "none") {
                var row = $("<tr />").appendTo(parent);
                $("<td />", {text: "No Members in this team"}).appendTo(row);
            } else {
                $.each(team, function (id, member) {
                    var row = $("<tr />").appendTo(parent);
                    $("<td />", {text: member["first_name"]}).appendTo(row);
                    $("<td />", {text: member["last_name"]}).appendTo(row);
                    $("<td />", {text: member["email"]}).appendTo(row);
                    $("<td />", {text: member["phone_number"]}).appendTo(row);
                    $("<td />", {text: member["date_joined"]}).appendTo(row);
                });
            }
        }
    });
}

function getTeamResources(teamid) {
    $.ajax({
        url: extension + "TeamServlet",
        type: 'GET',
        data: {
            type: "GetTeamResources",
            id: teamid
        },
        success: function (data) {
            var parent = $(".resourceList");
            parent.empty();
            $.each(data, function (id, resource) {
                var row = $("<div />", {class: "row"}).appendTo(parent);

                var ext = resource["extension"];
                var icon = GetExtensionIcon(ext);
                var left = $("<div />", {class: "left"}).appendTo(row);
                $("<i />", {class: "fa fa-fw fa-" + icon + " parts extralargetext mini-marginright"}).appendTo(left);
                var parts = $("<div />", {class: "parts"}).appendTo(left);
                $("<div />", {class: "greytext mediumtext", text: "File Name:"}).appendTo(parts);
                $("<div />", {class: "linkbtn", text: resource["file_name"]}).appendTo(parts);

                var right = $("<div />", {class: "right"}).appendTo(row);
                var parts2 = $("<div />", {class: "parts double-marginright"}).appendTo(right);
                $("<div />", {class: "greytext mediumtext", text: "uploaded on:"}).appendTo(parts2);
                $("<div />", {text: resource["date"]}).appendTo(parts2);
                var parts3 = $("<div />", {class: "parts double-marginright"}).appendTo(right);
                $("<div />", {class: "greytext mediumtext", text: "uploaded by:"}).appendTo(parts3);
                $("<div />", {text: resource["user_name"]}).appendTo(parts3);
                $("<i />", {class: "fa fa-fw fa-download greentext slightly-largetext cursor parts margin-horizontal mini-margintop", click: function () {
                        window.open(extension + "resources/" + resource["file_name"]);
                    }}).appendTo(right);
                if (type === "manager")
                    $("<i />", {class: "fa fa-fw fa-times redtext slightly-largetext cursor parts margin-horizontal mini-margintop"}).appendTo(right);

                $("<div />", {style: "clear:both"}).appendTo(row);
            });
        }
    });
}

function getTeamTasks(usertype, usid) {
    $.ajax({
        url: extension + "TeamServlet",
        type: 'GET',
        data: {
            type: "GetTeamTasks",
            id: usid,
            userType: usertype
        },
        success: function (data) {
            var parent = $("#pre-task-list");
            parent.find(".pre").remove();
            $.each(data, function (id, taskname) {
                var opt = $("<option />", {class: "pre", value: id, click: function () {
                        if (usertype !== "Team") {
//                            alert(taskname);
                            addUserTask(userid, usid, id);
                        }
                    }}).appendTo(parent);
                $("<span />", {class: "nam", text: taskname}).appendTo(opt);
            });
            sortText(parent, ".pre", ".nam", "asc");
        }
    });
}

function getMessages(id, objType, parent, count) {
    parent.find(".none").addClass("hide").hide();
    $.ajax({
        url: extension + "MessageServlet",
        type: 'GET',
        data: {
            type: "GetMessages",
            id: id,
            objtype: objType,
            user: userid
        },
        success: function (data) {
            if (data !== "none") {
                if (count === 0)
                    parent.find(".dyn-msg").remove();
                $.each(data, function (id, messages) {
                    var row = parent.find(".msg_tile").clone();
                    var name = messages["sender"];
                    if ("" + userid === "" + messages["sender_id"]) {
                        name = "You";
                        row.find(".tile").addClass("right green");
                    }
                    row.find(".msg-name").text(name);
                    row.find(".msg-time").text(messages["time"].substring(0, 5));
                    row.find(".msg-msg").text(messages["message"]);
                    row.removeClass("hide");
                    row.removeClass("msg_tile");
                    row.addClass("dyn-msg");
                    row.appendTo(parent).show();
                });
            } else {
                parent.find(".dyn-msg").remove();
                parent.find(".none").removeClass("hide").show();
            }
            $(".sendMessage").unbind("click").click(function () {
                var message = $(parent).closest(".msg").find(".messageBody").val();
                if (message.trim() !== "") {
                    sendMessage(userid, message, objType, id, parent);
                } else {
                    CustomAlert("Please type your message");
                }
            });
        }
    });
}

function GetExtensionIcon(extension) {
    var icon = "file-o";
    switch (extension) {
        case "doc":
        {
            icon = "file-word-o bluetext";
            break;
        }
        case "docx":
        {
            icon = "file-word-o bluetext";
            break;
        }
        case "pdf":
        {
            icon = "file-pdf-o redtext";
            break;
        }
        case "xls":
        {
            icon = "file-excel-o greentext";
            break;
        }
        case "xlsx":
        {
            icon = "file-excel-o greentext";
            break;
        }
        case "ppt":
        {
            icon = "file-powerpoint-o orangetext";
            break;
        }
        case "pptx":
        {
            icon = "file-powerpoint-o orangetext";
            break;
        }
        case "mp3":
        {
            icon = "file-audio-o greytext";
            break;
        }
        case "wav":
        {
            icon = "file-audio-o greytext";
            break;
        }
        case "mp4":
        {
            icon = "file-video-o bluetext";
            break;
        }
        case "mkv":
        {
            icon = "file-video-o bluetext";
            break;
        }
        case "avi":
        {
            icon = "file-video-o bluetext";
            break;
        }
        case "jpg":
        {
            icon = "file-photo-o bluetext";
            break;
        }
        case "png":
        {
            icon = "file-photo-o bluetext";
            break;
        }
        case "gif":
        {
            icon = "file-photo-o bluetext";
            break;
        }
    }
    return icon;
}

function getUserTasks(userid, objtype, status, count) {
    $(".loader").show();
    var counter = 0;
    if (count) {
        counter = count;
    }
    $.ajax({
        url: extension + "TaskServlet",
        type: 'GET',
        data: {
            type: "GetUserTasks",
            id: userid,
            objtype: objtype,
            status: status,
            count: counter
        },
        success: function (data) {
            var newcount = data[0];
            if (status === "doing") {
                getUserCurrentTask(userid, "User");
            } else {
                var parent = $("#tasks-" + status);
                if (counter === 0)
                    parent.empty();
                loadTasks(parent, data, objtype, userid, newcount, status);
            }
        }
    });
}

function loadTasks(parent, data, type, objid, count, stat) {
    mason = parent;
    if (data !== "none") {
        var dt = data[1];
        $.each(dt, function (id, tasks) {
            var status = tasks["status"];
            var for_id = tasks["created_for"];
            var task = $("<div />", {class: "task task-" + status, click: function () {
                    $(this).css('width', '');
                    $(this).css('height', '');
                }}).appendTo(parent);

            //Task Type
            $("<div />", {class: "task-type task_" + tasks["task_type"], text: capitaliseFirstLetter(tasks["task_type"])}).appendTo(task);

            // Task topic
            var taskTopic = $("<div />", {class: "task-topic"}).appendTo(task);
            $("<div />", {text: tasks["task_topic"]}).appendTo(taskTopic);
            var parentid = tasks["parent_id"];
            if (parentid > 0)
                $("<div />", {class: "task-parent borderleft lightborder paddingleft searchable", text: tasks["parent_name"]}).appendTo(taskTopic);

            // Task Description
            var desc = tasks["description"];
            if (desc.trim() !== "")
                $("<div />", {class: "paddingbottom mediumtext", text: desc}).appendTo(task);

            // Task Details and some information
            var taskDetails = $("<div />", {class: "task-details"}).appendTo(task);
            $("<div />", {class: "left", text: tasks["createdFor"]}).appendTo(taskDetails);
            var time = tasks["time_created"];
            time = time.substring(0, 5);
//            dates.push(tasks["date_created"]);
            $("<div />", {class: "right", text: time}).appendTo(taskDetails);
            $("<div />", {style: "clear: both"}).appendTo(taskDetails);

            $("<div />", {class: "task-date", text: " " + tasks["date_created"] + " "}).appendTo(taskDetails).hide();
            $("<div />", {class: "task-read-date", text: " " + tasks["read_date_created"] + " "}).appendTo(taskDetails).hide();

            // Task Comments
            var commentDiv = $("<div />", {class: "accordion-parent comments"}).appendTo(task).hide();
            var cmnts = $("<div />", {class: "task-details blacktext cursor", text: "Comments"}).appendTo(commentDiv);
            $("<span />", {class: "half-marginright cmnt-count", text: "10"}).prependTo(cmnts);
            $("<span />", {class: "right accordion-handler"}).appendTo(cmnts);
            $("<div />", {style: "clear: both"}).appendTo(cmnts);
            var commentOptions = $("<div />", {class: "accordion-content mh-300 overflow"}).appendTo(commentDiv);
            getTaskComments(id, "Task", commentOptions);
            Accordion();

            //Task Option buttons
            var taskOptions = $("<div />", {class: "task-options"}).appendTo(task);
            if (for_id === userid && status !== "doing") {
                var opt1 = $("<div />", {class: "task-option left tooltipParent", click: function () {
                        var session = $(".session-id").text();
                        if (session.trim() === "")
                            session = 0;
                        updateTaskStatus(id, "doing", status, type, objid, session);
                    }}).appendTo(taskOptions);
                $("<i />", {class: "fa fa-fw fa-hourglass-o"}).appendTo(opt1);
                $("<div />", {class: "tooltip bottomtip hide margintop", text: "Doing"}).appendTo(opt1);
            }
            if (status === "doing" && for_id === userid) {
//                var opt1 = $("<div />", {class: "task-option left tooltipParent", click: function () {
//                        updateTaskStatus(id, "todo", status, type, objid);
//                    }}).appendTo(taskOptions);
//                $("<i />", {class: "fa fa-fw fa-pause"}).appendTo(opt1);
//                $("<div />", {class: "tooltip bottomtip hide margintop", text: "Pause"}).appendTo(opt1);
                var opt1 = $("<div />", {class: "task-option left mediumtext bold half-margintop", text: "Ongoing..."}).appendTo(taskOptions);
            }
            if (status === "paused" && for_id === userid) {
                var opt1 = $("<div />", {class: "task-option left mediumtext bold half-margintop", text: "Paused..."}).appendTo(taskOptions);
            }
            if (status === "todo" && for_id === userid) {
                var opt2 = $("<div />", {class: "task-option left tooltipParent", click: function () {
                        updateTaskStatus(id, "done", status, type, objid, 0);
                    }}).appendTo(taskOptions);
                $("<i />", {class: "fa fa-fw fa-check"}).appendTo(opt2);
                $("<div />", {class: "tooltip bottomtip hide margintop", text: "Done"}).appendTo(opt2);
            }

//            var opt5 = $("<div />", {class: "task-option right tooltipParent", click: function () {
//                    $(".task-hanger").hide();
//                    $(this).closest(".task").find(".task-hanger").toggle(500);
//                }}).appendTo(taskOptions);
//            $("<i />", {class: "fa fa-fw fa-chevron-down"}).appendTo(opt5);
//            $("<div />", {class: "tooltip bottomtip hide margintop", text: "Other Options"}).appendTo(opt5);
            var opt5 = $("<div />", {class: "task-option right tooltipParent", click: function () {
                    deleteTask(userid, id, for_id, status, type);
                }}).appendTo(taskOptions);
            $("<i />", {class: "fa fa-fw fa-times"}).appendTo(opt5);
            $("<div />", {class: "tooltip bottomtip hide margintop", text: "Delete"}).appendTo(opt5);

            opt = $("<div />", {class: "task-option right tooltipParent"}).appendTo(taskOptions);
            $("<i />", {class: "fa fa-fw fa-link"}).appendTo(opt);
            $("<div />", {class: "tooltip bottomtip hide margintop", text: "Link Task to Object"}).appendTo(opt);

            var opt3 = $("<div />", {class: "task-option right tooltipParent", click: function () {
                    createSubTaskForm(for_id, id, type, tasks["task_topic"]);
                }}).appendTo(taskOptions);
            $("<i />", {class: "fa fa-fw fa-plus"}).appendTo(opt3);
            $("<div />", {class: "tooltip bottomtip hide margintop", text: "Add Sub Task"}).appendTo(opt3);
//            $("<div />", {style: "clear: both"}).appendTo(taskOptions);

//            var hanger = $("<div />", {class: "task-hanger"}).appendTo(taskOptions).hide();
            var opt = $("<div />", {class: "task-option right tooltipParent", click: function () {
                    createCommentForm(task, id, "Task", userid, commentOptions);
                }}).appendTo(taskOptions);
            $("<i />", {class: "fa fa-fw fa-comment"}).appendTo(opt);
            $("<div />", {class: "tooltip bottomtip hide margintop", text: "Comment"}).appendTo(opt);

            opt = $("<div />", {class: "task-option right tooltipParent", click: function () {
                    geTaskDetails(id, status, type, objid);
                }}).appendTo(taskOptions);
            $("<i />", {class: "fa fa-fw fa-expand"}).appendTo(opt);
            $("<div />", {class: "tooltip bottomtip hide margintop", text: "Open Task"}).appendTo(opt);

//            mason.masonry();
//            mason.masonry('appended', task);
//            mason.masonry('reloadItems');
//            drag(".task", "#task-doing");
        });
        if (!getIsMobile()) {
            tooltip();
        }
        $("#sort-list").val("1");
        sortTasks(sortvalue, parent);
        $(".task-hanger").mouseleave(function () {
            var hang = $(this);
            setTimeout(function () {
                hang.hide(500)
            }, 1000);
        });
        $("#moreTasks").removeClass("hide").show();
        $("#moreTasks").unbind("click").click(function () {
            getUserTasks(objid, type, stat, count);
        });
    } else {
        if (count === 0) {
//            alert("found nothing");
//            parent.empty();
            $("<div />", {class: "mediumtext border-vertical lightborder half-padding-vertical", text: "No Tasks found"}).appendTo(parent);
        }
        $("#moreTasks").addClass("hide").hide();
        $(".loader").hide();
    }
}

function getUserCurrentTask(userid, objtype) {
//    alert(userid);
    $.ajax({
        url: extension + "TaskServlet",
        type: 'GET',
        data: {
            type: "GetUserCurrentTask",
            id: userid,
            objtype: objtype
        },
        success: function (data) {
//            alert(data);
            $(".active-pane-empty").hide();
            if (data !== "none") {
                var par = $(".active-pane-paused");
                par.find(".newclone").remove();
                $(".doing-type").text(capitaliseFirstLetter(data["task_type"]));
                $(".doing-type").removeClass("task_meeting");
                $(".doing-type").removeClass("task_work");
                $(".doing-type").removeClass("task_research");
                $(".doing-type").addClass("task_" + data["task_type"]);
                $(".doing-topic").text(data["task_topic"]);
                $(".session-id").text(data["session-id"]);
                $(".doing-description").text(data["description"]);
                $(".doing-created-by").text(data["created-by"]);
                $(".doing-started-date").text(data["date_started"]);
                $(".doing-time-started").text(data["time_started"]);
                $(".doing-budget").text(data["budget"]);
                var stat = data["status"];
                if (stat === "paused") {
                    loadBreak(data["break_text"], data["id"], data["session-id"]);
                }
//                $(".doing-pause").unbind("click").click(function () {
//                    updateTaskStatus(data["id"], "todo", "doing", objtype, userid);
//                });
                $(".doing-break").unbind("click").click(function () {
                    var breakTxt = $(this).find(".doing-break-text").text().trim();
//                    var breakIcon = $(this).find(".doing-break-icon").text().trim();
                    Break(data["id"], breakTxt, "start", data["session-id"]);
                    loadBreak(breakTxt, data["id"], data["session-id"]);
                });
                $(".doing-complete").unbind("click").click(function () {
                    updateTaskStatus(data["id"], "done", "doing", objtype, userid, data["session-id"]);
                });
                $(".c-t-open").unbind("click").click(function () {
                    geTaskDetails(data["id"], status, type, userid);
                });
//                var parent = $(".doing-comment");
//                getTaskComments(data["id"], "Task", parent);
            } else {
                $(".active-pane-empty").show();
                if(getIsMobile()){
                    $(".active-pane").addClass("hide").hide();
                }
            }
        }
    });
}

function loadBreak(breakTxt, taskid, sessionid) {
    var breakIcon = "";
    breakTxt = "" + breakTxt.trim();
    switch (breakTxt) {
        case "Bathroom Break":
        {
            breakIcon = "fa-bathtub";
            break;
        }
        case "Lunch Break":
        {
            breakIcon = "fa-cutlery";
            break;
        }
        case "Phone Call Break":
        {
            breakIcon = "fa-phone";
            break;
        }
        case "Off Seat Break":
        {
            breakIcon = "fa-user-secret";
            break;
        }
        default:
            break;
    }
    var par = $(".active-pane-paused");
    par.find(".newclone").remove();
    var newbreak = par.find(".break-clone").clone();
    newbreak.removeClass("hide");
    newbreak.removeClass("break-clone");
    newbreak.addClass("newclone");
    newbreak.find(".break-text").text(breakTxt);
    newbreak.find(".break-icon").addClass(breakIcon);
    newbreak.appendTo(par).show();
//                    Break(data["id"], "todo", "doing", objtype, userid);
    $(".doing-resume").unbind("click").click(function () {
        Break(taskid, breakTxt, "end", sessionid);
        newbreak.remove();
    });
}

function geTaskDetails(taskid, type, objid) {
    $(".loader").show();
    $.ajax({
        url: extension + "TaskServlet",
        type: 'GET',
        data: {
            type: "GetTaskDetails",
            id: taskid
        },
        success: function (data) {
            $(".t-v-user").text(data["createdFor"]);
            $(".t-v-type").text(data["task_type"]);
            $(".t-v-title").text(data["task_topic"]);
            $(".t-v-desc").text(data["description"]);
            $(".t-v-start-date").text(data["date_started"]);
            $(".t-v-start-time").text(data["time_started"]);
            $(".t-v-duration").text(data["budget"]);
            getTaskComments(taskid, "Task", $(".t-v-comments"));
            getTaskJournals(taskid, $(".t-v-journals"));

            var status = data["status"];
            var for_id = data["created_for"];

            var taskOptions = $(".t-v-options");
            taskOptions.empty();
            if (for_id === userid && status !== "doing") {
                var opt1 = $("<div />", {class: "task-option left tooltipParent border lightborder half-marginright", click: function () {
                        var session = $(".session-id").text();
                        if (session.trim() === "")
                            session = 0;
                        updateTaskStatus(taskid, "doing", status, type, objid, session);
                    }}).appendTo(taskOptions);
                $("<i />", {class: "fa fa-fw fa-hourglass-o"}).appendTo(opt1);
                $("<div />", {class: "tooltip bottomtip hide half-margintop", text: "Doing"}).appendTo(opt1);
            }
            if (status === "doing" && for_id === userid) {
                var opt1 = $("<div />", {class: "task-option left mediumtext bold half-margintop marginright", text: "Ongoing..."}).appendTo(taskOptions);
            }
            if (status === "paused" && for_id === userid) {
                var opt1 = $("<div />", {class: "task-option left mediumtext bold half-margintop marginright", text: "Paused..."}).appendTo(taskOptions);
            }
            if (status === "todo" && for_id === userid) {
                var opt2 = $("<div />", {class: "task-option left tooltipParent border lightborder half-marginright", click: function () {
                        updateTaskStatus(taskid, "done", status, type, objid, 0);
                    }}).appendTo(taskOptions);
                $("<i />", {class: "fa fa-fw fa-check"}).appendTo(opt2);
                $("<div />", {class: "tooltip bottomtip hide half-margintop", text: "Done"}).appendTo(opt2);
            }

            var opt5 = $("<div />", {class: "task-option right tooltipParent border lightborder half-marginright", click: function () {
                    deleteTask(userid, taskid, for_id, status, type);
                }}).appendTo(taskOptions);
            $("<i />", {class: "fa fa-fw fa-times"}).appendTo(opt5);
            $("<div />", {class: "tooltip bottomtip hide half-margintop", text: "Delete"}).appendTo(opt5);

            var opt3 = $("<div />", {class: "task-option right tooltipParent border lightborder half-marginright", click: function () {
                    createSubTaskForm(for_id, taskid, type, data["task_topic"]);
                }}).appendTo(taskOptions);
            $("<i />", {class: "fa fa-fw fa-plus"}).appendTo(opt3);
            $("<div />", {class: "tooltip bottomtip hide half-margintop", text: "Add Sub Task"}).appendTo(opt3);

            opt = $("<div />", {class: "task-option right tooltipParent border lightborder half-marginright"}).appendTo(taskOptions);
            $("<i />", {class: "fa fa-fw fa-link"}).appendTo(opt);
            $("<div />", {class: "tooltip bottomtip hide half-margintop", text: "Link Task to Object"}).appendTo(opt);

            $(".taskBack").removeClass("hide").show();
            $(".loader").hide();

            $(".t-v-post-comment").unbind("click").click(function () {
                var comment = $(".t-v-comment-text").val();
                commentOnTask(userid, taskid, "Task", comment, $(".t-v-comments"));
                $(".t-v-comment-text").val("");
//                $(".taskBack").addClass("hide").hide();
            });
            $(".t-v-post-new-journal").unbind("click").click(function () {
                var text = $(".t-v-new-journal-text").val();
                AddTaskJournal(userid, taskid, text, $(".t-v-journals"));
                $(".t-v-new-journal-text").val("");
            });
            if (!getIsMobile()) {
                tooltip();
            }
        }
    });
}

function sortTasks(val, parent) {
//    alert("sorting");
    $(".loader").show();
    sortvalue = val;
    switch (val) {
        case "1":
        {
            var dates = [];
            var datePars = parent.find(".task-date");
            $.each(datePars, function (ind, par) {
                dates.push($(par).text().trim());
            });
            var uniqueDates = [];
            $.each(dates, function (i, el) {
                if ($.inArray(el, uniqueDates) === -1)
                    uniqueDates.push(el);
            });
            uniqueDates.sort();
            uniqueDates.reverse();
            sortTodoByDates(uniqueDates, parent);
//            alert(dates);
            break;
        }
        case "2":
        {
            sortTodoByAlphabeticalOrder(parent, "asc");
            break;
        }
        case "3":
        {
            sortTodoByAlphabeticalOrder(parent, "desc");
            break;
        }
    }
}

function sortTodoByDates(dates, parent) {
    parent.find(".task-seg").find(".task").appendTo(parent);
    parent.find(".task-seg").remove();

    parent.find(".task").removeClass(".task-sorted");
    $.each(dates, function (ind, dt) {
        var seg = $("<div />", {class: "task-seg double-margintop"}).appendTo(parent);
        var segHead = $("<div />", {class: "paddingbottom center bordertop lightborder"}).appendTo(seg);
        var segCont = $("<div />", {class: "task-seg-cont"}).appendTo(seg);
        var realdt = parent.find(".task-date:contains(" + " " + dt + " " + ")").closest(".task").find(".task-read-date").first().text();
        $("<div />", {class: "padding half-padding-vertical greybtn whitetext parts radius mediumtext neg-double-margintop", text: realdt}).appendTo(segHead);
//        var unsortedTasks = $(".task").not(".task-sorted");
        parent.find(".task").not(".task-sorted").find(".task-date:contains(" + " " + dt + " " + ")").closest(".task").addClass(".task-sorted").appendTo(segCont);
    });

    $(".task-seg-cont").sortable({
        connectWith: ".task-seg-cont",
        handle: ".task-topic",
        placeholder: "portlet-placeholder ui-corner-all"
    });
    $(".loader").hide();
    $("#sort-list").unbind("change").change(function () {
        var ex = $(this).val();
        sortTasks(ex, $("#tasks-todo"));
        sortTasks(ex, $("#tasks-done"));
    });
}

function sortTodoByAlphabeticalOrder(parent, order) {
    parent.find(".task-seg").find(".task").appendTo(parent);
    parent.find(".task-seg").remove();
    var alphabets = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
    if (order === "desc") {
        alphabets.reverse();
    }

    parent.find(".task").removeClass(".task-sorted");
    $.each(alphabets, function (ind, ab) {
        var seg = $("<div />", {class: "task-seg double-margintop"}).appendTo(parent);
        var segHead = $("<div />", {class: "paddingbottom center bordertop lightborder"}).appendTo(seg);
        var segCont = $("<div />", {class: "task-seg-cont"}).appendTo(seg);
        $("<div />", {class: "padding half-padding-vertical greybtn whitetext parts radius mediumtext neg-double-margintop", text: ab}).appendTo(segHead);
        parent.find(".task").not(".task-sorted").find(".task-topic").filter(function (index) {
            return $(this).text().charAt(0) === ab;
        }).closest(".task").addClass(".task-sorted").appendTo(segCont);
        if ($(segCont).children().length === 0) {
            seg.hide();
        }
        sortText(segCont, ".task", ".task-topic", "asc");
    });

    $(".task-seg-cont").sortable({
        connectWith: ".task-seg-cont",
        handle: ".task-topic",
        placeholder: "portlet-placeholder ui-corner-all"
    });

    $(".loader").hide();
    $("#sort-list").unbind("change").change(function () {
        var ex = $(this).val();
        sortTasks(ex, $("#tasks-todo"));
        sortTasks(ex, $("#tasks-done"));
    });
}

function createCommentForm(parent, parid, parType, userid, commentParent) {
    var form = $("<div />", {class: "task-options margintop"}).appendTo(parent);
    var comment = $("<textarea />", {rows: "3", class: "txt lightborder radius almost-normaltext", placeholder: "Type your Comment here"}).appendTo(form);
    $("<div />", {class: "btn radius half-marginright parts padding-horizontal mini-padding-vertical orangebtn", text: "Post Comment", click: function () {
            commentOnTask(userid, parid, parType, comment.val(), commentParent);
            form.remove();
        }}).appendTo(form);
    $("<div />", {class: "btn radius parts padding-horizontal mini-padding-vertical greybtn", text: "Cancel", click: function () {
            form.remove();
        }}).appendTo(form);
}

function createSubTaskForm(for_id, id, type, parText) {
    $(".createsubtask").removeClass("hide");
    $(".createsubtask").show();
    $(".addsubtask_parent").text(parText);
    $(".create_addsubtask").unbind("click").click(function () {
        $(".createsubtask").addClass("hide");
        $(".createsubtask").hide();
        $(".txt_text").val("");
        $(".txt_num").val("0");
    });
    $("#btn_createsubtask").unbind("click").click(function () {
        var topic = $("#addsubtask_topic").val();
        var desc = $("#addsubtask_desc").val();
        var task_type = $('input[name=task_sub_type]:checked').val();
        var budget_hrs = $("#addsubtask_budget_hours").val().trim();
        var budget_min = $("#addsubtask_budget_minutes").val().trim();
        if (budget_hrs === "")
            budget_hrs = 0;
        if (budget_min === "")
            budget_min = 0;
        budget_hrs = parseInt(budget_hrs);
        budget_min = parseInt(budget_min);
        var budget = (budget_hrs * 60) + budget_min;
        //        alert(topic +", "+ desc +", "+ budget +", "+ task_type);
        createTask(for_id, for_id, budget, id, topic, "todo", type, desc, task_type);
        $(".createsubtask").addClass("hide");
        $(".createsubtask").hide();
        $(".txt_text").val("");
        $(".txt_num").val("0");
    });
}

function checkAddTask(type, usid, name) {
    currId = usid;
    currType = type;
//    if (type === "User") {
    $("#addtask_user").empty();
    $("<option />", {text: name, value: usid, selected: true}).appendTo("#addtask_user");
    $("#addtask_user").prop("disabled", "disabled");
//    } else if (type === "Team") {
//        $("#addtask_user").empty();
//        $("#addtask_user").prop("disabled", false);
//        var members = $(".teamMember");
//        $.each(members, function (index, item) {
//            var memName = $(item).find(".member-name").text();
    //            var id = $(item).find(".user-id").text();
//            $("<option />", {text: memName, value: id}).appendTo("#addtask_user");
//        });
//    }
    $(".btn_addtask").unbind("click").click(function () {
        //        $(".addtask").addClass("hide");
        //        $(".addtask").hide();
        $(".createtask").removeClass("hide");
        $(".createtask").show();
    });
    $(".create_addtask").unbind("click").click(function () {
        $(".createtask").addClass("hide");
        $(".createtask").hide();
        $(".txt_text").val("");
        $(".txt_num").val("0");
        //        $(".addtask").removeClass("hide");
//        $(".addtask").show();
    });
    $("#btn_createtask").unbind("click").click(function () {
        var foruserid = $("#addtask_user").val();
        var topic = $("#addtask_topic").val();
        var desc = $("#addtask_desc").val();
        var task_type = $('input[name=task_type]:checked').val();
        var budget_hrs = $("#addtask_budget_hours").val().trim();
        var budget_min = $("#addtask_budget_minutes").val().trim();
        if (budget_hrs === "")
            budget_hrs = 0;
        if (budget_min === "")
            budget_min = 0;
        budget_hrs = parseInt(budget_hrs);
        budget_min = parseInt(budget_min);
        var budget = (budget_hrs * 60) + budget_min;
        //        alert(topic +", "+ desc +", "+ budget +", "+ task_type);
        createTask(userid, foruserid, budget, 0, topic, "todo", type, desc, task_type);
        $(".createtask").addClass("hide");
        $(".createtask").hide();
        $(".txt_text").val("");
        $(".txt_num").val("0");
//        $(".addtask").removeClass("hide");
        //        $(".addtask").show();
    });
}

function createTask(id, forid, bud, parentid, topic, status, objtype, desc, task_type) {
    $.ajax({
        url: extension + "TaskServlet",
        type: 'GET',
        data: {
            type: "CreateTask",
            userid: id,
            foruserid: forid, budget: bud,
            parentid: parentid,
            topic: topic,
            description: desc,
            TaskType: task_type,
            objType: objtype
        },
        success: function (data) {
            if (data === "success") {
                CustomAlert("Task Created");
                //                alert("forid: "+forid+" object: "+objtype+" status: "+status+" date: "+date);
                getUserTasks(forid, objtype, status);
                getUserTasks(forid, objtype, "todo");
                getTeamTasks(objtype, forid);
            } else {
                CustomAlert(data);
            }
        }
    });
}

function addUserTask(id, forid, task_id) {
    $.ajax({
        url: extension + "TaskServlet",
        type: 'GET',
        data: {
            type: "addUserTask",
            userid: id,
            foruserid: forid,
            taskid: task_id
        },
        success: function (data) {
            if (data === "success") {
                CustomAlert("Task Created");
                //                alert("forid: "+forid+" object: "+objtype+" status: "+status+" date: "+date);
                getUserTasks(forid, "User", "todo");
                getTeamTasks("User", forid);
            } else {
                CustomAlert(data);
            }
        }
    });
}

function deleteTask(id, taskid, forid, status, objtype) {
    $.ajax({
        url: extension + "TaskServlet",
        type: 'GET',
        data: {
            type: "DeleteTask",
            userid: id,
            taskid: taskid
        },
        success: function (data) {
            if (data === "successful") {
                CustomAlert("Task Deleted");
                getUserTasks(forid, objtype, status);
                getUserTasks(forid, objtype, "todo");
            } else {
                CustomAlert(data);
            }
        }
    });
}

function commentOnTask(id, parentid, parType, comment, parent) {
    $.ajax({
        url: extension + "TaskServlet",
        type: 'GET',
        data: {
            type: "commentOnTask",
            userid: id,
            parentid: parentid,
            parenttype: parType,
            comment: comment
        },
        success: function (data) {
            if (data === "success") {
                CustomAlert("Comment Posted");
//                if (status === "doing") {
//                    getUserCurrentTask(id, "User");
//                } else {
//                    getUserTasks(objid, objtype, status);
//                }
//                getUserTasks(objid, objtype, "todo");
//                alert(parentid+", "+parType+", "+parent);
                getTaskComments(parentid, parType, parent);
            } else {
                CustomAlert(data);
            }
        }
    });
}

function AddTaskJournal(id, taskid, journalText, parent) {
    $.ajax({
        url: extension + "TaskServlet",
        type: 'GET',
        data: {
            type: "AddTaskJournal",
            userid: id,
            id: taskid,
            text: journalText
        },
        success: function (data) {
            if (data === "success") {
                getTaskJournals(taskid, parent);
            } else {
                CustomAlert(data);
            }
        }
    });
}

function getTaskComments(parid, partype, parent) {
    $.ajax({
        url: extension + "TaskServlet",
        type: 'GET', data: {
            type: "getComments",
            parid: parid,
            partype: partype
        },
        success: function (data) {
            var count = 0;
            parent.empty();
            if (data !== "none") {
                $.each(data, function (id, comment) {
                    var par = $("<div />", {class: "task-details blacktext normaltext"}).appendTo(parent);
                    var cmt = $("<div />", {class: "greytext mediumtext"}).appendTo(par);
                    $("<div />", {class: "bold", text: comment["user_name"]}).appendTo(cmt);
                    $("<div />", {class: "mini-margintop", text: comment["date"] + " | " + comment["time"]}).appendTo(cmt);
                    $("<div />", {style: "clear: both"}).appendTo(cmt);
                    $("<div />", {class: "half-margin-vertical mediumtext", html: comment["comment"]}).appendTo(par);
                    var rep = $("<div />", {class: "mediumtext bluetext"}).appendTo(par);
                    $("<div />", {class: "parts left linkbtn", text: "reply", click: function () {
                            createCommentForm(newcnt, id, "Comment", userid, reps);
                        }}).appendTo(rep);
                    var reps = comment["replies"];
                    if (reps > 0) {
                        $("<div />", {class: "parts right linkbtn", text: comment["replies"] + " replies", click: function () {
                                getTaskComments(id, "Comment", reps);
                            }}).appendTo(rep);
                    }
                    $("<div />", {style: "clear: both"}).appendTo(rep);
                    var newcnt = $("<div />", {class: "half-margin-vertical"}).appendTo(par);
                    var reps = $("<div />", {class: "margin-vertical"}).appendTo(par);
                    count++;
                });
                parent.closest(".comments").show();
                if (partype === "Task")
                    parent.closest(".comments").find(".cmnt-count").text(count);
            } else {
                $("<div />", {class: "greytext textcenter mediumtext", text: "No Comments"}).appendTo(parent);
            }
        }
    });
}

function getTaskJournals(taskid, parent) {
    $.ajax({
        url: extension + "TaskServlet",
        type: 'GET', data: {
            type: "getJournals",
            id: taskid
        },
        success: function (data) {
            var count = 0;
            parent.find(".newclone").remove();
//            parent.empty();
            if (data !== "none") {
                parent.find(".no-journal").addClass("hide").hide();
                var childclone = parent.find(".clone");
                $.each(data, function (id, details) {
                    var newchild = childclone.clone();
                    newchild.removeClass("clone");
                    newchild.removeClass("hide");
                    newchild.addClass("newclone");
                    newchild.find(".t-v-journal-text").text(details["text"]);
                    newchild.find(".t-v-journal-date").text(details["date"]);
                    newchild.find(".t-v-journal-time").text(details["time"]);
                    newchild.find(".t-v-journal-quote").unbind("click").click(function () {
                        var txt = $(".t-v-comment-text").val();
                        txt = txt + " @jnl" + id + " ";
                        $(".t-v-comment-text").val(txt);
                    });
                    newchild.appendTo(parent).show();
                });
            } else {
                parent.find(".no-journal").removeClass("hide").show();
//                $("<div />", {class: "greytext textcenter mediumtext", text: "No Journals"}).appendTo(parent);
            }
        }
    });
}

function updateTaskStatus(taskid, status, oldstatus, objtype, objid, sessionid) {
    $.ajax({
        url: extension + "TaskServlet",
        type: 'GET',
        data: {
            type: "UpdateTaskStatus",
            id: taskid,
            userid: userid,
            status: status,
            session: sessionid
        },
        success: function (data) {
//            CustomAlert(data);             
            getUserTasks(objid, objtype, oldstatus);
            getUserTasks(objid, objtype, status);
        }
    });
}

function Break(taskid, breakText, status, sessionid) {
    $.ajax({
        url: extension + "TaskServlet", type: 'GET',
        data: {
            type: "Break",
            id: taskid,
            userid: userid,
            breakTxt: breakText,
            status: status,
            session: sessionid
        },
        success: function (data) {
            getUserTasks(userid, "User", "todo");
            getUserCurrentTask(userid, "User");
        }
    });
}

function sendMessage(userid, message, recipient, recipientId, parent) {
    $.ajax({
        url: extension + "MessageServlet",
        type: 'GET',
        data: {
            type: "SendMessage",
            id: userid,
            message: message, recipient: recipient,
            recipientId: recipientId
        },
        success: function (data) {
            $(".messageBody").val("");
            getMessages(recipientId, recipient, parent, 0);
        }});
}

function getUserTimesheet(userid, objtype, filter, date) {
    $.ajax({
        url: extension + "TimesheetServlet",
        type: 'GET',
        data: {
            type: "GetUserTimesheet",
            id: userid,
            objtype: objtype,
            filter: filter,
            date: date
        },
        success: function (data) {
            var parent = $("#timesheetDisplay");
            loadTimesheet(parent, data);
        }
    });
}

function getUserReports(userid, objtype, filter, date1, date2, count) {
    $(".loader").show();
    var counter = 0;
    if (count)
        counter = count;
//    alert("id : " + userid + ", type : " + objtype);
    $.ajax({
        url: extension + "TimesheetServlet",
        type: 'GET',
        data: {
            type: "GetUserReports",
            id: userid,
            objtype: objtype,
            filter: filter,
            dateStart: date1,
            dateEnd: date2,
            count: counter
        },
        success: function (data) {
            var newcount = data[2];
            var parent = $(".reportsParent");
            if (counter === 0)
                parent.children(".newReportClone").remove();
            loadReport(parent, data, userid, objtype, filter, date1, date2, newcount, counter);
        }
    });
}

function getTaskReports(currId, currType, count) {
    $(".loader").show();
    var counter = 0;
    if (count)
        counter = count;
//    alert("id : " + currId + ", type : " + currType);
    $.ajax({
        url: extension + "TimesheetServlet",
        type: 'GET',
        data: {
            type: "GetTaskReports",
            id: currId,
            objtype: currType,
            count: counter
        },
        success: function (data) {
            var newcount = data[3];
            var parent = $(".task-rep-tasks");
            if (counter === 0)
                parent.children(".newclone").remove();
            loadTaskReport(parent, data, currId, currType, newcount);
        }
    });
}

function getUserTaskReportDetails(userid, taskid, parent) {
    $(".loader").show();
    $.ajax({
        url: extension + "TimesheetServlet",
        type: 'GET',
        data: {
            type: "GetUserTaskReportDetails",
            userid: userid,
            taskid: taskid
        },
        success: function (data) {
            if (data === "none") {
                parent.children(".none").removeClass("hide");
                parent.children(".none").show();
                $(".loader").hide();
            } else {
                parent.children(".none").addClass("hide");
                parent.children(".none").hide();
                parent.children(".newclone").remove();
                $.each(data, function (id, report) {
                    var newclone = parent.find(".clone").clone();
                    newclone.removeClass("hide");
                    newclone.removeClass("clone");
                    newclone.addClass("newclone");
                    newclone.find(".user-rep-comment").text(report["task-comment"]);
                    newclone.find(".user-rep-start").text(report["start-time"]);
                    newclone.find(".user-rep-end").text(report["end-time"]);
                    newclone.find(".user-rep-duration").text(report["duration"]);
                    newclone.find(".user-rep-status").text(report["status"]);
                    newclone.find(".user-rep-date").text(report["text-date"]);
                    newclone.appendTo(parent).show();
                });
                $(parent).closest(".task-rep-user").find(".task-user-rep-status").text("loaded");
                $(".loader").hide();
            }
        }
    });
}

function loadTimesheet(parent, data) {
    if (data !== "none") {
        var count = 0;
        parent.find(".newclone").remove();
        $.each(data, function (date, timesheet) {
            var newclone = parent.find(".clone").clone();
            if (count === 0) {
                newclone.find(".tms-long-line").hide();
            }
            newclone.find(".tms-date").text(date);
            newclone.removeClass("hide-on-load");
            newclone.removeClass("clone");
            newclone.addClass("newclone");
            newclone.appendTo(parent).show();

            $.each(timesheet, function (id, entry) {
                var newEntry = newclone.find(".tms-entry").clone();
                newEntry.addClass("newentryclone");
                newEntry.removeClass("tms-entry");
                newEntry.removeClass("hide-on-load");
//                alert(id);
                if (count % 2 === 1) {
                    newEntry.find(".tms-entry-position").removeClass("timesheet-tile-left");
                    newEntry.find(".tms-entry-position").addClass("timesheet-tile-right");
                }
                newEntry.find(".tms-entry-time").text(entry["time"]);
                newEntry.find(".tms-entry-topic").text(entry["topic"]);
                newEntry.find(".tms-entry-comment").text(entry["comment"]);
                newEntry.find(".tms-entry-id").text(id).hide();
                newEntry.appendTo(newclone).show();
                count++;
            });
        });
    }
}

function loadReport(parent, data, userid, objtype, filter, date1, date2, count, counter) {
    $(".loader").show();
    if (data[0] !== "none") {
        $(".none").addClass("hide");
        $(".none").hide();
        $.each(data[0], function (date, reports) {
            $(".loader").show();
//            alert(date);
            var parentClone = parent.find(".reportsClone").clone();
            parentClone.removeClass("hide");
            parentClone.removeClass("reportsClone");
            parentClone.addClass("newReportClone");
            parentClone.find(".rep-date").text(date);

            if (reports["none"]) {
                parentClone.remove();
            } else {
                var repPar = parentClone.find(".reportsDisplay");
                repPar.find(".none").hide();
                var realdate = "";
                $.each(reports, function (id, report) {
                    var newclone = repPar.find(".clone").clone();
                    newclone.removeClass("hide");
                    newclone.removeClass("clone");
                    newclone.find(".rep-task-topic").text(report["task-name"]);
                    newclone.find(".rep-comment").text(report["task-comment"]);
                    newclone.find(".rep-start").text(report["start-time"]);
                    newclone.find(".rep-end").text(report["end-time"]);
                    newclone.find(".rep-duration").text(report["duration"]);
                    newclone.find(".rep-status").text(report["status"]);
                    newclone.find(".rep-id").text(id);
                    newclone.find(".rep-view-task").unbind("click").click(function () {
                        geTaskDetails(report["task-id"], status, type);
                    });
                    newclone.appendTo(repPar).show();
                    realdate = report["date"];
                });

                var SumPar = parentClone.find(".summary-parent");
                var summary = data[1][realdate];
                $.each(summary, function (cnt, sumData) {
                    if (cnt !== "0") {
                        var sumclone = SumPar.find(".summary-clone").clone();
                        sumclone.removeClass("hide");
                        sumclone.removeClass("summary-clone");
                        sumclone.find(".summary-activity").text(sumData["TaskTopic"]);
                        sumclone.find(".summary-time").text(sumData["TaskTime"]);
                        sumclone.find(".summary-sessions").text(sumData["TaskSessions"]);
                        sumclone.appendTo(SumPar).show();
                    } else {
//                        alert("doing summary");
                        SumPar.find(".summary-total-tasks").text(sumData["total-tasks"]);
                        SumPar.find(".summary-total-time").text(sumData["total-time"]);
                        SumPar.find(".summary-total").removeClass("hide").show();
                    }
                });

                parentClone.appendTo(parent).show();
                $(".loader").hide();
            }
        });
        if (count > 0)
            getUserReports(userid, objtype, filter, date1, date2, count);
    } else {
//        alert("counter: "+counter);
//        alert("count: " + data[1]);
        if (counter === 0) {
            parent.find(".newReportClone").remove();
            $(".none").removeClass("hide");
            $(".none").show();
            $(".loader").hide();
        }
        if (data[1] !== 0) {
            getUserReports(userid, objtype, filter, date1, date2, data[1]);
        } else {
            $(".loader").hide();
        }
    }
    tileOptions();
}

function loadTaskReport(parent, data, currId, currType, count) {
    $(".loader").show();
    if (data === "none") {
        parent.children(".none").removeClass("hide");
        parent.children(".none").show();
        $(".loader").hide();
    } else {
        parent.children(".none").addClass("hide");
        parent.children(".none").hide();
        var taskList = data[0];
        var taskDetails = data[1];
        var userTaskReport = data[2];
        var clone = parent.children(".clone");
        $.each(taskList, function (ind, id) {
            var det = taskDetails[id];
            var newchild = clone.clone();
            newchild.removeClass("clone");
            newchild.removeClass("hide");
            newchild.addClass("newclone");
            newchild.find(".task-rep-topic").text(det["task_topic"]);
            newchild.find(".task-rep-parent").text(det["parent_name"]);
            newchild.find(".task-rep-duration").text(det["text_duration"]);
            newchild.find(".task-rep-view-task").unbind("click").click(function () {
                geTaskDetails(id, status, type);
            });

            var taskUsers = userTaskReport[id];
            var utrParent = newchild.find(".task-rep-users");
            var utrClone = utrParent.children(".clone");
            $.each(taskUsers, function (uid, utrData) {
                var utrchild = utrClone.clone();
                utrchild.removeClass("clone");
                utrchild.removeClass("hide");
                utrchild.find(".task-user-rep-username").text(utrData["user-name"]);
                utrchild.find(".task-user-rep-sessions").text(utrData["total-sessions"]);
                utrchild.find(".task-user-rep-duration").text(utrData["total-duration"]);
                utrchild.find(".task-user-rep-open").click(function () {
                    var status = $(this).closest(".task-rep-user").find(".task-user-rep-status").text().trim();
                    var subPar = $(this).closest(".task-rep-user").find(".user-reports-display");
                    if (status === "") {
                        getUserTaskReportDetails(uid, id, subPar);
                    }
                });
                utrchild.appendTo(utrParent).show();
            });
            newchild.appendTo(parent).show();
        });
        getTaskReports(currId, currType, count);
    }
    $(".loader").hide();
    Accordion();
}

function linkToFunction(action, params) {
    switch (action) {
        case "teamFunctions":
        {
            teamFunctions(params[0], params[1]);
            break;
        }
        case "teamMemberFunctions":
        {
            teamMemberFunctions(params);
            break;
        }
        case "taskFunctions":
        {
            taskFunctions(params[0], params[1]);
            break;
        }
        case "memberTaskFunctions":
        {
            memberTaskFunctions(params);
            break;
        }
        case "timesheetFunctions":
        {
            timesheetFunctions(params[0], params[1]);
            break;
        }
        case "memberTimesheetFunctions":
        {
            memberTimesheetFunctions(params);
            break;
        }
        case "reportsFunctions":
        {
            reportsFunctions(params[0], params[1]);
            break;
        }
        case "memberReportFunctions":
        {
            memberReportFunctions(params);
            break;
        }
    }
}