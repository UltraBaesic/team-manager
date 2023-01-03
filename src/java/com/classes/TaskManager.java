/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.classes;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.engine.*;

/**
 *
 * @author Stephen
 */
public class TaskManager {

    public static String Table = Tables.Tasks.Table;
    public static String TaskID = Tables.Tasks.ID;
    public static String TaskTopic = Tables.Tasks.TaskTopic;
    public static String Description = Tables.Tasks.Description;
    public static String CreatedBy = Tables.Tasks.CreatedBy;
    public static String CreatedFor = Tables.Tasks.CreatedFor;
    public static String TimeBudjet = Tables.Tasks.TimeBudjet;
    public static String Status = Tables.Tasks.Status;
    public static String TaskType = Tables.Tasks.TaskType;
    public static String DateCreated = Tables.Tasks.DateCreated;
    public static String TimeCreated = Tables.Tasks.TimeCreated;
    public static String TimeStarted = Tables.Tasks.TimeStarted;
    public static String DateStarted = Tables.Tasks.DateStarted;
    public static String TimeFinished = Tables.Tasks.TimeFinished;
    public static String DateFinished = Tables.Tasks.DateFinished;
    public static String Duration = Tables.Tasks.Duration;

    public TaskManager() {

    }

    public static String CreateTask(String Topic, String desc, int ForUserID, int UserID, int Budget, int ParentID, String type, String objType) throws ClassNotFoundException, SQLException {
        String userName = UserManager.getUserName(UserID);
        String comment = userName + " created '" + Topic + "' as a new task";
        HashMap<String, Object> tableData = new HashMap<>();
        tableData.put(TaskTopic, Topic);
        tableData.put(Description, desc);
        tableData.put(CreatedBy, UserID);
        tableData.put(CreatedFor, ForUserID);
        tableData.put(TimeBudjet, Budget);
        tableData.put(Status, "todo");
        tableData.put(TaskType, type);
        int taskID = DBManager.insertTableDataReturnID(Table, tableData, "");
        DBManager.UpdateCurrentDate(Table, DateCreated, "where " + TaskID + " = " + taskID);
        DBManager.UpdateCurrentTime(Table, TimeCreated, "where " + TaskID + " = " + taskID);
        if (ParentID > 0) {
            DBManager.LinkObjects(Tables.TaskJoin.Table, "Task", ParentID, "Task", taskID);
            String parentTopic = DBManager.GetString(TaskTopic, Table, "where " + TaskID + " = " + ParentID);
//            comment = comment + " under '" + parentTopic + "'";
        }
//        String result = LinkTaskToUser(ForUserID, taskID);

        String result = DBManager.LinkObjects(Tables.TaskJoin.Table, objType, ForUserID, "Task", taskID);

        if (UserID != ForUserID) {
            String name = UserManager.getUserName(ForUserID);
            comment = comment + ". This task was created for " + name;
        }
        TimesheetManager.LogTimesheet(taskID, UserID, "New Task Created", comment, "", 0, 0);
        return result;
    }

