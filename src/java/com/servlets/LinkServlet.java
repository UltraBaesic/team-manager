/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlets;

import com.classes.UserManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stephen
 */
public class LinkServlet extends HttpServlet {

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
            HttpSession session = request.getSession(true);
            String temp = "" + session.getAttribute("Id");
            String location = request.getParameter("location");
            switch (location) {
                case "Home": {
                    if (temp.equals("null") || temp.equals("")) {
                        response.sendRedirect("index.jsp");
                    } else {
                        int id = Integer.parseInt(temp);
                        String userType = UserManager.getUserType(id);
                        if (userType.equalsIgnoreCase("member")){
                            response.sendRedirect("pages/UserDashBoard.jsp");
                        } else if (userType.equalsIgnoreCase("manager")){
                            response.sendRedirect("pages/ManagerDashBoard.jsp");
                        } else if (userType.equalsIgnoreCase("admin")){
                            response.sendRedirect("pages/AdminDashBoard.jsp");
                        }                
                    }
                    break;
                }
                case "Team": {
                    if (temp.equals("null") || temp.equals("")) {
                        response.sendRedirect("index.jsp");
                    } else {
                        response.sendRedirect("pages/team.jsp");
                    }
                    break;
                }
                case "Tasks": {
                    if (temp.equals("null") || temp.equals("")) {
                        response.sendRedirect("index.jsp");
                    } else {
                        response.sendRedirect("pages/tasks.jsp");
                    }
                    break;
                }
                case "Records": {
                    if (temp.equals("null") || temp.equals("")) {
                        response.sendRedirect("index.jsp");
                    } else {
                        response.sendRedirect("pages/records.jsp");
                    }
                    break;
                }
                case "Logout": {
                    session.setAttribute("Id", "");
                    session.setAttribute("typo", "");
                    session.setAttribute("userName", "");
                    session.setAttribute("UserType", "");
                    response.sendRedirect("index.jsp");
                    break;
                }
                default:
                    session.setAttribute("alertMessage", "Sign in to your Tribizle Account");
                    response.sendRedirect("index.jsp");
                    break;
            }
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
            Logger.getLogger(LinkServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LinkServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(LinkServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LinkServlet.class.getName()).log(Level.SEVERE, null, ex);
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
