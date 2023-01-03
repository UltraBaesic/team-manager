/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlets;

import com.classes.TeamManager;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Stephen
 */
public class TeamServlet extends HttpServlet {

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
                case "GetTeams": {
                    HashMap<Integer, String> teams = TeamManager.getTeams();
                    json = new Gson().toJson(teams);
                    break;
                }
                case "GetMemberTeams": {
                    String userid = request.getParameter("id");
                    int id = Integer.parseInt(userid);
                    HashMap<Integer, String> teams = TeamManager.getMemberTeams(id);
                    json = new Gson().toJson(teams);
                    break;
                }
                case "GetTeamMembers": {
                    String teamid = request.getParameter("id");
                    int id = Integer.parseInt(teamid);
                    HashMap<Integer, String> teams = TeamManager.getTeamMembers(id);
                    int leader = TeamManager.getTeamLeader(id);
                    String json1 = new Gson().toJson(teams);
                    String json2 = new Gson().toJson(leader);
                    json = "["+json1+","+json2+"]";
                    break;
                }
                case "GetTeamMembersDetails": {
                    String teamid = request.getParameter("id");
                    int id = Integer.parseInt(teamid);
                    HashMap<Integer, HashMap<String, String>> teams = TeamManager.getTeamMembersDetails(id);
                    if(teams.isEmpty()){
                        json = new Gson().toJson("none");
                    } else {
                        json = new Gson().toJson(teams);
                    }                    
                    break;
                }
                case "GetTeamResources": {
                    String teamid = request.getParameter("id");
                    int id = Integer.parseInt(teamid);
                    HashMap<Integer, HashMap<String, String>> teams = TeamManager.getTeamResources(id);
                    json = new Gson().toJson(teams);
                    break;
                }
                case "GetTeamTasks": {
                    String userid = request.getParameter("id");
                    String usertype = request.getParameter("userType");
                    int id = Integer.parseInt(userid);
                    HashMap<Integer, String> teams = TeamManager.getTeamTasks(id, usertype);
                    json = new Gson().toJson(teams);
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
            Logger.getLogger(TeamServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TeamServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TeamServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TeamServlet.class.getName()).log(Level.SEVERE, null, ex);
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