    public static HashMap<Integer, HashMap<String, String>> getUserTasks(int objid, String objType, String status, int start, int limit) throws ClassNotFoundException, SQLException {
        HashMap<Integer, HashMap<String, String>> tasks = new HashMap<>();
        ArrayList<Integer> userTaskIds = new ArrayList<>();

        if (objType.equalsIgnoreCase("Team")) {
            ArrayList<Integer> users = TeamManager.getTeamMemberIDs(objid);
            if (!users.isEmpty()) {
                for (int user : users) {
                    userTaskIds.addAll(DBManager.GetIntArrayList(Tables.TaskJoin.ItemTwoID, Tables.TaskJoin.Table, "where " + Tables.TaskJoin.ItemOneType + " = 'User' and " + Tables.TaskJoin.ItemTwoType + " = 'Task' and " + Tables.TaskJoin.ItemOneID + " = " + user));
                }
            }
        } else {
            userTaskIds = DBManager.GetIntArrayList(Tables.TaskJoin.ItemTwoID, Tables.TaskJoin.Table, "where " + Tables.TaskJoin.ItemOneType + " = 'User' and " + Tables.TaskJoin.ItemTwoType + " = 'Task' and " + Tables.TaskJoin.ItemOneID + " = " + objid);
        }

        ArrayList<Integer> todoTasks = DBManager.GetIntArrayList(TaskID, Table, "where " + Status + " = '" + status + "'");
        if (!status.equalsIgnoreCase("done")) {
            todoTasks.addAll(DBManager.GetIntArrayList(TaskID, Table, "where " + Status + " = 'doing'"));
            todoTasks.addAll(DBManager.GetIntArrayList(TaskID, Table, "where " + Status + " = 'paused'"));
        }
        userTaskIds.retainAll(todoTasks);
        int req = start + limit;
        int total = userTaskIds.size();
        if (req > total) {
            limit = total - start;
        }
        int end = start + limit;
        if (limit > 0) {
            List<Integer> newList = new ArrayList<>(userTaskIds.subList(start, end));
            if (!newList.isEmpty()) {
                for (int id : newList) {
                    try {
                        HashMap<String, String> det = DBManager.GetTableData(Table, "where " + TaskID + " = " + id);
                        int createdById = Integer.parseInt(det.get(CreatedBy));
                        int createdForId = Integer.parseInt(det.get(CreatedFor));
                        int parentid = DBManager.GetInt(Tables.TaskJoin.ItemOneID, Tables.TaskJoin.Table, "where " + Tables.TaskJoin.ItemOneType + " = 'Task' and " + Tables.TaskJoin.ItemTwoType + " = 'Task' and " + Tables.TaskJoin.ItemTwoID + " = " + id);

                        String fullname = UserManager.getUserName(createdById);
                        String forname = UserManager.getUserName(createdForId);
                        String parentname = "none";
                        if (parentid > 0) {
                            parentname = DBManager.GetString(TaskTopic, Table, "where " + TaskID + " = " + parentid);
                        }
                        String createdDate = det.get(Tables.Tasks.DateCreated);
                        String readDate = DateUtil.readDate(createdDate);
                        det.put("createdBy", fullname);
                        det.put("createdFor", forname);
                        det.put("date_created", createdDate);
                        det.put("read_date_created", readDate);
                        det.put("parent_id", "" + parentid);
                        det.put("parent_name", parentname);
                        tasks.put(id, det);
                    } catch (Exception e) {
                        String error = e.getMessage();
                        String err = error;
                    }
                }
            }
        }
        return tasks;
    }

