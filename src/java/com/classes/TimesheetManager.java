/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.classes;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.engine.*;

/**
 *
 * @author Stephen
 */
public class TimesheetManager {

    public static HashMap<String, Long> totalTimes = new HashMap<>();
    public static HashMap<String, Integer> TaskSessions = new HashMap<>();
    public static HashMap<String, Object> DateSummary = new HashMap<>();
    public static int counter = 0;

    public TimesheetManager() {

    }

    public static String LogTimesheet(int taskID, int userID, String topic, String comment, String otherObject, int otherObjID, int ses) throws ClassNotFoundException, SQLException {
        HashMap<String, Object> tableData = new HashMap<>();
        if (ses == 0) {
            ses = getSessionID();
        }
        tableData.put(Tables.Timesheet.TaskID, taskID);
        tableData.put(Tables.Timesheet.UserID, userID);
        tableData.put(Tables.Timesheet.SessionID, ses);
        tableData.put(Tables.Timesheet.Topic, topic);
        tableData.put(Tables.Timesheet.Comment, comment);
        tableData.put(Tables.Timesheet.OtherObjectType, otherObject);
        tableData.put(Tables.Timesheet.OtherObjectID, otherObjID);
        int timesheetID = DBManager.insertTableDataReturnID(Tables.Timesheet.Table, tableData, "");
        DBManager.UpdateCurrentDate(Tables.Timesheet.Table, Tables.Timesheet.Date, "where " + Tables.Timesheet.ID + " = " + timesheetID);
        String result = DBManager.UpdateCurrentTime(Tables.Timesheet.Table, Tables.Timesheet.Time, "where " + Tables.Timesheet.ID + " = " + timesheetID);
        return result;
    }

    public static int getSessionID() throws SQLException, ClassNotFoundException {
        int sessionID = 0;
        int exists = 1;
        while (exists > 0) {
            sessionID = Utilities.RandomNumber(999999, 0);
            exists = DBManager.GetInt(Tables.Timesheet.ID, Tables.Timesheet.Table, "where " + Tables.Timesheet.SessionID + " = " + sessionID);
        }
        return sessionID;
    }

    public static ArrayList<String> getTimesheetDates(int objid, String objtype, String filter) throws ClassNotFoundException, SQLException {
        ArrayList<String> dates = new ArrayList<>();
        if (objtype.equalsIgnoreCase("User")) {
            dates = DBManager.GetStringArrayList(Tables.Timesheet.Date, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + objid);
        } else if (objtype.equalsIgnoreCase("Team")) {
            ArrayList<Integer> userids = TeamManager.getTeamMembersIDs(objid);
            if (!userids.isEmpty()) {
                for (int id : userids) {
                    dates.addAll(DBManager.GetStringArrayList(Tables.Timesheet.Date, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + id));
                }
            }
        }
        dates = Utilities.removeDuplicatesStringArrayList(dates);
        Collections.sort(dates);
        return dates;
    }

    public static HashMap<Integer, HashMap<String, String>> getTImesheetEntriesByDate(String date) throws ClassNotFoundException, SQLException {
        HashMap<Integer, HashMap<String, String>> entries = new HashMap<>();
        ArrayList<Integer> ids = DBManager.GetIntArrayList(Tables.Timesheet.ID, Tables.Timesheet.Table, "where " + Tables.Timesheet.Date + " = '" + date + "'");
        if (!ids.isEmpty()) {
            for (int id : ids) {
                HashMap<String, String> entry = DBManager.GetTableData(Tables.Timesheet.Table, "where " + Tables.Timesheet.ID + " = " + id);
                entries.put(id, entry);
            }
        }
        return entries;
    }

