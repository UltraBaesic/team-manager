/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlets;

import com.classes.Login;
import com.classes.Registration;
import com.classes.UserManager;
import com.google.gson.Gson;
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
public class UserServlet extends HttpServlet {

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
            HttpSession session = request.getSession(true);

            switch (type) {
                case "MemberRegistration": {
                    String FirstName = request.getParameter("firstname");
                    String LastName = request.getParameter("lastname");
                    String EmailAddress = request.getParameter("emailaddress");
                    String PhoneNumber = request.getParameter("phonenumber");
                    String Password = request.getParameter("password");
                    String Team = request.getParameter("team");
                    String result = "Account Created Successfully";
                    int TeamID = Integer.parseInt(Team);
                    if (!Login.checkEmailAddressOrPhoneNumberExist(EmailAddress)) {
                        if (!Login.checkEmailAddressOrPhoneNumberExist(PhoneNumber)) {
                            int UserID = Registration.CreateMember(FirstName, LastName, EmailAddress, TeamID, Password, PhoneNumber);
                            session.setAttribute("Id", UserID);
                            session.setAttribute("userName", FirstName+" "+LastName);
                            session.setAttribute("userType", "member");
                        } else {
                            result = "Account with Phone Number already Exists";
                        }
                    } else {
                        result = "User Account with Email Address already Exists";
                    }
                    json = new Gson().toJson(result);
                    break;
                }

                case "Login": {
                    String Email_PhoneNumber = request.getParameter("email_phone");
                    String Password = request.getParameter("password");
                    String result = "";
                    int UserID = 0;
                    if (Login.checkEmailAddressOrPhoneNumberExist(Email_PhoneNumber)) {
                        UserID = Login.checkPasswordEmailMatch(Password, Email_PhoneNumber);
                        if (UserID != 0) {
                            session.setAttribute("Id", UserID);
                            String userName = UserManager.getUserName(UserID);
                            String userType = UserManager.getUserType(UserID);
                            session.setAttribute("userName", userName);
                            session.setAttribute("userType", userType);
                            result = "Successful";
                            String json1 = new Gson().toJson(result);
                            String json2 = new Gson().toJson(userType);
                            json = "["+json1+","+json2+"]";
                            break;
                        } else {
                            result = "Incorrect Login Parameters";
                            json = new Gson().toJson(result);
                        }
                        break;
                    } else {
                        result = "Email or Phone Number Entered Doesn't Exist";
                        json = new Gson().toJson(result);
                        break;
                    }
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
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
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