    public static HashMap<String, String> getCurrentTask(int userid) throws ClassNotFoundException, SQLException {
        HashMap<String, String> det = new HashMap<>();
        try {
            int taskid = DBManager.GetInt(Tables.Tasks.ID, Table, "where " + Status + " = 'doing' and " + Tables.Tasks.CreatedFor + " = " + userid + " or (" + Status + " = 'paused' and " + Tables.Tasks.CreatedFor + " = " + userid + ")");
            if (taskid == 0) {
                return det;
            }
            det = DBManager.GetTableData(Table, "where " + TaskID + " = " + taskid);
            int createdById = Integer.parseInt(det.get(CreatedBy));
            int createdForId = Integer.parseInt(det.get(CreatedFor));
            int parentid = DBManager.GetInt(Tables.TaskJoin.ItemOneID, Tables.TaskJoin.Table, "where " + Tables.TaskJoin.ItemOneType + " = 'Task' and " + Tables.TaskJoin.ItemTwoType + " = 'Task' and " + Tables.TaskJoin.ItemTwoID + " = " + taskid);
            int timeBudget = Integer.parseInt(det.get(TimeBudjet));
            String timeStarted = det.get(TimeStarted);
            String status = det.get(Status);
            if (status.equals("paused")) {
                String cmt = DBManager.GetLastString(Tables.Timesheet.Comment, Tables.Timesheet.Table, "where " + Tables.Timesheet.TaskID + " = " + taskid + " and " + Tables.Timesheet.Topic + " = 'Break Started'");
                Pattern p = Pattern.compile("(?:^|\\s)'([^']*?)'(?:$|\\s)", Pattern.MULTILINE);
                Matcher m = p.matcher(cmt);
                if (m.find()) {
                    String str = m.group();
                    str = str.replaceAll("\'", "");
                    det.put("break_text", str);
                }
            }

            String fullname = UserManager.getUserName(createdById);
            String forname = UserManager.getUserName(createdForId);
            String parentname = "none";
            if (parentid > 0) {
                parentname = DBManager.GetString(TaskTopic, Table, "where " + TaskID + " = " + parentid);
            }
            String budget = DateUtil.ConvertMinutesToHours(timeBudget);
            int sessionID = DBManager.GetInt(Tables.Timesheet.SessionID, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + userid + " and " + Tables.Timesheet.TaskID + " = " + taskid);

            det.put("created-by", fullname);
            det.put("createdFor", forname);
            det.put("parent_id", "" + parentid);
            det.put("parent_name", parentname);
            det.put("session-id", "" + sessionID);
            det.put("time_started", DateUtil.readTime(timeStarted));
            det.put(DateStarted, DateUtil.readDate(det.get(DateStarted)));
            det.put("budget", budget);
        } catch (Exception e) {
            return det;
        }
        return det;
    }

    public static HashMap<String, String> getTaskDetails(int taskid) throws ClassNotFoundException, SQLException {
        HashMap<String, String> det = new HashMap<>();
        try {
            det = DBManager.GetTableData(Table, "where " + TaskID + " = " + taskid);
            int createdById = Integer.parseInt(det.get(CreatedBy));
            int createdForId = Integer.parseInt(det.get(CreatedFor));
            int parentid = DBManager.GetInt(Tables.TaskJoin.ItemOneID, Tables.TaskJoin.Table, "where " + Tables.TaskJoin.ItemOneType + " = 'Task' and " + Tables.TaskJoin.ItemTwoType + " = 'Task' and " + Tables.TaskJoin.ItemTwoID + " = " + taskid);
            int timeBudget = Integer.parseInt(det.get(TimeBudjet));
            int duration = TimesheetManager.calculateTotalUserTaskTime(0, taskid);
            String timeStarted = det.get(TimeStarted);
            String status = det.get(Status);
            String dateStarted = det.get(Tables.Tasks.DateStarted);
            dateStarted = DateUtil.readDate(dateStarted);
            det.put(Tables.Tasks.DateStarted, dateStarted);

            String fullname = UserManager.getUserName(createdById);
            String forname = UserManager.getUserName(createdForId);
            String parentname = "N/A";
            if (parentid > 0) {
                parentname = DBManager.GetString(TaskTopic, Table, "where " + TaskID + " = " + parentid);
            }
            String budget = DateUtil.ConvertMinutesToHours(timeBudget);
            String textDuration = DateUtil.ConvertMinutesToHours(duration);

            det.put("created-by", fullname);
            det.put("createdFor", forname);
            det.put("parent_id", "" + parentid);
            det.put("parent_name", parentname);
            det.put("time_started", DateUtil.readTime(timeStarted));
            det.put("budget", budget);
            det.put("text_duration", textDuration);
        } catch (Exception e) {
            return det;
        }
        return det;
    }

    public static String getTaskTopicByID(int taskid) throws ClassNotFoundException, SQLException {
        String Topic = DBManager.GetString(TaskTopic, Table, "where " + TaskID + " = " + taskid);
        return Topic;
    }

    public static ArrayList<Integer> getSubTasks(int taskID) throws ClassNotFoundException, SQLException {
        ArrayList<Integer> subs = DBManager.GetIntArrayList(Tables.TaskJoin.ItemTwoID, Tables.TaskJoin.Table, "where " + Tables.TaskJoin.ItemOneType + " = 'Task' and " + Tables.TaskJoin.ItemTwoType + " = 'Task' and " + Tables.TaskJoin.ItemOneID + " = " + taskID);
        return subs;
    }

