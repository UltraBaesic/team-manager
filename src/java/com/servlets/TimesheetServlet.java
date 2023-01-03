/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlets;

import com.classes.DateUtil;
import com.classes.TaskManager;
import com.classes.TimesheetManager;
import com.classes.Utilities;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.engine.*;

/**
 *
 * @author Stephen
 */
public class TimesheetServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException, ParseException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String json = "";
            String type = request.getParameter("type").trim();
            String empty = "none";

            switch (type) {
                case "GetUserTimesheet": {
                    String userid = request.getParameter("id");
                    String objtype = request.getParameter("objtype");
                    String filter = request.getParameter("filter");
                    String date = request.getParameter("date");
                    int id = Integer.parseInt(userid);

                    ArrayList<String> dates = TimesheetManager.getTimesheetDates(id, objtype, filter);
                    HashMap<String, Object> timesheet = new HashMap<>();

                    if (!dates.isEmpty()) {
                        for (String dt : dates) {
                            HashMap<Integer, Object> entry = TimesheetManager.getUserTImesheetEntriesByDate(dt, id, objtype);
                            String strdate = DateUtil.readDate(dt);
                            timesheet.put(strdate, entry);
                        }
                        timesheet = Utilities.sortHashMapByStringKeys(timesheet);
                        json = new Gson().toJson(timesheet);
                    } else {
                        json = new Gson().toJson(empty);
                    }
                    break;
                }
                case "GetUserReports": {
                    String userid = request.getParameter("id");
                    String objtype = request.getParameter("objtype");
                    String filter = request.getParameter("filter");
                    String dateStart = request.getParameter("dateStart");
                    String dateEnd = request.getParameter("dateEnd");
                    String count = request.getParameter("count");
                    int id = Integer.parseInt(userid);
                    int start = Integer.parseInt(count);
                    int limit = 20;

                    HashMap<String, Object> reports = TimesheetManager.getUserReport(id, objtype, dateStart, dateEnd, start, limit);
                    HashMap<String, Object> summary = TimesheetManager.getUserReportSummary();
                    int counter = TimesheetManager.getCounter();

                    if (!reports.isEmpty()) {
                        String json1 = new Gson().toJson(reports);
                        String json2 = new Gson().toJson(summary);
                        String json3 = new Gson().toJson(counter);
                        json = "[" + json1 + "," + json2 + "," + json3 + "]";
                    } else {
                        String json1 = new Gson().toJson(empty);
                        String json2 = new Gson().toJson(counter);
                        json = "[" + json1 + "," + json2 + "]";
                    }
                    break;
                }
                case "GetTaskReports": {
                    String userid = request.getParameter("id");
                    String objtype = request.getParameter("objtype");
                    String count = request.getParameter("count");
                    int start = Integer.parseInt(count);
                    int id = Integer.parseInt(userid);
                    int limit = 20;

                    ArrayList<Integer> tasks = TimesheetManager.getUserTaskList(id, objtype);
                    HashMap<Integer, HashMap<String, String>> taskDetails = new HashMap<>();
                    HashMap<Integer, Object> taskUserReports = new HashMap<>();
                    ArrayList<String> taskTopics = new ArrayList<>();
                    ArrayList<Integer> dups = new ArrayList<>();
                    int req = start + limit;
                    int total = tasks.size();
                    if (req > total) {
                        limit = total - start;
                    }
                    int end = start + limit;
                    if (limit > 0) {
                        tasks = new ArrayList<>(tasks.subList(start, end));
                        for (int taskid : tasks) {
                            String topic = DBManager.GetString(Tables.Tasks.TaskTopic, Tables.Tasks.Table, "where " + Tables.Tasks.ID + " = " + taskid);
                            if (taskTopics.contains(topic.toUpperCase().trim())) {
//                            tasks.remove(taskid);
                                dups.add(taskid);
                            } else {
                                taskTopics.add(topic.toUpperCase().trim());
                                HashMap<String, String> details = TaskManager.getTaskDetails(taskid);
                                HashMap<Integer, Object> usertaskRep = TimesheetManager.getUserTaskReport(id, objtype, taskid);
                                taskDetails.put(taskid, details);
                                taskUserReports.put(taskid, usertaskRep);
                            }
                        }
                    } else {
                        tasks.clear();
                    }
                    tasks.removeAll(dups);
                    int counter = start + tasks.size();

                    if (!tasks.isEmpty()) {
                        String json1 = new Gson().toJson(tasks);
                        String json2 = new Gson().toJson(taskDetails);
                        String json3 = new Gson().toJson(taskUserReports);
                        String json4 = new Gson().toJson(counter);
                        json = "[" + json1 + "," + json2 + "," + json3 + "," + json4 + "]";
                    } else {
                        json = new Gson().toJson(empty);
                    }
                    break;
                }
                case "GetUserTaskReportDetails": {
                    String userid = request.getParameter("userid");
                    String taskid = request.getParameter("taskid");
                    int id = Integer.parseInt(userid);
                    int tid = Integer.parseInt(taskid);
                    HashMap<Integer, Object> data = TimesheetManager.getUserTaskReportDetails(id, tid);
                    if (!data.isEmpty()) {
                        json = new Gson().toJson(data);
                    } else {
                        json = new Gson().toJson(empty);
                    }
                    break;
                }
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TimesheetServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TimesheetServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(TimesheetServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TimesheetServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TimesheetServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(TimesheetServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