    public static HashMap<Integer, Object> getUserTImesheetEntriesByDate(String date, int objid, String objtype) throws ClassNotFoundException, SQLException {
        HashMap<Integer, Object> entries = new HashMap<>();
        ArrayList<Integer> ids = new ArrayList<>();
        if (objtype.equalsIgnoreCase("User")) {
            ids = DBManager.GetIntArrayList(Tables.Timesheet.ID, Tables.Timesheet.Table, "where " + Tables.Timesheet.Date + " = '" + date + "' and " + Tables.Timesheet.UserID + " = " + objid);
        } else if (objtype.equalsIgnoreCase("Team")) {
            ArrayList<Integer> userids = TeamManager.getTeamMembersIDs(objid);
            if (!userids.isEmpty()) {
                for (int id : userids) {
                    ids.addAll(DBManager.GetIntArrayList(Tables.Timesheet.ID, Tables.Timesheet.Table, "where " + Tables.Timesheet.Date + " = '" + date + "' and " + Tables.Timesheet.UserID + " = " + id));
                }
            }
        }

        if (!ids.isEmpty()) {
            for (int id : ids) {
                HashMap<String, String> entry = DBManager.GetTableData(Tables.Timesheet.Table, "where " + Tables.Timesheet.ID + " = " + id);
                entries.put(id, entry);
            }
        }
        entries = Utilities.sortHashMapByIntegerKeys(entries);
        return entries;
    }