    public static String deleteTask(int taskID, int userID) throws ClassNotFoundException, SQLException {
        String Topic = DBManager.GetString(TaskTopic, Table, "where " + TaskID + " = " + taskID);

//        ArrayList<Integer> subs = getSubTasks(taskID);
//        subs.add(taskID);
//
//        if (!subs.isEmpty()) {
//            for (int taskid : subs) {
//                DBManager.DeleteObject(Tables.TaskJoin.Table, "where " + Tables.TaskJoin.ItemOneType + " = 'Task' and " + Tables.TaskJoin.ItemOneID + " = " + taskid);
//                DBManager.DeleteObject(Tables.TaskJoin.Table, "where " + Tables.TaskJoin.ItemTwoType + " = 'Task' and " + Tables.TaskJoin.ItemTwoID + " = " + taskid);
//                DBManager.DeleteObject(Table, "where " + TaskID + " = " + taskid);
//            }
//        }
        DBManager.DeleteObject(Tables.TaskJoin.Table, "where " + Tables.TaskJoin.ItemOneType + " = 'User' and " + Tables.TaskJoin.ItemOneID + " = " + userID + " and " + Tables.TaskJoin.ItemTwoType + " = 'Task' and " + Tables.TaskJoin.ItemTwoID + " = " + taskID);

        String name = "Someone";
        if (userID != 0) {
            name = UserManager.getUserName(userID);
        }
        String comment = name + " deleted task '" + Topic + "'";
        String result = TimesheetManager.LogTimesheet(taskID, userID, "Task Deleted", comment, "", 0, 0);
        return result;
    }

    public static String updateTaskStatus(int taskID, int UserID, String status, int sessionID) throws ClassNotFoundException, SQLException {
        String result = DBManager.UpdateStringData(Table, Status, status, "where " + TaskID + " = " + taskID);

        String def = "started working on";
        String act = "Started";
        switch (status) {
            case "done":
                def = "finished";
                act = "Completed";
                DBManager.UpdateCurrentTime(Table, TimeFinished, "where " + TaskID + " = " + taskID);
                DBManager.UpdateCurrentDate(Table, DateFinished, "where " + TaskID + " = " + taskID);
                break;
            case "todo":
                def = "stopped working on";
                act = "Paused";
                DBManager.UpdateCurrentTime(Table, TimeFinished, "where " + TaskID + " = " + taskID);
                DBManager.UpdateCurrentDate(Table, DateFinished, "where " + TaskID + " = " + taskID);
                break;
            case "doing":
                DBManager.UpdateCurrentTime(Table, TimeStarted, "where " + TaskID + " = " + taskID);
                DBManager.UpdateCurrentDate(Table, DateStarted, "where " + TaskID + " = " + taskID);
                sessionID = 0;
                break;
        }
        String Topic = DBManager.GetString(TaskTopic, Table, "where " + TaskID + " = " + taskID);
        String name = UserManager.getUserName(UserID);
        String comment = name + " " + def + " '" + Topic + "'";
        TimesheetManager.LogTimesheet(taskID, UserID, "Task " + act, comment, "", 0, sessionID);
        if (!status.equals("doing")) {
            updateDuration(taskID);
        }
        return result;
    }

    public static String breakTask(int taskID, int UserID, String breakText, int sessionID) throws ClassNotFoundException, SQLException {
        String result = DBManager.UpdateStringData(Table, Status, "paused", "where " + TaskID + " = " + taskID);

        String def = "took a '" + breakText + "'";
        DBManager.UpdateCurrentTime(Table, TimeFinished, "where " + TaskID + " = " + taskID);
        DBManager.UpdateCurrentDate(Table, DateFinished, "where " + TaskID + " = " + taskID);

        String Topic = DBManager.GetString(TaskTopic, Table, "where " + TaskID + " = " + taskID);
        String name = UserManager.getUserName(UserID);
        String comment = name + " stopped working on '" + Topic + "'";
        TimesheetManager.LogTimesheet(taskID, UserID, "Break Started", name + " " + def, "", 0, 0);
        TimesheetManager.LogTimesheet(taskID, UserID, "Task Paused", comment, "", 0, sessionID);
        updateDuration(taskID);
        return result;
    }

