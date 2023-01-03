/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlets;

import com.classes.UserManager;
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
public class DialogServlet extends HttpServlet {

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
                case "GetUsers": {
                    String countstr = request.getParameter("count");
                    String searchtxt = request.getParameter("search");
                    String objectid = request.getParameter("linkedObjectId");
                    String object = request.getParameter("linkedObject");
                    int linkedID = Integer.parseInt(objectid);
                    int count = Integer.parseInt(countstr);
                    int end = 50;
                    ArrayList<Integer> ids = new ArrayList<>();
                    HashMap<Integer, String> Names = new HashMap<>();
                    HashMap<Integer, String> Icons = new HashMap<>();
                    HashMap<Integer, String> Dates = new HashMap<>();
                    ids = UserManager.getLimitedMemberIDs(count, end, searchtxt);                    
                    if (!ids.isEmpty()) {
                        for (int id : ids) {
                            String username = UserManager.getUserName(id);
                            String date = UserManager.getDateJoined(id);
                            Names.put(id, username);
                            Dates.put(id, date);
                        }
                        String json1 = new Gson().toJson(ids);
                        String json2 = new Gson().toJson(Names);
                        String json3 = new Gson().toJson(Icons);
                        String json4 = new Gson().toJson(Dates);
                        json = "[" + json1 + "," + json2 + "," + json3 + "," + json4 + "]";
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
            Logger.getLogger(DialogServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DialogServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DialogServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DialogServlet.class.getName()).log(Level.SEVERE, null, ex);
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