    public static HashMap<String, Object> getUserReport(int objid, String objType, String dateStart, String dateEnd, int start, int limit) throws ClassNotFoundException, SQLException, ParseException {
        HashMap<String, Object> FinalReports = new HashMap<>();
        totalTimes.clear();
        Date curdate = new Date();
        String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(curdate);
        LocalDate today = LocalDate.parse(modifiedDate);

        LocalDate start_date;
        if (dateStart.equals("today")) {
            start_date = today;
        } else {
            start_date = LocalDate.parse(dateStart);
        }

        LocalDate end_date;
        if (dateEnd.equals("today")) {
            end_date = today;
        } else {
            end_date = LocalDate.parse(dateEnd);
        }

        ArrayList<String> totalDates = new ArrayList<>();
        while (!start_date.isAfter(end_date)) {
            totalDates.add(start_date.toString());
            start_date = start_date.plusDays(1);
        }
        int req = start + limit;
        int total = totalDates.size();
        if (req > total) {
            limit = total - start;
        }
        int end = limit + start;
        if (limit > 0) {
            List<String> newList = new ArrayList<>(totalDates.subList(start, end));
            if (req > total) {
                counter = 0;
            } else {
                counter = start + newList.size();
            }
            if (!newList.isEmpty()) {
                for (String date : newList) {
                    ArrayList<Integer> ids = new ArrayList<>();
                    HashMap<String, HashMap<String, String>> reports = new HashMap<>();
                    HashMap<String, String> noreport = new HashMap<>();
                    totalTimes.clear();
                    TaskSessions.clear();
                    if (objType.equalsIgnoreCase("Team")) {
                        ArrayList<Integer> users = TeamManager.getTeamMemberIDs(objid);
                        if (!users.isEmpty()) {
                            for (int user : users) {
                                ids.addAll(DBManager.GetIntArrayList(Tables.Timesheet.ID, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + user + " and " + Tables.Timesheet.Date + " = '" + date + "' and " + Tables.Timesheet.Topic + " = 'Task Started' or (" + Tables.Timesheet.Topic + " = 'Break Started' and " + Tables.Timesheet.UserID + " = " + user + " and " + Tables.Timesheet.Date + " = '" + date + "')"));
                            }
                        }
                    } else {
                        ids = DBManager.GetIntArrayList(Tables.Timesheet.ID, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + objid + " and " + Tables.Timesheet.Date + " = '" + date + "' and " + Tables.Timesheet.Topic + " = 'Task Started' or (" + Tables.Timesheet.Topic + " = 'Break Started' and " + Tables.Timesheet.UserID + " = " + objid + " and " + Tables.Timesheet.Date + " = '" + date + "')");
                    }

                    if (!ids.isEmpty()) {
                        for (int id : ids) {
                            try {
                                HashMap<String, String> report = new HashMap<>();
                                HashMap<String, String> timesheet_data = DBManager.GetTableData(Tables.Timesheet.Table, "where " + Tables.Timesheet.ID + " = " + id);
                                String task = timesheet_data.get(Tables.Timesheet.TaskID);
                                int taskid = Integer.parseInt(task);
                                String user = timesheet_data.get(Tables.Timesheet.UserID);
                                int userid = Integer.parseInt(user);
                                String session = timesheet_data.get(Tables.Timesheet.SessionID);
                                int sessionid = Integer.parseInt(session);
                                HashMap<String, String> task_data = DBManager.GetTableData(Tables.Tasks.Table, "where " + Tables.Tasks.ID + " = " + taskid);

                                String taskTopic = "";
                                taskTopic = task_data.get(Tables.Tasks.TaskTopic);
                                Date endDate = DBManager.GetDate(Tables.Timesheet.Date, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + userid + " and " + Tables.Timesheet.Topic + " = 'Task Paused' and " + Tables.Timesheet.SessionID + " = " + sessionid);
                                String endTime = DBManager.GetString(Tables.Timesheet.Time, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + userid + " and " + Tables.Timesheet.Topic + " = 'Task Paused' and " + Tables.Timesheet.SessionID + " = " + sessionid);
                                if (endTime.equals("none")) {
                                    endTime = DBManager.GetString(Tables.Timesheet.Time, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + userid + " and " + Tables.Timesheet.Topic + " = 'Task Completed' and " + Tables.Timesheet.SessionID + " = " + sessionid);
                                    endDate = DBManager.GetDate(Tables.Timesheet.Date, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + userid + " and " + Tables.Timesheet.Topic + " = 'Task Completed' and " + Tables.Timesheet.SessionID + " = " + sessionid);
                                }

                                String reportTopic = timesheet_data.get(Tables.Timesheet.Topic);
                                if (reportTopic.equalsIgnoreCase("Break Started")) {
                                    taskTopic = "Break";
                                    endTime = DBManager.GetString(Tables.Timesheet.Time, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + userid + " and " + Tables.Timesheet.Topic + " = 'Break Finished' and " + Tables.Timesheet.SessionID + " = " + sessionid);
                                    endDate = DBManager.GetDate(Tables.Timesheet.Date, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + userid + " and " + Tables.Timesheet.Topic + " = 'Break Finished' and " + Tables.Timesheet.SessionID + " = " + sessionid);
                                }

//                        String taskTotalTime = "N/A";
//                        String cmt = timesheet_data.get(Tables.Timesheet.Comment);
//                        Pattern p = Pattern.compile("(?:^|\\s)'([^']*?)'(?:$|\\s)", Pattern.MULTILINE);
//                        Matcher m = p.matcher(cmt);
//                        if (m.find()) {
//                            String str = m.group();
//                            taskTopic = str.replaceAll("\'", "");
//                        }
                                long totTime = 0;
                                int sessionCount = 1;
                                String startTime = timesheet_data.get(Tables.Timesheet.Time);
                                String startDate = timesheet_data.get(Tables.Timesheet.Date);
                                String duration = "N/A";
                                if (!endTime.equals("none") && !endTime.equals(null)) {
                                    Timestamp start_timestamp = Timestamp.valueOf(startDate + " " + startTime);
                                    Timestamp end_timestamp = Timestamp.valueOf("" + endDate + " " + endTime);
                                    java.sql.Timestamp stTime = DateUtil.getTimestamp(start_timestamp);
                                    java.sql.Timestamp ndTime = DateUtil.getTimestamp(end_timestamp);
                                    if (stTime != null && ndTime != null) {
                                        duration = "" + DateUtil.calculateTimeDifference(stTime, ndTime);
                                        totTime = DateUtil.calculateTimeDifferenceReturnMilliseconds(stTime, ndTime);
                                        if (totalTimes.containsKey(taskTopic)) {
                                            long oldtot = totalTimes.get(taskTopic);
                                            totTime = oldtot + totTime;
                                            int oldSessCount = TaskSessions.get(taskTopic);
                                            sessionCount = oldSessCount + 1;
                                        }
                                    }
                                    endTime = DateUtil.readTime(endTime);
                                } else {
                                    endTime = "N/A";
                                }
                                totalTimes.put(taskTopic, totTime);
                                TaskSessions.put(taskTopic, sessionCount);

                                startTime = DateUtil.readTime(startTime);
                                report.put("task-id", "" + taskid);
                                report.put("task-name", taskTopic);
                                report.put("task-comment", timesheet_data.get(Tables.Timesheet.Comment));
                                report.put("start-time", startTime);
                                report.put("end-time", endTime);
                                report.put("duration", duration);
                                report.put("date", date);
                                report.put("status", task_data.get(Tables.Tasks.Status));
                                reports.put("" + id, report);
//                    String newdate = DateUtil.readDate(date);
                                FinalReports.put(date, reports);
                                DateSummary.putAll(getDateReportSummary(date));
                            } catch (Exception e) {
                                String error = e.getMessage();
                                String err = error;
                            }
                        }
                    }
                }
            }
        }
        FinalReports = Utilities.sortHashMapByDateKeysReadDate(FinalReports, "desc");
        return FinalReports;
    }