    public static String endBreak(int taskID, int UserID, String breakText) throws ClassNotFoundException, SQLException {
        String name = UserManager.getUserName(UserID);
        String def = "finished '" + breakText + "'";
        DBManager.UpdateStringData(Table, Status, "todo", "where " + TaskID + " = " + taskID);
        int sessionID = DBManager.GetInt(Tables.Timesheet.SessionID, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + UserID + " and " + Tables.Timesheet.TaskID + " = " + taskID + " and " + Tables.Timesheet.Topic + " = 'Break Started'");
        String result = TimesheetManager.LogTimesheet(taskID, UserID, "Break Finished", name + " " + def, "", 0, sessionID);
        return result;
    }

    public static String resumeTask(int taskID, int UserID, String breakText) throws ClassNotFoundException, SQLException {
        String result = DBManager.UpdateStringData(Table, Status, "doing", "where " + TaskID + " = " + taskID);

        DBManager.UpdateCurrentTime(Table, TimeStarted, "where " + TaskID + " = " + taskID);
        DBManager.UpdateCurrentDate(Table, DateStarted, "where " + TaskID + " = " + taskID);

        String Topic = DBManager.GetString(TaskTopic, Table, "where " + TaskID + " = " + taskID);
        String name = UserManager.getUserName(UserID);
        String comment = name + " started working on '" + Topic + "'";
        String def = "finished '" + breakText + "'";
        int sessionID = DBManager.GetInt(Tables.Timesheet.SessionID, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + UserID + " and " + Tables.Timesheet.TaskID + " = " + taskID + " and " + Tables.Timesheet.Topic + " = 'Break Started'");
        TimesheetManager.LogTimesheet(taskID, UserID, "Break Finished", name + " " + def, "", 0, sessionID);
        TimesheetManager.LogTimesheet(taskID, UserID, "Task Started", comment, "", 0, 0);
        return result;
    }

    public static String updateDuration(int taskID) throws ClassNotFoundException, SQLException {
        Date start_date = DBManager.GetDate(DateStarted, Table, "where " + TaskID + " = " + taskID);
        Time start_time = DBManager.GetTime(TimeStarted, Table, "where " + TaskID + " = " + taskID);
        Date end_date = DBManager.GetDate(DateFinished, Table, "where " + TaskID + " = " + taskID);
        Time end_time = DBManager.GetTime(TimeFinished, Table, "where " + TaskID + " = " + taskID);

        int minutesDiff = DateUtil.minutesDiff(start_date, end_date);
        int timeDiff = DateUtil.minutesDiff(start_time, end_time);

        int totalDiff = minutesDiff + timeDiff;
        int currentDuration = DBManager.GetInt(Duration, Table, "where " + TaskID + " = " + taskID);
        int newDuration = currentDuration + totalDiff;

        String result = DBManager.UpdateIntData(Duration, newDuration, Table, "where " + TaskID + " = " + taskID);
        return result;
    }

    public static String AddComment(int userid, int parentid, String comment, String parentType) throws ClassNotFoundException, SQLException {
        HashMap<String, Object> data = new HashMap<>();
        data.put(Tables.Comments.SenderID, userid);
        data.put(Tables.Comments.Comment, comment);
        int commentID = DBManager.insertTableDataReturnID(Tables.Comments.Table, data, "");
        DBManager.UpdateCurrentDate(Tables.Comments.Table, Tables.Comments.Date, "where " + Tables.Comments.ID + " = " + commentID);
        DBManager.UpdateCurrentTime(Tables.Comments.Table, Tables.Comments.Time, "where " + Tables.Comments.ID + " = " + commentID);
        String result = DBManager.LinkObjects(Tables.TaskJoin.Table, parentType, parentid, "Comment", commentID);
        return result;
    }

