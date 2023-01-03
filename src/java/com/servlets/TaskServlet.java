/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlets;

import com.classes.TaskManager;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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
public class TaskServlet extends HttpServlet {

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
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String json = "";
            String type = request.getParameter("type").trim();
            String empty = "none";

            switch (type) {
                case "GetUserTasks": {
                    String userid = request.getParameter("id");
                    String objtype = request.getParameter("objtype");
                    String status = request.getParameter("status");
                    String count = request.getParameter("count");
                    int start = Integer.parseInt(count);
                    int id = Integer.parseInt(userid);
                    int limit = 20;
                    HashMap<Integer, HashMap<String, String>> tasks = TaskManager.getUserTasks(id, objtype, status, start, limit);
                    int counter = start+tasks.size();
                    if (!tasks.isEmpty()) {
                        String json1 = new Gson().toJson(counter);
                        String json2 = new Gson().toJson(tasks);
                        json = "[" + json1 + "," + json2 + "]";
                    } else {
                        json = new Gson().toJson(empty);
                    }
                    break;
                }
                case "GetUserCurrentTask": {
                    String userid = request.getParameter("id");
                    String objtype = request.getParameter("objtype");
                    int id = Integer.parseInt(userid);
                    HashMap<String, String> taskdetails = TaskManager.getCurrentTask(id);
                    if (!taskdetails.isEmpty()) {
                        json = new Gson().toJson(taskdetails);
                    } else {
                        json = new Gson().toJson(empty);
                    }
                    break;
                }
                case "GetTaskDetails": {
                    String taskid = request.getParameter("id");
                    int id = Integer.parseInt(taskid);
                    HashMap<String, String> taskdetails = TaskManager.getTaskDetails(id);
                    if (!taskdetails.isEmpty()) {
                        json = new Gson().toJson(taskdetails);
                    } else {
                        json = new Gson().toJson(empty);
                    }
                    break;
                }
                case "CreateTask": {
                    String userid = request.getParameter("userid");
                    String foruserid = request.getParameter("foruserid");
                    String budget = request.getParameter("budget");
                    String parentid = request.getParameter("parentid");
                    String topic = request.getParameter("topic");
                    String description = request.getParameter("description");
                    String task_type = request.getParameter("TaskType");
                    String obj_type = request.getParameter("objType");

                    int ForUserID = Integer.parseInt(foruserid);
                    int UserID = Integer.parseInt(userid);
                    int Budget = Integer.parseInt(budget);
                    int ParentID = Integer.parseInt(parentid);

                    String result = TaskManager.CreateTask(topic, description, ForUserID, UserID, Budget, ParentID, task_type, obj_type);
                    json = new Gson().toJson(result);
                    break;
                }
                case "addUserTask": {
                    String userid = request.getParameter("userid");
                    String foruserid = request.getParameter("foruserid");
                    String taskid = request.getParameter("taskid");

                    int ForUserID = Integer.parseInt(foruserid);
                    int UserID = Integer.parseInt(userid);
                    int TaskID = Integer.parseInt(taskid);
                    
                    HashMap<String, String> details = DBManager.GetTableData(Tables.Tasks.Table, "where "+Tables.Tasks.ID+" = "+TaskID);
                    int Budget = Integer.parseInt(details.get(Tables.Tasks.TimeBudjet));
                    String topic = details.get(Tables.Tasks.TaskTopic);
                    String description = details.get(Tables.Tasks.Description);
                    String task_type = details.get(Tables.Tasks.TaskType);
                    
                    String result = TaskManager.CreateTask(topic, description, ForUserID, UserID, Budget, 0, task_type, "User");
                    json = new Gson().toJson(result);
                    break;
                }
                case "DeleteTask": {
                    String userid = request.getParameter("userid");
                    String taskid = request.getParameter("taskid");

                    int TaskID = Integer.parseInt(taskid);
                    int UserID = Integer.parseInt(userid);

                    String result = TaskManager.deleteTask(TaskID, UserID);
                    json = new Gson().toJson(result);
                    break;
                }
                case "commentOnTask": {
                    String userid = request.getParameter("userid");
                    String parentid = request.getParameter("parentid");
                    String parentType = request.getParameter("parenttype");
                    String comment = request.getParameter("comment");

                    int UserID = Integer.parseInt(userid);
                    int parID = Integer.parseInt(parentid);
                    String result = TaskManager.AddComment(UserID, parID, comment, parentType);
                    json = new Gson().toJson(result);
                    break;
                }
                case "AddTaskJournal": {
                    String userid = request.getParameter("userid");
                    String taskid = request.getParameter("id");
                    String text = request.getParameter("text");

                    int UserID = Integer.parseInt(userid);
                    int taskID = Integer.parseInt(taskid);
                    String result = TaskManager.AddJournal(UserID, taskID, text);
                    json = new Gson().toJson(result);
                    break;
                }
                case "getComments": {
                    String parid = request.getParameter("parid");
                    String partype = request.getParameter("partype");
                    int parID = Integer.parseInt(parid);
                    HashMap<Integer, HashMap<String, String>> comments = TaskManager.getComments(parID, partype);
                    if (!comments.isEmpty()) {
                        json = new Gson().toJson(comments);
                    } else {
                        json = new Gson().toJson(empty);
                    }
                    break;
                }
                case "getJournals": {
                    String taskid = request.getParameter("id");
                    int taskID = Integer.parseInt(taskid);
                    HashMap<Integer, HashMap<String, String>> comments = TaskManager.getJournals(taskID);
                    if (!comments.isEmpty()) {
                        json = new Gson().toJson(comments);
                    } else {
                        json = new Gson().toJson(empty);
                    }
                    break;
                }
                case "UpdateTaskStatus": {
                    String taskid = request.getParameter("id");
                    String userid = request.getParameter("userid");
                    String status = request.getParameter("status");
                    String session = request.getParameter("session");
                    int sessionID = Integer.parseInt(session);
                    int id = Integer.parseInt(taskid);
                    int usid = Integer.parseInt(userid);
                    if (status.equals("doing")) {
                        int curr_doing = DBManager.GetInt(Tables.Tasks.ID, Tables.Tasks.Table, "where " + Tables.Tasks.Status + " = 'doing' and " + Tables.Tasks.CreatedFor + " = " + usid +" or (" + Tables.Tasks.Status + " = 'paused' and " + Tables.Tasks.CreatedFor + " = " + usid +")");
                        if (curr_doing != 0) {
                            String stat = DBManager.GetString(Tables.Tasks.Status, Tables.Tasks.Table, "where " + Tables.Tasks.ID + " = " + curr_doing);
                            if (stat.equals("paused")) {
                                TaskManager.endBreak(curr_doing, usid, "Break");
                            } else {
                                TaskManager.updateTaskStatus(curr_doing, usid, "todo", sessionID);
                            }
                        }
                    }
                    String result = TaskManager.updateTaskStatus(id, usid, status, sessionID);
                    json = new Gson().toJson(result);
                    break;
                }
                case "Break": {
                    String taskid = request.getParameter("id");
                    String userid = request.getParameter("userid");
                    String breakText = request.getParameter("breakTxt");
                    String status = request.getParameter("status");
                    String session = request.getParameter("session");
                    int sessionID = Integer.parseInt(session);
                    int id = Integer.parseInt(taskid);
                    int usid = Integer.parseInt(userid);
                    String result = "";

                    if (status.equals("end")) {
                        result = TaskManager.resumeTask(id, usid, breakText);
                    } else {
                        result = TaskManager.breakTask(id, usid, breakText, sessionID);
                    }

                    json = new Gson().toJson(result);
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
            Logger.getLogger(TaskServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TaskServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TaskServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TaskServlet.class.getName()).log(Level.SEVERE, null, ex);
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