    public static int getCounter() {
        return counter;
    }

    public static HashMap<String, Object> getDateReportSummary(String date) throws ClassNotFoundException, SQLException {
        HashMap<Integer, HashMap<String, String>> sumDetails = new HashMap<>();
        HashMap<String, Object> summary = new HashMap<>();
        long totalMilli = 0;

        int count = 0;
        Set<String> keys = totalTimes.keySet();
        Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            count++;
            HashMap<String, String> det = new HashMap<>();
            String taskTopic = iter.next();
            long milli = totalTimes.get(taskTopic);
            totalMilli += milli;
            String times = DateUtil.ReadMilliseconds(milli);
            det.put("TaskTopic", taskTopic);
            det.put("TaskTime", times);
            det.put("TaskSessions", "" + TaskSessions.get(taskTopic));
            sumDetails.put(count, det);
        }
        HashMap<String, String> totals = new HashMap<>();
        String times = DateUtil.ReadMilliseconds(totalMilli);
        totals.put("total-time", times);
        totals.put("total-tasks", "" + count);
        sumDetails.put(0, totals);

        summary.put(date, sumDetails);
        return summary;
    }

    public static HashMap<String, Object> getUserReportSummary() throws ClassNotFoundException, SQLException {
        DateSummary = Utilities.sortHashMapByStringKeys(DateSummary);
        return DateSummary;
    }

    public static ArrayList<Integer> getUserTaskList(int objid, String objType) throws ClassNotFoundException, SQLException {
        ArrayList<Integer> tasks = new ArrayList<>();
        if (objType.equalsIgnoreCase("Team")) {
            ArrayList<Integer> users = TeamManager.getTeamMemberIDs(objid);
            if (!users.isEmpty()) {
                for (int user : users) {
//                    tasks.addAll(DBManager.GetIntArrayList(Tables.Tasks.ID, Tables.Tasks.Table, "ORDER BY " + Tables.Tasks.TaskTopic + " DESC"));
                    tasks.addAll(DBManager.GetIntArrayList(Tables.Tasks.ID, Tables.Tasks.Table, "where " + Tables.Tasks.CreatedFor + " = " + user + " ORDER BY " + Tables.Tasks.TaskTopic + " DESC"));
                }
            }
        } else {
//            tasks = DBManager.GetIntArrayList(Tables.Tasks.ID, Tables.Tasks.Table, "ORDER BY " + Tables.Tasks.TaskTopic + " DESC");
            tasks = DBManager.GetIntArrayList(Tables.Tasks.ID, Tables.Tasks.Table, "where " + Tables.Tasks.CreatedFor + " = " + objid + " ORDER BY " + Tables.Tasks.TaskTopic + " DESC");
        }
        return tasks;
    }

    public static HashMap<Integer, Object> getUserTaskReport(int objid, String objType, int taskid) throws ClassNotFoundException, SQLException {
        HashMap<Integer, Object> userrep = new HashMap<>();
        ArrayList<Integer> users = new ArrayList<>();
        if (objType.equalsIgnoreCase("Team")) {
            users.addAll(TeamManager.getTeamMemberIDs(objid));
        } else {
            users.add(objid);
        }

        if (!users.isEmpty()) {
            for (int usid : users) {
                HashMap<String, String> rep = new HashMap<>();
                int totSessions = calculateTotalUserTaskSessions(usid, taskid);
                if (totSessions > 0) {
                    int totDuration = calculateTotalUserTaskTime(usid, taskid);
                    String username = UserManager.getUserName(usid);
                    String textDuration = DateUtil.ConvertMinutesToHours(totDuration);
                    rep.put("user-name", username);
                    rep.put("total-duration", textDuration);
                    rep.put("total-sessions", "" + totSessions);
                    userrep.put(usid, rep);
                }
            }
        }
        return userrep;
    }

    public static HashMap<Integer, Object> getUserTaskReportDetails(int userid, int taskid) throws ClassNotFoundException, SQLException {
        HashMap<Integer, Object> data = new HashMap<>();
        String topic = DBManager.GetString(Tables.Tasks.TaskTopic, Tables.Tasks.Table, "where " + Tables.Tasks.ID + " = " + taskid);
        ArrayList<Integer> taskids = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();
        taskids = DBManager.GetIntArrayList(Tables.Tasks.ID, Tables.Tasks.Table, "where " + Tables.Tasks.TaskTopic + " = '" + topic + "'");

        totalTimes.clear();
        TaskSessions.clear();
        if (!taskids.isEmpty()) {
            for (int tid : taskids) {
                ids.addAll(DBManager.GetIntArrayList(Tables.Timesheet.ID, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + userid + " and " + Tables.Timesheet.TaskID + " = " + tid + " and " + Tables.Timesheet.Topic + " = 'Task Started'"));
            }
        }

        if (!ids.isEmpty()) {
            for (int id : ids) {
                try {
                    HashMap<String, String> report = new HashMap<>();
                    HashMap<String, String> timesheet_data = DBManager.GetTableData(Tables.Timesheet.Table, "where " + Tables.Timesheet.ID + " = " + id);
                    String task = timesheet_data.get(Tables.Timesheet.TaskID);
                    int tid = Integer.parseInt(task);
                    String user = timesheet_data.get(Tables.Timesheet.UserID);
                    int uid = Integer.parseInt(user);
                    String session = timesheet_data.get(Tables.Timesheet.SessionID);
                    int sessionid = Integer.parseInt(session);
                    HashMap<String, String> task_data = DBManager.GetTableData(Tables.Tasks.Table, "where " + Tables.Tasks.ID + " = " + tid);

                    String taskTopic = "";
                    taskTopic = task_data.get(Tables.Tasks.TaskTopic);
                    Date endDate = DBManager.GetDate(Tables.Timesheet.Date, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + uid + " and " + Tables.Timesheet.Topic + " = 'Task Paused' and " + Tables.Timesheet.SessionID + " = " + sessionid);
                    String endTime = DBManager.GetString(Tables.Timesheet.Time, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + uid + " and " + Tables.Timesheet.Topic + " = 'Task Paused' and " + Tables.Timesheet.SessionID + " = " + sessionid);
                    if (endTime.equals("none")) {
                        endTime = DBManager.GetString(Tables.Timesheet.Time, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + uid + " and " + Tables.Timesheet.Topic + " = 'Task Completed' and " + Tables.Timesheet.SessionID + " = " + sessionid);
                        endDate = DBManager.GetDate(Tables.Timesheet.Date, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + uid + " and " + Tables.Timesheet.Topic + " = 'Task Completed' and " + Tables.Timesheet.SessionID + " = " + sessionid);
                    }

                    String reportTopic = timesheet_data.get(Tables.Timesheet.Topic);
                    if (reportTopic.equalsIgnoreCase("Break Started")) {
                        taskTopic = "Break";
                        endTime = DBManager.GetString(Tables.Timesheet.Time, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + uid + " and " + Tables.Timesheet.Topic + " = 'Break Finished' and " + Tables.Timesheet.SessionID + " = " + sessionid);
                        endDate = DBManager.GetDate(Tables.Timesheet.Date, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + uid + " and " + Tables.Timesheet.Topic + " = 'Break Finished' and " + Tables.Timesheet.SessionID + " = " + sessionid);
                    }

                    long totTime = 0;
                    int sessionCount = 1;
                    String startTime = timesheet_data.get(Tables.Timesheet.Time);
                    String startDate = timesheet_data.get(Tables.Timesheet.Date);
                    String duration = "N/A";
                    if (!endTime.equals("none")) {
                        Timestamp start_timestamp = Timestamp.valueOf(startDate + " " + startTime);
                        Timestamp end_timestamp = Timestamp.valueOf("" + endDate + " " + endTime);
                        java.sql.Timestamp stTime = DateUtil.getTimestamp(start_timestamp);
                        java.sql.Timestamp ndTime = DateUtil.getTimestamp(end_timestamp);
                        if (stTime != null && ndTime != null) {
                            duration = "" + DateUtil.calculateTimeDifference(stTime, ndTime);
                            totTime = DateUtil.calculateTimeDifferenceReturnMilliseconds(stTime, ndTime);
                            if (totalTimes.containsKey(taskTopic)) {
                                long oldtot = totalTimes.get(taskTopic);
                                totTime = oldtot + totTime;
                                int oldSessCount = TaskSessions.get(taskTopic);
                                sessionCount = oldSessCount + 1;
                            }
                        }
                        endTime = DateUtil.readTime(endTime);
                    } else {
                        endTime = "N/A";
                    }
                    totalTimes.put(taskTopic, totTime);
                    TaskSessions.put(taskTopic, sessionCount);

                    startTime = DateUtil.readTime(startTime);
                    String date = DateUtil.readDate(startDate);
                    report.put("task-id", "" + tid);
                    report.put("task-name", taskTopic);
                    report.put("task-comment", timesheet_data.get(Tables.Timesheet.Comment));
                    report.put("start-time", startTime);
                    report.put("end-time", endTime);
                    report.put("duration", duration);
                    report.put("text-date", date);
                    report.put("status", task_data.get(Tables.Tasks.Status));
                    data.put(id, report);
                } catch (Exception e) {
                    String error = e.getMessage();
                    String err = error;
                }
            }
        }

        return data;
    }

    public static int calculateTotalUserTaskTime(int userid, int taskid) throws ClassNotFoundException, SQLException {
        int totalTime = 0;
        String topic = DBManager.GetString(Tables.Tasks.TaskTopic, Tables.Tasks.Table, "where " + Tables.Tasks.ID + " = " + taskid);
        ArrayList<Integer> taskids = new ArrayList<>();
        if (userid > 0) {
            taskids = DBManager.GetIntArrayList(Tables.Tasks.ID, Tables.Tasks.Table, "where " + Tables.Tasks.TaskTopic + " = '" + topic + "' and " + Tables.Tasks.CreatedFor + " = " + userid);
        } else {
            taskids = DBManager.GetIntArrayList(Tables.Tasks.ID, Tables.Tasks.Table, "where " + Tables.Tasks.TaskTopic + " = '" + topic + "'");
        }

        if (!taskids.isEmpty()) {
            for (int tid : taskids) {
                String duration = DBManager.GetString(Tables.Tasks.Duration, Tables.Tasks.Table, "where " + Tables.Tasks.ID + " = " + tid);
                int dur = Integer.parseInt(duration);
                totalTime += dur;
            }
        }
        return totalTime;
    }

    public static int calculateTotalUserTaskSessions(int userid, int taskid) throws ClassNotFoundException, SQLException {
        int totalSessions = 0;
        String topic = DBManager.GetString(Tables.Tasks.TaskTopic, Tables.Tasks.Table, "where " + Tables.Tasks.ID + " = " + taskid);
        ArrayList<Integer> taskids = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();
        taskids = DBManager.GetIntArrayList(Tables.Tasks.ID, Tables.Tasks.Table, "where " + Tables.Tasks.TaskTopic + " = '" + topic + "'");

        if (!taskids.isEmpty()) {
            for (int tid : taskids) {
//                ids.addAll(DBManager.GetIntArrayList(Tables.Timesheet.ID, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + userid + " and " + Tables.Timesheet.TaskID + " = " + tid));
                ids.addAll(DBManager.GetIntArrayList(Tables.Timesheet.ID, Tables.Timesheet.Table, "where " + Tables.Timesheet.UserID + " = " + userid + " and " + Tables.Timesheet.TaskID + " = " + tid + " and " + Tables.Timesheet.Topic + " = 'Task Started'"));
            }
        }
        totalSessions = ids.size();
        return totalSessions;
    }
}