    public static String AddJournal(int userid, int parentid, String text) throws ClassNotFoundException, SQLException {
        HashMap<String, Object> data = new HashMap<>();
        data.put(Tables.Journals.SenderID, userid);
        data.put(Tables.Journals.Text, text);
        int journalID = DBManager.insertTableDataReturnID(Tables.Journals.Table, data, "");
        DBManager.UpdateCurrentDate(Tables.Journals.Table, Tables.Journals.Date, "where " + Tables.Journals.ID + " = " + journalID);
        DBManager.UpdateCurrentTime(Tables.Journals.Table, Tables.Journals.Time, "where " + Tables.Journals.ID + " = " + journalID);
        String result = DBManager.LinkObjects(Tables.TaskJoin.Table, "Task", parentid, "Journal", journalID);
        return result;
    }

    public static HashMap<Integer, HashMap<String, String>> getComments(int parid, String parType) throws ClassNotFoundException, SQLException {
        HashMap<Integer, HashMap<String, String>> comments = new HashMap<>();
        ArrayList<Integer> commentIDs = DBManager.GetIntArrayList(Tables.TaskJoin.ItemTwoID, Tables.TaskJoin.Table, "where " + Tables.TaskJoin.ItemOneType + " = '" + parType + "' and " + Tables.TaskJoin.ItemTwoType + " = 'Comment' and " + Tables.TaskJoin.ItemOneID + " = " + parid);
        if (!commentIDs.isEmpty()) {
            for (int id : commentIDs) {
                HashMap<String, String> det = DBManager.GetTableData(Tables.Comments.Table, "where " + Tables.Comments.ID + " = " + id);
                int userId = Integer.parseInt(det.get(Tables.Comments.SenderID));
                String fullname = UserManager.getUserName(userId);
                String time = det.get("time");
                time = DateUtil.readTime(time);
                String date = det.get("date");
                date = DateUtil.readDate(date);
                det.put("user_name", fullname);
                det.put("date", date);
                det.put("time", time);
                ArrayList<Integer> repcommentIDs = DBManager.GetIntArrayList(Tables.TaskJoin.ID, Tables.TaskJoin.Table, "where " + Tables.TaskJoin.ItemOneType + " = 'Comment' and " + Tables.TaskJoin.ItemTwoType + " = 'Comment' and " + Tables.TaskJoin.ItemOneID + " = " + id);
                int replies = repcommentIDs.size();
                det.put("replies", "" + replies);
                comments.put(id, det);
            }
        }
        return comments;
    }

    public static HashMap<Integer, HashMap<String, String>> getJournals(int parid) throws ClassNotFoundException, SQLException {
        HashMap<Integer, HashMap<String, String>> journals = new HashMap<>();
        ArrayList<Integer> journalIDs = DBManager.GetIntArrayList(Tables.TaskJoin.ItemTwoID, Tables.TaskJoin.Table, "where " + Tables.TaskJoin.ItemOneType + " = 'Task' and " + Tables.TaskJoin.ItemTwoType + " = 'Journal' and " + Tables.TaskJoin.ItemOneID + " = " + parid);
        if (!journalIDs.isEmpty()) {
            for (int id : journalIDs) {
                HashMap<String, String> det = DBManager.GetTableData(Tables.Journals.Table, "where " + Tables.Journals.ID + " = " + id);
                int userId = Integer.parseInt(det.get(Tables.Journals.SenderID));
                String fullname = UserManager.getUserName(userId);
                String time = det.get("time");
                time = DateUtil.readTime(time);
                String date = det.get("date");
                date = DateUtil.readDate(date);
                det.put("user_name", fullname);
                det.put(Tables.Journals.Date, date);
                det.put(Tables.Journals.Time, time);
                journals.put(id, det);
            }
        }
        return journals;
    }
}
